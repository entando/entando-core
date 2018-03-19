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

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileTypeService;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

public class ProfileTypeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IUserProfileTypeService userProfileTypeService;

    @Autowired
    private IUserProfileManager userProfileManager;

    @Autowired
    @InjectMocks
    private ProfileTypeController controller;

    @Test
    public void testGetUserProfileTypes() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/profileTypes")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetValidUserProfileType() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/profileTypes/{profileTypeCode}", new Object[]{"PFL"})
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetInvalidUserProfileType() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/profileTypes/{profileTypeCode}", new Object[]{"XXX"})
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testAddUpdateUserProfileType_1() throws Exception {
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype("AAA"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            InputStream isJsonPost = this.getClass().getResourceAsStream("1_POST_valid.json");
            String jsonPost = FileTextReader.getText(isJsonPost);
            ResultActions result1 = mockMvc
                    .perform(post("/profileTypes")
                            .content(jsonPost)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result1.andExpect(status().isOk());
            Assert.assertNotNull(this.userProfileManager.getEntityPrototype("AAA"));
            UserProfile addedType = (UserProfile) this.userProfileManager.getEntityPrototype("AAA");
            Assert.assertEquals("Profile Type AAA", addedType.getTypeDescription());
            Assert.assertEquals(1, addedType.getAttributeList().size());

            InputStream isJsonPutInvalid = this.getClass().getResourceAsStream("1_PUT_invalid.json");
            String jsonPutInvalid = FileTextReader.getText(isJsonPutInvalid);
            ResultActions result2 = mockMvc
                    .perform(put("/profileTypes/{profileTypeCode}", new Object[]{"AAA"})
                            .content(jsonPutInvalid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2.andExpect(status().isBadRequest());

            InputStream isJsonPutValid = this.getClass().getResourceAsStream("1_PUT_valid.json");
            String jsonPutValid = FileTextReader.getText(isJsonPutValid);
            ResultActions result3 = mockMvc
                    .perform(put("/profileTypes/{profileTypeCode}", new Object[]{"AAA"})
                            .content(jsonPutValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result3.andExpect(status().isOk());

            addedType = (UserProfile) this.userProfileManager.getEntityPrototype("AAA");
            Assert.assertEquals("Profile Type AAA Modified", addedType.getTypeDescription());
            Assert.assertEquals(2, addedType.getAttributeList().size());

            ResultActions result4 = mockMvc
                    .perform(delete("/profileTypes/{profileTypeCode}", new Object[]{"AAA"})
                            .header("Authorization", "Bearer " + accessToken));
            result4.andExpect(status().isOk());
            Assert.assertNull(this.userProfileManager.getEntityPrototype("AAA"));
        } finally {
            if (null != this.userProfileManager.getEntityPrototype("AAA")) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype("AAA");
            }
        }
    }

    @Test
    public void testAddUpdateUserProfileType_2() throws Exception {
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype("TST"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            InputStream isJsonPostInvalid = this.getClass().getResourceAsStream("2_POST_invalid.json");
            String jsonPostInvalid = FileTextReader.getText(isJsonPostInvalid);
            ResultActions result1 = mockMvc
                    .perform(post("/profileTypes")
                            .content(jsonPostInvalid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result1.andExpect(status().isBadRequest());
            Assert.assertNull(this.userProfileManager.getEntityPrototype("TST"));

            InputStream isJsonPostValid = this.getClass().getResourceAsStream("2_POST_valid.json");
            String jsonPostValid = FileTextReader.getText(isJsonPostValid);
            ResultActions result2 = mockMvc
                    .perform(post("/profileTypes")
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2.andExpect(status().isOk());
            UserProfile addedDataObject = (UserProfile) this.userProfileManager.getEntityPrototype("TST");
            Assert.assertNotNull(addedDataObject);
            Assert.assertEquals(3, addedDataObject.getAttributeList().size());

            ResultActions result2_bis = mockMvc
                    .perform(post("/profileTypes")
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2_bis.andExpect(status().isConflict());

            InputStream isJsonPutValid = this.getClass().getResourceAsStream("2_PUT_valid.json");
            String jsonPutValid = FileTextReader.getText(isJsonPutValid);
            ResultActions result3 = mockMvc
                    .perform(put("/profileTypes/{profileTypeCode}", new Object[]{"AAA"})
                            .content(jsonPutValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result3.andExpect(status().isBadRequest());

            ResultActions result3_bis = mockMvc
                    .perform(put("/profileTypes/{profileTypeCode}", new Object[]{"TST"})
                            .content(jsonPutValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result3_bis.andExpect(status().isOk());

            UserProfile modifiedDataObject = (UserProfile) this.userProfileManager.getEntityPrototype("TST");
            Assert.assertNotNull(modifiedDataObject);
            Assert.assertEquals(5, modifiedDataObject.getAttributeList().size());

            ResultActions result4 = mockMvc
                    .perform(delete("/profileTypes/{profileTypeCode}", new Object[]{"TST"})
                            .header("Authorization", "Bearer " + accessToken));
            result4.andExpect(status().isOk());
            Assert.assertNull(this.userProfileManager.getEntityPrototype("TST"));
        } finally {
            if (null != this.userProfileManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype("TST");
            }
        }
    }

}
