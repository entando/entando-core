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
package org.entando.entando.web.widget;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WidgetControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private WidgetController controller;

    @Test
    public void testGetCategories() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/widgets")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetWidget() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets/1")
                .header("Authorization", "Bearer " + accessToken)
        );
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetList_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets").param("pageSize", "100")
                .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(10)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(10)));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetList_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets").param("pageSize", "5")
                .param("sort", "code").param("direction", "DESC")
                .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(5)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(10)));
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.payload[0].code", is("search_result")));
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetList_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets").param("pageSize", "5")
                .param("sort", "code").param("direction", "DESC")
                .param("filters[0].attribute", "typology").param("filters[0].value", "oc")
                .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(4)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(4)));
        result.andExpect(jsonPath("$.payload[0].code", is("messages_system")));
        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

}
