/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.role.RoleManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;

/**
 * @author E.Santoboni
 */
public class TestAuthenticationProviderManager extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetUser() throws Throwable {
    	UserDetails adminUser = this._authenticationProvider.getUser("admin", "admin");//nel database di test, username e password sono uguali
    	assertEquals("admin", adminUser.getUsername());
    	assertEquals("admin", adminUser.getPassword());
    	assertEquals(1, adminUser.getAuthorizations().size());
    	
    	adminUser = this._authenticationProvider.getUser("admin", "wrongPassword");
    	assertNull(adminUser);
    	
    	UserDetails nullUser = this._authenticationProvider.getUser("wrongUserName", "wrongPassword");
    	assertNull(nullUser);
    }
    
    public void testUpdateUserAuthorities() throws Throwable {
    	String username = "UserForTest2";
    	String password = "PasswordForTest2";
    	this.addUserForTest(username, password);
    	UserDetails extractedUser = null;
		try {
			extractedUser = this._authenticationProvider.getUser(username, password);
			assertEquals(username, extractedUser.getUsername());
			assertNotNull(extractedUser);
			assertEquals(1, extractedUser.getAuthorizations().size());
			
			this._authorizationManager.addUserAuthorization(username, Group.FREE_GROUP_NAME, "admin");
			
			extractedUser = this._authenticationProvider.getUser(username, password);
			assertNotNull(extractedUser);
			assertEquals(2, extractedUser.getAuthorizations().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(extractedUser);
			extractedUser = this._userManager.getUser(username);
			assertNull(extractedUser);
		}
	}
    
    public void testGetUserWithPrivacyModuleEnabled() throws Throwable {
    	String username = "MEMisUserExpired";
    	String password = "123456";
    	
    	this.addUserForTest(username, password);
    	MockUserDAO mockUserDao = new MockUserDAO(this._dataSource);
    	try {
    		boolean privacyModuleStatus = this.getPrivacyModuleStatus();
    		assertTrue(!privacyModuleStatus);
    		this.togglePrivacyModuleStatus(true);
    		privacyModuleStatus = this.getPrivacyModuleStatus();
    		assertTrue(privacyModuleStatus);
    		UserDetails user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(1, user.getAuthorizations().size());
    		
    		// change the last access date  
    		mockUserDao.setLastAccessDate(username, DateConverter.parseDate("02/06/1977", "dd/MM/yyyy"));
    		// reload user auths
    		user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertTrue(!user.isAccountNotExpired());
    		assertEquals(0, user.getAuthorizations().size());
    		
    		mockUserDao.setLastAccessDate(username, new Date());
    		assertTrue(!user.isAccountNotExpired());
    		assertEquals(0, user.getAuthorizations().size());
    		
    		user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertTrue(user.isAccountNotExpired());
    		assertEquals(1, user.getAuthorizations().size());
    		
    	} catch (Throwable t) {
    		throw t;
    	} finally {
    		this.togglePrivacyModuleStatus(false);
    		this._userManager.removeUser(username);
    		UserDetails verify = this._userManager.getUser(username);
    		assertNull(verify);
    	}
    }
    
    public void testAuthWithPrivacyModuleEnabled() throws Throwable {
    	String username = "MEMhasAuthExpired";
    	String password = "123456";
    	String newPassword  = "EequalsMsquareC";
    	Calendar pastDate = Calendar.getInstance();
    	pastDate.add(Calendar.MONTH, -4);
    	this.addUserForTest(username, password);
    	MockUserDAO mockUserDao = new MockUserDAO(this._dataSource);
    	try {
    		boolean privacyModuleStatus = this.getPrivacyModuleStatus();
    		assertTrue(!privacyModuleStatus);
    		this.togglePrivacyModuleStatus(true);
    		privacyModuleStatus = this.getPrivacyModuleStatus();
    		assertTrue(privacyModuleStatus);
    		UserDetails user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(1, user.getAuthorizations().size());
    		assertTrue(user.isAccountNotExpired());
    		assertTrue(user.isCredentialsNotExpired());
    		
    		// change the last password date 
    		mockUserDao.setLastPasswordChange(username, pastDate.getTime());
    		// check credentials
    		user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(0, user.getAuthorizations().size());
    		assertTrue(user.isAccountNotExpired());    		
    		assertTrue(!user.isCredentialsNotExpired());
    		
    		// change password
    		this._userManager.changePassword(username, newPassword);
    		user = this._authenticationProvider.getUser(username, newPassword);
    		assertNotNull(user);
    		assertEquals(1, user.getAuthorizations().size());
    		assertTrue(user.isAccountNotExpired());
    		assertTrue(user.isCredentialsNotExpired());
    	} catch (Throwable t) {
    		throw t;
    	} finally {
    		this.togglePrivacyModuleStatus(false);
    		this._userManager.removeUser(username);
    		UserDetails verify = this._userManager.getUser(username);
    		assertNull(verify);
    	}
    }
        
    public void testUpdateRoleWithPrivacyModuleEnabled() throws Throwable {
    	String username = "MEMisToUpdateRole";
    	String password = "123456";    	
    	this.addUserForTest(username, password);
    	try {
    		boolean privacyModuleStatus = this.getPrivacyModuleStatus();
    		assertTrue(!privacyModuleStatus);
    		this.togglePrivacyModuleStatus(true);
    		privacyModuleStatus = this.getPrivacyModuleStatus();
    		assertTrue(privacyModuleStatus);
    		UserDetails user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(1, user.getAuthorizations().size());
    		
    		// update role
    		Role adminRole = this._roleManager.getRole("admin");
			Group freeGroup = this._groupManager.getGroup(Group.FREE_GROUP_NAME);
			this._authorizationManager.addUserAuthorization(username, Group.FREE_GROUP_NAME, "admin");
			
			// verify role
			user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(2, user.getAuthorizations().size());
    	} catch (Throwable t) {
    		throw t;
    	} finally {
    		this.togglePrivacyModuleStatus(false);
    		this._userManager.removeUser(username);
    		UserDetails verify = this._userManager.getUser(username);
    		assertNull(verify);
    	}
    }
    
    /**
     * Toggle the privacy module on or off
     * @param enable if true 'enables' the privacy module whereas 'false' disables it 
     */    
    private void togglePrivacyModuleStatus(boolean enable) throws Throwable {
    	try {
    		String originalParams = this._configurationManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
    		assertNotNull(originalParams);
    		Map<String, String> systemParams = SystemParamsUtils.getParams(originalParams);
    		String status = enable ? "true":"false";
    		systemParams.put("extendedPrivacyModuleEnabled", status);
    		String newXmlParams = SystemParamsUtils.getNewXmlParams(originalParams, systemParams);
        	this._configurationManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
    	} catch (Throwable t) {
    		throw t;
    	}
    }
    
    /**
     * Get the status of the privacy module
     * @return 'tre' if the module is enabled, false otherwise
     * @throws Throwable
     */
    private boolean getPrivacyModuleStatus() throws Throwable {
    	Boolean status = false;
    	try {
    		String originalParams = this._configurationManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
    		assertNotNull(originalParams);
    		Map<String, String> systemParams = SystemParamsUtils.getParams(originalParams);
    		status = systemParams.containsKey("extendedPrivacyModuleEnabled") && systemParams.get("extendedPrivacyModuleEnabled").trim().equalsIgnoreCase("true");
    	} catch (Throwable t) {
    		throw t;
    	}
    	return status;
    }
    
    private void init() throws Exception {
    	try {
    		this._dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		this._authenticationProvider = (IAuthenticationProviderManager) this.getService(SystemConstants.AUTHENTICATION_PROVIDER_MANAGER); 
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		this._roleManager = (RoleManager) this.getService(SystemConstants.ROLE_MANAGER);
    		this._groupManager = (GroupManager) this.getService(SystemConstants.GROUP_MANAGER);
    		this._configurationManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
			this._authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
		} catch (Throwable t) {
			throw new Exception(t);
		}
    }
    
    private void addUserForTest(String username, String password) throws Throwable {
    	MockUser user = new MockUser();
		user.setUsername(username);
        user.setPassword(password);
        Authorization auth = new Authorization(this._groupManager.getGroup(Group.FREE_GROUP_NAME), 
				this._roleManager.getRole("editor"));
		user.addAuthorization(auth);
        this._userManager.removeUser(user);
		UserDetails extractedUser = _userManager.getUser(username);
		assertNull(extractedUser);
		this._userManager.addUser(user);
		this._authorizationManager.addUserAuthorization(username, auth);
	}
    
    private IAuthenticationProviderManager _authenticationProvider = null;
	private IUserManager _userManager = null;
	private ConfigInterface _configurationManager = null;
	private DataSource _dataSource = null;
	
	private RoleManager _roleManager = null;
	private GroupManager _groupManager = null;
	
	private IAuthorizationManager _authorizationManager;
	
}