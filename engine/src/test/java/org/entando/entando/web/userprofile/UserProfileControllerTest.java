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
package org.entando.entando.web.userprofile;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileService;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.userprofile.validator.ProfileValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

public class UserProfileControllerTest extends AbstractControllerTest {

    @Mock
    private ProfileValidator profileValidator;

    @Mock
    private IUserManager userManager;

    @Mock
    private IUserProfileService userProfileService;

    @Mock
    private IUserProfileManager userProfileManager;

    @InjectMocks
    private ProfileController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void shouldGetExistingProfile() throws Exception {
        when(this.profileValidator.existProfile("user_with_profile")).thenReturn(true);
        when(this.userProfileService.getUserProfile("user_with_profile")).thenReturn(Mockito.mock(EntityDto.class));
        ResultActions result = performGetUserProfiles("user_with_profile");
        result.andExpect(status().isOk());
    }

    @Test
    public void shouldGetNewlyCreatedProfile() throws Exception {
        when(this.userManager.getUser("user_without_profile")).thenReturn(Mockito.mock(UserDetails.class));
        when(this.userProfileManager.getDefaultProfileType()).thenReturn(Mockito.mock(IUserProfile.class));
        ResultActions result = performGetUserProfiles("user_without_profile");
        result.andExpect(status().isOk());
    }

    @Test
    public void testUnexistingProfile() throws Exception {
        ResultActions result = performGetUserProfiles("user_without_profile");
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testAddProfile() throws Exception {
        when(this.userProfileService.addUserProfile(any(EntityDto.class), any(BindingResult.class)))
                .thenReturn(Mockito.mock(EntityDto.class));

        String mockJson = "{\n"
                + "    \"id\": \"user\",\n"
                + "    \"typeCode\": \"" + SystemConstants.DEFAULT_PROFILE_TYPE_CODE + "\",\n"
                + "    \"attributes\": [\n"
                + "         {\"code\": \"fullname\", \"value\": \"User\"},\n"
                + "         {\"code\": \"email\", \"value\": \"test@example.com\"}\n"
                + "    ]}";

        ResultActions result = performPostUserProfiles(mockJson);
        result.andExpect(status().isOk());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        when(this.userProfileService.updateUserProfile(any(EntityDto.class), any(BindingResult.class)))
                .thenReturn(Mockito.mock(EntityDto.class));

        String mockJson = "{\n"
                + "    \"id\": \"user\",\n"
                + "    \"typeCode\": \"" + SystemConstants.DEFAULT_PROFILE_TYPE_CODE + "\",\n"
                + "    \"attributes\": [\n"
                + "         {\"code\": \"fullname\", \"value\": \"User Renamed\"},\n"
                + "         {\"code\": \"email\", \"value\": \"test@example.com\"}\n"
                + "    ]}";

        ResultActions result = performPutUserProfiles("user", mockJson);
        result.andExpect(status().isOk());
    }

    private ResultActions performGetUserProfiles(String username) throws Exception {
        String accessToken = this.createAccessToken();
        return mockMvc.perform(
                get("/userProfiles/{username}", username)
                        .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performPostUserProfiles(String jsonContent) throws Exception {
        String accessToken = this.createAccessToken();
        return mockMvc.perform(
                post("/userProfiles")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));

    }

    private ResultActions performPutUserProfiles(String username, String jsonContent) throws Exception {
        String accessToken = this.createAccessToken();
        return mockMvc.perform(
                put("/userProfiles/{username}", username)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
    }

    private String createAccessToken() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        return mockOAuthInterceptor(user);
    }
}
