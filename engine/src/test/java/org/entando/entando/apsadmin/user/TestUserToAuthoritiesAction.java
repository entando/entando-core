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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.group.GroupManager;
import com.agiletec.aps.system.services.role.RoleManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Mezzano - E.Santoboni
 */
public class TestUserToAuthoritiesAction extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
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
		UserToAuthoritiesAction action = (UserToAuthoritiesAction) this.getAction();
		assertNotNull(action.getUsername());
		assertFalse(action.getGroups().isEmpty());
		assertFalse(action.getRoles().isEmpty());
		UserAuthsFormBean authbean = action.getUserAuthsFormBean();
		assertEquals(1, authbean.getGroups().size());
		assertEquals(1, authbean.getRoles().size());
	}
	
	public void testAddGroups() throws Throwable {
		// preparazione action
		String username = "newUser";
		String password = "newUser";
		this.addUserForTest(username, password, null, null);
		try {
			this.executeEdit("admin", username);
			// Nuovo gruppo
			String result = this.executeAddGroup("admin", "administrators");
			assertEquals(Action.SUCCESS, result);
			UserToAuthoritiesAction action = (UserToAuthoritiesAction) this.getAction();
			assertFalse(action.getGroups().isEmpty());
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			assertEquals(1, authbean.getGroups().size());
			
			// Gruppo inesistente
			result = this.executeAddGroup("admin", "nonexistantGroup");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(1, authbean.getGroups().size());
			
			// Nuovo gruppo
			result = this.executeAddGroup("admin", "customers");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(2, authbean.getGroups().size());
			
			// Gruppo già inserito
			result = this.executeAddGroup("admin", "administrators");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(2, authbean.getGroups().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testRemoveGroups() throws Throwable {
		// preparazione action
		String username = "newUser";
		String password = "newUser";
		this.addUserForTest(username, password, null, null);
		try {
			this.executeEdit("admin", username);
			UserToAuthoritiesAction action = (UserToAuthoritiesAction) this.getAction();
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			authbean.addGroup(this._groupManager.getGroup("administrators"));
			authbean.addGroup(this._groupManager.getGroup("customers"));
			
			// Rimozione gruppo
			String result = this.executeRemoveGroup("admin", "administrators");
			assertEquals(Action.SUCCESS, result);
			action = (UserToAuthoritiesAction) this.getAction();
			assertFalse(action.getGroups().isEmpty());
			authbean = action.getUserAuthsFormBean();
			assertEquals(1, authbean.getGroups().size());
			
			// Rimozione gruppo inesistente
			result = this.executeRemoveGroup("admin", "nonexistantGroup");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(1, authbean.getGroups().size());
			
			// Rimozione gruppo non presente
			result = this.executeRemoveGroup("admin", "administrators");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(1, authbean.getGroups().size());
			
			// Rimozione gruppo
			result = this.executeRemoveGroup("admin", "customers");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(0, authbean.getGroups().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testAddRoles() throws Throwable {
		// preparazione action
		String username = "newUser";
		String password = "newUser";
		this.addUserForTest(username, password, null, null);
		try {
			this.executeEdit("admin", username);
			
			// Nuovo ruolo
			String result = this.executeAddRole("admin", "admin");
			assertEquals(Action.SUCCESS, result);
			UserToAuthoritiesAction action = (UserToAuthoritiesAction) this.getAction();
			assertFalse(action.getRoles().isEmpty());
			UserAuthsFormBean authbean = action.getUserAuthsFormBean();
			assertEquals(1, authbean.getRoles().size());
			
			// Ruolo inesistente
			result = this.executeAddRole("admin", "nonexistantRole");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(1, authbean.getRoles().size());
			
			// Nuovo ruolo
			result = this.executeAddRole("admin", "editor");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(2, authbean.getRoles().size());
			
			// Ruolo già inserito
			result = this.executeAddRole("admin", "admin");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(2, authbean.getRoles().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testRemoveRoles() throws Throwable {
		// preparazione action
		String username = "newUser";
		String password = "newUser";
		this.addUserForTest(username, password, null, null);
		try {
			this.executeEdit("admin", username);
			
			UserAuthsFormBean authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			authbean.addRole(this._roleManager.getRole("admin"));
			authbean.addRole(this._roleManager.getRole("editor"));
			
			// Rimozione ruolo
			String result = this.executeRemoveRole("admin", "admin");
			assertEquals(Action.SUCCESS, result);
			UserToAuthoritiesAction action = (UserToAuthoritiesAction) this.getAction();
			assertFalse(action.getRoles().isEmpty());
			authbean = action.getUserAuthsFormBean();
			assertEquals(1, authbean.getRoles().size());
			
			// Rimozione ruolo inesistente
			result = this.executeRemoveRole("admin", "nonexistantRole");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(1, authbean.getRoles().size());
			
			// Rimozione ruolo non presente
			result = this.executeRemoveRole("admin", "admin");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(1, authbean.getRoles().size());
			
			// Rimozione ruolo
			result = this.executeRemoveRole("admin", "editor");
			assertEquals(Action.SUCCESS, result);
			authbean = ((UserToAuthoritiesAction) this.getAction()).getUserAuthsFormBean();
			assertEquals(0, authbean.getRoles().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testAddEdit() throws Throwable {
		String username = "username";
		String password = "password";
		List<IApsAuthority> groups = new ArrayList<IApsAuthority>();
		groups.add(this._groupManager.getGroup("customers"));
		List<IApsAuthority> roles = new ArrayList<IApsAuthority>();
		roles.add(this._roleManager.getRole("admin"));
		try {
			this.addUserForTest(username, password, roles, groups);
			UserDetails user = this.getUser(username, password);
			IApsAuthority[] authorities = user.getAuthorities();
			assertEquals(2, authorities.length);
			
			this.executeEdit("admin", username);
			this.executeRemoveGroup("admin", "customers");
			String result = this.executeSaveEdit("admin", username);
			assertEquals(Action.SUCCESS, result);
			
			user = this.getUser(username, password);
			authorities = user.getAuthorities();
			assertEquals(1, authorities.length);
			assertEquals("admin", authorities[0].getAuthority());
		} catch(Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	private String executeEdit(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Auth", "edit");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private String executeAddRole(String currentUser, String roleName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Auth", "addRole");
		this.addParameter("roleName", roleName);
		return this.executeAction();
	}
	
	private String executeRemoveRole(String currentUser, String roleName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Auth", "removeRole");
		this.addParameter("roleName", roleName);
		return this.executeAction();
	}
	
	private String executeAddGroup(String currentUser, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Auth", "addGroup");
		this.addParameter("groupName", groupName);
		return this.executeAction();
	}
	
	private String executeRemoveGroup(String currentUser, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Auth", "removeGroup");
		this.addParameter("groupName", groupName);
		return this.executeAction();
	}
	
	private String executeSaveEdit(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User/Auth", "save");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private void addUserForTest(String username, String password, List<IApsAuthority> roles, 
			List<IApsAuthority> groups) throws ApsSystemException {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		this._userManager.addUser(user);
		if (groups != null) {
			this._groupManager.setUserAuthorizations(username, groups);
		}
		if (roles != null) {
			this._roleManager.setUserAuthorizations(username, roles);
		}
	}
	
	private void init() {
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		this._roleManager = (RoleManager) this.getService(SystemConstants.ROLE_MANAGER);
		this._groupManager = (GroupManager) this.getService(SystemConstants.GROUP_MANAGER);
	}

	private IUserManager _userManager;
	private RoleManager _roleManager;
	private GroupManager _groupManager;
	
}
