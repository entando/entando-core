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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.input.CloseShieldInputStream;
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
        ActionLogRecord record = new ActionLogRecord();
        try {
            String username = this.getCurrentUsername(request);
            String namespace = request.getRequestURI();
            String actionName = request.getMethod();
            String parameters = this.createParamsFromUri(namespace, mapping);
            parameters = this.addParamsFromQueryString(parameters, request.getQueryString());
            parameters = this.addParamsFromRequestBody(parameters, request.getInputStream());
            record.setUsername(username);
            record.setNamespace(namespace);
            record.setActionName(actionName);
            record.setParameters(parameters);
            this.actionLogManager.addActionRecord(record);
        } catch (Exception ex) {
            logger.error("System exception {}", ex.getMessage(), ex);
            throw new EntandoTokenException("error parsing request", request, "guest");
        }
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

    private String addParamsFromQueryString(String prev, String queryString) {
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

    private String addParamsFromRequestBody(String prev, InputStream is) {
        if (null == is) {
            return prev;
        }
        try {
            CloseShieldInputStream cloned = new CloseShieldInputStream(is);
            String body = FileTextReader.getText(cloned);
            int bodyLength = body.length();
            if (bodyLength > 0) {
                byte[] bytes = body.getBytes();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> params;
                params = objectMapper.readValue(bytes, HashMap.class);
                return this.addParameters(prev, params);
            }
            return prev;
        } catch (Exception e) {
            logger.error("System exception {}", e.getMessage(), e);
            return prev;
        }
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
