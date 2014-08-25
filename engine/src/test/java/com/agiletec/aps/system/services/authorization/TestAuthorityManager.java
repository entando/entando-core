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
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

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
		List<UserDetails> usersByRole = this._roleManager.getUsersByAuthority(role);
		assertNotNull(usersByRole);
		assertTrue(usersByRole.size() >= 2);
		
		List<UserDetails> usersByInvalidGroup = this._groupManager.getUsersByAuthority(role);
		assertNull(usersByInvalidGroup);
		
		Group group = this.getGroup("coach");
		List<UserDetails> usersByGroup = this._groupManager.getUsersByAuthority(group);
		assertNotNull(usersByGroup);
		assertTrue(usersByGroup.size() >= 3);
		
		List<UserDetails> usersByNullGroup = this._groupManager.getUsersByAuthority(null);
		assertNull(usersByNullGroup);
		
		Group noExistingGroup = new Group();
		noExistingGroup.setName("test");
		noExistingGroup.setDescr("test");
		List<UserDetails> usersByInvaliGroup = this._groupManager.getUsersByAuthority(noExistingGroup);
		assertNull(usersByInvaliGroup);
	}
	
	public void testGetUsersByAuthority_2() throws Throwable {
		Group groupForTest = this.createGroupForTest("pageManager");//name equal to an existing role
		try {
			((IGroupManager) this._groupManager).addGroup(groupForTest);
			Group group = this.getGroup(groupForTest.getName());
			assertNotNull(group);
			
			List<UserDetails> usersByGroup = this._groupManager.getUsersByAuthority(group);
			assertNotNull(usersByGroup);
			assertEquals(0, usersByGroup.size());
			
			List<UserDetails> usersByRole = this._roleManager.getUsersByAuthority(group);
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
		Group groupForTest = this.getGroup("coach");
		assertNotNull(groupForTest);
		List<UserDetails> usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
		assertNotNull(usersByGroup);
		int initSize = usersByGroup.size();
		assertTrue(initSize >= 3);
		try {
			this._groupManager.setUserAuthorization(username, groupForTest);
			usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(initSize + 1, usersByGroup.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._groupManager.removeUserAuthorization(username, groupForTest);
			usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(initSize, usersByGroup.size());
		}
	}
	
	public void testSetRemoveUserAuthorization_2() throws Throwable {
		String username = "pageManagerCustomers";
		Group groupForTest = this.createGroupForTest("testgroupname");
		try {
			this._groupManager.setUserAuthorization(username, groupForTest);
			List<UserDetails> usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
			assertNull(usersByGroup);
		} catch (Throwable t) {
			this._groupManager.removeUserAuthorization(username, groupForTest);
			throw t;
		}
	}
	
	public void testSetRemoveUserAuthorizations_1() throws Throwable {
		String username = "pageManagerCustomers";
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		UserDetails user = userManager.getUser(username);
		List<IApsAuthority> authorities = new ArrayList<IApsAuthority>();
		List<IApsAuthority> joinedAuthority = this._groupManager.getAuthorizationsByUser(user);
		authorities.addAll(joinedAuthority);
		Group groupForTest = this.getGroup("management");
		assertNotNull(groupForTest);
		authorities.add(groupForTest);
		List<UserDetails> usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
		assertNotNull(usersByGroup);
		assertEquals(0, usersByGroup.size());
		try {
			this._groupManager.setUserAuthorizations(username, authorities);
			usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(1, usersByGroup.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._groupManager.removeUserAuthorization(username, groupForTest);
			usersByGroup = this._groupManager.getUsersByAuthority(groupForTest);
			assertNotNull(usersByGroup);
			assertEquals(0, usersByGroup.size());
		}
	}
	
	public void testSetRemoveUserAuthorizations_2() throws Throwable {
		String username = "pageManagerCustomers";
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		UserDetails user = userManager.getUser(username);
		List<IApsAuthority> authorities = new ArrayList<IApsAuthority>();
		List<IApsAuthority> joinedAuthority = this._groupManager.getAuthorizationsByUser(user);
		authorities.addAll(joinedAuthority);
		
		Group existentGroup = this.getGroup("management");//existent group
		assertNotNull(existentGroup);
		authorities.add(existentGroup);
		Group nonExistentGroup = this.createGroupForTest("testgroupname");//nonexistent group
		authorities.add(nonExistentGroup);
		List<UserDetails> usersByGroup = this._groupManager.getUsersByAuthority(existentGroup);
		assertNotNull(usersByGroup);
		assertEquals(0, usersByGroup.size());
		usersByGroup = this._groupManager.getUsersByAuthority(nonExistentGroup);
		assertNull(usersByGroup);
		try {
			this._groupManager.setUserAuthorizations(username, authorities);
			usersByGroup = this._groupManager.getUsersByAuthority(existentGroup);
			assertNotNull(usersByGroup);
			assertEquals(0, usersByGroup.size());
		} catch (Throwable t) {
			this._groupManager.setUserAuthorizations(username, joinedAuthority);
			throw t;
		}
	}
	
	public void testGetAuthorizationsByUser() throws Throwable {
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		UserDetails user = userManager.getUser("pageManagerCoach");
		List<IApsAuthority> groups = this._groupManager.getAuthorizationsByUser(user);
		assertNotNull(groups);
		assertEquals(2, groups.size());
		
		List<IApsAuthority> roles = this._roleManager.getAuthorizationsByUser(user);
		assertNotNull(roles);
		assertEquals(1, roles.size());
	}
	
	private Role getRole(String roleName) {
		return (Role) this._roleManager.getAuthority(roleName);
	}
    
	private Group getGroup(String groupName) {
		return (Group) this._groupManager.getAuthority(groupName);
	}
	
	private Group createGroupForTest(String code) {
		Group groupForTest = new Group();
		groupForTest.setName(code);
		groupForTest.setDescr("Description");
		return groupForTest;
	}
    
	private void init() throws Exception {
    	try {
    		this._roleManager = (IApsAuthorityManager) this.getService(SystemConstants.ROLE_MANAGER);
    		this._groupManager = (IApsAuthorityManager) this.getService(SystemConstants.GROUP_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
	private IApsAuthorityManager _roleManager = null;
	private IApsAuthorityManager _groupManager = null;
	
}
