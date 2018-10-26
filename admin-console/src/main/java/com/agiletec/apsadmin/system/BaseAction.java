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
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Class beneath all actions.
 * @author E.Santoboni
 */
public class BaseAction extends ActionSupport implements ServletRequestAware, ParameterAware {

	private static final Logger _logger = LoggerFactory.getLogger(BaseAction.class);
	
	/**
	 * Check if the current user belongs to the given group. It always returns true if the user
	 * belongs to the Administrators group.
	 * @param groupName The name of the group to check against the current user.
	 * @return true if the user belongs to the given group, false otherwise.
	 */
	protected boolean isCurrentUserMemberOf(String groupName) {
		UserDetails currentUser = this.getCurrentUser();
		IAuthorizationManager authManager = this.getAuthorizationManager();
		return authManager.isAuthOnGroup(currentUser, groupName) || authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME);
	}
	
	/**
	 * Check if the current user has the given permission granted. It always returns true if the 
	 * user has the the "superuser" permission set in some role.
	 * @param permissionName The name of the permission to check against the current user.
	 * @return true if the user has the permission granted, false otherwise.
	 */
	protected boolean hasCurrentUserPermission(String permissionName) {
		UserDetails currentUser = this.getCurrentUser();
		IAuthorizationManager authManager = this.getAuthorizationManager();
		return authManager.isAuthOnPermission(currentUser, permissionName) || authManager.isAuthOnPermission(currentUser, Permission.SUPERUSER);
	}
	
	protected UserDetails getCurrentUser() {
		UserDetails currentUser = (UserDetails) this.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return currentUser;
	}
	
	@Override
	public void setParameters(Map<String, String[]> params) {
		this._params = params;
	}
	
	protected Map<String, String[]> getParameters() {
		return this._params;
	}
	
	protected String getParameter(String paramName) {
		Object param = this.getParameters().get(paramName);
		if (param != null && param instanceof String[]) {
			return ((String[])param)[0];
		} else if (param instanceof String) {
			return (String)param;
		}
		return null;
	}
	
	protected Set<String> getRequiredPermissions() {
		return _requiredPermissions;
	}
	protected void setRequiredPermissions(Set<String> requiredPermissions) {
		this._requiredPermissions = requiredPermissions;
	}
	
	protected List<Group> getActualAllowedGroups() {
		if (null != this._actualAllowedGroups) {
			return _actualAllowedGroups;
		}
		this._actualAllowedGroups = new ArrayList<Group>();
		UserDetails currentUser = this.getCurrentUser();
		if (null == currentUser || null == this.getRequiredPermissions()) {
			return this._actualAllowedGroups;
		}
		Iterator<String> iter = this.getRequiredPermissions().iterator();
		while (iter.hasNext()) {
			String permissionName = iter.next();
			List<Group> groupsByPermission = this.getAuthorizationManager().getGroupsByPermission(currentUser, permissionName);
			if (null != groupsByPermission) {
				for (int i = 0; i < groupsByPermission.size(); i++) {
					Group group = groupsByPermission.get(i);
					if (null != group && !this._actualAllowedGroups.contains(group)) {
						this._actualAllowedGroups.add(group);
					}
				}
			}
		}
		Collections.sort(this._actualAllowedGroups, new BeanComparator("description"));
		return this._actualAllowedGroups;
	}
	
	protected List<String> getActualAllowedGroupCodes() {
		List<String> codes = new ArrayList<String>();
		List<Group> groups = this.getActualAllowedGroups();
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			if (null != group && !codes.contains(group.getName())) {
				codes.add(group.getName());
			}
		}
		return codes;
	}
	
	/**
	 * Return the current system language used in the back-end interface. If this language does not
	 * belong to those known by the system the default language is returned. A log line will 
	 * report the problem.
	 * @return The current language.
	 */
	public Lang getCurrentLang() {
		Locale locale = this.getLocale();
		String langCode = locale.getLanguage();
		Lang currentLang = this.getLangManager().getLang(langCode);
		if (null != currentLang) {
			return currentLang;
		} else {
			_logger.info("Required Lang '{}' invalid", langCode);
			return this.getLangManager().getDefaultLang();
		}
	}
	
	/**
	 * Return a title by current lang.
	 * @param defaultValue The default value returned in case there is no valid title in properties.
	 * @param titles The titles.
	 * @return The title.
	 */
	public String getTitle(String defaultValue, Properties titles) {
		if (null == titles) return defaultValue;
		Lang currentLang = this.getCurrentLang();
		String title = titles.getProperty(currentLang.getCode());
		if (null == title) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			title = titles.getProperty(defaultLang.getCode());
		}
		if (null == title) {
			title = defaultValue;
		}
		return title;
	}
	
	/**
	 * Return the Activity informations (showable to the dashboard) joined to executed action.
	 * this method has to be extended for custom action.
	 * @return The Activity informations
	 */
	public List<ActivityStreamInfo> getActivityStreamInfos() {
		return this._activityStreamInfos;
	}
	
	protected void addActivityStreamInfo(ActivityStreamInfo asi) {
		if (null == asi) {
			return;
		}
		if (null == this._activityStreamInfos) {
			this._activityStreamInfos = new ArrayList<ActivityStreamInfo>();
		}
		this._activityStreamInfos.add(asi);
	}
	
	public boolean isComponentInstalled(String componentName) {
		return this.getComponentManager().isComponentInstalled(componentName);
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this._request = request;
	}
	protected HttpServletRequest getRequest() {
		return _request;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	@Deprecated
	protected IAuthorizationManager getAuthManager() {
		return _authorizationManager;
	}
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	protected IComponentManager getComponentManager() {
		return _componentManager;
	}
	public void setComponentManager(IComponentManager componentManager) {
		this._componentManager = componentManager;
	}
	
	private ILangManager _langManager;
	
	private IAuthorizationManager _authorizationManager;
	private IComponentManager _componentManager;
	
	public static final String FAILURE = "failure";
	
	private HttpServletRequest _request;
	private Map<String, String[]> _params;
	private Set<String> _requiredPermissions;
	private List<Group> _actualAllowedGroups;
	
	public static final String USER_NOT_ALLOWED = "userNotAllowed";
	
	private List<ActivityStreamInfo> _activityStreamInfos;
	
}
