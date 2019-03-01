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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import org.entando.entando.aps.system.services.oauth2.OAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiConsumerControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/consumers";

    private static final String CONSUMER_KEY_1 = "consumerKey1";
    private static final String CONSUMER_KEY_2 = "consumerKey2";
    private static final String CONSUMER_KEY_3 = "consumerKey3";

    private static final String EXPIRATION_DATE = "2020-01-01 00:00:00";

    @Autowired
    private OAuthConsumerManager consumerManager;

    private final ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private String accessToken;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .grantedToRoleAdmin().build();
        accessToken = mockOAuthInterceptor(user);
    }

    @Test
    public void testCRUDConsumer() throws Exception {

        try {
            ApiConsumer consumer = getPayload();

            // Create
            ResultActions result = authRequest(post(BASE_URL)
                    .content(jsonMapper.writeValueAsString(consumer)));

            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.payload.key", is(CONSUMER_KEY_3)))
                    .andExpect(jsonPath("$.payload.name", is(consumer.getName())))
                    .andExpect(jsonPath("$.payload.description", is(consumer.getDescription())))
                    .andExpect(jsonPath("$.payload.secret").isEmpty())
                    .andExpect(jsonPath("$.payload.issuedDate").isNotEmpty())
                    .andExpect(jsonPath("$.payload.expirationDate", is(EXPIRATION_DATE)))
                    .andExpect(jsonPath("$.payload.scope", is(consumer.getScope())))
                    .andExpect(jsonPath("$.payload.callbackUrl", is(consumer.getCallbackUrl())))
                    .andExpect(jsonPath("$.payload.authorizedGrantTypes", hasSize(2)))
                    .andExpect(jsonPath("$.payload.authorizedGrantTypes", contains("authorization_code", "implicit")));

            // Secret must be serialized but not deserialized
            assertThat(consumerManager.getConsumerRecord(CONSUMER_KEY_3).getSecret()).isNotNull();

            // Read
            result = authRequest(get(BASE_URL + "/" + CONSUMER_KEY_3));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.key", is(CONSUMER_KEY_3)))
                    .andExpect(jsonPath("$.payload.name", is(consumer.getName())))
                    .andExpect(jsonPath("$.payload.description", is(consumer.getDescription())))
                    .andExpect(jsonPath("$.payload.secret").isEmpty())
                    .andExpect(jsonPath("$.payload.issuedDate").isNotEmpty())
                    .andExpect(jsonPath("$.payload.expirationDate", is(EXPIRATION_DATE)))
                    .andExpect(jsonPath("$.payload.scope", is(consumer.getScope())))
                    .andExpect(jsonPath("$.payload.callbackUrl", is(consumer.getCallbackUrl())))
                    .andExpect(jsonPath("$.payload.authorizedGrantTypes", hasSize(2)))
                    .andExpect(jsonPath("$.payload.authorizedGrantTypes", contains("authorization_code", "implicit")));

            // Update
            String updatedName = "updated_name";
            consumer.setName(updatedName);

            result = authRequest(put(BASE_URL + "/" + CONSUMER_KEY_3)
                    .content(jsonMapper.writeValueAsString(consumer)));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.key", is(CONSUMER_KEY_3)))
                    .andExpect(jsonPath("$.payload.name", is(updatedName)));

            assertThat(consumerManager.getConsumerRecord(CONSUMER_KEY_3).getName()).isEqualTo(updatedName);

            // Delete
            result = authRequest(delete(BASE_URL + "/" + CONSUMER_KEY_3));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload", is(CONSUMER_KEY_3)));

            assertThat(consumerManager.getConsumerRecord(CONSUMER_KEY_3)).isNull();

        } catch (Throwable t) {
            throw t;
        } finally {
            consumerManager.deleteConsumer(CONSUMER_KEY_3);
        }
    }

    @Test
    public void testFilterAndSort() throws Exception {

        try {
            consumerManager.addConsumer(getConsumer1());
            consumerManager.addConsumer(getConsumer2());

            // No filters
            ResultActions result = authRequest(get(BASE_URL));
            result.andExpect(status().isOk());

            // Name filter
            result = authRequest(get(BASE_URL)
                    .param("filters[0].attribute", "name")
                    .param("filters[0].value", "Consumer 1"));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload", hasSize(1)))
                    .andExpect(jsonPath("$.payload[0].key", is(CONSUMER_KEY_1)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(1)));

            // Sort by name
            result = authRequest(getSortRequest("name", "DESC"));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload", hasSize(2)))
                    .andExpect(jsonPath("$.payload[0].key", is(CONSUMER_KEY_2)))
                    .andExpect(jsonPath("$.payload[1].key", is(CONSUMER_KEY_1)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(2)));

            // Sort by issuedDate
            result = authRequest(getSortRequest("issuedDate", "ASC"));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload", hasSize(2)))
                    .andExpect(jsonPath("$.payload[0].key", is(CONSUMER_KEY_2)))
                    .andExpect(jsonPath("$.payload[1].key", is(CONSUMER_KEY_1)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(2)));

            // Sort by expirationDate
            result = authRequest(getSortRequest("expirationDate", "ASC"));

            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload", hasSize(2)))
                    .andExpect(jsonPath("$.payload[0].key", is(CONSUMER_KEY_1)))
                    .andExpect(jsonPath("$.payload[1].key", is(CONSUMER_KEY_2)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(2)));

        } catch (Throwable t) {
            consumerManager.deleteConsumer(CONSUMER_KEY_1);
            consumerManager.deleteConsumer(CONSUMER_KEY_2);
        }
    }

    private MockHttpServletRequestBuilder getSortRequest(String sortKey, String direction) {
        return get(BASE_URL)
                .param("filters[0].attribute", "key")
                .param("filters[0].value", String.join(",", CONSUMER_KEY_1, CONSUMER_KEY_2))
                .param("sort", sortKey)
                .param("direction", direction);
    }

    private ApiConsumer getPayload() {
        ApiConsumer consumer = new ApiConsumer();
        consumer.setKey(CONSUMER_KEY_3);
        consumer.setName("Consumer 1");
        consumer.setDescription("descr");
        consumer.setSecret("secret");
        consumer.setExpirationDate(getDate(EXPIRATION_DATE));
        consumer.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "implicit"));
        consumer.setScope("scope");
        consumer.setCallbackUrl("http://entando.org");
        return consumer;
    }

    private ConsumerRecordVO getConsumer1() {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        consumer.setKey(CONSUMER_KEY_1);
        consumer.setName("Consumer 1");
        consumer.setDescription("Description 1");
        consumer.setSecret("secret");
        consumer.setIssuedDate(getDate2());
        consumer.setExpirationDate(getDate3());
        return consumer;
    }

    private ConsumerRecordVO getConsumer2() {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        consumer.setKey(CONSUMER_KEY_2);
        consumer.setName("Consumer 2");
        consumer.setDescription("Description 2");
        consumer.setSecret("secret");
        consumer.setIssuedDate(getDate1());
        consumer.setExpirationDate(getDate4());
        return consumer;
    }

    private Date getDate1() {
        return getDate("2018-01-01 00:00:00");
    }

    private Date getDate2() {
        return getDate("2019-01-01 00:00:00");
    }

    private Date getDate3() {
        return getDate("2020-01-01 00:00:00");
    }

    private Date getDate4() {
        return getDate("2022-01-01 00:00:00");
    }

    private Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ResultActions authRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder
                .header("Authorization", "Bearer " + accessToken)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(APPLICATION_JSON_UTF8));
    }
}
