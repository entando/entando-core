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
package com.agiletec.apsadmin.user.role;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.*;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.user.AbstractAuthorityAction;
import org.slf4j.*;

import java.util.*;

/**
 * Classe action della gestione Ruoli.
 */
public class RoleAction extends AbstractAuthorityAction {

	private static final Logger logger = LoggerFactory.getLogger(RoleAction.class);
	private static final String ERROR_ROLE_NOT_EXIST = "error.role.notExist";
	private static final String ROLE_LIST = "roleList";


	private int strutsAction;
	private String name;
	private String description;
	private Set<String> permissionNames;

	private List<String> references;

	private IRoleManager roleManager;


	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			this.checkDuplicatedRole();
		} else if (!this.existsRole()) {
			addActionError(getText(ERROR_ROLE_NOT_EXIST));
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
				addActionError(getText(ERROR_ROLE_NOT_EXIST));
				return ROLE_LIST;
			}
			Role role = this.getRoleManager().getRole(this.getName());
			this.setDescription(role.getDescription());
			this.setPermissionNames(role.getPermissions());
		} catch (Throwable t) {
			logger.error("error in edit", t);
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
			logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Replaced by {@link #showDetail()}
	 */
	@Deprecated
	public String view() {
		return this.showDetail();
	}
	
	public String showDetail() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			if (!this.existsRole()) {
				addActionError(getText(ERROR_ROLE_NOT_EXIST));
				return ROLE_LIST;
			}
			Role role = this.getRoleManager().getRole(this.getName());
			this.setDescription(role.getDescription());
			this.setPermissionNames(role.getPermissions());
			this.isRoleInUse();
		} catch (Throwable t) {
			logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			String result = this.checkRoleForDelete();
			if (null != result) return result;
		} catch (Throwable t) {
			logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			String result = this.checkRoleForDelete();
			if (null != result) return result;
			Role role = roleManager.getRole(this.getName());
			roleManager.removeRole(role);
		} catch (Throwable t) {
			logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Verifica l'esistenza del ruolo.
	 * @return true in caso positivo, false nel caso il ruolo non esista.
	 */
	protected boolean existsRole() {
		return (name != null) && (roleManager.getRole(name) != null);
	}
	
	/**
	 * Verifica l'utilizzo del ruolo.
	 * @return true in caso positivo, false nel caso il ruolo non sia utilizzato.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected boolean isRoleInUse() throws ApsSystemException {
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
			addActionError(getText(ERROR_ROLE_NOT_EXIST));
			return ROLE_LIST;
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
		return strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this.strutsAction = strutsAction;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<String> getPermissionNames() {
		return permissionNames;
	}
	public void setPermissionNames(Set<String> permissionNames) {
		this.permissionNames = permissionNames;
	}
	
	public Set<Permission> getRolePermissions() {
		Set<Permission> rolePermissions = new HashSet<>(permissionNames.size());
		for (String permissionName : permissionNames) {
			Permission permission = this.getRoleManager().getPermission(permissionName);
			rolePermissions.add(permission);
		}
		return rolePermissions;
	}
	
	public List<Permission> getSystemPermissions() {
		return this.getRoleManager().getPermissions();
	}
	
	public List<String> getReferences() {
		return references;
	}
	protected void setReferences(List<String> references) {
		this.references = references;
	}
	
	protected IRoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
}