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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthEndpointServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthEndpointServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doGet(req, resp);
        } catch (ServletException | IOException e) {
            logger.error("Exception {} ", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        OAuthAuthzRequest oauthRequest = null;
        IApiOAuthorizationCodeManager codeManager = (IApiOAuthorizationCodeManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_AUTHORIZATION_CODE_MANAGER, request);
        try {
            oauthRequest = new OAuthAuthzRequest(request);
            if (validateClient(oauthRequest, request, response)) {
                //build response according to response_type
                String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE) == null ? OAuth.OAUTH_RESPONSE_TYPE : oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
                OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                        .authorizationResponse(request, HttpServletResponse.SC_FOUND);
                final String authorizationCode = DigestUtils.md5Hex(System.nanoTime() + "_authorizationCode");
                final int expires = 3;
                AuthorizationCode authCode = new AuthorizationCode();
                authCode.setAuthorizationCode(authorizationCode);
                Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
                calendar.add(Calendar.SECOND, expires);
                authCode.setExpires(calendar.getTimeInMillis());
                authCode.setClientId(oauthRequest.getClientId());
                authCode.setSource(request.getRemoteAddr());
                codeManager.addAuthorizationCode(authCode);
                if (responseType.equals(ResponseType.CODE.toString())) {
                    builder.setCode(authorizationCode);
                }
                if (responseType.equals(ResponseType.TOKEN.toString())) {
                    builder.setAccessToken(authorizationCode);
                    builder.setExpiresIn((long) expires);
                }
                String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
                final OAuthResponse resp = builder.location(redirectURI).buildQueryMessage();
                final int status = resp.getResponseStatus();
                response.setStatus(status);
                response.sendRedirect(resp.getLocationUri());
            } else {
                logger.warn("OAuth2 authentication failed");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception ex) {
            logger.error("System exception {} ", ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
         */
    }

    /*
    private boolean validateClient(final OAuthAuthzRequest oauthRequest, HttpServletRequest request, HttpServletResponse response) throws OAuthProblemException {
        final IOAuthConsumerManager consumerManager
                = (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
        final String clientId = oauthRequest.getClientId();
        try {
            final ConsumerRecordVO clientDetail = consumerManager.getConsumerRecord(clientId);
            if (clientDetail != null) {
                if (!clientDetail.getKey().equals(oauthRequest.getClientId())) {
                    throw OAuthUtils.handleOAuthProblemException("Invalid clientId");
                } else if (clientDetail.getExpirationDate().getTime() < System.currentTimeMillis()) {
                    throw OAuthUtils.handleOAuthProblemException("ClientId is expired");
                } else if (!clientDetail.getCallbackUrl().equals(oauthRequest.getRedirectURI())) {
                    throw OAuthUtils.handleOAuthProblemException("Invalid redirectUri");
                }
                return true;
            }
        } catch (ApsSystemException e) {
            logger.error("ApsSystemException {}", e.getMessage());
            try {
                response.sendError(500);
            } catch (IOException e1) {
                logger.error("IOException {}", e1);
            }
            return false;
        }
        return false;
    }
     */
}
