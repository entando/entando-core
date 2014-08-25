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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class TestRoleAction extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNew() throws Throwable {
		// Utente non abilitato
		String result = this.executeNew("developersConf");
		assertEquals("apslogin", result);
		
		result = this.executeNew("admin");
		assertEquals(Action.SUCCESS, result);
		RoleAction roleAction = (RoleAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.ADD, roleAction.getStrutsAction());
	}
	
	public void testFailureEdit() throws Throwable {
		// Utente non autorizzato
		String result = this.executeEdit("developersConf", "editor");
		assertEquals("apslogin", result);
		
		// Ruolo inesistente
		result = this.executeEdit("admin", "ruoloInesistente");
		assertEquals("roleList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testEdit() throws Throwable {
		String roleName = "editor";
		String result = this.executeEdit("admin", roleName);
		assertEquals(Action.SUCCESS, result);
		
		RoleAction roleAction = (RoleAction) this.getAction();
		Role role = this._roleManager.getRole(roleName);
		assertEquals(ApsAdminSystemConstants.EDIT, roleAction.getStrutsAction());
		assertEquals(role.getName(), roleAction.getName());
		assertEquals(role.getDescription(), roleAction.getDescription());
		assertEquals(role.getPermissions().size(), roleAction.getPermissionNames().size());
		assertEquals(this._roleManager.getPermissions().size(), roleAction.getSystemPermissions().size());
	}
	
	public void testSaveNew() throws Throwable {
		String roleName = "newRole";
		String[] permissions = { "editContents" };
		try {
			String result = this.executeNew("admin");
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeSaveNew("admin", roleName, "roleDescription", permissions);
			assertEquals(Action.SUCCESS, result);
			this.checkRole(roleName, "roleDescription", permissions);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteRole(roleName);
		}
	}
	
	public void testSaveEdit() throws Throwable {
		String roleName = "newRole";
		String[] permissions = { "editContents" };
		try {
			this.addRole(roleName, "roleDescription", permissions);
			
			this.executeEdit("admin", roleName);
			String[] modifiedPermissions = new String[]{ "managePages", "superuser" };
			String result = this.executeSaveEdit("admin", roleName, "modifiedDescription", modifiedPermissions);
			assertEquals(Action.SUCCESS, result);
			this.checkRole(roleName, "modifiedDescription", modifiedPermissions);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteRole(roleName);
		}
	}
	
	public void testFailureSave() throws Throwable {
		this.executeNew("admin");
		String[] permissions = { "editContents" };
		
		// permessi non disponibili
		String result = this.executeSaveNew("developersConf", "roleName", "description", permissions);
		assertEquals("apslogin", result);
		
		// roleName già esistente
		result = this.executeSaveNew("admin", "editor", "description", permissions);
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErrors = action.getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> errors = fieldErrors.get("name");
		assertEquals(1, errors.size());
		
		// description non valorizzato
		result = this.executeSaveNew("admin", "roleName", "", permissions);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		errors = fieldErrors.get("description");
		assertEquals(1, errors.size());
		
		// name e description non valorizzati
		result = this.executeSaveNew("admin", "", "", permissions);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		errors = fieldErrors.get("name");
		assertEquals(1, errors.size());
		errors = fieldErrors.get("description");
		assertEquals(1, errors.size());
		
		// name troppo lungo
		result = this.executeSaveNew("admin", "roleNameDecisamenteTroppoLungo", "description", permissions);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		errors = fieldErrors.get("name");
		assertEquals(1, errors.size());
	}
	
	public void testTrash() throws Throwable {
		String roleName = "newRole";
		String[] permissions = { };
		try {
			this.addRole(roleName, "roleDescription", permissions);
			
			String result = this.executeTrash("admin", roleName);
			assertEquals(Action.SUCCESS, result);
			assertNotNull(this._roleManager.getRole(roleName));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteRole(roleName);
		}
	}
	
	public void testDelete() throws Throwable {
		String roleName = "newRole";
		String[] permissions = { };
		try {
			this.addRole(roleName, "roleDescription", permissions);
			
			String result = this.executeDelete("admin", roleName);
			assertEquals(Action.SUCCESS, result);
			assertNull(this._roleManager.getRole(roleName));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteRole(roleName);
		}
	}
	
	private String executeNew(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "new");
		return this.executeAction();
	}
	
	private String executeEdit(String currentUser, String roleName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "edit");
		this.addParameter("name", roleName);
		return this.executeAction();
	}
	
	private String executeSaveNew(String currentUser, String roleName, String descr, String[] permissionNames) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		this.addParameter("name", roleName);
		this.addParameter("description", descr);
		Set<String> perms = new HashSet<String>();
		for (int i=0; i<permissionNames.length; i++) {
			perms.add(permissionNames[i]);
		}
		this.addParameter("permissionNames", perms);
		return this.executeAction();
	}
	
	private String executeSaveEdit(String currentUser, String roleName, String descr, String[] permissionNames) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
		this.addParameter("name", roleName);
		this.addParameter("description", descr);
		Set<String> perms = new HashSet<String>();
		for (int i=0; i<permissionNames.length; i++) {
			perms.add(permissionNames[i]);
		}
		this.addParameter("permissionNames", perms);
		return this.executeAction();
	}
	
	private String executeTrash(String currentUser, String roleName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "trash");
		this.addParameter("name", roleName);
		return this.executeAction();
	}
	
	private String executeDelete(String currentUser, String roleName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "delete");
		this.addParameter("name", roleName);
		return this.executeAction();
	}
	
	private void checkRole(String roleName, String descr, String[] permissions) {
		Role role = this._roleManager.getRole(roleName);
		assertEquals(role.getName(), roleName);
		assertEquals(role.getDescription(), descr);
		Set<String> rolePerms = role.getPermissions();
		assertEquals(rolePerms.size(), permissions.length);
		for (int i=0; i<permissions.length; i++) {
			assertTrue(rolePerms.contains(permissions[i]));
		}
	}
	
	private void addRole(String roleName, String descr, String[] permissions) throws ApsSystemException {
		Role role = new Role();
		role.setName(roleName);
		role.setDescription(descr);
		for (int i=0; i<permissions.length; i++) {
			role.addPermission(permissions[i]);
		}
		this._roleManager.addRole(role);
	}
	
	private void deleteRole(String roleName) throws ApsSystemException {
		Role role = this._roleManager.getRole(roleName);
		if (role!=null) {
			this._roleManager.removeRole(role);
		}
	}
	
	private void init() {
		this._roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
	}
	
	private IRoleManager _roleManager;
	
}