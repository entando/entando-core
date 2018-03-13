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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModelService;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.entando.entando.web.dataobjectmodel.validator.DataObjectModelValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataObjectModelControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DataObjectModelService dataObjectModelService;

    @InjectMocks
    private DataObjectModelController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_load_the_list_of_dataModels() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(dataObjectModelService.getDataObjectModels(any(RestListRequest.class))).thenReturn(new PagedMetadata<DataModelDto>());
        ResultActions result = mockMvc.perform(
                get("/dataModels").param("page", "1")
                .param("pageSize", "4")
                .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
        restListReq.setPageSize(4);
        Mockito.verify(dataObjectModelService, Mockito.times(1)).getDataObjectModels(restListReq);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_load_the_list_of_dataModels_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(dataObjectModelService.getDataObjectModels(any(RestListRequest.class))).thenReturn(new PagedMetadata<DataModelDto>());
        ResultActions result = mockMvc.perform(
                get("/dataModels").param("page", "1")
                .param("pageSize", "4")
                .param("filter[0].attribute", "code")
                .param("filter[0].value", "1")
                .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
        restListReq.setPageSize(4);
        restListReq.addFilter(new Filter("code", "1"));
        Mockito.verify(dataObjectModelService, Mockito.times(1)).getDataObjectModels(restListReq);
    }

    @Test
    public void should_be_unauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc.perform(
                get("/dataModels")
                .header("Authorization", "Bearer " + accessToken)
        );
        //String response = result.andReturn().getResponse().getContentAsString();
        //System.out.println(response);
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void should_validate_put_path_mismatch() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ObjectMapper mapper = new ObjectMapper();
        DataObjectModelRequest group = new DataObjectModelRequest();
        group.setModelId("2l");
        group.setDescr("Description");
        group.setType("AAA");
        group.setModel("<p>Test</p>");
        String payload = mapper.writeValueAsString(group);
        this.controller.setDataObjectModelValidator(new DataObjectModelValidator());
        ResultActions result = mockMvc.perform(put("/dataModels/{dataModelId}", "67")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
    }

}
