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

import org.entando.entando.aps.system.services.digitalexchange.marketplace.MarketplacesService;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.Marketplace;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MarketplacesControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/marketplaces";

    @Autowired
    private MarketplacesService marketplacesService;

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
    public void testCRUDMarketplace() throws Exception {

        String marketplaceName = "New Marketplace";

        try {
            // Create
            Marketplace marketplace = getMarketplace(marketplaceName);

            ResultActions result = createAuthRequest(post(BASE_URL))
                    .setContent(marketplace).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload.name", is(marketplaceName)));

            // Read
            result = createAuthRequest(get(BASE_URL + "/{name}", marketplaceName)).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload.name", is(marketplaceName)));

            // Update
            String url = "http://www.entando.com/";
            marketplace.setUrl(url);
            result = createAuthRequest(put(BASE_URL + "/{name}", marketplaceName))
                    .setContent(marketplace).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload.name", is(marketplaceName)));
            result.andExpect(jsonPath("$.payload.url", is(url)));

            // Delete
            result = createAuthRequest(delete(BASE_URL + "/{name}", marketplaceName)).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload", is(marketplaceName)));
        } catch (Exception ex) {
            marketplacesService.delete(marketplaceName);
            throw ex;
        }
    }

    @Test
    public void shouldFailCreatingMarketplaceBecauseAlreadyExists() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getMarketplace("Marketplace 1")).execute();

        result.andExpect(status().isConflict());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(MarketplaceValidator.ERRCODE_MARKETPLACE_ALREADY_EXISTS)));
    }

    @Test
    public void shouldFailFindingMarketplace() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/{name}", "Inexistent Marketplace")).execute();

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(MarketplaceValidator.ERRCODE_MARKETPLACE_NOT_FOUND)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldFailUpdatingMarketplaceBecauseNotFound() throws Exception {

        Marketplace marketplace = getMarketplace("Inexistent Marketplace");

        ResultActions result = createAuthRequest(put(BASE_URL + "/{name}", marketplace.getName()))
                .setContent(marketplace).execute();

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(MarketplaceValidator.ERRCODE_MARKETPLACE_NOT_FOUND)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldFailDeletingMarketplaceBecauseNotFound() throws Exception {

        ResultActions result = createAuthRequest(delete(BASE_URL + "/{name}", "Inexistent Marketplace")).execute();

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(MarketplaceValidator.ERRCODE_MARKETPLACE_NOT_FOUND)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }
    
    private Marketplace getMarketplace(String name) {
        Marketplace marketplace = new Marketplace();
        marketplace.setName(name);
        return marketplace;
    }
}
