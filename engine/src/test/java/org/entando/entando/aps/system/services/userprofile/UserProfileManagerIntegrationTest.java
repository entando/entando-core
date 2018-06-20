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
package org.entando.entando.aps.system.services.userprofile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.cache.CacheInfoManager;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;

/**
 * @author E.Santoboni
 */
public class UserProfileManagerIntegrationTest extends BaseTestCase {
	
	private IUserProfileManager profileManager;
	private IUserManager userManager;
    private CacheInfoManager cacheInfoManager;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testInitialize() {
		assertNotNull(this.profileManager);
	}
	
	public void testAttributeSupportObjects() throws Throwable {
		assertTrue(this.profileManager.getAttributeRoles().size()>=2);
		assertEquals(this.profileManager.getAttributeDisablingCodes().size(), 1);
	}
	
	public void testAddProfile_1() throws Throwable {
		String username = "admin";
		Date birthdate = this.getBirthdate(1982, 10, 25);
		IUserProfile profile = this.createProfile("stefano", "puddu", "spuddu@agiletec.it", birthdate, "it");
		try {
			this.profileManager.addProfile("admin", profile);
			IUserProfile added = this.profileManager.getProfile(username);
			assertEquals("spuddu@agiletec.it", added.getValue("email"));
			assertEquals(username, added.getUsername());
			MonoTextAttribute emailAttr = (MonoTextAttribute) ((IUserProfile) profile).getAttribute("email");
			emailAttr.setText("agiletectest@gmail.com");
			this.profileManager.updateProfile(profile.getUsername(), profile);
			IUserProfile updated = this.profileManager.getProfile(username);
			assertEquals("agiletectest@gmail.com", updated.getValue("email"));
			assertEquals(username, added.getUsername());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.profileManager.deleteProfile(username);
			assertNull(this.profileManager.getProfile(username));
		}
	}
	
