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
package org.entando.entando.apsadmin.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author E.Santoboni - S.Puddu
 */
public class ActionLoggerInterceptor extends AbstractInterceptor {

    private static final Logger _logger = LoggerFactory.getLogger(ActionLoggerInterceptor.class);

    private String excludeRequestParameters = "";
    private String includeActionProperties = "";

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        ActionLogRecord actionRecord = null;
        String result = null;
        try {
            actionRecord = this.buildActionRecord(invocation);
            result = invocation.invoke();
            List<ActivityStreamInfo> asiList = null;
            Object actionObject = invocation.getAction();
            if (actionObject instanceof BaseAction) {
                BaseAction action = (BaseAction) actionObject;
                asiList = action.getActivityStreamInfos();
            }
            this.includeActionProperties(actionRecord, actionObject);
            if (null == asiList || asiList.isEmpty()) {
                this.getActionLoggerManager().addActionRecord(actionRecord);
            } else {
                for (ActivityStreamInfo asi : asiList) {
                    ActionLogRecord clone = this.createClone(actionRecord);
                    clone.setActivityStreamInfo(asi);
                    this.getActionLoggerManager().addActionRecord(clone);
                }
            }
        } catch (Throwable t) {
            _logger.error("error in intercept", t);
        }
        return result;
    }

    private ActionLogRecord createClone(ActionLogRecord record) {
        ActionLogRecord clone = new ActionLogRecord();
        clone.setActionDate(new Date());
        clone.setActionName(record.getActionName());
        clone.setId(-1);
        clone.setNamespace(record.getNamespace());
        clone.setParameters(record.getParameters());
        clone.setUsername(record.getUsername());
        return clone;
    }

    /**
     * Build an {@link ActionLoggerRecord} object related to the current action
     *
     * @param invocation
     * @return an {@link ActionLoggerRecord} for the current action
     */
    private ActionLogRecord buildActionRecord(ActionInvocation invocation) {
        ActionLogRecord record = new ActionLogRecord();
        String username = this.getCurrentUsername();
        String namespace = invocation.getProxy().getNamespace();
        String actionName = invocation.getProxy().getActionName();
        String parameters = this.getParameters();
        record.setUsername(username);
        record.setNamespace(namespace);
        record.setActionName(actionName);
        record.setParameters(parameters);
        return record;
    }

    /**
     * Gets the username of the user in session
     *
     * @return the username of the current user
     */
    private String getCurrentUsername() {
        String username = null;
        UserDetails currentUser = this.getCurrentUser();
        if (null == currentUser) {
            username = "ANONYMOUS";
        } else {
            username = currentUser.getUsername();
        }
        return username;
    }

    private String getParameters() {
        String[] paramsToExclude = this.getExcludeRequestParameters().split(",");
        StringBuilder params = new StringBuilder();
        Map<String, String[]> reqParams = this.getRequest().getParameterMap();
        if (null != reqParams && !reqParams.isEmpty()) {
            for (Entry<String, String[]> pair : reqParams.entrySet()) {
                String key = pair.getKey();
                if (!this.isParamToExclude(key, paramsToExclude)) {
                    params.append(key).append("=");
                    String[] values = pair.getValue();
                    if (null != values) {
                        for (int i = 0; i < values.length; i++) {
                            params.append(values[i]).append(",");
                        }
                    }
                    params.deleteCharAt(params.length() - 1);
                    params.append("\n");
                }
            }
        }
        return params.toString();
    }

    private void includeActionProperties(ActionLogRecord actionRecord, Object action) {
        if (StringUtils.isEmpty(this.getIncludeActionProperties())) {
            return;
        }
        String[] propertyToInclude = this.getIncludeActionProperties().split(",");
        StringBuilder params = new StringBuilder(actionRecord.getParameters());
        for (String property : propertyToInclude) {
            try {
                Object value = BeanUtils.getProperty(action, property);
                if (null != value) {
                    params.append(property).append("=").append(value.toString());
                    params.append("\n");
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                _logger.debug("Error extracting property " + property + " from action", ex);
            }
        }
        actionRecord.setParameters(params.toString());
    }

    private boolean isParamToExclude(String key, String[] paramsToExclude) {
        for (int i = 0; i < paramsToExclude.length; i++) {
            if (key.equals(paramsToExclude[i])) {
                return true;
            }
        }
        return false;
    }

    private UserDetails getCurrentUser() {
        HttpSession session = this.getRequest().getSession();
        return (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
    }

    private HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    private IActionLogManager getActionLoggerManager() {
        return (IActionLogManager) ApsWebApplicationUtils.getBean(SystemConstants.ACTION_LOGGER_MANAGER, this.getRequest());
    }

    public String getExcludeRequestParameters() {
        return excludeRequestParameters;
    }

    public void setExcludeRequestParameters(String excludeRequestParameters) {
        this.excludeRequestParameters = excludeRequestParameters;
    }

    public String getIncludeActionProperties() {
        return includeActionProperties;
    }

    public void setIncludeActionProperties(String includeActionParameters) {
        this.includeActionProperties = includeActionParameters;
    }

}
