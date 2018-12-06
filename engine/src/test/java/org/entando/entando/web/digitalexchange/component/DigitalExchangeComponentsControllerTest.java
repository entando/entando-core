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
package org.entando.entando.web.digitalexchange.component;

import java.util.Arrays;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentsService;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.RestError;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DigitalExchangeComponentsControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/digitalExchange/components";

    @Mock
    private DigitalExchangeComponentsService service;

    @InjectMocks
    private DigitalExchangeComponentsController controller;

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
    public void shouldReturnComponents() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.metaData").isNotEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.payload", hasSize(1)));
        result.andExpect(jsonPath("$.payload[0].name", is("A")));
    }

    private void initServiceMocks() {

        DigitalExchangeComponent component = new DigitalExchangeComponent();
        component.setName("A");
        List<DigitalExchangeComponent> components = Arrays.asList(component);

        RestError error = new RestError("1", "DE not available");

        ResilientPagedMetadata<DigitalExchangeComponent> fakeResponse = new ResilientPagedMetadata<>();
        fakeResponse.setBody(components);

        fakeResponse.setErrors(Arrays.asList(error));

        when(service.getComponents(any()))
                .thenReturn(fakeResponse);
    }
}
