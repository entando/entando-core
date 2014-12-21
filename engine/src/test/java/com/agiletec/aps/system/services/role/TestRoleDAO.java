/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.role;

import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;

/**
 * @version 1.0
 * @author M.Diana
 */
public class TestRoleDAO extends BaseTestCase {

    public void testAddUpdateDeleteRole() throws Throwable {
    	DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		RoleDAO roleDAO = new RoleDAO();
		roleDAO.setDataSource(dataSource);
		Role role = new Role();
		role.setName("temp");
		try {
			roleDAO.deleteRole(role);
        } catch (Throwable t) {
        	throw t;
        }
		role.setDescription("temp");
		role.addPermission(Permission.SUPERVISOR);        
        try {
        	roleDAO.addRole(role);
        } catch (Throwable t) {
        	throw t;
        }
		Map<String, Role> roles = null;
        try {
        	roles = roleDAO.loadRoles();
        } catch (Throwable t) {
        	throw t;
        }
        assertTrue(roles.containsKey("temp"));
        role = (Role)roles.get("temp");
        assertEquals(role.getDescription(), "temp");
        assertTrue(role.getPermissions().contains(Permission.SUPERVISOR));
        this.updateRole(roleDAO);
        this.deleteRole(roleDAO);
	}
	
	private void updateRole(RoleDAO roleDAO) throws Throwable {
		Role role = new Role();
		role.setName("temp");
		role.setDescription("temp1");
		role.addPermission(Permission.CONFIG); 
        try {
			roleDAO.updateRole(role);
        } catch (Throwable t) {
        	throw t;
        }
		Map<String, Role> roles = null;
        try {
        	roles = roleDAO.loadRoles();
        } catch (Throwable t) {
        	throw t;
        }
        assertTrue(roles.containsKey("temp"));
        role = (Role) roles.get("temp");
        assertEquals(role.getDescription(), "temp1");
        assertTrue(role.getPermissions().contains(Permission.CONFIG));
	}
	
	private void deleteRole(RoleDAO roleDAO) throws Throwable {
		Role role = new Role();
		role.setName("temp");
        try {
			roleDAO.deleteRole(role);
        } catch (Throwable t) {
        	throw t;
        }
		Map<String, Role> roles = null;
        try {
        	roles = roleDAO.loadRoles();
        } catch (Throwable t) {
        	throw t;
        }
        assertFalse(roles.containsKey("temp"));   
	}	
    	
}
