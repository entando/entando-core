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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.common.entity.parse.IEntityTypeFactory;
import com.agiletec.aps.system.common.entity.parse.attribute.MonoTextAttributeHandler;
import com.agiletec.aps.system.common.notify.INotifyManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedEvent;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.entando.entando.aps.system.services.userprofile.parse.UserProfileTypeDOM;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class UserProfileManagerTest {

	@InjectMocks
	private UserProfileManager userProfileManager;

	@Mock
	private IEntityTypeFactory entityTypeFactory;

	@Mock
	private UserProfileTypeDOM entityTypeDom;

	@Mock
	private IApsEntityDOM entityDom;

	@Mock
	private IUserProfileDAO userProfileDAO;

	@Mock
	private INotifyManager notifyManager;

	private String beanName = "UserProfileManager";

	private String className = "org.entando.entando.aps.system.services.userprofile.model.UserProfile";

	private String configItemName = "userProfileTypes";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.userProfileManager.setEntityClassName(className);
		this.userProfileManager.setConfigItemName(configItemName);
		this.userProfileManager.setBeanName(this.beanName);
	}

	@Test
	public void testGetDefaultProfileType() throws ApsSystemException {
		// @formatter:off
		when(entityTypeFactory.extractEntityType(
				SystemConstants.DEFAULT_PROFILE_TYPE_CODE, 
				UserProfile.class, 
				configItemName, 
				this.entityTypeDom, 
				userProfileManager.getName(), 
				this.entityDom))
		.thenReturn(this.createFakeProfile(SystemConstants.DEFAULT_PROFILE_TYPE_CODE));
		// @formatter:on

		IUserProfile userProfile = userProfileManager.getDefaultProfileType();
		assertThat(userProfile, is(not(nullValue())));
		assertThat(userProfile.getAttributeList().size(), is(1));
	}

	@Test
	public void testAddProfile() throws ApsSystemException {
		// @formatter:off
		when(entityTypeFactory.extractEntityType(
				SystemConstants.DEFAULT_PROFILE_TYPE_CODE, 
				UserProfile.class, 
				configItemName, 
				this.entityTypeDom, 
				userProfileManager.getName(), 
				this.entityDom))
		.thenReturn(this.createFakeProfile(SystemConstants.DEFAULT_PROFILE_TYPE_CODE));
		// @formatter:on

		IUserProfile userProfile = userProfileManager.getDefaultProfileType();

		String name = "Jack_Bower";
		MonoTextAttribute attribute = (MonoTextAttribute) userProfile.getAttribute("Name");
		attribute.setText(name);

		this.userProfileManager.addProfile(name, userProfile);
		assertThat(userProfile.getId(), is(name));

		Mockito.verify(userProfileDAO, Mockito.times(1)).addEntity(userProfile);
		Mockito.verify(notifyManager, Mockito.times(1)).publishEvent(Mockito.any(ProfileChangedEvent.class));
	}

	private IApsEntity createFakeProfile(String defaultProfileTypeCode) {
		UserProfile userProfile = new UserProfile();
		MonoTextAttribute monoTextAttribute = new MonoTextAttribute();
		monoTextAttribute.setName("Name");
		monoTextAttribute.setHandler(new MonoTextAttributeHandler());
		userProfile.addAttribute(monoTextAttribute);
		userProfile.setTypeCode(defaultProfileTypeCode);
		return userProfile;
	}
}
