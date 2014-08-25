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
package org.entando.entando.apsadmin.common.currentuser;

import java.util.List;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestCurrentUserProfileAction extends ApsAdminBaseTestCase {
    
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testEditProfile_1() throws Throwable {
    	this.setUserOnSession(USERNAME_FOR_TEST);
        this.initAction("/do/currentuser/profile", "edit");
        String result = this.executeAction();
        assertEquals("currentUserWithoutProfile", result);
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
        assertNull(currentUserProfile);
    }
    
    public void testEditProfile_2() throws Throwable {
    	this.setUserOnSession("editorCustomers");
        this.initAction("/do/currentuser/profile", "edit");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
        assertNotNull(currentUserProfile);
        assertEquals("editorCustomers", currentUserProfile.getUsername());
    }
    
    public void testValidateProfile() throws Throwable {
    	this.setUserOnSession("editorCustomers");
        this.initAction("/do/currentuser/profile", "edit");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
		
        this.initAction("/do/currentuser/profile", "save");
		this.addParameter("Monotext:fullname", "");
        this.addParameter("Monotext:email", "");
        result = this.executeAction();
        assertEquals(Action.INPUT, result);
		
        ActionSupport action = this.getAction();
        assertEquals(2, action.getFieldErrors().size());
		
        this.initAction("/do/currentuser/profile", "save");
        this.addParameter("Monotext:fullname", "Ronald Rossi");
        this.addParameter("Monotext:email", "");
        this.addParameter("Date:birthdate", "25/09/1972");
        this.addParameter("Monotext:language", "it");
        result = this.executeAction();
        assertEquals(Action.INPUT, result);
		
        action = this.getAction();
        assertEquals(1, action.getFieldErrors().size());
        assertEquals(1, ((List<String>) action.getFieldErrors().get("Monotext:email")).size());

        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
        assertNotNull(currentUserProfile);
        assertEquals("editorCustomers", currentUserProfile.getUsername());
        assertEquals("Ronald Rossi", currentUserProfile.getValue("fullname"));
    }
    
    private void init() throws Exception {
        try {
            this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
            User user = this.createUserForTest(USERNAME_FOR_TEST);
            this._userManager.addUser(user);
            this._roleManager = (IApsAuthorityManager) this.getService(SystemConstants.ROLE_MANAGER);
            this._role = (Role) this._roleManager.getAuthority("editor");
            this._roleManager.setUserAuthorization(USERNAME_FOR_TEST, this._role);
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
    
	@Override
    protected void tearDown() throws Exception {
        this._roleManager.removeUserAuthorization(USERNAME_FOR_TEST, this._role);
        this._userManager.removeUser(USERNAME_FOR_TEST);
        super.tearDown();
    }
    
    protected User createUserForTest(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(username);
        return user;
    }
    
    private IUserManager _userManager = null;
    private static final String USERNAME_FOR_TEST = "userprofile_testUser";
    private IApsAuthorityManager _roleManager;
    private IApsAuthority _role = null;
    
}