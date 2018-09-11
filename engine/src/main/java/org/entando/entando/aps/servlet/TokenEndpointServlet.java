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
package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.IApiOAuthorizationCodeManager;
import org.entando.entando.aps.system.services.oauth2.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.digest.DigestUtils;

public class TokenEndpointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(TokenEndpointServlet.class);
    private static final String ERROR_AUTHENTICATION_FAILED = "OAuth2 authentication failed";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = null;
        try {
            final OAuthResponse oAuthResponse = this.validateClientWithAuthorizationCode(request);
            if (oAuthResponse != null) {
                response.setStatus(oAuthResponse.getResponseStatus());
                StringBuilder contentType = new StringBuilder(MediaType.APPLICATION_JSON);
                contentType.append("; charset=").append(StandardCharsets.UTF_8.name());
                response.setContentType(contentType.toString());
                pw = response.getWriter();
                pw.print(oAuthResponse.getBody());
                pw.flush();
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ERROR_AUTHENTICATION_FAILED);
            }
        } catch (Throwable e) {
            _logger.error("OAuthSystemException exception {} ", e.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                _logger.error("IOException - IOException exception {} ", e1);
            }
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }

    private OAuthResponse registerToken(HttpServletRequest request, final String clientId, final String oauthType, final String localUser) throws OAuthSystemException, ApsSystemException {
        int expires = 3600;
        IApiOAuth2TokenManager tokenManager = (IApiOAuth2TokenManager) ApsWebApplicationUtils.getBean(IApiOAuth2TokenManager.BEAN_NAME, request);
        String tokenPrefix = clientId + "_" + localUser + "_" + System.nanoTime();
        final String accessToken = DigestUtils.md5Hex(tokenPrefix + "_accessToken");
        final String refreshToken = DigestUtils.md5Hex(tokenPrefix + "_refreshToken");
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(accessToken);
        oAuth2Token.setRefreshToken(refreshToken);
        oAuth2Token.setClientId(clientId);
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, expires);
        oAuth2Token.setExpiresIn(calendar.getTime());
        oAuth2Token.setGrantType(oauthType);
        if (localUser == null) {
            tokenManager.addApiOAuth2Token(oAuth2Token, false);
        } else {
            oAuth2Token.setLocalUser(localUser);
            tokenManager.addApiOAuth2Token(oAuth2Token, true);
        }
        return OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setExpiresIn(Long.toString(expires))
                .setRefreshToken(refreshToken)
                .buildJSONMessage();
    }

    private OAuthResponse validateClientWithAuthorizationCode(HttpServletRequest request) throws Throwable {
        try {
            final OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            IOAuthConsumerManager consumerManager = (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
            IApiOAuthorizationCodeManager codeManager = (IApiOAuthorizationCodeManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_AUTHORIZATION_CODE_MANAGER, request);
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())
                    || oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                final String clientId = oauthRequest.getClientId();
                final String oauthType = GrantType.AUTHORIZATION_CODE.toString();
                final String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
                final String clientSecret = oauthRequest.getClientSecret();
                boolean checkVerifyAccess = codeManager.verifyAccess(clientId, clientSecret, consumerManager);
                if (!checkVerifyAccess) {
                    _logger.error(ERROR_AUTHENTICATION_FAILED);
                    return null;
                } else if (!codeManager.verifyCode(authCode, request.getRemoteAddr())) {
                    _logger.error("OAuth2 authcode does not match or the source of client is different");
                    return null;
                }
                return this.registerToken(request, clientId, oauthType, null);
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                    .equals(GrantType.PASSWORD.toString())) {
                final String username = oauthRequest.getUsername();
                final String password = oauthRequest.getPassword();
                final String oauthType = GrantType.PASSWORD.toString();
                IUserManager userManager = (IUserManager) ApsWebApplicationUtils.getBean(SystemConstants.USER_MANAGER, request);
                UserDetails user = userManager.getUser(username, password);
                if (user == null) {
                    _logger.error(ERROR_AUTHENTICATION_FAILED);
                    return null;
                }
                return this.registerToken(request, username, oauthType, null);
            } else {
                return null;
            }
        } catch (OAuthSystemException e) {
            _logger.error("OAuthSystemException - {} ", e);
            return null;
        } catch (OAuthProblemException e) {
            _logger.error("OAuthProblemException - {} ", e.getError().concat(" ").concat(e.getDescription()));
            _logger.debug("OAuthProblemException - {} ", e);
            return null;
        }
    }

}
