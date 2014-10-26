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
package com.agiletec.aps.system.services.authorization;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;

/**
 * @author E.Santoboni
 */
public class TestAuthorityManager extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetUsersByAuthority_1() throws Throwable {
		Role role = this.getRole("pageManager");
		List<String> usersByRole = this._authorizationManager.getUsersByAuthority(role);
		assertNotNull(usersByRole);
		assertTrue(usersByRole.size() >= 2);
		
		usersByRole = this._authorizationManager.getUsersByRole(role);
		assertNotNull(usersByRole);
		assertTrue(usersByRole.size() >= 2);
		
		List<String> usersByInvalidGroup = this._authorizationManager.getUsersByGroup(role);
		assertNull(usersByInvalidGroup);
		
		Group group = this.getGroup("coach");
		List<String> usersByGroup = this._authorizationManager.getUsersByAuthority(group);
		assertNotNull(usersByGroup);
		assertTrue(usersByGroup.size() >= 3);
		
		List<String> usersByNullGroup = this._authorizationManager.getUsersByAuthority(null);
		assertNull(usersByNullGroup);
		
		Group noExistingGroup = new Group();
		noExistingGroup.setName("test");
		noExistingGroup.setDescr("test");
		List<String> usersByInvaliGroup = this._authorizationManager.getUsersByGroup(noExistingGroup);
		assertNull(usersByInvaliGroup);
	}
	
	public void testGetUsersByAuthority_2() throws Throwable {
		Group groupForTest = this.createGroupForTest("pageManager");//name equal to an existing role
		try {
			((IGroupManager) this._groupManager).addGroup(groupForTest);
			Group group = this.getGroup(groupForTest.getName());
			assertNotNull(group);
			
			List<String> usersByGroup = this._authorizationManager.getUsersByAuthority(group);
			assertNotNull(usersByGroup);
			assertTrue(usersByGroup.isEmpty());
			usersByGroup = this._authorizationManager.getUsersByGroup(group);
			assertNotNull(usersByGroup);
			assertTrue(usersByGroup.isEmpty());
			
			List<String> usersByRole = this._authorizationManager.getUsersByRole(group);
			assertNull(usersByRole);
		} catch (Throwable t) {
			throw t;
		} finally {
			((IGroupManager) this._groupManager).removeGroup(groupForTest);
			Group group = this.getGroup(groupForTest.getName());
			assertNull(group);
		}
	}
	
	public void testSetRemoveUserAuthorization_1() throws Throwable {
		String username = "pageManagerCustomers";
		String groupName = "coach";
		String roleName = "pageManager";
		Group groupForTest = this.getGroup(groupName);
		assertNotNull(groupForTest);
		List<String> usersByGroup = this._authorizationManager.getUsersByAuthority(groupForTest);
		assertNotNull(usersByGroup);
		int initSize = usersByGroup.size();
		assertTrue(initSize >= 3);
		try {
			this._authorizationManager.addUserAuthorization(username, groupName, roleName);
			usersByGroup = this._authorizationManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(initSize + 1, usersByGroup.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._authorizationManager.deleteUserAuthorization(username, groupName, roleName);
			usersByGroup = this._authorizationManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(initSize, usersByGroup.size());
		}
	}
	
	public void testSetRemoveUserAuthorization_2() throws Throwable {
		String username = "pageManagerCustomers";
		String groupName = "testgroupname";
		String roleName = "pageManager";
		Group invalidGroupForTest = this.createGroupForTest(groupName);
		try {
			this._authorizationManager.addUserAuthorization(username, groupName, roleName);
			List<String> usersByGroup = this._authorizationManager.getUsersByGroup(invalidGroupForTest);
			assertNull(usersByGroup);
		} catch (Throwable t) {
			this._authorizationManager.deleteUserAuthorization(username, groupName, roleName);
			throw t;
		}
	}
	
	public void testSetRemoveUserAuthorizations_1() throws Throwable {
		String username = "pageManagerCustomers";
		String groupName = "management";
		String roleName = "pageManager";
		Group groupForTest = this.getGroup(groupName);
		assertNotNull(groupForTest);
		Role roleForTest = this.getRole(roleName);
		assertNotNull(roleForTest);
		List<String> usersByGroup = this._authorizationManager.getUsersByAuthority(groupForTest);
		assertNotNull(usersByGroup);
		assertEquals(0, usersByGroup.size());
		try {
			this._authorizationManager.addUserAuthorization(username, groupName, roleName);
			usersByGroup = this._authorizationManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(1, usersByGroup.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._authorizationManager.deleteUserAuthorization(username, groupName, roleName);
			usersByGroup = this._authorizationManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(0, usersByGroup.size());
		}
	}
	
	public void testSetRemoveUserAuthorizations_2() throws Throwable {
		String username = "pageManagerCustomers";
		String notExistentGroupName = "testgroupname";
		String existentGroupName = "management";
		String roleName = "pageManager";
		
		Role roleForTest = this.getRole(roleName);
		assertNotNull(roleForTest);
		Group existentGroup = this.getGroup(existentGroupName);//existent group
		assertNotNull(existentGroup);
		Group nonExistentGroup = this.createGroupForTest(notExistentGroupName);//nonexistent group
		List<String> usersByGroup = this._authorizationManager.getUsersByAuthority(existentGroup);
		assertNotNull(usersByGroup);
		assertEquals(0, usersByGroup.size());
		usersByGroup = this._authorizationManager.getUsersByGroup(nonExistentGroup);
		assertNull(usersByGroup);
		try {
			Authorization auth1 = new Authorization(existentGroup, roleForTest);
			Authorization auth2 = new Authorization(nonExistentGroup, roleForTest);
			List<Authorization> authorizations = new ArrayList<Authorization>();
			authorizations.add(auth1);
			authorizations.add(auth2);
			this._authorizationManager.addUserAuthorizations(username, authorizations);
			
			usersByGroup = this._authorizationManager.getUsersByAuthority(existentGroup);
			assertNotNull(usersByGroup);
			assertEquals(1, usersByGroup.size());
			usersByGroup = this._authorizationManager.getUsersByGroup(nonExistentGroup);
			assertNull(usersByGroup);
		} catch (Throwable t) {
			this._authorizationManager.deleteUserAuthorization(username, notExistentGroupName, roleName);
			throw t;
		} finally {
			this._authorizationManager.deleteUserAuthorization(username, existentGroupName, roleName);
			usersByGroup = this._authorizationManager.getUsersByAuthority(existentGroup);
			assertNotNull(usersByGroup);
			assertEquals(0, usersByGroup.size());
		}
	}
	
	public void testGetAuthorizationsByUser() throws Throwable {
		String username = "pageManagerCoach";
		List<Authorization> authorizations = this._authorizationManager.getUserAuthorizations(username);
		assertNotNull(authorizations);
		assertEquals(2, authorizations.size());
	}
	
	private Role getRole(String roleName) {
		return this._roleManager.getRole(roleName);
	}
    
	private Group getGroup(String groupName) {
		return this._groupManager.getGroup(groupName);
	}
	
	private Group createGroupForTest(String code) {
		Group groupForTest = new Group();
		groupForTest.setName(code);
		groupForTest.setDescr("Description");
		return groupForTest;
	}
    
	private void init() throws Exception {
    	try {
    		this._roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
    		this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
			this._authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
	private IRoleManager _roleManager = null;
	private IGroupManager _groupManager = null;
	private IAuthorizationManager _authorizationManager;
	
}
