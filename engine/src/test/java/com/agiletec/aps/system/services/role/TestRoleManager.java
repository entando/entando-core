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
package com.agiletec.aps.system.services.role;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @version 1.0
 * @author M.Casari
 */
public class TestRoleManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testAddUpdateDeletePermission() throws Throwable {
		Role role = new Role();
		role.setName("temp");
		try {
			_roleManager.removeRole(role);
			_roleManager.removePermission("temp");
		} catch (Throwable t) {
			throw t;
		}
		Permission permission = new Permission();
		permission.setName("temp");
		permission.setDescription("Permesso temporaneo");
		_roleManager.addPermission(permission);		
		role = new Role();
		role.setName("temp");
		role.setDescription("Ruolo temporaneo");
		role.addPermission("temp");
		_roleManager.addRole(role);
		
		this.getRolesAndPermissions();
		this.updateRoleAndPermission();
		this.getNewRolesAndPermissions();
		this.deleteRoleAndPermission();
		this.deletedRoleAndPermission();
	}
	
	private void getRolesAndPermissions() throws Throwable {
		Role role = this._roleManager.getRole("temp");
		assertNotNull(role);
		
		assertEquals(role.getDescription(), "Ruolo temporaneo");
		Iterator<Permission> iter = this._roleManager.getPermissions().iterator();
		boolean contains = false;
		while (iter.hasNext()) {
			Permission permission = (Permission) iter.next();
			if (permission.getName().equals("temp")) {
				contains = true;
			}
		}
		assertTrue(contains);
		Set<String> permissionSet = role.getPermissions();
		contains = permissionSet.contains("temp");
		assertTrue(contains);		
	}
	
	private void updateRoleAndPermission() throws Throwable {
		Role role = new Role();
		role.setName("temp");
		role.setDescription("Ruolo temporaneo 1");
		role.addPermission("temp");
		this._roleManager.updateRole(role);
		Permission permission = new Permission();
		permission.setName("temp");
		permission.setDescription("Permesso temporaneo 1");
		this._roleManager.updatePermission(permission);
	}
	
	private void getNewRolesAndPermissions() throws Throwable {
		Role role = this._roleManager.getRole("temp");
		assertEquals(role.getDescription(), "Ruolo temporaneo 1");
		Iterator<Permission> iter = this._roleManager.getPermissions().iterator();
		boolean contains = false;
		while (iter.hasNext()) {
			Permission permission = (Permission) iter.next();
			if (permission.getDescription().equals("Permesso temporaneo 1")) {
				contains = true;
			}
		}
		assertTrue(contains);
	}
	
	private void deleteRoleAndPermission() throws Throwable {
		Role role = new Role();
		role.setName("temp");
		role.setDescription("temp description");
		this._roleManager.updateRole(role);
		this._roleManager.removeRole(role);
		this._roleManager.removePermission("temp");
	} 	
	
	private void deletedRoleAndPermission() throws Throwable {
		Role role = this._roleManager.getRole("temp");
		assertNull(role);
		Iterator<Permission> iter = this._roleManager.getPermissions().iterator();
		boolean contains = false;
		while (iter.hasNext()) {
			Permission permission = iter.next();
			if (permission.getName().equals("temp")) {
				contains = true;
			}
		}
		assertFalse(contains);
	}
	
	public void testGetRolesWithPemission() throws Throwable {
    	String permission = Permission.SUPERVISOR;
    	List<Role> roles = this._roleManager.getRolesWithPermission(permission);
    	assertEquals(1, roles.size());
    	for (int i=0; i<roles.size(); i++) {
    		Role role = roles.get(i);
    		assertEquals("supervisor", role.getName());
    	}
    }
	
	private void init() throws Exception {
    	try {
    		_roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IRoleManager _roleManager = null;
	
}
