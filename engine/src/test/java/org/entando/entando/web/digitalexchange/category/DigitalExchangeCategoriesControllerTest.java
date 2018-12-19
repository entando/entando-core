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
package org.entando.entando.web.digitalexchange.category;

import java.util.Arrays;
import org.entando.entando.aps.system.services.digitalexchange.category.DigitalExchangeCategoriesService;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientListWrapper;
import org.entando.entando.web.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

public class DigitalExchangeCategoriesControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/digitalExchange/categories";

    @Mock
    private DigitalExchangeCategoriesService service;

    @InjectMocks
    private DigitalExchangeCategoriesResourceController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        initServiceMocks();
    }

    @Test
    public void shouldReturnCategories() throws Exception {
        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", hasSize(2)));
        result.andExpect(jsonPath("$.payload", hasItem("pageModels")));
        result.andExpect(jsonPath("$.payload", hasItem("fragments")));
    }

    private void initServiceMocks() {

        ResilientListWrapper<String> response = new ResilientListWrapper<>();
        response.getList().addAll(Arrays.asList("pageModels", "fragments"));

        when(service.getCategories()).thenReturn(response);
    }
}
