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
package com.agiletec.aps.system.services.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.role.RoleManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.admin.SystemParamsUtils;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestAuthenticationProviderManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetUser() throws Throwable {
    	UserDetails adminUser = this._authenticationProvider.getUser("admin", "admin");//nel database di test, username e password sono uguali
    	assertEquals("admin", adminUser.getUsername());
    	assertEquals("admin", adminUser.getPassword());
    	assertEquals(2, adminUser.getAuthorities().length);
    	
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
			assertEquals(2, extractedUser.getAuthorities().length);
			
			Role adminRole = this._roleManager.getRole("admin");
			this._roleManager.setUserAuthorization(username, adminRole);
			
			extractedUser = this._authenticationProvider.getUser(username, password);
			assertNotNull(extractedUser);
			assertEquals(3, extractedUser.getAuthorities().length);
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
    		assertEquals(2, user.getAuthorities().length);
    		
    		// change the last access date  
    		mockUserDao.setLastAccessDate(username, DateConverter.parseDate("02/06/1977", "dd/MM/yyyy"));
    		// reload user auths
    		user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertTrue(!user.isAccountNotExpired());
    		assertEquals(0, user.getAuthorities().length);
    		
    		mockUserDao.setLastAccessDate(username, new Date());
    		assertTrue(!user.isAccountNotExpired());
    		assertEquals(0, user.getAuthorities().length);
    		
    		user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertTrue(user.isAccountNotExpired());
    		assertEquals(2, user.getAuthorities().length);
    		
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
    		assertEquals(2, user.getAuthorities().length);
    		assertTrue(user.isAccountNotExpired());
    		assertTrue(user.isCredentialsNotExpired());
    		
    		// change the last password date 
    		mockUserDao.setLastPasswordChange(username, pastDate.getTime());
    		// check credentials
    		user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(0, user.getAuthorities().length);
    		assertTrue(user.isAccountNotExpired());    		
    		assertTrue(!user.isCredentialsNotExpired());
    		
    		// change password
    		this._userManager.changePassword(username, newPassword);
    		user = this._authenticationProvider.getUser(username, newPassword);
    		assertNotNull(user);
    		assertEquals(2, user.getAuthorities().length);
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
    		assertEquals(2, user.getAuthorities().length);
    		
    		// update role
    		Role adminRole = this._roleManager.getRole("admin");
			this._roleManager.setUserAuthorization(username, adminRole);
			// verify role
			user = this._authenticationProvider.getUser(username, password);
    		assertNotNull(user);
    		assertEquals(3, user.getAuthorities().length);
    		
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
		} catch (Throwable t) {
			throw new Exception(t);
		}
    }
    
    private void addUserForTest(String username, String password) throws Throwable {
    	MockUser user = new MockUser();
		user.setUsername(username);
        user.setPassword(password);
        user.addRole(this._roleManager.getRole("editor"));
        user.addGroup(this._groupManager.getGroup(Group.FREE_GROUP_NAME));
        
        this._userManager.removeUser(user);
		UserDetails extractedUser = _userManager.getUser(username);
		assertNull(extractedUser);
		this._userManager.addUser(user);
        
		this._roleManager.setUserAuthorizations(username, new ArrayList<IApsAuthority>(user.getRoles()));
		this._groupManager.setUserAuthorizations(username, new ArrayList<IApsAuthority>(user.getGroups()));		
	}
    
    private IAuthenticationProviderManager _authenticationProvider = null;
	private IUserManager _userManager = null;
	private ConfigInterface _configurationManager = null;
	private DataSource _dataSource = null;
	
	private RoleManager _roleManager = null;
	private GroupManager _groupManager = null;
	
}