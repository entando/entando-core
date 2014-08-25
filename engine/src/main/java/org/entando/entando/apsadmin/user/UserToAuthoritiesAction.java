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
package org.entando.entando.apsadmin.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Classe action delegata alla gestione delle operazioni di associazione 
 * tra utenza e autorizzazioni.
 * @author E.Santoboni - E.Mezzano
 */
public class UserToAuthoritiesAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(UserToAuthoritiesAction.class);
	
	public String edit() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			String username = this.getUsername();
			UserDetails user = this.getUserManager().getUser(username);
			List<IApsAuthority> groups = ((IApsAuthorityManager) this.getGroupManager()).getAuthorizationsByUser(user);
			List<IApsAuthority> roles = ((IApsAuthorityManager) this.getRoleManager()).getAuthorizationsByUser(user);
			this.setUsername(user.getUsername());
			UserAuthsFormBean userAuthsFormBean = new UserAuthsFormBean(username, roles, groups);
			//FIXME MODIFICARE FUNZIONALITA' PER RIMUOVERE L'UTILIZZO DELLA SESSIONE
			this.getRequest().getSession().setAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME,  userAuthsFormBean);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			//ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			String username = this.getUsername();
			UserAuthsFormBean authsBean = this.getUserAuthsFormBean();
			if (!username.equals(authsBean.getUsername())) {
				throw new RuntimeException("ERRORE INATTESO: Username in Bean non corrispondente a campo di form!");
			}
			List<IApsAuthority> roles = authsBean.getRoles();
			((IApsAuthorityManager) this.getRoleManager()).setUserAuthorizations(username, roles);
			List<IApsAuthority> groups = authsBean.getGroups();
			((IApsAuthorityManager) this.getGroupManager()).setUserAuthorizations(username, groups);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			//ApsSystemUtils.logThrowable(t, this, "save");
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
	
	public String addGroup() {
		try {
			String groupName = this.getGroupName();
			IApsAuthority group = this.getGroupManager().getGroup(groupName);
			if (group != null) {
				this.getUserAuthsFormBean().addGroup(group);
			}
		} catch (Throwable t) {
			_logger.error("error in addGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "addGroup");
			return FAILURE;
		}
		this.setSection(GROUP_SECTION);
		return SUCCESS;
	}
	
	public String addRole() {
		try {
			String roleName = this.getRoleName();
			IApsAuthority role = this.getRoleManager().getRole(roleName);
			if (role != null) {
				this.getUserAuthsFormBean().addRole(role);
			}
		} catch (Throwable t) {
			_logger.error("error in addRole", t);
			//ApsSystemUtils.logThrowable(t, this, "addRole");
			return FAILURE;
		}
		this.setSection(ROLE_SECTION);
		return SUCCESS;
	}
	
	public String removeGroup() {
		try {
			IApsAuthority group = this.getGroupManager().getGroup(this.getGroupName());
			if (group != null) {
				this.getUserAuthsFormBean().removeGroup(group);
			}
		} catch (Throwable t) {
			_logger.error("error in removeGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "removeGroup");
			return FAILURE;
		}
		this.setSection(GROUP_SECTION);
		return SUCCESS;
	}
	
	public String removeRole() {
		try {
			IApsAuthority role = this.getRoleManager().getRole(this.getRoleName());
			if (role != null) {
				this.getUserAuthsFormBean().removeRole(role);
			}
		} catch (Throwable t) {
			_logger.error("error in removeRole", t);
			//ApsSystemUtils.logThrowable(t, this, "removeRole");
			return FAILURE;
		}
		this.setSection(ROLE_SECTION);
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
	
	public UserAuthsFormBean getUserAuthsFormBean() {
		return (UserAuthsFormBean) this.getRequest().getSession().getAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME);
	}
	
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}
	
	public List<Role> getRoles() {
		return this.getRoleManager().getRoles();
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
	
	public String getSection() {
		return _section;
	}
	public void setSection(String section) {
		this._section = section;
	}
	
	private IUserManager _userManager;
	private IRoleManager _roleManager;
	private IGroupManager _groupManager;
	
	private String _username;
	
	private String _roleName;
	private String _groupName;
	
	private String _section;
	
	private static final String GROUP_SECTION = "groups";
	private static final String ROLE_SECTION = "roles";
	
	public static final String CURRENT_FORM_USER_AUTHS_PARAM_NAME = "currentUserOnForm";
	
}