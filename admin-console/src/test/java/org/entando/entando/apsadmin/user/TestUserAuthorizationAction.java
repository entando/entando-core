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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

import java.util.Collection;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class TestUserAuthorizationAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.addTestUserAndAuthorities();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.removeTestUserAndAuthorities();
		super.tearDown();
	}
	
	public void testFailureEdit() throws Throwable {
		// Utente non abilitato
		String result = this.executeEdit("developersConf", "developersConf");
		assertEquals("apslogin", result);
		
		// Modifica utente admin
		result = this.executeEdit("admin", "admin");
		assertEquals("userList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testEdit() throws Throwable {
		String result = this.executeEdit("admin", "mainEditor");
		assertEquals(Action.SUCCESS, result);
		UserAuthorizationAction action = (UserAuthorizationAction) this.getAction();
		assertNotNull(action.getUsername());
		assertFalse(action.getGroups().isEmpty());
		assertFalse(action.getRoles().isEmpty());
		UserAuthsFormBean authbean = action.getUserAuthsFormBean();
		assertEquals(1, authbean.getAuthorizations().size());
		
		result = this.executeEdit("admin", TEST_USER_NAME);
		assertEquals(Action.SUCCESS, result);
		action = (UserAuthorizationAction) this.getAction();
		assertNotNull(action.getUsername());
		assertFalse(action.getGroups().isEmpty());
		assertFalse(action.getRoles().isEmpty());
		authbean = action.getUserAuthsFormBean();
		assertEquals(2, authbean.getAuthorizations().size());
	}
	
	public void testAddAuthorization_1() throws Throwable {
		try {
			this.executeEdit("admin", TEST_USER_NAME);
			// New Authorization
			String result = this.executeAddAuthorization("admin", TEST_USER_NAME, "administrators", TEST_ROLE_NAME);
			assertEquals(Action.SUCCESS, result);
			UserAuthorizationAction action = (UserAuthorizationAction) this.getAction();
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			assertEquals(3, authbean.getAuthorizations().size());
			
			// New Authorization without role
			result = this.executeAddAuthorization("admin", TEST_USER_NAME, "helpdesk", null);
			assertEquals(Action.SUCCESS, result);
			action = (UserAuthorizationAction) this.getAction();
			authbean = action.getUserAuthsFormBean();
			assertEquals(4, authbean.getAuthorizations().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testAddAuthorization_2() throws Throwable {
		try {
			String result = this.executeAddAuthorization("admin", TEST_USER_NAME, Group.ADMINS_GROUP_NAME, TEST_ROLE_NAME);
			assertEquals("userList", result);
			UserAuthorizationAction action = (UserAuthorizationAction) this.getAction();
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			assertNull(authbean);
			
			this.executeEdit("admin", TEST_USER_NAME);
			
			// New Authorization with NULL Group
			result = this.executeAddAuthorization("admin", TEST_USER_NAME, null, "pageManager");
			assertEquals(Action.INPUT, result);
			action = (UserAuthorizationAction) this.getAction();
			assertEquals(1, action.getFieldErrors().size());
			assertNotNull(action.getFieldErrors().get("groupName"));
			authbean = action.getUserAuthsFormBean();
			assertEquals(2, authbean.getAuthorizations().size());
			
			// New Authorization with non-existant Group
			result = this.executeAddAuthorization("admin", TEST_USER_NAME, "nonexistantGroup", TEST_ROLE_NAME);
			assertEquals(Action.INPUT, result);
			action = (UserAuthorizationAction) this.getAction();
			assertEquals(1, action.getFieldErrors().size());
			assertNotNull(action.getFieldErrors().get("groupName"));
			authbean = action.getUserAuthsFormBean();
			assertEquals(2, authbean.getAuthorizations().size());
			
			// New Authorization with non-existant Role
			result = this.executeAddAuthorization("admin", TEST_USER_NAME, Group.ADMINS_GROUP_NAME, "nonexistantRole");
			assertEquals(Action.INPUT, result);
			action = (UserAuthorizationAction) this.getAction();
			assertEquals(1, action.getFieldErrors().size());
			assertNotNull(action.getFieldErrors().get("roleName"));
			authbean = action.getUserAuthsFormBean();
			assertEquals(2, authbean.getAuthorizations().size());
			
			// Already existint Authorization
			result = this.executeAddAuthorization("admin", TEST_USER_NAME, Group.FREE_GROUP_NAME, TEST_ROLE_NAME);
			assertEquals(Action.INPUT, result);
			action = (UserAuthorizationAction) this.getAction();
			assertEquals(1, action.getActionErrors().size());
			authbean = action.getUserAuthsFormBean();
			assertEquals(2, authbean.getAuthorizations().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testRemoveAuthorization_1() throws Throwable {
		try {
			this.executeEdit("admin", TEST_USER_NAME);
			
			String result = this.executeRemoveAuthorization("admin", TEST_USER_NAME, "1");
			assertEquals(Action.SUCCESS, result);
			UserAuthorizationAction action = (UserAuthorizationAction) this.getAction();
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			assertEquals(1, authbean.getAuthorizations().size());
			
			result = this.executeRemoveAuthorization("admin", TEST_USER_NAME, "0");
			assertEquals(Action.SUCCESS, result);
			action = (UserAuthorizationAction) this.getAction();
			authbean = action.getUserAuthsFormBean();
			assertEquals(0, authbean.getAuthorizations().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testRemoveAuthorization_2() throws Throwable {
		try {
			this.executeEdit("admin", TEST_USER_NAME);
			
			String result = this.executeRemoveAuthorization("admin", TEST_USER_NAME, null);
			assertEquals(Action.INPUT, result);
			UserAuthorizationAction action = (UserAuthorizationAction) this.getAction();
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			assertEquals(2, authbean.getAuthorizations().size());
			
			result = this.executeRemoveAuthorization("admin", TEST_USER_NAME, "7");
			assertEquals(Action.INPUT, result);
			action = (UserAuthorizationAction) this.getAction();
			authbean = action.getUserAuthsFormBean();
			assertEquals(2, authbean.getAuthorizations().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSave() throws Throwable {
		List<Authorization> authorizations = this._authorizationManager.getUserAuthorizations(TEST_USER_NAME);
		assertEquals(2, authorizations.size());
		try {
			String result = this.executeAddAuthorization("admin", TEST_USER_NAME, "administrators", TEST_ROLE_NAME);
			assertEquals(Action.SUCCESS, result);
			result = this.executeAddAuthorization("admin", TEST_USER_NAME, "helpdesk", null);
			assertEquals(Action.SUCCESS, result);
			result = this.executeSave("admin", TEST_USER_NAME);
			assertEquals(Action.SUCCESS, result);
			authorizations = this._authorizationManager.getUserAuthorizations(TEST_USER_NAME);
			assertEquals(4, authorizations.size());
		} catch(Throwable t) {
			throw t;
		}
	}
	
	private String executeEdit(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Authorization", "edit");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private String executeAddAuthorization(String currentUser, String username, String groupName, String roleName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Authorization", "addAuthorization");
		this.addParameter("username", username);
		this.addParameter("groupName", groupName);
		this.addParameter("roleName", roleName);
		return this.executeAction();
	}
	
	private String executeRemoveAuthorization(String currentUser, String username, String index) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Authorization", "removeAuthorization");
		this.addParameter("username", username);
		this.addParameter("index", index);
		return this.executeAction();
	}
	
	private String executeSave(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Authorization", "save");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private void addTestUserAndAuthorities() throws ApsSystemException {
		Group groupForTest = new Group();
		groupForTest.setName(TEST_GROUP_NAME);
		groupForTest.setDescription("group test description");
		this._groupManager.addGroup(groupForTest);
		
		Role roleForTest = new Role();
		roleForTest.setName(TEST_ROLE_NAME);
		roleForTest.setDescription("role test description");
		this._roleManager.addRole(roleForTest);
		
		User user = new User();
		user.setUsername(TEST_USER_NAME);
		user.setPassword(TEST_USER_PASSWORD);
		this._userManager.addUser(user);
		this._authorizationManager.addUserAuthorization(TEST_USER_NAME, Group.FREE_GROUP_NAME, TEST_ROLE_NAME);
		this._authorizationManager.addUserAuthorization(TEST_USER_NAME, TEST_GROUP_NAME, "admin");
	}
	
	private void removeTestUserAndAuthorities() throws ApsSystemException {
		this._userManager.removeUser(TEST_USER_NAME);
		Group groupForTest = this._groupManager.getGroup(TEST_GROUP_NAME);
		this._groupManager.removeGroup(groupForTest);
		Role roleForTest = this._roleManager.getRole(TEST_ROLE_NAME);
		this._roleManager.removeRole(roleForTest);
	}
	
	private void init() {
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		this._roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
		this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
		this._authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
	}
	
	private IUserManager _userManager;
	private IRoleManager _roleManager;
	private IGroupManager _groupManager;
	private IAuthorizationManager _authorizationManager;
	
	private static final String TEST_USER_NAME = "username_test";
	private static final String TEST_USER_PASSWORD = "password_test";
	private static final String TEST_GROUP_NAME = "group_test";
	private static final String TEST_ROLE_NAME = "role_test";
	
}