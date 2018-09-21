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
package org.entando.entando.apsadmin.system;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.apache.struts2.interceptor.TokenInterceptor.INVALID_TOKEN_CODE;

/**
 * This interceptor can make sure that back buttons and double clicks don't
 * cause un-intended side affects. For example, you can use this to prevent
 * careless users who might double click on a "checkout" button at an online
 * store. This interceptor uses a fairly primitive technique for when an invalid
 * token is found: it returns the result invalid.token.
 *
 * TypeMessages parameter to specify the type of message to associate at
 * invalid.token result The values of type are the following: error: return an
 * action error message message: return an action message none: don't return
 * message
 *
 * @author Entando
 */
public class CustomTokenInterceptor extends org.apache.struts2.interceptor.TokenInterceptor {

    private static final Logger LOG = LogManager.getLogger(CustomTokenInterceptor.class);

    public static final String TYPE_RETURN_ACTION_ERROR_MESSAGE = "error";
    public static final String TYPE_RETURN_ACTION_MESSAGE = "message";
    public static final String TYPE_RETURN_NONE_MESSAGE = "none";

    private TextProvider textProvider;

    private String typeMessages;

    protected String getCustomMessage(ActionInvocation invocation, String key, String defaultText) {
        Object action = invocation.getAction();
        if (action instanceof TextProvider) {
            return ((TextProvider) action).getText(key, defaultText);
        }
        return textProvider.getText(key, defaultText);
    }

    @Override
    protected String handleInvalidToken(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        String errorMessage = this.getCustomMessage(invocation, "struts.messages.invalid.token",
                "The form has already been processed or no token was supplied, please try again.");
        String message = this.getCustomMessage(invocation, "struts.messages.invalid.token.message",
                "Stop double-submission of forms.");
        if (action instanceof ValidationAware) {
            if (null == this.getTypeMessages() || this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_NONE_MESSAGE)) {
                //nothing to do
            } else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_ERROR_MESSAGE)) {
                ((ValidationAware) action).addActionError(errorMessage);
            } else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_MESSAGE)) {
                ((ValidationAware) action).addActionMessage(message);
            } else {
                LOG.warn("Invalid message type : {}", this.getTypeMessages());
            }
        } else {
            LOG.warn(errorMessage);
        }
        return INVALID_TOKEN_CODE;
    }

    @Override
    @Inject
    public void setTextProviderFactory(TextProviderFactory textProviderFactory) {
        super.setTextProviderFactory(textProviderFactory);
        this.textProvider = textProviderFactory.createInstance(getClass());
    }

    public void setTypeMessages(String typeMessages) {
        this.typeMessages = typeMessages;
    }

    public String getTypeMessages() {
        return typeMessages;
    }

}
