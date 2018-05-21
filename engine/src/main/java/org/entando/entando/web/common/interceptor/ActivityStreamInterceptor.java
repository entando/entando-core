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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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

public class ActivityStreamInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IActionLogManager actionLogManager;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethod().isAnnotationPresent(RequestMapping.class) && method.getMethod().isAnnotationPresent(ActivityStreamAuditable.class)) {
                RequestMapping requestMapping = method.getMethod().getAnnotation(RequestMapping.class);
                String mapping = requestMapping.value()[0];
                this.logMethod(mapping, request);
            }
        }
    }

    protected void logMethod(String mapping, HttpServletRequest request) {
        try {
            ActionLogRecord record = this.buildActionRecord(mapping, request);
            this.actionLogManager.addActionRecord(record);
        } catch (ApsSystemException ex) {
            logger.error("System exception {}", ex.getMessage());
            throw new EntandoTokenException("error parsing OAuth parameters", request, "guest");
        }
    }

    private ActionLogRecord buildActionRecord(String mapping, HttpServletRequest request) {
        ActionLogRecord record = new ActionLogRecord();
        String username = this.getCurrentUsername(request);
        String namespace = request.getRequestURI();
        String actionName = request.getMethod();
        String parameters = this.createParamsFromUri(namespace, mapping);
        parameters = this.createParamsFromQueryString(parameters, request.getQueryString());
        record.setUsername(username);
        record.setNamespace(namespace);
        record.setActionName(actionName);
        record.setParameters(parameters);
        return record;
    }

    private String createParamsFromUri(String namespace, String mapping) {
        Map<String, String> params = new HashMap<>();
        mapping = (mapping.startsWith("/")) ? mapping.substring(1) : mapping;
        namespace = (namespace.startsWith("/")) ? namespace.substring(1) : namespace;
        mapping = (mapping.endsWith("/")) ? mapping.replaceAll(".$", "") : mapping;
        namespace = (namespace.endsWith("/")) ? namespace.replaceAll(".$", "") : namespace;
        String[] mappingSections = mapping.split("/");
        String[] namespaceSections = namespace.split("/");
        int startIndex = (namespaceSections.length > mappingSections.length) ? (namespaceSections.length - mappingSections.length) : 0;
        for (int i = startIndex; i < namespaceSections.length; i++) {
            String nsp = namespaceSections[i];
            String mpp = mappingSections[i - startIndex];
            if (mpp.startsWith("{") && mpp.endsWith("}")) {
                mpp = mpp.substring(1);
                mpp = mpp.replaceAll(".$", "");
                params.put(mpp, nsp);
            }
        }
        return this.addParameters("", params);
    }

    private String createParamsFromQueryString(String prev, String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return prev;
        }
        Map<String, String> params = new HashMap<>();
        String[] sections = queryString.split("&");
        for (int i = 0; i < sections.length; i++) {
            String[] param = sections[i].split("=");
            if (param.length == 2) {
                params.put(param[0], param[1]);
            }
        }
        return this.addParameters(prev, params);
    }

    private String addParameters(String init, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return init;
        }
        StringBuilder paramString = new StringBuilder(init);
        for (Map.Entry<String, String> pair : params.entrySet()) {
            String key = pair.getKey();
            paramString.append(key).append("=");
            String value = pair.getValue();
            paramString.append(value).append("\n");
        }
        return paramString.toString();
    }

    private String getCurrentUsername(HttpServletRequest request) {
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        if (null != user) {
            return user.getUsername();
        }
        return SystemConstants.GUEST_USER_NAME;
    }

}
