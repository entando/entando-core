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
package com.agiletec.plugins.jacms.aps.system.services.content.authorization;

import java.util.ArrayList;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.RoleManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.MockUser;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author E.Santoboni
 */
public class TestContentAuthorization extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testCheckAdminUser() throws Throwable {
    	UserDetails adminUser = this.getUser("admin");
    	assertEquals("admin", adminUser.getUsername());
    	assertEquals("admin", adminUser.getPassword());
    	assertEquals(2, adminUser.getAuthorities().length);
    	
    	IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    	Content content = contentManager.loadContent("ART111", true);
    	boolean check = this._authorizationManager.isAuth(adminUser, content);
    	assertTrue(check);
    	content = contentManager.loadContent("EVN25", true);
    	check = this._authorizationManager.isAuth(adminUser, content);
    	assertTrue(check);
    	content = contentManager.loadContent("EVN41", true);
    	check = this._authorizationManager.isAuth(adminUser, content);
    	assertTrue(check);
    }
    
    public void testCheckCustomerUser() throws Throwable {
    	UserDetails extractedUser = this.getUser("pageManagerCustomers");
    	assertEquals("pageManagerCustomers", extractedUser.getUsername());
    	assertEquals("pageManagerCustomers", extractedUser.getPassword());
    	assertEquals(2, extractedUser.getAuthorities().length);
    	
		IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    	Content content = contentManager.loadContent("ART111", true);
    	boolean checkContent = this._authorizationManager.isAuth(extractedUser, content);
    	assertTrue(checkContent);
    	content = contentManager.loadContent("EVN25", true);
    	checkContent = this._authorizationManager.isAuth(extractedUser, content);
    	assertTrue(checkContent);
    	content = contentManager.loadContent("EVN41", true);
    	checkContent = this._authorizationManager.isAuth(extractedUser, content);
    	assertFalse(checkContent);
    }
    
    /*
     * This test is fully inherited from the original TestAuthorizationManager in jAPS2 (2.0.6)
     */
    public void testCheckNewUser() throws Throwable {
    	String username = "UserForTest";
    	String password = "PasswordForTest";
    	this.addUserForTest(username, password);
    	UserDetails extractedUser = null;
		try {
			extractedUser = this.getUser(username, password);
			assertEquals(username, extractedUser.getUsername());
			assertNotNull(extractedUser);
			assertEquals(2, extractedUser.getAuthorities().length);
			
			Group group = this._groupManager.getGroup("coach");
    		boolean checkGroup = this._authorizationManager.isAuth(extractedUser, group);
    		assertFalse(checkGroup);
    		group = this._groupManager.getGroup(Group.FREE_GROUP_NAME);
    		checkGroup = this._authorizationManager.isAuth(extractedUser, group);
    		assertTrue(checkGroup);
    		
    		boolean checkPermission = this._authorizationManager.isAuthOnPermission(extractedUser, Permission.SUPERVISOR);
    		assertFalse(checkPermission);
    		checkPermission = this._authorizationManager.isAuthOnPermission(extractedUser, Permission.SUPERUSER);
    		assertFalse(checkPermission);
    		checkPermission = this._authorizationManager.isAuthOnPermission(extractedUser, Permission.BACKOFFICE);
    		assertTrue(checkPermission);
    		checkPermission = this._authorizationManager.isAuthOnPermission(extractedUser, "editContents");
    		assertTrue(checkPermission);
    		
	    	IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
	    	Content content = contentManager.loadContent("ART111", true);
	    	boolean checkContent = this._authorizationManager.isAuth(extractedUser, content);
	    	assertFalse(checkContent);
	    	content = contentManager.loadContent("EVN25", true);
	    	checkContent = this._authorizationManager.isAuth(extractedUser, content);
	    	assertTrue(checkContent);
	    	content = contentManager.loadContent("EVN41", true);
	    	checkContent = this._authorizationManager.isAuth(extractedUser, content);
	    	assertFalse(checkContent);
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != extractedUser) {
				this._userManager.removeUser(extractedUser);
			}
			extractedUser = this._userManager.getUser(username);
			assertNull(extractedUser);
		}
	}
    
    private void addUserForTest(String username, String password) throws Throwable {
    	MockUser user = new MockUser();
		user.setUsername(username);
        user.setPassword(password);
        user.setDisabled(false);
        user.addRole(this._roleManager.getRole("editor"));
        user.addGroup(this._groupManager.getGroup(Group.FREE_GROUP_NAME));
        this._userManager.removeUser(user);
		UserDetails extractedUser = _userManager.getUser(username);
		assertNull(extractedUser);
		this._userManager.addUser(user);
		this._roleManager.setUserAuthorizations(username, new ArrayList<IApsAuthority>(user.getRoles()));
		this._groupManager.setUserAuthorizations(username, new ArrayList<IApsAuthority>(user.getGroups()));
	}
    
    private void init() throws Exception {
    	try {
    		this._authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		this._roleManager = (RoleManager) this.getService(SystemConstants.ROLE_MANAGER);
    		this._groupManager = (GroupManager) this.getService(SystemConstants.GROUP_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
    }
    
    private IAuthorizationManager _authorizationManager;
    private IUserManager _userManager = null;
	private RoleManager _roleManager = null;
	private GroupManager _groupManager = null;
	
}
