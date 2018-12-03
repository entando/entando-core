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

import org.junit.Test;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangesMocker;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DigitalExchangeComponentsControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/components";

    @Configuration
    public static class TestConfig {

        @Bean
        @Primary
        public RestTemplate getRestTemplate() {
            return DigitalExchangesMocker.getRestMockedRestTemplate();
        }
    }

    @Test
    public void shouldReturnFirstPage() throws Exception {
        ResultActions result = createAuthRequest(get(BASE_URL)
                .param("sort", "name")
                .param("pageSize", "3")
                .param("page", "1")
        ).execute();

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData").isNotEmpty());
        result.andExpect(jsonPath("$.metaData.pageSize", is(3)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(DigitalExchangesMocker.getTotalComponentsCount())));

        result.andExpect(jsonPath("$.errors", hasSize(1)));

        result.andExpect(jsonPath("$.payload[0].name", is("A")));
        result.andExpect(jsonPath("$.payload[1].name", is("B")));
        result.andExpect(jsonPath("$.payload[2].name", is("C")));
    }
}
