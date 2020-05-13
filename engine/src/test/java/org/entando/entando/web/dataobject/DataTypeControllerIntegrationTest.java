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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import org.entando.entando.aps.servlet.security.CORSFilter;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.IDataObjectService;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", CORSFilter.ALLOWED_METHODS));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetValidDataType() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypes/{code}", new Object[]{"RAH"})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetInvalidDataType() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypes/{code}", new Object[]{"XXX"})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result4.andExpect(status().isOk());
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("AAA"));
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype("AAA")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("AAA");
            }
        }
    }
    
    @Test
    public void testValidateTypeCode() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        try {
            String payload = "{\"code\": null,\"name\": \"Data Type Invalid\",\"attributes\": []}";
            ResultActions result = this.executeDataTypePostByPayload(payload, accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("51")));
            result.andExpect(jsonPath("$.metaData.size()", is(0)));
            
            this.executeTypeCodeValidation("", "54", accessToken);
            this.executeTypeCodeValidation("TTTT", "54", accessToken);
            this.executeTypeCodeValidation("TTb", "58", accessToken);
            this.executeTypeCodeValidation("TT%", "58", accessToken);
            this.executeTypeCodeValidation("TT_", "58", accessToken);
            
            String payload2 = "{\"code\": \"T12\",\"name\": \"Data Type Invalid\",\"attributes\": []}";
            this.executeDataTypePostByPayload(payload2, accessToken, status().isOk());
            DataObject addedDataObject = (DataObject) this.dataObjectManager.getEntityPrototype("T12");
            Assert.assertNotNull(addedDataObject);
            Assert.assertEquals(0, addedDataObject.getAttributeList().size());
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype("TTTT")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TTTT");
            }
            if (null != this.dataObjectManager.getEntityPrototype("TTb")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TTb");
            }
            if (null != this.dataObjectManager.getEntityPrototype("TT%")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TT%");
            }
            if (null != this.dataObjectManager.getEntityPrototype("TT_")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TT_");
            }
            if (null != this.dataObjectManager.getEntityPrototype("T12")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("T12");
            }
        }
    }
    
    private void executeTypeCodeValidation(String typeCode, String expectedError, String accessToken) throws Exception {
        String payload = "{\"code\": \""+typeCode+"\",\"name\": \"Data Type Invalid\",\"attributes\": []}";
        ResultActions result = this.executeDataTypePostByPayload(payload, accessToken, status().isBadRequest());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(expectedError)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
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
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
        return this.executeDataTypePostByPayload(jsonPostValid, accessToken, expected);
    }
    
    private ResultActions executeDataTypePostByPayload(String payload, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/dataTypes")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", CORSFilter.ALLOWED_METHODS));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

    // ------------------------------------
    @Test
    public void testGetDataTypeAttribute() throws Exception {
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("TST"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());

            ResultActions result1 = mockMvc
                    .perform(get("/dataTypes/{dataTypeCode}/attribute/{attributeCode}", new Object[]{"XXX", "TextAttribute"})
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result1.andExpect(status().isNotFound());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = mockMvc
                    .perform(get("/dataTypes/{dataTypeCode}/attribute/{attributeCode}", new Object[]{"TST", "WrongCpde"})
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result2.andExpect(status().isNotFound());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result3 = mockMvc
                    .perform(get("/dataTypes/{dataTypeCode}/attribute/{attributeCode}", new Object[]{"TST", "TextAttribute"})
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result3.andExpect(status().isOk());
            result3.andExpect(jsonPath("$.payload.code", is("TextAttribute")));
            result3.andExpect(jsonPath("$.payload.type", is("Text")));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.metaData.size()", is(1)));
            result3.andExpect(jsonPath("$.metaData.dataTypeCode", is("TST")));

        } finally {
            if (null != this.dataObjectManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TST");
            }
        }
    }

    @Test
    public void testAddDataTypeAttribute() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype(typeCode));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());

            ResultActions result1 = this.executeDataTypeAttributePost("3_POST_attribute_invalid_1.json", typeCode, accessToken, status().isConflict());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.errors[0].code", is("14")));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeDataTypeAttributePost("3_POST_attribute_invalid_2.json", typeCode, accessToken, status().isBadRequest());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("53")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result3 = this.executeDataTypeAttributePost("3_POST_attribute_invalid_3.json", typeCode, accessToken, status().isBadRequest());
            result3.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("13")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeDataTypeAttributePost("3_POST_attribute_invalid_4.json", typeCode, accessToken, status().isBadRequest());
            result4.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result4.andExpect(jsonPath("$.errors", Matchers.hasSize(2)));
            result4.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result5 = this.executeDataTypeAttributePost("3_POST_attribute_valid.json", typeCode, accessToken, status().isOk());
            result5.andExpect(jsonPath("$.payload.code", is("added_mt")));
            result5.andExpect(jsonPath("$.payload.roles", Matchers.hasSize(1)));
            result5.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result5.andExpect(jsonPath("$.metaData.size()", is(1)));
            result5.andExpect(jsonPath("$.metaData.dataTypeCode", is(typeCode)));

            IApsEntity dataType = this.dataObjectManager.getEntityPrototype(typeCode);
            Assert.assertEquals(4, dataType.getAttributeList().size());
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype(typeCode);
            }
        }
    }

    @Test
    public void testUpdateDataTypeAttribute() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype(typeCode));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());
            IApsEntity dataType = this.dataObjectManager.getEntityPrototype(typeCode);
            Assert.assertEquals(3, dataType.getAttributeList().size());

            ResultActions result1 = this.executeDataTypeAttributePut("4_PUT_attribute_invalid_1.json", typeCode, "list_wrong", accessToken, status().isNotFound());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.errors[0].code", is("15")));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeDataTypeAttributePut("4_PUT_attribute_invalid_2.json", typeCode, "list", accessToken, status().isBadRequest());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("16")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result3 = this.executeDataTypeAttributePut("4_PUT_attribute_valid.json", typeCode, "wrongname", accessToken, status().isConflict());
            result3.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("6")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeDataTypeAttributePut("4_PUT_attribute_valid.json", typeCode, "list", accessToken, status().isOk());
            result4.andExpect(jsonPath("$.payload.code", is("list")));
            result4.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result4.andExpect(jsonPath("$.metaData.size()", is(1)));
            result4.andExpect(jsonPath("$.metaData.dataTypeCode", is(typeCode)));

            dataType = this.dataObjectManager.getEntityPrototype(typeCode);
            Assert.assertEquals(3, dataType.getAttributeList().size());
            Assert.assertNotNull(dataType.getAttribute("list"));
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype(typeCode);
            }
        }
    }

    @Test
    public void testDeleteDataAttribute() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype(typeCode));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());
            IApsEntity dataType = this.dataObjectManager.getEntityPrototype(typeCode);
            Assert.assertEquals(3, dataType.getAttributeList().size());
            Assert.assertNotNull(dataType.getAttribute("list"));

            ResultActions result1 = this.executeDataTypeAttributeDelete("wrongCode", "list_wrong", accessToken, status().isNotFound());
            result1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result1.andExpect(jsonPath("$.errors[0].code", is("1")));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeDataTypeAttributeDelete(typeCode, "list_wrong", accessToken, status().isOk());
            result2.andExpect(jsonPath("$.payload.dataTypeCode", is(typeCode)));
            result2.andExpect(jsonPath("$.payload.attributeCode", is("list_wrong")));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            dataType = this.dataObjectManager.getEntityPrototype(typeCode);
            Assert.assertEquals(3, dataType.getAttributeList().size());

            ResultActions result3 = this.executeDataTypeAttributeDelete(typeCode, "list", accessToken, status().isOk());
            result3.andExpect(jsonPath("$.payload.dataTypeCode", is(typeCode)));
            result3.andExpect(jsonPath("$.payload.attributeCode", is("list")));
            result3.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            dataType = this.dataObjectManager.getEntityPrototype(typeCode);
            Assert.assertEquals(2, dataType.getAttributeList().size());
            Assert.assertNull(dataType.getAttribute("list"));
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype(typeCode);
            }
        }
    }

    private ResultActions executeDataTypeAttributePost(String fileName, String typeCode, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/dataTypes/{dataTypeCode}/attribute", new Object[]{typeCode})
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeDataTypeAttributePut(String fileName, String typeCode, String attributeCode, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPutValid = this.getClass().getResourceAsStream(fileName);
        String jsonPutValid = FileTextReader.getText(isJsonPutValid);
        ResultActions result = mockMvc
                .perform(put("/dataTypes/{dataTypeCode}/attribute/{attributeCode}", new Object[]{typeCode, attributeCode})
                        .content(jsonPutValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeDataTypeAttributeDelete(String typeCode,
            String attributeCode, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/dataTypes/{dataTypeCode}/attribute/{attributeCode}", new Object[]{typeCode, attributeCode})
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    @Test
    public void testRefreshDataTypeType() throws Exception {
        String typeCode = "TST";
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype(typeCode));
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("XXX"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());
            Assert.assertNotNull(this.dataObjectManager.getEntityPrototype(typeCode));

            ResultActions result1 = mockMvc
                    .perform(post("/dataTypes/refresh/{dataTypeCode}", new Object[]{typeCode})
                            .content("{}")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result1.andExpect(status().isOk());
            result1.andExpect(jsonPath("$.payload.dataTypeCode", is(typeCode)));
            result1.andExpect(jsonPath("$.payload.status", is("success")));
            result1.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result1.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = mockMvc
                    .perform(post("/dataTypes/refresh/{dataTypeCode}", new Object[]{"XXX"})
                            .content("{}")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result2.andExpect(status().isNotFound());
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

        } finally {
            if (null != this.dataObjectManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype(typeCode);
            }
        }
    }

    @Test
    public void testGetDataTypesStatus() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataTypesStatus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.size()", is(3)));
        result.andExpect(jsonPath("$.payload.ready", Matchers.hasSize(4)));
        result.andExpect(jsonPath("$.payload.toRefresh", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.payload.refreshing", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

    @Test
    public void testRefreshUserProfileTypes() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(post("/dataTypesStatus")
                        .content("{\"dataTypeCodes\":[\"AAA\",\"BBB\"]}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));

        result = mockMvc
                .perform(post("/dataTypesStatus")
                        .content("{\"dataTypeCodes\":[\"ALL\",\"ART\",\"EVN\"]}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.size()", is(2)));
        result.andExpect(jsonPath("$.payload.result", is("success")));
        result.andExpect(jsonPath("$.payload.dataTypeCodes.size()", is(3)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

    @Test
    public void testMoveAttribute() throws Exception {
        try {
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("TST"));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            this.executeDataTypePost("2_POST_valid.json", accessToken, status().isOk());
            DataObject addedDataObject = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertNotNull(addedDataObject);
            Assert.assertEquals(3, addedDataObject.getAttributeList().size());
            Assert.assertEquals("TextAttribute", addedDataObject.getAttributeList().get(0).getName());
            Assert.assertEquals("DataAttribute", addedDataObject.getAttributeList().get(1).getName());
            Assert.assertEquals("list", addedDataObject.getAttributeList().get(2).getName());

            this.executeMoveAttribute("TST", "TextAttribute", true, accessToken, status().isBadRequest());
            this.executeMoveAttribute("TST", "TextAttribute", false, accessToken, status().isOk());
            DataObject modified = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertEquals(3, modified.getAttributeList().size());
            Assert.assertEquals("DataAttribute", modified.getAttributeList().get(0).getName());
            Assert.assertEquals("TextAttribute", modified.getAttributeList().get(1).getName());
            Assert.assertEquals("list", modified.getAttributeList().get(2).getName());

            this.executeMoveAttribute("TST", "TextAttribute", false, accessToken, status().isOk());
            modified = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertEquals(3, modified.getAttributeList().size());
            Assert.assertEquals("DataAttribute", modified.getAttributeList().get(0).getName());
            Assert.assertEquals("list", modified.getAttributeList().get(1).getName());
            Assert.assertEquals("TextAttribute", modified.getAttributeList().get(2).getName());

            this.executeMoveAttribute("TST", "TextAttribute", false, accessToken, status().isBadRequest());

            this.executeMoveAttribute("TST", "TextAttribute", true, accessToken, status().isOk());
            modified = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertEquals(3, modified.getAttributeList().size());
            Assert.assertEquals("DataAttribute", modified.getAttributeList().get(0).getName());
            Assert.assertEquals("TextAttribute", modified.getAttributeList().get(1).getName());
            Assert.assertEquals("list", modified.getAttributeList().get(2).getName());
        } finally {
            if (null != this.dataObjectManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.dataObjectManager).removeEntityPrototype("TST");
            }
        }
    }

    private ResultActions executeMoveAttribute(String typeCode, String attributeCode, boolean moveUp, String accessToken, ResultMatcher expected) throws Exception {
        String suffix = (moveUp) ? "moveUp" : "moveDown";
        ResultActions result = mockMvc
                .perform(put("/dataTypes/{dataTypeCode}/attribute/{attributeCode}/" + suffix, new Object[]{typeCode, attributeCode})
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

}
