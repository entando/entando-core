/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

import org.entando.entando.aps.system.services.oauth.IOAuthConsumerManager;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Request token request handler.
 * Special thanks to Praveen Alavilli and OAuth examples.
 * http://oauth.googlecode.com/svn/code/java/example/oauth-provider/src/net/oauth/example/provider/servlets/RequestTokenServlet.java
 * @author Praveen Alavilli - E.Santoboni
 */
public class RequestTokenOAuthServlet extends HttpServlet {
    
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
        IOAuthConsumerManager consumerManager = 
                (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
        try {
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            OAuthConsumer consumer = consumerManager.getConsumer(requestMessage);
            OAuthAccessor accessor = new OAuthAccessor(consumer);
            consumerManager.getOAuthValidator().validateMessage(requestMessage, accessor);
            String secret = requestMessage.getParameter("oauth_accessor_secret");
            if (secret != null) {
                accessor.setProperty(OAuthConsumer.ACCESSOR_SECRET, secret);
            }
            consumerManager.generateRequestToken(accessor);
            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            OAuth.formEncode(OAuth.newList("oauth_token", accessor.requestToken, 
                    "oauth_token_secret", accessor.tokenSecret), out);
            out.close();
        } catch (Exception e){
            consumerManager.handleException(e, request, response, true);
        }
    }
    
}
