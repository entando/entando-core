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
package com.agiletec.apsadmin.user.role;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Classe action della gestione Ruoli.
 * @version 1.0
 * @author E.Mezzano - E.Santoboni
 */
public class RoleAction extends BaseAction implements IRoleAction {

	private static final Logger _logger = LoggerFactory.getLogger(RoleAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			this.checkDuplicatedRole();
		} else if (!this.existsRole()) {
			this.addActionError(this.getText("error.role.notExist"));
		}
	}
	
	/**
	 * Esegue in fase di aggiunta la verifica sulla duplicazione del ruolo.<br />
	 * Nel caso la verifica risulti negativa aggiunge un fieldError.
	 */
	protected void checkDuplicatedRole() {
		if (this.existsRole()) {
			String[] args = {this.getName()};
			this.addFieldError("name", this.getText("error.role.duplicated", args));
		}
	}
	
	@Override
	public String newRole() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
	@Override
	public String edit() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			if (!this.existsRole()) {
				this.addActionError(this.getText("error.role.notExist"));
				return "roleList";
			}
			Role role = this.getRoleManager().getRole(this.getName());
			this.setDescription(role.getDescription());
			this.setPermissionNames(role.getPermissions());
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			//ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			Role role = this.createRole();
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getRoleManager().addRole(role);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getRoleManager().updateRole(role);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			//ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String view() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			if (!this.existsRole()) {
				this.addActionError(this.getText("error.role.notExist"));
				return "roleList";
			}
			Role role = this.getRoleManager().getRole(this.getName());
			this.setDescription(role.getDescription());
			this.setPermissionNames(role.getPermissions());
		} catch (Throwable t) {
			_logger.error("error in view", t);
			//ApsSystemUtils.logThrowable(t, this, "view");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String trash() {
		try {
			if (!this.checkRoleForDelete()) {
				return "roleList";
			}
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			//ApsSystemUtils.logThrowable(t, this, "trash");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			if (!this.checkRoleForDelete()) {
				return "roleList";
			}
			IRoleManager roleManager = this.getRoleManager();
			Role role = roleManager.getRole(this.getName());
			roleManager.removeRole(role);
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			//ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Verifica l'esistenza del ruolo.
	 * @return true in caso positivo, false nel caso il ruolo non esista.
	 */
	protected boolean existsRole() {
		String name = this.getName();
		boolean exists = (name != null 
				&& name.trim().length()>=0 
				&& this.getRoleManager().getRole(name) != null);
		return exists;
	}
	
	/**
	 * Verifica l'utilizzo del ruolo.
	 * @return true in caso positivo, false nel caso il ruolo non sia utilizzato.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected boolean isRoleInUse() throws ApsSystemException {
		IRoleManager roleManager = this.getRoleManager();
		Role role = roleManager.getRole(this.getName());
		boolean roleInUse = roleManager.getRoleUses(role)>0;
		return roleInUse;
	}
	
	/**
	 * Esegue i controlli necessari per la cancellazione di un ruolo. Imposta gli opportuni messaggi di errore come actionMessages.
	 * Restituisce l'esito del controllo.
	 * @return true in caso di cancellazione consentita, false in caso contrario.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected boolean checkRoleForDelete() throws ApsSystemException {
		if (!this.existsRole()) {
			this.addActionError(this.getText("error.role.notExist"));
			return false;
		} else if (this.isRoleInUse()) {
			this.addActionError(this.getText("error.role.used"));
			return false;
		}
		return true;
	}
	
	/**
	 * Prepara il ruolo con i dati ricevuti dal form.
	 * @return Il ruolo popolato con tutti i dati.
	 */
	protected Role createRole() {
		Role role = new Role();
		role.setName(this.getName());
		role.setDescription(this.getDescription());
		Set<String> permissionNames = this.getPermissionNames();
		if (permissionNames != null) {
			role.getPermissions().addAll(permissionNames);
		}
		return role;
	}
	
	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public Set<String> getPermissionNames() {
		return _permissionNames;
	}
	public void setPermissionNames(Set<String> permissionNames) {
		this._permissionNames = permissionNames;
	}
	
	public Set<Permission> getRolePermissions() {
		Set<String> permissionNames = this.getPermissionNames();
		Set<Permission> rolePermissions = new HashSet<Permission>(permissionNames.size());
		Iterator<String> permsIter = permissionNames.iterator();
		while (permsIter.hasNext()) {
			String permissionName = permsIter.next();
			Permission permission = this.getRoleManager().getPermission(permissionName);
			rolePermissions.add(permission);
		}
		return rolePermissions;
	}
	
	public List<Permission> getSystemPermissions() {
		return this.getRoleManager().getPermissions();
	}
	
	protected IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	
	private int _strutsAction;
	private String _name;
	private String _description;
	private Set<String> _permissionNames;
	
	private IRoleManager _roleManager;
	
}