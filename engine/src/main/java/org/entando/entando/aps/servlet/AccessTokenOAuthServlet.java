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
package org.entando.entando.aps.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Access Token request handler Special thanks to Praveen Alavilli and OAuth
 * examples.
 * http://oauth.googlecode.com/svn/code/java/example/oauth-provider/src/net/oauth/example/provider/servlets/AccessTokenServlet.java
 *
 * @author Praveen Alavilli - E.Santoboni
 */
public class AccessTokenOAuthServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.processRequest(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.processRequest(request, response);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		/*
        IOAuthConsumerManager consumerManager =
                (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
        try {
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            OAuthAccessor accessor = consumerManager.getAccessor(requestMessage);
            consumerManager.getOAuthValidator().validateMessage(requestMessage, accessor);
            if (!Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
                OAuthProblemException problem = new OAuthProblemException("permission_denied");
                throw problem;
            }
            consumerManager.generateAccessToken(accessor);
            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            OAuth.formEncode(OAuth.newList("oauth_token", accessor.accessToken,
                    "oauth_token_secret", accessor.tokenSecret), out);
            out.close();
        } catch (Exception e) {
            consumerManager.handleException(e, request, response, true);
        }
		 */
	}

}
