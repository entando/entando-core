/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.EntandoAuthorizationException;
import org.entando.entando.web.common.exceptions.EntandoTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author paddeo
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
            if (method.getMethod().isAnnotationPresent(RequestMapping.class)) {
                RestAccessControl rqm = method.getMethodAnnotation(RestAccessControl.class);
                String permission = rqm.permission();
                this.extractOAuthParameters(request, permission);
            }
        }
        return true;
    }

    protected void extractOAuthParameters(HttpServletRequest request, String permission) {
        try {
            logger.info("Permission required: {}", permission);
            OAuthAccessResourceRequest requestMessage = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);

            String accessToken = requestMessage.getAccessToken();
            if (StringUtils.isBlank(accessToken)) {
                throw new EntandoTokenException("no access token found", request, null);
            }

            final OAuth2Token token = oAuth2TokenManager.getApiOAuth2Token(accessToken);
            this.validateToken(request, accessToken, token);

            String username = token.getClientId();
            this.checkAuthorization(username, permission, request);

        } catch (OAuthSystemException | ApsSystemException | OAuthProblemException ex) {
            logger.error("System exception {}", ex.getMessage());
            throw new EntandoTokenException("error parsing OAuth parameters", request, "guest");
        }
    }

    protected void checkAuthorization(String username, String permission, HttpServletRequest request) throws ApsSystemException {
        UserDetails user = authenticationProviderManager.getUser(username);
        if (user != null) {
            request.getSession().setAttribute("user", user);
            logger.info("User {} requesting resource that requires {} permission ", username, permission);
            if (StringUtils.isNotBlank(permission)) {
                if (!authorizationManager.isAuthOnPermission(user, permission)) {
                    throw new EntandoAuthorizationException(null, request, username);
                }
            }
        } else {
            logger.info("User {} not found ", username);
        }
    }

    protected void validateToken(HttpServletRequest request, String accessToken, final OAuth2Token token) {
        if (null == token) {
            throw new EntandoTokenException("no token found", request, "guest");
        } else if (!token.getAccessToken().equals(accessToken)) {
            throw new EntandoTokenException("invalid token", request, "guest");
        } else if (token.getExpiresIn().getTime() < System.currentTimeMillis()) {
            throw new EntandoTokenException("expired token", request, "guest");
        }
    }

}
