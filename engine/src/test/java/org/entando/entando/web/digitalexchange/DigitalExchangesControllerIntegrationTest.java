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

import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
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
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;

public class DigitalExchangesControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/exchanges";

    @Autowired
    private DigitalExchangesService digitalExchangeService;

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
    public void testCRUDDigitalExchange() throws Exception {

        String digitalExchangeName = "New DE";

        try {
            // Create
            DigitalExchange digitalExchange = getDigitalExchange(digitalExchangeName);

            ResultActions result = createAuthRequest(post(BASE_URL))
                    .setContent(digitalExchange).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload.name", is(digitalExchangeName)));

            // Read
            result = createAuthRequest(get(BASE_URL + "/{name}", digitalExchangeName)).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload.name", is(digitalExchangeName)));

            // Update
            String url = "http://www.entando.com/";
            digitalExchange.setUrl(url);
            result = createAuthRequest(put(BASE_URL + "/{name}", digitalExchangeName))
                    .setContent(digitalExchange).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload.name", is(digitalExchangeName)));
            result.andExpect(jsonPath("$.payload.url", is(url)));

            // Delete
            result = createAuthRequest(delete(BASE_URL + "/{name}", digitalExchangeName)).execute();

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.metaData").isEmpty());
            result.andExpect(jsonPath("$.errors").isEmpty());
            result.andExpect(jsonPath("$.payload", is(digitalExchangeName)));
        } catch (Exception ex) {
            digitalExchangeService.delete(digitalExchangeName);
            throw ex;
        }
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseAlreadyExists() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getDigitalExchange("DE 1")).execute();

        result.andExpect(status().isConflict());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_ALREADY_EXISTS)));
    }

    @Test
    public void shouldFailFindingDigitalExchange() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/{name}", "Inexistent DE")).execute();

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldFailUpdatingDigitalExchangeBecauseNotFound() throws Exception {

        DigitalExchange digitalExchange = getDigitalExchange("Inexistent DE");

        ResultActions result = createAuthRequest(put(BASE_URL + "/{name}", digitalExchange.getName()))
                .setContent(digitalExchange).execute();

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldFailDeletingDigitalExchangeBecauseNotFound() throws Exception {

        ResultActions result = createAuthRequest(delete(BASE_URL + "/{name}", "Inexistent DE")).execute();

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND)));
        result.andExpect(jsonPath("$.payload").isEmpty());
    }
    
    private DigitalExchange getDigitalExchange(String name) {
        DigitalExchange digitalExchange = new DigitalExchange();
        digitalExchange.setName(name);
        return digitalExchange;
    }
}
