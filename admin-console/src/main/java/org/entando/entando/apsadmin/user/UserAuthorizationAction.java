/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
import java.util.Collections;

import java.util.List;
import org.apache.commons.beanutils.BeanComparator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe action delegata alla gestione delle operazioni di associazione 
 * tra utenza e autorizzazioni.
 * @author E.Santoboni
 */
public class UserAuthorizationAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(UserAuthorizationAction.class);
	
	public String edit() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			List<Authorization> authorizations = super.getAuthorizationManager().getUserAuthorizations(this.getUsername());
			UserAuthsFormBean userAuthsFormBean = new UserAuthsFormBean(this.getUsername(), authorizations);
			this.getRequest().getSession().setAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME,  userAuthsFormBean);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private String checkUser() throws Throwable {
		if (!this.existsUser()) {
			this.addActionError(this.getText("error.user.notExist"));
			return "userList";
		}
		if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotModifyAdminUser"));
			return "userList";
		}
		if (this.isCurrentUser()) {
			this.addActionError(this.getText("error.user.cannotModifyCurrentUser"));
			return "userList";
		}
		return null;
	}
	
	public String addAuthorization() {
		try {
			if (!this.checkAuthorizationSessionBean()) {
				return "userList";
			}
			String groupName = this.getGroupName();
			String roleName = this.getRoleName();
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
			_logger.error("error adding user authorization", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeAuthorization() {
		try {
			if (!this.checkAuthorizationSessionBean()) {
				return "userList";
			}
			if (null == this.getIndex()) return INPUT;
			boolean result = this.getUserAuthsFormBean().removeAuthorization(this.getIndex());
			if (!result) return INPUT;
		} catch (Throwable t) {
			_logger.error("error removing user authorization", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			if (!this.checkAuthorizationSessionBean()) {
				return "userList";
			}
			String username = this.getUsername();
			UserAuthsFormBean authsBean = this.getUserAuthsFormBean();
			this.getAuthorizationManager().updateUserAuthorizations(username, authsBean.getAuthorizations());
			this.getRequest().getSession().removeAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME);
		} catch (Throwable t) {
			t.printStackTrace();
			_logger.error("error in save", t);
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
		String username = this.getUsername();
		boolean exists = (username!=null && username.trim().length()>=0 && this.getUserManager().getUser(username)!=null);
		return exists;
	}
	
	private boolean checkAuthorizationSessionBean() {
		String username = this.getUsername();
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
		Collections.sort(groups, new BeanComparator("description"));
		return groups;
	}
	
	public List<Role> getRoles() {
		List<Role> roles = this.getRoleManager().getRoles();
		Collections.sort(roles, new BeanComparator("description"));
		return roles;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public String getRoleName() {
		return _roleName;
	}
	public void setRoleName(String roleName) {
		this._roleName = roleName;
	}
	
	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}
	
	public Integer getIndex() {
		return _index;
	}
	public void setIndex(Integer index) {
		this._index = index;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	private String _username;
	
	private String _roleName;
	private String _groupName;
	
	private Integer _index;
	
	private IUserManager _userManager;
	private IRoleManager _roleManager;
	private IGroupManager _groupManager;
	
	public static final String CURRENT_FORM_USER_AUTHS_PARAM_NAME = "currentUserAuthoritiesOnForm";
	
}