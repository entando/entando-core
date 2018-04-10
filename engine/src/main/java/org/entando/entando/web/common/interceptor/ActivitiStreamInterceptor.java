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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.web.common.annotation.ActivityStreamAuditable;
import org.entando.entando.web.common.exceptions.EntandoTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class ActivitiStreamInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IActionLogManager actionLogManager;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethod().isAnnotationPresent(RequestMapping.class) && method.getMethod().isAnnotationPresent(ActivityStreamAuditable.class)) {

                this.logMethod(request);
            }
        }
    }

    protected void logMethod(HttpServletRequest request) {
        try {
            this.buildActionRecord(request);
            this.actionLogManager.addActionRecord(buildActionRecord(request));
        } catch (ApsSystemException ex) {
            logger.error("System exception {}", ex.getMessage());
            throw new EntandoTokenException("error parsing OAuth parameters", request, "guest");
        }
    }


    private ActionLogRecord buildActionRecord(HttpServletRequest request) {
        ActionLogRecord record = new ActionLogRecord();
        String username = this.getCurrentUsername(request);
        String namespace = request.getRequestURI();
        String actionName = request.getMethod();
        String parameters = request.getQueryString();
        record.setUsername(username);
        record.setNamespace(namespace);
        record.setActionName(actionName);
        record.setParameters(parameters);
        return record;
    }


    private String getCurrentUsername(HttpServletRequest request) {
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        if (null != user) {
            return user.getUsername();
        }
        return SystemConstants.GUEST_USER_NAME;
    }

}
