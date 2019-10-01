/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AccessTokenImpl;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.EntandoAuthorizationException;
import org.entando.entando.web.common.exceptions.EntandoTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author P.Addeo - E.Santoboni
 */
public class EntandoOauth2Interceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IApiOAuth2TokenManager oAuth2TokenManager;

    @Autowired
    private IAuthenticationProviderManager authenticationProviderManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.hasMethodAnnotation(RequestMapping.class)) {
                UserDetails user = this.extractOAuthParameters(request);
                RestAccessControl rqm = method.getMethodAnnotation(RestAccessControl.class);
                if (null == rqm) {
                    return true;
                }

                this.checkAuthorization(user, rqm.permission(), request);
            }
        }
        return true;
    }

    protected UserDetails extractOAuthParameters(HttpServletRequest request) {
        try {
            request.getSession().setAttribute("user", null); //Clear previous session

            String accessToken = new EntandoBearerTokenExtractor().extractToken(request);
            if (StringUtils.isBlank(accessToken)) {
                return null;
            }

            final OAuth2AccessToken token = this.getoAuth2TokenManager().readAccessToken(accessToken);
            this.validateToken(request, accessToken, token);
            String username;
            if (token instanceof OAuth2AccessTokenImpl) {
                username = ((OAuth2AccessTokenImpl) token).getLocalUser();
            } else {
                Authentication auth = new EntandoBearerTokenExtractor().extract(request);
                username = auth.getPrincipal().toString();
            }

            UserDetails user = this.getAuthenticationProviderManager().getUser(username);
            if (user == null) {
                logger.warn("User {} not found ", username);
                return null;
            }

            request.getSession().setAttribute("user", user);
            return user;
        } catch (ApsSystemException ex) {
            logger.error("System exception {}", ex.getMessage());
            throw new EntandoTokenException("error parsing OAuth parameters", request, "guest");
        }
    }

    protected void checkAuthorization(UserDetails user, String permission, HttpServletRequest request) throws ApsSystemException {
        if (null == user) {
            throw new EntandoTokenException("no access token found", request, null);
        }

        logger.debug("User {} requesting resource that requires {} permission ", user.getUsername(), permission);
        if (StringUtils.isNotBlank(permission)) {
            if (!this.getAuthorizationManager().isAuthOnPermission(user, permission)) {
                logger.warn("User {} is missing the required permission {}", user.getUsername(), permission);
                throw new EntandoAuthorizationException(null, request, user.getUsername());
            }
        }
    }

    protected void validateToken(HttpServletRequest request, String accessToken, final OAuth2AccessToken token) {
        if (null == token) {
            throw new EntandoTokenException("no token found", request, "guest");
        } else if (!token.getValue().equals(accessToken)) {
            throw new EntandoTokenException("invalid token", request, "guest");
        } else if (token.isExpired()) {
            throw new EntandoTokenException("expired token", request, "guest");
        }
    }

    protected IApiOAuth2TokenManager getoAuth2TokenManager() {
        return oAuth2TokenManager;
    }

    public void setoAuth2TokenManager(IApiOAuth2TokenManager oAuth2TokenManager) {
        this.oAuth2TokenManager = oAuth2TokenManager;
    }

    protected IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    protected IAuthenticationProviderManager getAuthenticationProviderManager() {
        return authenticationProviderManager;
    }

    public void setAuthenticationProviderManager(IAuthenticationProviderManager authenticationProviderManager) {
        this.authenticationProviderManager = authenticationProviderManager;
    }

}
