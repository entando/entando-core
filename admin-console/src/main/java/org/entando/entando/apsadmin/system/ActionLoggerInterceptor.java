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

/**
 * @author E.Santoboni - S.Puddu
 */
public class ActionLoggerInterceptor extends AbstractInterceptor {

	private static final Logger _logger =  LoggerFactory.getLogger(ActionLoggerInterceptor.class);
	
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
			if (null == asiList || asiList.isEmpty()) {
				this.getActionLoggerManager().addActionRecord(actionRecord);
			} else {
				for (int i = 0; i < asiList.size(); i++) {
					ActivityStreamInfo asi = asiList.get(i);
					ActionLogRecord clone = this.createClone(actionRecord);
					clone.setActivityStreamInfo(asi);
					this.getActionLoggerManager().addActionRecord(clone);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in intercept", t);
			//ApsSystemUtils.logThrowable(t, this, "intercept");
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
		return _excludeRequestParameters;
	}
	public void setExcludeRequestParameters(String excludeRequestParameters) {
		this._excludeRequestParameters = excludeRequestParameters;
	}
	
	public String getIncludeSessionParameters() {
		return _includeSessionParameters;
	}
	public void setIncludeSessionParameters(String includeSessionParameters) {
		this._includeSessionParameters = includeSessionParameters;
	}
	
	/*
	public String getExcludeParams() {
		return _excludeParams;
	}
	public void setExcludeParams(String excludeParams) {
		this._excludeParams = excludeParams;
	}
	*/
	/*
	public String getIncludeSessionParameters() {
		return _excludeParams;
	}
	public void setIncludeSessionParameters(String excludeParams) {
		this._excludeParams = excludeParams;
	}
	*/
	private String _excludeRequestParameters = "";
	private String _includeSessionParameters = "";
	
}
