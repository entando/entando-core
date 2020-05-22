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
package com.agiletec.apsadmin.system;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor responsible to make CSRF Token available in the ValueStack
 *
 * @author breno.queiroz
 */
public class CsrfInterceptor extends AbstractInterceptor {

    private static final String CSRF_TOKEN = "XSRF-TOKEN";
    private static final String CSRF_FIELD_NAME = "csrfToken";

	@Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Cookie[] cookies = ServletActionContext.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CSRF_TOKEN)) {
                    ValueStack stack = ActionContext.getContext().getValueStack();
                    stack.setValue(CSRF_FIELD_NAME, cookie.getValue());
                }
            }
        }
        return invocation.invoke();
    }

}