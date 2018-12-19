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
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesMocker;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

@ActiveProfiles("DEcategoriesTest")
public class DigitalExchangeCategoriesControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/categories";

    @Configuration
    @Profile("DEcategoriesTest")
    public static class TestConfig {

        @Bean
        @Primary
        public RestTemplate getRestTemplate() {
            return new DigitalExchangesMocker()
                    .addDigitalExchange("DE 1", new SimpleRestResponse<>(Arrays.asList("pageModels", "fragments", "unsupportedType1")))
                    .addDigitalExchange("DE 2", new SimpleRestResponse<>(Arrays.asList("pageModels", "widgets", "unsupportedType2")))
                    .initMocks();
        }
    }

    @Test
    public void shouldReturnCategories() throws Exception {
        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.metaData").isEmpty());
        result.andExpect(jsonPath("$.errors").isEmpty());
        result.andExpect(jsonPath("$.payload", hasSize(3)));
        result.andExpect(jsonPath("$.payload", hasItem("pageModels")));
        result.andExpect(jsonPath("$.payload", hasItem("fragments")));
        result.andExpect(jsonPath("$.payload", hasItem("widgets")));
    }
}
