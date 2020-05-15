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
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import org.entando.entando.aps.servlet.security.CORSFilter;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileService;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserProfileControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IUserProfileManager userProfileManager;

    @Autowired
    private IUserManager userManager;

    @Autowired
    private ProfileController controller;

    @Test
    public void testGetUserProfileType() throws Exception {
        String accessToken = this.createAccessToken();
        ResultActions result = mockMvc
                .perform(get("/userProfiles/{username}", new Object[]{"editorCoach"})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", CORSFilter.ALLOWED_METHODS));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetInvalidUserProfileType() throws Exception {
        String accessToken = this.createAccessToken();
        ResultActions result = mockMvc
                .perform(get("/userProfiles/{username}", new Object[]{"xxxxx"})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetValidUserProfileType() throws Exception {
        String accessToken = this.createAccessToken();
        ResultActions result = mockMvc
                .perform(get("/userProfiles/{username}", new Object[]{"editorCoach"})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
    }

    @Test
    public void testAddUpdateUserProfile() throws Exception {
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype("TST"));
            String accessToken = this.createAccessToken();

            this.executeProfileTypePost("5_POST_type_valid.json", accessToken, status().isOk());

            Assert.assertNull(this.userManager.getUser("new_user"));
            User user = new User();
            user.setUsername("new_user");
            user.setPassword("new_user");
            this.userManager.addUser(user);
            Assert.assertNotNull(this.userProfileManager.getEntityPrototype("TST"));

            Assert.assertNull(this.userProfileManager.getProfile("new_user"));
            ResultActions result = this.executeProfilePost("5_POST_invalid.json", accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.payload.size()", is(0)));
            result.andExpect(jsonPath("$.errors.size()", is(3)));
            result.andExpect(jsonPath("$.metaData.size()", is(0)));
            Assert.assertNull(this.userProfileManager.getProfile("new_user"));

            Assert.assertNull(this.userProfileManager.getProfile("new_user"));
            ResultActions result2 = this.executeProfilePost("5_POST_valid.json", accessToken, status().isOk());
            result2.andExpect(jsonPath("$.payload.id", is("new_user")));
            result2.andExpect(jsonPath("$.errors.size()", is(0)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));
            IUserProfile profile = this.userProfileManager.getProfile("new_user");
            Assert.assertNotNull(profile);
            Date date = (Date) profile.getAttribute("Date").getValue();
            Assert.assertEquals("2017-09-21", DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
            Boolean booleanValue = (Boolean) profile.getAttribute("Boolean").getValue();
            Assert.assertTrue(booleanValue);
            Boolean threeState = (Boolean) profile.getAttribute("ThreeState").getValue();
            Assert.assertNull(threeState);

            ResultActions result3 = this.executeProfilePut("5_PUT_valid.json", "invalid", accessToken, status().isConflict());
            result3.andExpect(jsonPath("$.payload.size()", is(0)));
            result3.andExpect(jsonPath("$.errors.size()", is(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("2")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeProfilePut("5_PUT_valid.json", "new_user", accessToken, status().isOk());
            result4.andExpect(jsonPath("$.payload.id", is("new_user")));
            result4.andExpect(jsonPath("$.errors.size()", is(0)));
            result4.andExpect(jsonPath("$.metaData.size()", is(0)));
            result4.andExpect(jsonPath("$.payload.attributes[0].code", is("Title")));
            result4.andExpect(jsonPath("$.payload.attributes[0].value", is("My title")));
            result4.andExpect(jsonPath("$.payload.attributes[0].values", Matchers.anything()));
            result4.andExpect(jsonPath("$.payload.attributes[0].elements.size()", is(0)));
            result4.andExpect(jsonPath("$.payload.attributes[0].compositeelements.size()", is(0)));
            result4.andExpect(jsonPath("$.payload.attributes[0].listelements", Matchers.anything()));
            profile = this.userProfileManager.getProfile("new_user");
            date = (Date) profile.getAttribute("Date").getValue();
            Assert.assertEquals("2018-03-21", DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
            booleanValue = (Boolean) profile.getAttribute("Boolean").getValue();
            Assert.assertFalse(booleanValue);
            threeState = (Boolean) profile.getAttribute("ThreeState").getValue();
            Assert.assertNotNull(threeState);
            Assert.assertTrue(threeState);

            ListAttribute list = (ListAttribute) profile.getAttribute("multilist");
            Assert.assertEquals(4, list.getAttributeList("en").size());
        } finally {
            this.userProfileManager.deleteProfile("new_user");
            this.userManager.removeUser("new_user");
            if (null != this.userProfileManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype("TST");
            }
        }
    }

    /* For an user created without profile, the profile has to be created the
       first time the "/userProfiles/{user}" endpoint is requested. */
    @Test
    public void testGetProfileForNewUser() throws Exception {
        String username = "another_new_user";

        try {
            String accessToken = this.createAccessToken();

            Assert.assertNull(this.userManager.getUser(username));
            User user = new User();
            user.setUsername(username);
            user.setPassword(username);
            this.userManager.addUser(user);

            ResultActions result = executeProfileGet(username, accessToken, status().isOk());

            result.andExpect(jsonPath("$.payload.id", is(username)));
            result.andExpect(jsonPath("$.payload.typeCode", is(SystemConstants.DEFAULT_PROFILE_TYPE_CODE)));
            // Checking mandatory attributes with empty values
            result.andExpect(jsonPath("$.payload.attributes[?(@.code == 'fullname')].value", is(Arrays.asList(""))));
            result.andExpect(jsonPath("$.payload.attributes[?(@.code == 'email')].value", is(Arrays.asList(""))));
        } finally {
            this.userProfileManager.deleteProfile(username);
            this.userManager.removeUser(username);
        }
    }

    private String createAccessToken() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        return mockOAuthInterceptor(user);
    }

    private ResultActions executeProfileGet(String username, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(get("/userProfiles/{username}", new Object[]{username})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeProfilePost(String fileName, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/userProfiles")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeProfilePut(String fileName, String username, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(put("/userProfiles/{username}", new Object[]{username})
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeProfileTypePost(String fileName, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/profileTypes")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

}
