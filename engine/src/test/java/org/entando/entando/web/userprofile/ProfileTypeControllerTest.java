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

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileTypeService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.userprofile.model.ProfileTypeDtoRequest;
import org.entando.entando.web.userprofile.validator.ProfileTypeValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.validation.BindingResult;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ProfileTypeControllerTest extends AbstractControllerTest {

    @Mock
    private IUserProfileManager userProfileManager;

    @Mock
    private IUserProfileTypeService userProfileTypeService;

    @InjectMocks
    private ProfileTypeValidator profileTypeValidator;

    @InjectMocks
    private ProfileTypeController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
        controller.setProfileTypeValidator(this.profileTypeValidator);
    }

    @Test
    public void testUpdateNoPayload() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(post("/profileTypes")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testPayloadOk() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        InputStream isJsonPost = this.getClass().getResourceAsStream("1_POST_valid.json");
        String jsonPost = FileTextReader.getText(isJsonPost);
        ResultActions result = mockMvc
                .perform(post("/profileTypes")
                        .content(jsonPost)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        Mockito.verify(userProfileTypeService, Mockito.times(1)).addUserProfileType(any(ProfileTypeDtoRequest.class), any(BindingResult.class));
        result.andExpect(status().isOk());
    }

    @Test
    public void testReloadReferences() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(post("/profileTypes/refresh/{profileTypeCode}", new Object[]{"TST"})
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        Mockito.verify(userProfileTypeService, Mockito.times(1)).reloadProfileTypeReferences(ArgumentMatchers.anyString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.profileTypeCode", is("TST")));
        result.andExpect(jsonPath("$.payload.status", is("success")));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

}
