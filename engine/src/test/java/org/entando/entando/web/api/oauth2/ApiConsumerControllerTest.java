/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.api.oauth2;

import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.ApiConsumerServiceImpl;
import org.entando.entando.aps.system.services.oauth2.OAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.api.oauth2.validator.ApiConsumerValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ApiConsumerControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/consumers";

    private final ApiConsumerValidator validator = new ApiConsumerValidator();

    @Mock
    private OAuthConsumerManager consumerManager;

    private ApiConsumerServiceImpl apiConsumerService;

    private ApiConsumerResourceController controller;

    @Before
    public void setUp() throws Exception {

        apiConsumerService = new ApiConsumerServiceImpl(consumerManager);
        controller = new ApiConsumerResourceController(validator, apiConsumerService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void testNotFound() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/valid_key")).execute();
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(1)));

        result = createAuthRequest(put(BASE_URL + "/valid_key"))
                .setContent(getValidPayload()).execute();
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void testAlreadyExists() throws Exception {

        when(consumerManager.getConsumerRecord("valid_key")).thenReturn(new ConsumerRecordVO());

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getValidPayload()).execute();

        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is("4")));
    }

    @Test
    public void testKeyMismatch() throws Exception {

        ApiConsumer apiConsumer = getValidPayload();

        ResultActions result = createAuthRequest(put(BASE_URL + "/different_key"))
                .setContent(apiConsumer).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is("3")));
    }

    @Test
    public void testFieldsValidation() throws Exception {

        testValidationErrorPost(c -> c.setKey(null));
        testValidationErrorPost(c -> c.setKey("x")); // too short
        testValidationErrorPost(c -> c.setKey(StringUtils.repeat("x", 101))); // too long
        testValidationErrorPost(c -> c.setKey("not alphanumeric"));

        testValidationErrorPost(c -> c.setSecret(null));
        testValidationErrorPost(c -> c.setSecret(StringUtils.repeat("x", 101))); // too long
        testValidationErrorPost(c -> c.setSecret("not alphanumeric"));

        testValidationErrorPost(c -> c.setName(null));
        testValidationErrorPost(c -> c.setName(StringUtils.repeat("x", 101))); // too long

        testValidationErrorPost(c -> c.setDescription(null));
        testValidationErrorPost(c -> c.setDescription(StringUtils.repeat("x", 501))); // too long

        testValidationErrorPost(c -> c.getAuthorizedGrantTypes().add("invalid_grant"))
                .andExpect(jsonPath("$.errors[0].code", is("2")));
    }

    private ResultActions testValidationErrorPost(Consumer<ApiConsumer> consumer) throws Exception {

        ApiConsumer apiConsumer = getValidPayload();

        consumer.accept(apiConsumer);

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(apiConsumer).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)));

        return result;
    }

    private ApiConsumer getValidPayload() {

        ApiConsumer apiConsumer = new ApiConsumer();
        apiConsumer.setKey("valid_key");
        apiConsumer.setName("name");
        apiConsumer.setSecret("secret");
        apiConsumer.setDescription("description");
        return apiConsumer;
    }
}
