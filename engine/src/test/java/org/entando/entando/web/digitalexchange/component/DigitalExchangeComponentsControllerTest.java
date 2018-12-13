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

import com.agiletec.aps.system.SystemConstants;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
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

    private static final String LAST_UPDATE = "2018-12-01 12:00:00";

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
        result.andExpect(jsonPath("$.payload[0].lastUpdate", is(LAST_UPDATE)));
    }

    @Test
    public void shouldValidateDate() throws Exception {
        ResultActions result = createAuthRequest(get(BASE_URL)
                .param("filters[0].attribute", "lastUpdate")
                .param("filters[0].value", "invalid-date"))
                .execute();

        result.andExpect(status().is4xxClientError());

        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    private void initServiceMocks() throws Exception {

        DigitalExchangeComponent component = new DigitalExchangeComponent();
        component.setName("A");

        SimpleDateFormat df = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        component.setLastUpdate(df.parse(LAST_UPDATE));

        List<DigitalExchangeComponent> components = Arrays.asList(component);

        RestError error = new RestError("1", "DE not available");

        ResilientPagedMetadata<DigitalExchangeComponent> fakeResponse = new ResilientPagedMetadata<>();
        fakeResponse.setBody(components);

        fakeResponse.setErrors(Arrays.asList(error));

        when(service.getComponents(any()))
                .thenReturn(fakeResponse);
    }
}
