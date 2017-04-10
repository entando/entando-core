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
package org.entando.entando.apsadmin.tags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class GroupsByPermissionTag extends TagSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(GroupsByPermissionTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Set<String> groupCodes = this.getAllowedGroups();
			this.pageContext.setAttribute(this.getVar(), groupCodes);
			return super.doStartTag();
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			throw new JspException("Error during tag initialization ", t);
		}
	}
	
	protected Set<String> getAllowedGroups() {
		Set<String> groupCodes = new HashSet<String>();
		UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String permissionName = this.getPermission();
		if (null != currentUser && null != permissionName) {
			IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, this.pageContext);
			List<Group> groupsByPermission = authManager.getGroupsByPermission(currentUser, permissionName);
			if (null != groupsByPermission) {
				for (Group group : groupsByPermission) {
					if (null != group) {
						groupCodes.add(group.getName());
					}
				}
			}
		}
		return groupCodes;
	}
	
	/**
	 * Return the requested permission.
	 * @return The permission.
	 */
	public String getPermission() {
		return _permission;
	}
	
	/**
	 * Set the requested permission.
	 * @param permission The permission.
	 */
	public void setPermission(String permission) {
		this._permission = permission;
	}
	
	/**
	 * Set the name of the variable where the result 
	 * of the authorization checks is placed in form of an boolean value.
	 * @param var The name of the parameter.
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	/**
	 * Return the name of the variable where the result 
	 * of the authorization checks is placed in form of an boolean value.
	 * @return The name of the parameter.
	 */
	public String getVar() {
		return _var;
	}

	private String _permission;
	private String _var;
	
}
