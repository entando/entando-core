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
 * Authorization request handler. Special thanks to Praveen Alavilli and OAuth
 * examples.
 * http://oauth.googlecode.com/svn/code/java/example/oauth-provider/src/net/oauth/example/provider/servlets/AuthorizationServlet.java
 *
 * @author Praveen Alavilli - E.Santoboni
 */
public class AuthorizationOAuthServlet extends HttpServlet {

	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		/*
        IOAuthConsumerManager consumerManager =
                (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
        try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            OAuthAccessor accessor = consumerManager.getAccessor(requestMessage);
            if (Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
                this.returnToConsumer(request, response, accessor);
            } else {
                this.sendToAuthorizePage(request, response, accessor);
            }
        } catch (Exception e){
            consumerManager.handleException(e, request, response, true);
        }
		 */
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		/*
        IOAuthConsumerManager consumerManager =
                (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
        IAuthenticationProviderManager authenticationProvider =
                (IAuthenticationProviderManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHENTICATION_PROVIDER_MANAGER, request);
        try {
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            OAuthAccessor accessor = consumerManager.getAccessor(requestMessage);
            String loggedUserString = request.getParameter("loggedUser");
            Boolean loggedUser = (null != loggedUserString) ? Boolean.parseBoolean(loggedUserString) : false;
            String username = null;
            UserDetails user = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
            if (loggedUser && null != user && !user.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
                username = user.getUsername();
            } else {
                username = request.getParameter("username");
                String password = request.getParameter("password");
                user = authenticationProvider.getUser(username, password);
                if (null == user) {
                    request.setAttribute("oauthParam_USERNAME", username);
                    request.setAttribute("oauthParam_INVALID_CREDENTIALS", new Boolean(true));
                    this.sendToAuthorizePage(request, response, accessor);
                }
            }
            consumerManager.markAsAuthorized(accessor, username);
            this.returnToConsumer(request, response, accessor);
        } catch (Exception e) {
            consumerManager.handleException(e, request, response, true);
        }
		 */
	}
	/*
    private void sendToAuthorizePage(HttpServletRequest request,
            HttpServletResponse response, OAuthAccessor accessor) throws IOException, ServletException {
        String callback = request.getParameter("oauth_callback");
        if (callback == null || callback.length() <= 0) {
            callback = "none";
        }
        String consumer_description = (String)accessor.consumer.getProperty("description");
        request.setAttribute("oauthParam_CONSUMER_DESCRIPTION", consumer_description);
        request.setAttribute("oauthParam_CALLBACK_URL", callback);
        request.setAttribute("oauthParam_REQUEST_TOKEN", accessor.requestToken);
        request.getRequestDispatcher("/WEB-INF/oauth/authorize.jsp").forward(request, response);
    }

    private void returnToConsumer(HttpServletRequest request,
            HttpServletResponse response, OAuthAccessor accessor) throws IOException, ServletException {
        String callback = request.getParameter("oauth_callback");
        if ("none".equals(callback)
                && accessor.consumer.callbackURL != null
                && accessor.consumer.callbackURL.length() > 0) {
            callback = accessor.consumer.callbackURL;
        }
        if( "none".equals(callback) || "oob".equals(callback) ) {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println("You have successfully authorized '"
                    + accessor.consumer.getProperty("description")
                    + "'. Please close this browser window and click continue"
                    + " in the client.");
            out.close();
        } else {
            if (callback == null || callback.length() <= 0)
                callback = accessor.consumer.callbackURL;
            String token = accessor.requestToken;
            if (token != null) {
                callback = OAuth.addParameters(callback, "oauth_token", token);
            }
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", callback);
        }
    }
	 */
}
