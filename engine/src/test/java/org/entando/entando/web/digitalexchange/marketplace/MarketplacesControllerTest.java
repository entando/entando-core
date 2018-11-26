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
package org.entando.entando.web.digitalexchange.marketplace;

import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.MarketplacesService;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.Marketplace;
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

public class MarketplacesControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/digitalExchange/marketplaces";

    @Mock
    private MarketplacesService service;

    @Spy
    private MarketplaceValidator marketplaceValidator;

    @InjectMocks
    private MarketplacesControllerResource controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        when(service.getMarketplaces()).thenReturn(getFakeMarketplaces());
        when(service.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.findByName(any())).thenReturn(getFakeMarketplaces().get(0));
    }

    @Test
    public void shouldCreateMarketplace() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getMarketplace("New Marketplace")).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload.name", is("New Marketplace")));
    }

    @Test
    public void shouldFailCreatingMarketplaceBecauseNameIsEmpty() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(new Marketplace()).execute();

        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldReturnMarketplaces() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", hasSize(1)));
        result.andExpect(jsonPath("$.payload[0]", is("Marketplace 1")));
    }

    @Test
    public void shouldFindMarketplace() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/{marketplaceName}", "Marketplace 1")).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload.name", is("Marketplace 1")));
    }

    @Test
    public void shouldUpdateMarketplace() throws Exception {

        String name = "Marketplace 1";
        Marketplace marketplace = getMarketplace(name);
        String url = "http://www.entando.com";
        marketplace.setUrl(url);

        ResultActions result = createAuthRequest(put(BASE_URL + "/{marketplaceName}", name))
                .setContent(marketplace).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload.name", is(name)));
        result.andExpect(jsonPath("$.payload.url", is(url)));
    }

    @Test
    public void shouldFailUpdatingMarketplaceBecauseNameMismatch() throws Exception {

        Marketplace marketplace = getMarketplace("Marketplace 1");

        ResultActions result = createAuthRequest(put(BASE_URL + "/{marketplaceName}", "Different Name"))
                .setContent(marketplace).execute();

        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(MarketplaceValidator.ERRCODE_URINAME_MISMATCH)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldDeleteMarketplace() throws Exception {

        String name = "Marketplace 1";
        ResultActions result = createAuthRequest(delete(BASE_URL + "/{marketplaceName}", name)).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", is(name)));
    }

    private List<Marketplace> getFakeMarketplaces() {
        List<Marketplace> marketplaces = new ArrayList<>();
        marketplaces.add(getMarketplace("Marketplace 1"));
        return marketplaces;
    }

    private Marketplace getMarketplace(String name) {
        Marketplace marketplace = new Marketplace();
        marketplace.setName(name);
        return marketplace;
    }
}
