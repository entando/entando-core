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
package org.entando.entando.web.dataobjectmodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataObjectModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URI = "/dataModels";

    @Autowired
    private IDataObjectModelManager dataObjectModelManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetDataModelDictionary() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/dictionary")
                                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

    }

    @Test
    public void testGetDataModelDictionaryWithTypeCode() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/dictionary")
                                                                            .param("typeCode", "EVN")
                                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetDataModelDictionaryValidTypeCodeInvalid() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/dictionary")
                                                                            .param("typeCode", "LOL")
                                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors[0].code", is("6")));
    }

    @Test
    public void testGetDataModelsDefaultSorting() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/dataModels")
                                                                 .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.sort", is("modelId")));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
    }

    @Test
    public void testSearch() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc
                                      .perform(
                                               get("/dataModels")
                                                                 .param("filters[0].attribute", "type")
                                                                 .param("filters[0].value", "art")
                                                                 .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.sort", is("modelId")));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(4)));

        result = mockMvc
                        .perform(
                                 get("/dataModels")
                                                   .param("filters[0].attribute", "type")
                                                   .param("filters[0].value", "art")
                                                   .param("filters[1].attribute", "descr")
                                                   .param("filters[1].value", "model")
                                                   .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());


        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.sort", is("modelId")));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(2)));

        result = mockMvc
                        .perform(
                                 get("/dataModels")
                                                   .param("sort", "modelId")
                                                   .param("direction", FieldSearchFilter.DESC_ORDER)
                                                   .param("filters[0].attribute", "type")
                                                   .param("filters[0].value", "art")
                                                   .param("filters[1].attribute", "descr")
                                                   .param("filters[1].value", "model")
                                                   .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());


        result.andExpect(jsonPath("$.payload[0].modelId", is("11")));
        result.andExpect(jsonPath("$.payload[1].modelId", is("1")));
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.sort", is("modelId")));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(2)));
    }

    @Test
    public void testCrud() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        long id = 12345;
        try {

            DataObjectModelRequest request = new DataObjectModelRequest();
            request.setModelId(String.valueOf(id));
            request.setDescr("test_" + id);
            request.setType("ART");
            request.setModel("hello");

            String payload = mapper.writeValueAsString(request);

            //post
            ResultActions result = mockMvc
                    .perform(
                             post("/dataModels")
                             .content(payload)
                             .contentType(MediaType.APPLICATION_JSON)
                             .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            //get
            result = mockMvc
                            .perform(
                                     get("/dataModels/{modelId}", String.valueOf(id))
                                                                                     .content(payload)
                                                                                     .contentType(MediaType.APPLICATION_JSON)
                                                                                     .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            //put
            result = mockMvc
                            .perform(
                                     put("/dataModels/{modelId}", String.valueOf(id))
                                                                                     .content(payload)
                                                                                     .contentType(MediaType.APPLICATION_JSON)
                                                                                     .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            //delete
            result = mockMvc
                            .perform(
                                     delete("/dataModels/{modelId}", String.valueOf(id))
                                                                                        .content(payload)
                                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                                        .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

        } finally {
            DataObjectModel dm = this.dataObjectModelManager.getDataObjectModel(id);
            if (null != dm) {
                this.dataObjectModelManager.removeDataObjectModel(dm);
            }
        }
    }
}
