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

import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesMocker;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentBuilder;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentsMocker;
import org.junit.Test;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("DEcomponentsTest")
public class DigitalExchangeComponentsControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/components";

    private static final String LAST_UPDATE_B = "2018-12-01 12:00:00";
    private static final String LAST_UPDATE_D = "2018-10-01 00:00:00";

    private static final String[] COMPONENTS_1 = new String[]{"A", "C", "E"};
    private static final DigitalExchangeComponent[] COMPONENTS_2 = new DigitalExchangeComponent[]{getComponentB(), getComponentD()};

    @Configuration
    @Profile("DEcomponentsTest")
    public static class TestConfig {

        @Bean
        @Primary
        public DigitalExchangeOAuth2RestTemplateFactory getRestTemplateFactory() {
            return new DigitalExchangesMocker()
                    .addDigitalExchange("DE 1", DigitalExchangeComponentsMocker.mock(COMPONENTS_1))
                    .addDigitalExchange("DE 2", DigitalExchangeComponentsMocker.mock(COMPONENTS_2))
                    .initMocks();
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
        result.andExpect(jsonPath("$.metaData.totalItems", is(COMPONENTS_1.length + COMPONENTS_2.length)));

        result.andExpect(jsonPath("$.errors").isEmpty());

        result.andExpect(jsonPath("$.payload[0].name", is("A")));
        result.andExpect(jsonPath("$.payload[1].name", is("B")));
        result.andExpect(jsonPath("$.payload[2].name", is("C")));
    }

    @Test
    public void shouldFilterByDateEq() throws Exception {
        ResultActions result = createAuthRequest(get(BASE_URL)
                .param("filters[0].attribute", "lastUpdate")
                .param("filters[0].operator", "eq")
                .param("filters[0].value", LAST_UPDATE_B))
                .execute();

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.payload", hasSize(1)));
        result.andExpect(jsonPath("$.payload[0].name", is("B")));
    }

    @Test
    public void shouldFilterByDateLt() throws Exception {
        ResultActions result = createAuthRequest(get(BASE_URL)
                .param("filters[0].attribute", "lastUpdate")
                .param("filters[0].operator", "lt")
                .param("filters[0].value", "2018-11-01 00:00:00"))
                .execute();

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.payload", hasSize(1)));
        result.andExpect(jsonPath("$.payload[0].name", is("D")));
    }

    private static DigitalExchangeComponent getComponentB() {
        return new DigitalExchangeComponentBuilder("B")
                .setLastUpdate(LAST_UPDATE_B)
                .build();
    }

    private static DigitalExchangeComponent getComponentD() {
        return new DigitalExchangeComponentBuilder("D")
                .setLastUpdate(LAST_UPDATE_D)
                .build();
    }
}
