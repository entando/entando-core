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
package com.agiletec.aps.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Toggle the visibility of the elements contained in body tag, depending on user permissions.
 * The user authorisations are checked against either the given permission or the specified group membership.
 * Is possible to insert the result of the authorisation check in a variable placed in the page context.
 * @author E.Santoboni
 */
public class CheckPermissionTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(CheckPermissionTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		try {
			boolean isAuthorized = false;
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, this.pageContext);
			boolean isGroupSetted = (this.getGroupName() != null && this.getGroupName().length()>0);
			boolean isPermissionSetted = (this.getPermission() != null && this.getPermission().length()>0);
			
			boolean isAuthGr = !isGroupSetted || authManager.isAuthOnGroup(currentUser, this.getGroupName()) || authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME);
			boolean isAuthPerm = !isPermissionSetted || authManager.isAuthOnPermission(currentUser, this._permission) || authManager.isAuthOnPermission(currentUser, Permission.SUPERUSER);
			
			isAuthorized = isAuthGr && isAuthPerm;
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), new Boolean(isAuthorized));
			}
			if (isAuthorized) {
				return EVAL_BODY_INCLUDE;
			} else {
				return SKIP_BODY;
			}
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization ", t);
		}
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
	 * Return the name of the requested group.
	 * @return The name of the group.
	 */
	public String getGroupName() {
		return _groupName;
	}
	
	/**
	 * Set the name of the requested group.
	 * @param groupName The name of the group.
	 */
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}
	
	/**
	 * Set the name of the variable where the result of the authorisation checks is placed in
	 * form of an boolean value.
	 * @param resultParamName The name of the parameter.
	 */
	public void setVar(String var) {
		this._var = var;
	}

	/**
	 * Return the name of the variable where the result of the authorisation checks is placed in
	 * form of an boolean value.
	 * @return The name of the parameter.
	 */
	public String getVar() {
		return _var;
	}

	private String _permission;
	private String _groupName;
	private String _var;
	
}
