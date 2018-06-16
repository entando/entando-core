/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.parse.attribute.MonoTextAttributeHandler;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.User;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.junit.Assert;
import static org.mockito.Mockito.when;

public class UserProfileManagerAspectTest {

	@InjectMocks
	private UserProfileManagerAspect userProfileManagerAspect;
    
	@Mock
	private UserProfileManager userProfileManager;
    
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
    
    @Test
    public void testInjectProfile_1() throws ApsSystemException {
        //IUserProfile mock = this.createFakeProfile("TMP");
        IUserProfile returned = this.createFakeProfile("test", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
        when(userProfileManager.getProfile(Mockito.anyString())).thenReturn(returned);
        
        User user = new User();
        user.setUsername("test");
        Assert.assertNull(user.getProfile());
        
        userProfileManagerAspect.injectProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(1)).getProfile("test");
        IUserProfile profile = (IUserProfile) user.getProfile();
        Assert.assertNotNull(profile);
        Assert.assertEquals("test", profile.getUsername());
    }
    
    @Test
    public void testInjectProfile_2() throws ApsSystemException {
        when(userProfileManager.getProfile(Mockito.anyString())).thenThrow(ApsSystemException.class);
        
        User user = new User();
        user.setUsername("test");
        Assert.assertNull(user.getProfile());
        
        userProfileManagerAspect.injectProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(1)).getProfile("test");
        Assert.assertNull(user.getProfile());
    }
    
    @Test
    public void testInjectProfile_3() throws ApsSystemException {
        when(userProfileManager.getProfile(Mockito.anyString())).thenThrow(ApsSystemException.class);
        
        IUserProfile profile = this.createFakeProfile("test", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
        User user = new User();
        user.setUsername("test");
        user.setProfile(profile);
        
        userProfileManagerAspect.injectProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(0)).getProfile("test");
        Assert.assertNotNull(user.getProfile());
        Assert.assertSame(profile, user.getProfile());
    }
    
    @Test
    public void testAddProfile_1() throws ApsSystemException {
        IUserProfile profile = this.createFakeProfile("test", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
        User user = new User();
        user.setUsername("test");
        user.setProfile(profile);
        userProfileManagerAspect.addProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(1)).addProfile("test", profile);
    }
    
    @Test
    public void testAddProfile_2() throws ApsSystemException {
        User user = new User();
        user.setUsername("test");
        userProfileManagerAspect.addProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(0)).addProfile(Mockito.anyString(), Mockito.any(IUserProfile.class));
    }
    
    @Test
    public void testUpdateProfile_1() throws ApsSystemException {
        IUserProfile profile = this.createFakeProfile("test", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
        User user = new User();
        user.setUsername("test");
        user.setProfile(profile);
        userProfileManagerAspect.updateProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(1)).updateProfile("test", profile);
    }
    
    @Test
    public void testUpdateProfile_2() throws ApsSystemException {
        User user = new User();
        user.setUsername("test");
        userProfileManagerAspect.updateProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(0)).updateProfile(Mockito.anyString(), Mockito.any(IUserProfile.class));
    }
    
    @Test
    public void testDeleteProfile_1() throws ApsSystemException {
        User user = new User();
        user.setUsername("test");
        userProfileManagerAspect.deleteProfile(user);
        Mockito.verify(userProfileManager, Mockito.times(1)).deleteProfile("test");
    }
    
    @Test
    public void testDeleteProfile_2() throws ApsSystemException {
        userProfileManagerAspect.deleteProfile("test");
        Mockito.verify(userProfileManager, Mockito.times(1)).deleteProfile("test");
    }
    
    @Test
    public void testDeleteProfile_3() throws ApsSystemException {
        userProfileManagerAspect.deleteProfile(null);
        Mockito.verify(userProfileManager, Mockito.times(0)).deleteProfile("test");
    }
    
	private IUserProfile createFakeProfile(String username, String defaultProfileTypeCode) {
		UserProfile userProfile = new UserProfile();
        userProfile.setId(username);
		MonoTextAttribute monoTextAttribute = new MonoTextAttribute();
		monoTextAttribute.setName("Name");
		monoTextAttribute.setHandler(new MonoTextAttributeHandler());
		userProfile.addAttribute(monoTextAttribute);
		userProfile.setTypeCode(defaultProfileTypeCode);
		return userProfile;
	}
    
}
