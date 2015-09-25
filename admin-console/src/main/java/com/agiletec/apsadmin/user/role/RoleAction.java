/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import com.agiletec.apsadmin.user.AbstractAuthorityAction;

/**
 * Classe action della gestione Ruoli.
 * @author E.Mezzano - E.Santoboni
 */
public class RoleAction extends AbstractAuthorityAction {

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
	
	public String newRole() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
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
			return FAILURE;
		}
		return SUCCESS;
	}
	
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
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Deprecated
	public String view() {
		return this.showDetail();
	}
	
	public String showDetail() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			if (!this.existsRole()) {
				this.addActionError(this.getText("error.role.notExist"));
				return "roleList";
			}
			Role role = this.getRoleManager().getRole(this.getName());
			this.setDescription(role.getDescription());
			this.setPermissionNames(role.getPermissions());
			this.isRoleInUse();
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			String result = this.checkRoleForDelete();
			if (null != result) return result;
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			String result = this.checkRoleForDelete();
			if (null != result) return result;
			IRoleManager roleManager = this.getRoleManager();
			Role role = roleManager.getRole(this.getName());
			roleManager.removeRole(role);
		} catch (Throwable t) {
			_logger.error("error in delete", t);
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
		List<String> usernames = super.getAuthorizationManager().getUsersByRole(role, false);
		this.setReferences(usernames);
		return (null != usernames && !usernames.isEmpty());
	}
	
	/**
	 * Esegue i controlli necessari per la cancellazione di un ruolo. 
	 * Imposta gli opportuni messaggi di errore come actionMessages.
	 * Restituisce l'esito del controllo.
	 * @return il codice del risultato.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected String checkRoleForDelete() throws ApsSystemException {
		if (!this.existsRole()) {
			this.addActionError(this.getText("error.role.notExist"));
			return "roleList";
		} else if (this.isRoleInUse()) {
			this.addActionError(this.getText("error.role.used"));
			return "references";
		}
		return null;
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
	
	public List<String> getReferences() {
		return _references;
	}
	protected void setReferences(List<String> references) {
		this._references = references;
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
	
	private List<String> _references;
	
	private IRoleManager _roleManager;
	
}