/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.pagemodel;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.entando.entando.aps.system.services.pagemodel.PageModelTestUtil.validPageModelRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelManager;
import com.agiletec.aps.system.services.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.pagemodel.PageModelTestUtil;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class PageModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String USERNAME = "jack_bauer";
    private static final String PASSWORD = "0x24";
    private static final String PAGE_MODEL_CODE = "testPM";
    private static final String PAGE_MODEL_WITH_DOT_CODE = "test.PM";
    private static final String NONEXISTENT_PAGE_MODEL = "nonexistentPageModel";

    private String accessToken;
    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(NON_NULL);


    @Autowired
    private PageModelManager pageModelManager;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        setupAuthenticationDetails();
        deletePageModelsFromPreviousTests();
    }

    private void setupAuthenticationDetails() {
        User user = new OAuth2TestUtils.UserBuilder(USERNAME, PASSWORD)
                .grantedToRoleAdmin()
                .build();

        accessToken = mockOAuthInterceptor(user);
    }

    private void deletePageModelsFromPreviousTests() throws ApsSystemException {
        pageModelManager.deletePageModel(PAGE_MODEL_CODE);
    }


    @Test public void
    get_all_page_models_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
    }

    @Test public void
    get_page_model_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels/{code}", "home")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.references.length()", is(1)));
    }

    @Test public void
    get_page_models_reference_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels/{code}/references/{manager}", "home", "PageManager")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.totalItems", is(25)));
    }

    @Test
    public void shouldTestGetPageModelUsage() throws Exception {
        String code = "home";

        mockMvc.perform(get("/pageModels/{code}/usage", code)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.type", is(PageModelController.COMPONENT_ID)))
                .andExpect(jsonPath("$.payload.code", is(code)))
                .andExpect(jsonPath("$.payload.usage", is(25)))
                .andReturn();;
    }

    @Test public void
    add_repeated_page_model_return_conflict() throws Exception {
        // pageModel home always exists because it's created with DB.
        String payload = createPageModelPayload("home");

        ResultActions result = mockMvc.perform(
                post("/pageModels")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));

        result.andExpect(status().isConflict());
    }

    @Test public void
    add_page_model_return_OK() throws Exception {
        String payload = createPageModelPayload(PAGE_MODEL_CODE);

        ResultActions result = mockMvc.perform(
                post("/pageModels")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));

        result.andExpect(status().isOk());
    }

    private String createPageModelPayload(String pageModelCode) throws JsonProcessingException {
        PageModelRequest pageModelRequest = validPageModelRequest();
        pageModelRequest.setCode(pageModelCode);
        return createJson(pageModelRequest);
    }

    private String createJson(PageModelRequest pageModelRequest) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(pageModelRequest);
    }

    @Test public void
    get_nonexistent_page_model_return_not_found() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels/{code}", NONEXISTENT_PAGE_MODEL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isNotFound());
    }

    @Test public void
    delete_page_model_return_OK() throws Exception {

        PageModel pageModel = new PageModel();
        pageModel.setCode(PAGE_MODEL_CODE);
        pageModel.setDescription(PAGE_MODEL_CODE);
        pageModelManager.addPageModel(pageModel);

        ResultActions result = mockMvc.perform(
                delete("/pageModels/{code}", PAGE_MODEL_CODE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
    }

    @Test public void
    delete_page_model_nonexistent_code_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                delete("/pageModels/{code}", NONEXISTENT_PAGE_MODEL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
    }

    @Test public void
    add_page_model_with_dot_return_OK() throws Exception {
        try {
            PageModelRequest pageModelRequest = PageModelTestUtil.validPageModelRequest();

            pageModelRequest.setCode(PAGE_MODEL_WITH_DOT_CODE);

            ResultActions result = mockMvc.perform(
                    post("/pageModels")
                            .content(createJson(pageModelRequest))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(PAGE_MODEL_WITH_DOT_CODE)))
                    .andExpect(jsonPath("$.payload.descr", is("description")));

            pageModelRequest.setDescr("description2");

            result = mockMvc.perform(
                    put("/pageModels/{code}", PAGE_MODEL_WITH_DOT_CODE)
                            .content(createJson(pageModelRequest))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(PAGE_MODEL_WITH_DOT_CODE)))
                    .andExpect(jsonPath("$.payload.descr", is("description2")));

            result = mockMvc.perform(
                    get("/pageModels/{code}", PAGE_MODEL_WITH_DOT_CODE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(PAGE_MODEL_WITH_DOT_CODE)))
                    .andExpect(jsonPath("$.payload.descr", is("description2")));
        } finally {
            ResultActions result = mockMvc.perform(
                    delete("/pageModels/{code}", PAGE_MODEL_WITH_DOT_CODE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andDo(print())
                    .andExpect(status().isOk());
        }
    }
}
