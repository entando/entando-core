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
            InputStream isJsonPost = this.getClass().getResourceAsStream("1_POST_valid.json");
            String jsonPost = FileTextReader.getText(isJsonPost);
            ResultActions result1 = mockMvc
                    .perform(post("/dataTypes")
                            .content(jsonPost)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result1.andExpect(status().isOk());
            DataObject addedType = (DataObject) this.dataObjectManager.getEntityPrototype("AAA");
            Assert.assertNotNull(addedType);
            Assert.assertEquals("Type AAA", addedType.getTypeDescription());
            Assert.assertEquals(1, addedType.getAttributeList().size());

            InputStream isJsonPutInvalid = this.getClass().getResourceAsStream("1_PUT_invalid.json");
            String jsonPutInvalid = FileTextReader.getText(isJsonPutInvalid);
            ResultActions result2 = mockMvc
                    .perform(put("/dataTypes/{dataTypeCode}", new Object[]{"AAA"})
                            .content(jsonPutInvalid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2.andExpect(status().isBadRequest());

            InputStream isJsonPutValid = this.getClass().getResourceAsStream("1_PUT_valid.json");
            String jsonPutValid = FileTextReader.getText(isJsonPutValid);
            ResultActions result3 = mockMvc
                    .perform(put("/dataTypes/{dataTypeCode}", new Object[]{"AAA"})
                            .content(jsonPutValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result3.andExpect(status().isOk());

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
            InputStream isJsonPostInvalid = this.getClass().getResourceAsStream("2_POST_invalid.json");
            String jsonPostInvalid = FileTextReader.getText(isJsonPostInvalid);
            ResultActions result1 = mockMvc
                    .perform(post("/dataTypes")
                            .content(jsonPostInvalid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result1.andExpect(status().isBadRequest());
            Assert.assertNull(this.dataObjectManager.getEntityPrototype("TST"));

            InputStream isJsonPostValid = this.getClass().getResourceAsStream("2_POST_valid.json");
            String jsonPostValid = FileTextReader.getText(isJsonPostValid);
            ResultActions result2 = mockMvc
                    .perform(post("/dataTypes")
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2.andExpect(status().isOk());
            DataObject addedDataObject = (DataObject) this.dataObjectManager.getEntityPrototype("TST");
            Assert.assertNotNull(addedDataObject);
            Assert.assertEquals(3, addedDataObject.getAttributeList().size());

            ResultActions result2_bis = mockMvc
                    .perform(post("/dataTypes")
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result2_bis.andExpect(status().isConflict());

            InputStream isJsonPutValid = this.getClass().getResourceAsStream("2_PUT_valid.json");
            String jsonPutValid = FileTextReader.getText(isJsonPutValid);
            ResultActions result3 = mockMvc
                    .perform(put("/dataTypes/{dataTypeCode}", new Object[]{"AAA"})
                            .content(jsonPutValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result3.andExpect(status().isBadRequest());

            ResultActions result3_bis = mockMvc
                    .perform(put("/dataTypes/{dataTypeCode}", new Object[]{"TST"})
                            .content(jsonPutValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result3_bis.andExpect(status().isOk());

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

}
