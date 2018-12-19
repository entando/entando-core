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
package org.entando.entando.web.digitalexchange;

import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Spy;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;

public class DigitalExchangesControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/digitalExchange/exchanges";

    @Mock
    private DigitalExchangesService service;

    @Spy
    private DigitalExchangeValidator digitalExchangeValidator;

    @InjectMocks
    private DigitalExchangesResourceController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        when(service.getDigitalExchanges()).thenReturn(getFakeDigitalExchanges());
        when(service.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.findByName(any())).thenReturn(getFakeDigitalExchanges().get(0));
        when(service.test(any())).thenReturn(new ArrayList<>());
    }

    @Test
    public void shouldCreateDigitalExchange() throws Exception {

        String name = "New DE";

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getDigitalExchange(name)).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload.name", is(name)));
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseNameIsEmpty() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getDigitalExchange("")).execute();

        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseURLIsNotSet() throws Exception {
        DigitalExchange digitalExchange = getDigitalExchange("New DE");
        digitalExchange.setUrl(null);

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldReturnDigitalExchanges() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", hasSize(1)));
        result.andExpect(jsonPath("$.payload[0]", is("DE 1")));
    }

    @Test
    public void shouldFindDigitalExchange() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/{name}", "DE 1")).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload.name", is("DE 1")));
    }

    @Test
    public void shouldUpdateDigitalExchange() throws Exception {

        String name = "DE 1";
        DigitalExchange digitalExchange = getDigitalExchange(name);
        String url = "http://de1.entando.com";
        digitalExchange.setUrl(url);

        ResultActions result = createAuthRequest(put(BASE_URL + "/{name}", name))
                .setContent(digitalExchange).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload.name", is(name)));
        result.andExpect(jsonPath("$.payload.url", is(url)));
    }

    @Test
    public void shouldFailUpdatingDigitalExchangeBecauseNameMismatch() throws Exception {

        DigitalExchange digitalExchange = getDigitalExchange("DE 1");

        ResultActions result = createAuthRequest(put(BASE_URL + "/{name}", "Different Name"))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_URINAME_MISMATCH)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldDeleteDigitalExchange() throws Exception {

        String name = "DE 1";
        ResultActions result = createAuthRequest(delete(BASE_URL + "/{name}", name)).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", is(name)));
    }

    @Test
    public void shouldTestInstance() throws Exception {
        
        String name = "DE 1";
        ResultActions result = createAuthRequest(get(BASE_URL + "/test/{name}", name)).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", is("OK")));
    }
    
    private List<DigitalExchange> getFakeDigitalExchanges() {
        List<DigitalExchange> digitalExchanges = new ArrayList<>();
        digitalExchanges.add(getDigitalExchange("DE 1"));
        return digitalExchanges;
    }

    private DigitalExchange getDigitalExchange(String name) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        digitalExchange.setUrl("http://de1.entando.com/");
        return digitalExchange;
    }
}
