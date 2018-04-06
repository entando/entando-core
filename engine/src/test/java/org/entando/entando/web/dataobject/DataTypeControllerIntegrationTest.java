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
package org.entando.entando.web.dataobject;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.IDataObjectService;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataTypeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IDataObjectService dataObjectService;

    @Autowired
    private IDataObjectManager dataObjectManager;

    @Autowired
    @InjectMocks
    private DataTypeController controller;

    @Test
    public void testGetDataTypes() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypes")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetValidDataType() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypes/{code}", new Object[]{"RAH"})
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetInvalidDataType() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypes/{code}", new Object[]{"XXX"})
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testAddUpdateDataType_1() throws Exception {
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("AAA"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("1_POST_valid.json", accessToken, status().isOk());
            DataObject addedType = (DataObject) this.dataObjectManager.getEntityPrototype("AAA");
            Assert.assertNotNull(addedType);
            Assert.assertEquals("Type AAA", addedType.getTypeDescription());
            Assert.assertEquals(1, addedType.getAttributeList().size());

            this.executeDataTypePut("1_PUT_invalid.json", "AAA", accessToken, status().isBadRequest());

            this.executeDataTypePut("1_PUT_valid.json", "AAA", accessToken, status().isOk());

            addedType = (DataObject) this.dataObjectManager.getEntityPrototype("AAA");
            Assert.assertEquals("Type AAA Modified", addedType.getTypeDescription());
            Assert.assertEquals(2, addedType.getAttributeList().size());

            ResultActions result4 = mockMvc
                    .perform(delete("/dataTypes/{dataTypeCode}", new Object[]{"AAA"})
                            .header("Authorization", "Bearer " + accessToken));
            result4.andExpect(status().isOk());
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("AAA"));
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype("AAA")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("AAA");
            }
        }
    }

    @Test
    public void testAddUpdateDataType_2() throws Exception {
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("TST"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_invalid.json", accessToken, status().isBadRequest());
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("TST"));

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());
            DataObject addedDataObject = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertNotNull(addedDataObject);
            Assert.assertEquals(3, addedDataObject.getAttributeList().size());

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isConflict());

            this.executeDataTypePut("2_PUT_valid.json", "AAA", accessToken, status().isBadRequest());

            this.executeDataTypePut("2_PUT_valid.json", "TST", accessToken, status().isOk());

            DataObject modifiedDataObject = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertNotNull(modifiedDataObject);
            Assert.assertEquals(5, modifiedDataObject.getAttributeList().size());

            ResultActions result4 = mockMvc
                    .perform(delete("/dataTypes/{dataTypeCode}", new Object[]{"TST"})
                            .header("Authorization", "Bearer " + accessToken));
            result4.andExpect(status().isOk());
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("TST"));
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TST");
            }
        }
    }

    private ResultActions executeDataTypePost(String fileName, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/dataTypes")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeDataTypePut(String fileName, String typeCode, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPutValid = this.getClass().getResourceAsStream(fileName);
        String jsonPutValid = FileTextReader.getText(isJsonPutValid);
        ResultActions result = mockMvc
                .perform(put("/dataTypes/{dataTypeCode}", new Object[]{typeCode})
                        .content(jsonPutValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    // attributes
    @Test
    public void testGetDataTypeAttributeTypes_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypeAttributes")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetDataTypeAttributeTypes_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypeAttributes").param("pageSize", "5")
                        .param("sort", "code").param("direction", "DESC")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(5)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
        result.andExpect(jsonPath("$.metaData.lastPage", is(3)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(15)));
        result.andExpect(jsonPath("$.payload[0]", is("Timestamp")));
    }

    @Test
    public void testGetDataTypeAttributeTypes_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypeAttributes").param("pageSize", "7")
                        .param("sort", "code").param("direction", "ASC")
                        .param("filters[0].attribute", "code").param("filters[0].value", "tex")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(4)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(7)));
        result.andExpect(jsonPath("$.metaData.lastPage", is(1)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(4)));
        result.andExpect(jsonPath("$.payload[0]", is("Hypertext")));
    }

    @Test
    public void testGetDataTypeAttributeType_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypeAttributes/{attributeTypeCode}", new Object[]{"Monotext"})
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.code", is("Monotext")));
        result.andExpect(jsonPath("$.payload.simple", is(true)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

    @Test
    public void testGetDataTypeAttributeType_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypeAttributes/{attributeTypeCode}", new Object[]{"WrongTypeCode"})
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }
    /*
    // ------------------------------------
    @Test
    public void testGetUserProfileAttribute() throws Exception {
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype("TST"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeProfileTypePost("2_POST_valid.json", accessToken, status().isOk());

            ResultActions result1 = mockMvc
                    .perform(get("/profileTypes/{profileTypeCode}/attribute/{attributeCode}", new Object[]{"XXX", "TextAttribute"})
                            .header("Authorization", "Bearer " + accessToken));
            result1.andExpect(status().isNotFound());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = mockMvc
                    .perform(get("/profileTypes/{profileTypeCode}/attribute/{attributeCode}", new Object[]{"TST", "WrongCpde"})
                            .header("Authorization", "Bearer " + accessToken));
            result2.andExpect(status().isNotFound());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result3 = mockMvc
                    .perform(get("/profileTypes/{profileTypeCode}/attribute/{attributeCode}", new Object[]{"TST", "TextAttribute"})
                            .header("Authorization", "Bearer " + accessToken));
            result3.andExpect(status().isOk());
            result3.andExpect(jsonPath("$.payload.code", is("TextAttribute")));
            result3.andExpect(jsonPath("$.payload.type", is("Text")));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.metaData.size()", is(1)));
            result3.andExpect(jsonPath("$.metaData.profileTypeCode", is("TST")));

        } finally {
            if (null != this.userProfileManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype("TST");
            }
        }
    }

    @Test
    public void testAddUserProfileAttribute() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype(typeCode));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeProfileTypePost("2_POST_valid.json", accessToken, status().isOk());

            ResultActions result1 = this.executeProfileAttributePost("3_POST_attribute_invalid_1.json", typeCode, accessToken, status().isConflict());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.errors[0].code", is("14")));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeProfileAttributePost("3_POST_attribute_invalid_2.json", typeCode, accessToken, status().isBadRequest());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("53")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result3 = this.executeProfileAttributePost("3_POST_attribute_invalid_3.json", typeCode, accessToken, status().isBadRequest());
            result3.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("13")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeProfileAttributePost("3_POST_attribute_invalid_4.json", typeCode, accessToken, status().isBadRequest());
            result4.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result4.andExpect(jsonPath("$.errors", Matchers.hasSize(2)));
            result4.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result5 = this.executeProfileAttributePost("3_POST_attribute_valid.json", typeCode, accessToken, status().isOk());
            result5.andExpect(jsonPath("$.payload.code", is("added_mt")));
            result5.andExpect(jsonPath("$.payload.roles", Matchers.hasSize(1)));
            result5.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result5.andExpect(jsonPath("$.metaData.size()", is(1)));
            result5.andExpect(jsonPath("$.metaData.profileTypeCode", is(typeCode)));

            IApsEntity profileType = this.userProfileManager.getEntityPrototype(typeCode);
            Assert.assertEquals(4, profileType.getAttributeList().size());
        } finally {
            if (null != this.userProfileManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype(typeCode);
            }
        }
    }

    @Test
    public void testUpdateUserProfileAttribute() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype(typeCode));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeProfileTypePost("2_POST_valid.json", accessToken, status().isOk());

            ResultActions result1 = this.executeProfileAttributePut("4_PUT_attribute_invalid_1.json", typeCode, "list_wrong", accessToken, status().isNotFound());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.errors[0].code", is("15")));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeProfileAttributePut("4_PUT_attribute_invalid_2.json", typeCode, "list", accessToken, status().isBadRequest());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("16")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result3 = this.executeProfileAttributePut("4_PUT_attribute_valid.json", typeCode, "wrongname", accessToken, status().isConflict());
            result3.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("6")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeProfileAttributePut("4_PUT_attribute_valid.json", typeCode, "list", accessToken, status().isOk());
            result4.andExpect(jsonPath("$.payload.code", is("list")));
            result4.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result4.andExpect(jsonPath("$.metaData.size()", is(1)));
            result4.andExpect(jsonPath("$.metaData.profileTypeCode", is(typeCode)));

            IApsEntity profileType = this.userProfileManager.getEntityPrototype(typeCode);
            Assert.assertEquals(4, profileType.getAttributeList().size());
        } finally {
            if (null != this.userProfileManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype(typeCode);
            }
        }
    }

    @Test
    public void testDeleteUserProfileAttribute() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype(typeCode));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeProfileTypePost("2_POST_valid.json", accessToken, status().isOk());
            IApsEntity profileType = this.userProfileManager.getEntityPrototype(typeCode);
            Assert.assertEquals(3, profileType.getAttributeList().size());
            Assert.assertNotNull(profileType.getAttribute("list"));

            ResultActions result1 = this.executeProfileAttributeDelete("wrongCode", "list_wrong", accessToken, status().isNotFound());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.errors[0].code", is("1")));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeProfileAttributeDelete(typeCode, "list_wrong", accessToken, status().isOk());
            result2.andExpect(jsonPath("$.payload.profileTypeCode", is(typeCode)));
            result2.andExpect(jsonPath("$.payload.attributeCode", is("list_wrong")));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            profileType = this.userProfileManager.getEntityPrototype(typeCode);
            Assert.assertEquals(3, profileType.getAttributeList().size());

            ResultActions result3 = this.executeProfileAttributeDelete(typeCode, "list", accessToken, status().isOk());
            result3.andExpect(jsonPath("$.payload.profileTypeCode", is(typeCode)));
            result3.andExpect(jsonPath("$.payload.attributeCode", is("list")));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            profileType = this.userProfileManager.getEntityPrototype(typeCode);
            Assert.assertEquals(2, profileType.getAttributeList().size());
            Assert.assertNull(profileType.getAttribute("list"));
        } finally {
            if (null != this.userProfileManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype(typeCode);
            }
        }
    }

    private ResultActions executeProfileAttributePost(String fileName, String typeCode, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/profileTypes/{profileTypeCode}/attribute", new Object[]{typeCode})
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeProfileAttributePut(String fileName, String typeCode, String attributeCode, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPutValid = this.getClass().getResourceAsStream(fileName);
        String jsonPutValid = FileTextReader.getText(isJsonPutValid);
        ResultActions result = mockMvc
                .perform(put("/profileTypes/{profileTypeCode}/attribute/{attributeCode}", new Object[]{typeCode, attributeCode})
                        .content(jsonPutValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeProfileAttributeDelete(String typeCode,
            String attributeCode, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/profileTypes/{profileTypeCode}/attribute/{attributeCode}", new Object[]{typeCode, attributeCode})
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    @Test
    public void testRefreshUserProfileType() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.userProfileManager.getEntityPrototype(typeCode));
            Assert.assertNull(this.userProfileManager.getEntityPrototype("XXX"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeProfileTypePost("2_POST_valid.json", accessToken, status().isOk());
            Assert.assertNotNull(this.userProfileManager.getEntityPrototype(typeCode));

            ResultActions result1 = mockMvc
                    .perform(post("/profileTypes/refresh/{profileTypeCode}", new Object[]{typeCode})
                            .content("{}")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result1.andExpect(status().isOk());
            result1.andExpect(jsonPath("$.payload.profileTypeCode", is(typeCode)));
            result1.andExpect(jsonPath("$.payload.status", is("success")));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = mockMvc
                    .perform(post("/profileTypes/refresh/{profileTypeCode}", new Object[]{"XXX"})
                            .content("{}")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2.andExpect(status().isNotFound());
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));
            String x = result2.andReturn().getResponse().getContentAsString();
            System.out.println(x);
        } finally {
            if (null != this.userProfileManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.userProfileManager).removeEntityPrototype(typeCode);
            }
        }
    }
     */
}
