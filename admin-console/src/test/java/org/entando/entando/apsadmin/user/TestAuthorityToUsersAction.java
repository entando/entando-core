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

import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @version 1.0
 * @author E.Mezzano - E.Santoboni
 */
public class TestAuthorityToUsersAction extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testFailureAddRemoveUserToGroup() throws Throwable {
		String result = this.executeAddUserToGroup("developersConf", "administrators", "editorCoach");
		assertEquals("apslogin", result);
		
		result = this.executeRemoveUserToGroup("developersConf", "administrators", "editorCoach");
		assertEquals("apslogin", result);
	}
	
	public void testAddRemoveUserToGroup() throws Throwable {
		String authName = "administrators";
		String username = "editorCoach";
		IApsAuthority auth = this._groupManager.getAuthority(authName);
		List<UserDetails> users = this._groupManager.getUsersByAuthority(auth);
		String[] preTestAuthUsers = { "admin", "mainEditor" };
		String[] modifiedAuthUsers = { "admin", "mainEditor", username };
		this.verifyAuthUserList(users, preTestAuthUsers);
		
		String result = this.executeAddUserToGroup("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._groupManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, modifiedAuthUsers);
		
		// Aggiunto utente già presente
		result = this.executeAddUserToGroup("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._groupManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, modifiedAuthUsers);
		
		result = this.executeRemoveUserToGroup("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._groupManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, preTestAuthUsers);
		
		// Rimosso utente non presente
		result = this.executeRemoveUserToGroup("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._groupManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, preTestAuthUsers);
	}
	
	public void testFailureAddRemoveUserToRole() throws Throwable {
		String result = this.executeRemoveUserToRole("developersConf", "pageManager", "editorCoach");
		assertEquals("apslogin", result);
		
		result = this.executeAddUserToRole("developersConf", "pageManager", "editorCoach");
		assertEquals("apslogin", result);
	}
	
	public void testAddRemoveUserToRole() throws Throwable {
		String authName = "pageManager";
		String username = "editorCoach";
		IApsAuthority auth = this._roleManager.getAuthority(authName);
		List<UserDetails> users = this._roleManager.getUsersByAuthority(auth);
		String[] preTestAuthUsers = { "pageManagerCoach", "pageManagerCustomers" };
		String[] modifiedAuthUsers = { "pageManagerCoach", "pageManagerCustomers", username };
		this.verifyAuthUserList(users, preTestAuthUsers);
		
		String result = this.executeAddUserToRole("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._roleManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, modifiedAuthUsers);
		
		// Aggiunto utente già presente
		result = this.executeAddUserToRole("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._roleManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, modifiedAuthUsers);
		
		result = this.executeRemoveUserToRole("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._roleManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, preTestAuthUsers);
		
		// Rimosso utente non presente
		result = this.executeRemoveUserToRole("admin", authName, username);
		assertEquals(Action.SUCCESS, result);
		users = this._roleManager.getUsersByAuthority(auth);
		this.verifyAuthUserList(users, preTestAuthUsers);
	}
	
	public void testAddRemoveSystemAdminToRole() throws Throwable {
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		UserDetails adminUser = userManager.getUser(SystemConstants.ADMIN_USER_NAME);
		List<IApsAuthority> auths = this._roleManager.getAuthorizationsByUser(adminUser);
		assertEquals(1, auths.size());
		String result = this.executeAddUserToRole("admin", "pageManager", SystemConstants.ADMIN_USER_NAME);
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(1, action.getActionErrors().size());
		auths = this._roleManager.getAuthorizationsByUser(adminUser);
		assertEquals(1, auths.size());
	}
	
	private void verifyAuthUserList(List<UserDetails> users, String[] authUsers) {
		assertEquals(users.size(), authUsers.length);
		Iterator<UserDetails> authIter = users.iterator();
		while (authIter.hasNext()) {
			UserDetails user = authIter.next();
			boolean verified = false;
			for (int i=0; i<authUsers.length; i++) {
				if (user.getUsername().equals(authUsers[i])) {
					verified = true;
					break;
				}
			}
			assertTrue(verified);
		}
	}
	
	private String executeAddUserToGroup(String currentUser, String groupName, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group/Auth", "addUser");
		this.addParameter("authName", groupName);
		this.addParameter("usernameToSet", username);
		return this.executeAction();
	}
	
	private String executeRemoveUserToGroup(String currentUser, String groupName, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group/Auth", "removeUser");
		this.addParameter("usernameToSet", username);
		this.addParameter("authName", groupName);
		return this.executeAction();
	}
	
	private String executeAddUserToRole(String currentUser, String roleName, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role/Auth", "addUser");
		this.addParameter("authName", roleName);
		this.addParameter("usernameToSet", username);
		return this.executeAction();
	}
	
	private String executeRemoveUserToRole(String currentUser, String roleName, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role/Auth", "removeUser");
		this.addParameter("usernameToSet", username);
		this.addParameter("authName", roleName);
		return this.executeAction();
	}
	
	private void init() {
		this._roleManager = (IApsAuthorityManager) this.getService(SystemConstants.ROLE_MANAGER);
		this._groupManager = (IApsAuthorityManager) this.getService(SystemConstants.GROUP_MANAGER);
	}
	
	private IApsAuthorityManager _roleManager;
	private IApsAuthorityManager _groupManager;
	
}