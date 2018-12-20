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
package org.entando.entando.apsadmin.user;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Classe action delegata alla gestione delle operazioni di associazione 
 * tra utenza e autorizzazioni.
 */
public class UserAuthorizationAction extends BaseAction {

	private static final String CURRENT_FORM_USER_AUTHS_PARAM_NAME = "currentUserAuthoritiesOnForm";
	private static final String USER_LIST = "userList";


	private static final Logger logger =  LoggerFactory.getLogger(UserAuthorizationAction.class);


	private String username;

	private String roleName;
	private String groupName;

	private Integer index;

	private IUserManager userManager;
	private IRoleManager roleManager;
	private IGroupManager groupManager;


	public String edit() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			List<Authorization> authorizations = super.getAuthorizationManager().getUserAuthorizations(this.getUsername());
			UserAuthsFormBean userAuthsFormBean = new UserAuthsFormBean(this.getUsername(), authorizations);
			this.getRequest().getSession().setAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME,  userAuthsFormBean);
		} catch (Throwable t) {
			logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private String checkUser() throws Throwable {
		if (!this.existsUser()) {
			this.addActionError(this.getText("error.user.notExist"));
			return USER_LIST;
		}
		if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotModifyAdminUser"));
			return USER_LIST;
		}
		if (this.isCurrentUser()) {
			this.addActionError(this.getText("error.user.cannotModifyCurrentUser"));
			return USER_LIST;
		}
		return null;
	}
	
	public String addAuthorization() {
		try {
			if (!this.checkAuthorizationSessionBean()) {
				return USER_LIST;
			}
			Group group = this.getGroupManager().getGroup(groupName);
			Role role = this.getRoleManager().getRole(roleName);
			if (!StringUtils.isEmpty(groupName) && null == group) {
				this.addFieldError("groupName", this.getText("error.userAuthorization.invalidGroup", new String[]{groupName}));
			}
			if (!StringUtils.isEmpty(roleName) && null == role) {
				this.addFieldError("roleName", this.getText("error.userAuthorization.invalidRole", new String[]{groupName}));
			}
			if (null == group && null == role) {
				this.addFieldError("groupName", this.getText("error.userAuthorization.invalidGroupAndRole"));
				this.addFieldError("roleName", this.getText("error.userAuthorization.invalidGroupAndRole"));
			}
			if (this.hasFieldErrors()) {
				return INPUT;
			}
			Authorization authorization = new Authorization(group, role);
			boolean result = this.getUserAuthsFormBean().addAuthorization(authorization);
			if (!result) {
				this.addActionError(this.getText("error.userAuthorization.alreadyExists", new String[]{groupName, roleName}));
				return INPUT;
			}
		} catch (Throwable t) {
			logger.error("error adding user authorization", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeAuthorization() {
		try {
			if (!this.checkAuthorizationSessionBean()) {
				return USER_LIST;
			}
			if (null == this.getIndex()) return INPUT;
			boolean result = this.getUserAuthsFormBean().removeAuthorization(this.getIndex());
			if (!result) return INPUT;
		} catch (Throwable t) {
			logger.error("error removing user authorization", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			if (!this.checkAuthorizationSessionBean()) {
				return USER_LIST;
			}
			UserAuthsFormBean authsBean = this.getUserAuthsFormBean();
			this.getAuthorizationManager().updateUserAuthorizations(username, authsBean.getAuthorizations());
			this.getRequest().getSession().removeAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME);
		} catch (Throwable t) {
			logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected boolean isCurrentUser() {
		UserDetails currentUser = this.getCurrentUser();
		return currentUser.getUsername().equals(this.getUsername());
	}
	
	/**
	 * Verifica l'esistenza dell'utente.
	 * @return true in caso positivo, false nel caso l'utente non esista.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected boolean existsUser() throws ApsSystemException {
		return (username != null) && (userManager.getUser(username) != null);
	}
	
	private boolean checkAuthorizationSessionBean() {
		UserAuthsFormBean authsBean = this.getUserAuthsFormBean();
		if (null == username || null == authsBean || !username.equals(authsBean.getUsername())) {
			this.addActionError(this.getText("error.userAuthorization.invalidSessionBean"));
			return false;
		}
		return true;
	}
	
	public UserAuthsFormBean getUserAuthsFormBean() {
		return (UserAuthsFormBean) this.getRequest().getSession().getAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME);
	}
	
	public List<Group> getGroups() {
		List<Group> groups = this.getGroupManager().getGroups();
		groups.sort(new BeanComparator<>("description"));
		return groups;
	}
	
	public List<Role> getRoles() {
		List<Role> roles = this.getRoleManager().getRoles();
		roles.sort(new BeanComparator<>("description"));
		return roles;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	protected IUserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}
	
	protected IRoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	protected IGroupManager getGroupManager() {
		return groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
}