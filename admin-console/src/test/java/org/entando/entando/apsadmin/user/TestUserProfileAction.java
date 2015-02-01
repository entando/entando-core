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
package org.entando.entando.apsadmin.user;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestUserProfileAction extends ApsAdminBaseTestCase {
    
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testEditProfile_1() throws Throwable {
    	this.setUserOnSession("admin");
        this.initAction("/do/userprofile", "edit");
		this.addParameter("username", USERNAME_FOR_TEST);
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(UserProfileAction.USERPROFILE_ON_SESSION);
        assertNotNull(currentUserProfile);
		assertEquals(USERNAME_FOR_TEST, currentUserProfile.getUsername());
    }
    
    public void testEditProfile_2() throws Throwable {
    	this.setUserOnSession("admin");
        this.initAction("/do/userprofile", "edit");
		this.addParameter("username", "editorCustomers");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(UserProfileAction.USERPROFILE_ON_SESSION);
        assertNotNull(currentUserProfile);
        assertEquals("editorCustomers", currentUserProfile.getUsername());
    }
	
    public void testEditProfile_3() throws Throwable {
		IUserProfile prototype = this._profileManager.getDefaultProfileType();
		prototype.setTypeCode("XXX");
		((IEntityTypesConfigurer) this._profileManager).addEntityPrototype(prototype);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/userprofile", "edit");
			this.addParameter("username", USERNAME_FOR_TEST);
			String result = this.executeAction();
			assertEquals("chooseType", result);
			IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(UserProfileAction.USERPROFILE_ON_SESSION);
			assertNull(currentUserProfile);
		} catch (Throwable t) {
			throw t;
		} finally {
			((IEntityTypesConfigurer) this._profileManager).removeEntityPrototype("XXX");
		}
    }
    
    public void testSaveNewEmptyProfile() throws Throwable {
    	this.setUserOnSession("admin");
		try {
			this.initAction("/do/userprofile", "saveEmpty");
			this.addParameter("username", USERNAME_FOR_TEST);
			this.addParameter("profileTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(UserProfileAction.USERPROFILE_ON_SESSION);
			assertNull(currentUserProfile);
			assertNotNull(this._profileManager.getProfile(USERNAME_FOR_TEST));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._profileManager.deleteProfile(USERNAME_FOR_TEST);
		}
    }
    
    public void testNewProfile() throws Throwable {
    	this.setUserOnSession("admin");
		try {
			this.initAction("/do/userprofile", "saveAndContinue");
			this.addParameter("username", USERNAME_FOR_TEST);
			this.addParameter("profileTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(UserProfileAction.USERPROFILE_ON_SESSION);
			assertNotNull(currentUserProfile);
			assertEquals(USERNAME_FOR_TEST, currentUserProfile.getUsername());
			assertNotNull(this._profileManager.getProfile(USERNAME_FOR_TEST));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._profileManager.deleteProfile(USERNAME_FOR_TEST);
		}
    }
    
    public void testValidateNewProfile() throws Throwable {
    	this.setUserOnSession("admin");
        this.initAction("/do/userprofile", "saveAndContinue");
		this.addParameter("username", USERNAME_FOR_TEST);
        this.addParameter("profileTypeCode", "XXX");
        String result = this.executeAction();
        assertEquals(Action.INPUT, result);
        Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
        assertEquals(1, fieldErrors.size());
        assertEquals(1, fieldErrors.get("profileTypeCode").size());
    }
    
    public void testValidateProfile() throws Throwable {
    	this.setUserOnSession("admin");
        this.initAction("/do/userprofile", "edit");
		this.addParameter("username", "editorCustomers");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
		
        this.initAction("/do/userprofile", "save");
		this.addParameter("Monotext:fullname", "");
        result = this.executeAction();
        assertEquals(Action.INPUT, result);
		
        ActionSupport action = this.getAction();
        assertEquals(1, action.getFieldErrors().size());
		
        this.initAction("/do/userprofile", "save");
        this.addParameter("Monotext:fullname", "Ronald Rossi");
        this.addParameter("Monotext:email", "");
        this.addParameter("Date:birthdate", "25/09/1972");
        this.addParameter("Monotext:language", "it");
        result = this.executeAction();
        assertEquals(Action.INPUT, result);
		
        action = this.getAction();
        assertEquals(1, action.getFieldErrors().size());
        assertEquals(1, ((List<String>) action.getFieldErrors().get("Monotext:email")).size());
		
        IUserProfile currentUserProfile = (IUserProfile) this.getRequest().getSession().getAttribute(UserProfileAction.USERPROFILE_ON_SESSION);
        assertNotNull(currentUserProfile);
        assertEquals("editorCustomers", currentUserProfile.getUsername());
        assertEquals("Ronald Rossi", currentUserProfile.getValue("fullname"));
    }
	
    public void testSaveProfile() throws Throwable {
    	this.setUserOnSession("admin");
    	this.initAction("/do/userprofile", "saveAndContinue");
		this.addParameter("username", USERNAME_FOR_TEST);
        this.addParameter("profileTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
        try {
            this.initAction("/do/userprofile", "save");
            this.addParameter("Monotext:fullname", "Eugenio Montale");
            this.addParameter("Monotext:email", "eugenioxxxxx@xxxx.it");
            this.addParameter("Date:birthdate", "12/10/1896");
            this.addParameter("Monotext:language", "it");
            result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
			
            IUserProfile userProfile = this._profileManager.getProfile(USERNAME_FOR_TEST);
            assertNotNull(userProfile);
            assertEquals("Eugenio Montale", userProfile.getValue("fullname"));
        } catch (Throwable t) {
            throw t;
        } finally {
            this._profileManager.deleteProfile(USERNAME_FOR_TEST);
            IUserProfile userProfile = this._profileManager.getProfile(USERNAME_FOR_TEST);
            assertNull(userProfile);
        }
    }
	
    private void init() throws Exception {
        try {
            this._profileManager = (IUserProfileManager) this.getService(SystemConstants.USER_PROFILE_MANAGER);
            this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
            User user = this.createUserForTest(USERNAME_FOR_TEST);
            this._userManager.addUser(user);
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
    
	@Override
    protected void tearDown() throws Exception {
        this._userManager.removeUser(USERNAME_FOR_TEST);
        super.tearDown();
    }
    
    protected User createUserForTest(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(username);
        return user;
    }
    
    private IUserProfileManager _profileManager;
    private IUserManager _userManager = null;
    private static final String USERNAME_FOR_TEST = "userprofile_testUser";
    
}