	public void testAddProfile_2() throws Throwable {
		String username = "test_user";
		Date birthdate = this.getBirthdate(1982, 10, 25);
		IUserProfile profile = this.createProfile("joe", "black", "jblack@entando.com", birthdate, "en");
        profile.setId(username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(username);
		try {
            this.userManager.addUser(user);
			this.profileManager.addProfile(username, profile);
            
            Object cachedObject = this.cacheInfoManager.getFromCache(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfile_" + username);
            assertNull(cachedObject);
            
            UserDetails extractedUser = this.userManager.getUser(username);
            assertNotNull(extractedUser);
            assertNotNull(extractedUser.getProfile());
            IUserProfile extractedProfile = (IUserProfile) extractedUser.getProfile();
            assertEquals("jblack@entando.com", extractedProfile.getValue("email"));
            
            cachedObject = this.cacheInfoManager.getFromCache(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfile_" + username);
            assertNotNull(cachedObject);
            assertTrue(cachedObject instanceof IUserProfile);
            assertEquals("jblack@entando.com", ((IUserProfile) cachedObject).getValue("email"));
            
			IUserProfile added = this.profileManager.getProfile(username);
			assertEquals("jblack@entando.com", added.getValue("email"));
			assertEquals(username, added.getUsername());
            
			MonoTextAttribute emailAttr = (MonoTextAttribute) ((IUserProfile) profile).getAttribute("email");
			emailAttr.setText("jblack@gmail.com");
			this.profileManager.updateProfile(profile.getUsername(), profile);
            
            cachedObject = this.cacheInfoManager.getFromCache(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfile_" + username);
            assertNull(cachedObject);
            
			IUserProfile updated = this.profileManager.getProfile(username);
			assertEquals("jblack@gmail.com", updated.getValue("email"));
            
            cachedObject = this.cacheInfoManager.getFromCache(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfile_" + username);
            assertNotNull(cachedObject);
            assertTrue(cachedObject instanceof IUserProfile);
            assertEquals("jblack@gmail.com", ((IUserProfile) cachedObject).getValue("email"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.profileManager.deleteProfile(username);
			assertNull(this.profileManager.getProfile(username));
            this.userManager.removeUser(username);
            assertNull(this.cacheInfoManager.getFromCache(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfile_" + username));
		}
	}
	
	private IUserProfile createProfile(String name,	String surname, String email, Date birthdate, String language) {
		IUserProfile profile = this.profileManager.getDefaultProfileType();
		MonoTextAttribute nameAttr = (MonoTextAttribute) profile.getAttribute("fullname");
		nameAttr.setText(name + " " + surname);
		MonoTextAttribute emailAttr = (MonoTextAttribute) profile.getAttribute("email");
		emailAttr.setText(email);
		DateAttribute birthdateAttr = (DateAttribute) profile.getAttribute("birthdate");
		birthdateAttr.setDate(birthdate);
		MonoTextAttribute languageAttr = (MonoTextAttribute) profile.getAttribute("language");
		languageAttr.setText(language);
		((UserProfile) profile).setPublicProfile(true);
		return profile;
	}
	
	private Date getBirthdate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date birthdate = new Date(calendar.getTimeInMillis());
		return birthdate;
	}
	
	public void testRemoveInsesistentUser() throws ApsSystemException {
		assertNull(this.profileManager.getProfile("missing_user"));
		this.profileManager.deleteProfile("missing_user");
	}
	
	public void testSearchProfiles_1() throws Throwable {
		List<String> usernames = this.profileManager.searchId(null);
		assertNotNull(usernames);
    	assertEquals(4, usernames.size());
		EntitySearchFilter usernameFilter1 = new EntitySearchFilter(IUserProfileManager.ENTITY_ID_FILTER_KEY, false);
    	usernameFilter1.setOrder(EntitySearchFilter.Order.ASC);
		EntitySearchFilter[] filters1 = {usernameFilter1};
		usernames = this.profileManager.searchId(filters1);
		assertNotNull(usernames);
    	String[] expected1 = {"editorCoach", "editorCustomers", "mainEditor", "pageManagerCoach"};
    	assertEquals(expected1.length, usernames.size());
    	this.verifyOrder(usernames, expected1);
		EntitySearchFilter usernameFilter2 = new EntitySearchFilter(IUserProfileManager.ENTITY_ID_FILTER_KEY, false, "oa", true);
    	usernameFilter2.setOrder(EntitySearchFilter.Order.ASC);
		EntitySearchFilter[] filters2 = {usernameFilter2};
		usernames = this.profileManager.searchId(filters2);
		assertNotNull(usernames);
    	String[] expected2 = {"editorCoach", "pageManagerCoach"};
    	assertEquals(expected2.length, usernames.size());
    	this.verifyOrder(usernames, expected2);
	}
	
	public void testSearchProfiles_2() throws Throwable {
		EntitySearchFilter fullnameRoleFilter = EntitySearchFilter.createRoleFilter(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME);
		fullnameRoleFilter.setOrder(EntitySearchFilter.Order.ASC);
		EntitySearchFilter[] filters1 = {fullnameRoleFilter};
		List<String> usernames = this.profileManager.searchId(filters1);
		assertNotNull(usernames);
    	String[] expected1 = {"mainEditor", "pageManagerCoach", "editorCoach", "editorCustomers"};
		assertEquals(expected1.length, usernames.size());
    	this.verifyOrder(usernames, expected1);
		
		EntitySearchFilter fullnameRoleFilter2 = EntitySearchFilter.createRoleFilter(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME, "se", true);
		fullnameRoleFilter2.setOrder(EntitySearchFilter.Order.ASC);
		
		EntitySearchFilter[] filters2 = {fullnameRoleFilter2};
		usernames = this.profileManager.searchId(filters2);
		assertNotNull(usernames);
    	String[] expected2 = {"mainEditor", "editorCustomers"};
		assertEquals(expected2.length, usernames.size());
    	this.verifyOrder(usernames, expected2);
		
		EntitySearchFilter fullnameRoleFilter3 = EntitySearchFilter.createRoleFilter(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME, "se", true);
		EntitySearchFilter usernameFilter3 = new EntitySearchFilter(IUserProfileManager.ENTITY_ID_FILTER_KEY, false);
    	usernameFilter3.setOrder(EntitySearchFilter.Order.ASC);
		EntitySearchFilter[] filters3 = {fullnameRoleFilter3, usernameFilter3};
		usernames = this.profileManager.searchId(filters3);
		assertNotNull(usernames);
    	String[] expected3 = {"editorCustomers", "mainEditor"};
		assertEquals(expected3.length, usernames.size());
    	this.verifyOrder(usernames, expected3);
	}
	
	public void testSearchProfileRecords() throws Throwable {
		List<ApsEntityRecord> records = this.profileManager.searchRecords(null);
		assertNotNull(records);
    	assertEquals(4, records.size());
		
		EntitySearchFilter usernameFilter1 = new EntitySearchFilter(IUserProfileManager.ENTITY_ID_FILTER_KEY, false);
    	usernameFilter1.setOrder(EntitySearchFilter.Order.ASC);
		EntitySearchFilter[] filters1 = {usernameFilter1};
		records = this.profileManager.searchRecords(filters1);
		assertNotNull(records);
    	String[] expected1 = {"editorCoach", "editorCustomers", "mainEditor", "pageManagerCoach"};
    	assertEquals(expected1.length, records.size());
    	this.verifyRecordOrder(records, expected1);
		
		EntitySearchFilter usernameFilter2 = new EntitySearchFilter(IUserProfileManager.ENTITY_ID_FILTER_KEY, false, "oa", true);
    	usernameFilter2.setOrder(EntitySearchFilter.Order.ASC);
		EntitySearchFilter[] filters2 = {usernameFilter2};
		records = this.profileManager.searchRecords(filters2);
		assertNotNull(records);
    	String[] expected2 = {"editorCoach", "pageManagerCoach"};
    	assertEquals(expected2.length, records.size());
    	this.verifyRecordOrder(records, expected2);
	}
	
    private void verifyOrder(List<String> usernames, String[] order) {
    	for (int i=0; i<usernames.size(); i++) {
    		assertEquals(order[i], usernames.get(i));
    	}
	}
    
    private void verifyRecordOrder(List<ApsEntityRecord> records, String[] order) {
    	for (int i=0; i<records.size(); i++) {
			ApsEntityRecord record = records.get(i);
    		assertEquals(order[i], record.getId());
    	}
	}
    
	private void init() throws Exception {
    	try {
    		this.profileManager = (IUserProfileManager) this.getService(SystemConstants.USER_PROFILE_MANAGER);
    		this.userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		this.cacheInfoManager = (CacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }
	
}
