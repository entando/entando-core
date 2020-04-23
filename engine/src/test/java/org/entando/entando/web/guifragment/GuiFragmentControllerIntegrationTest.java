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
package org.entando.entando.web.guifragment;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.servlet.security.CORSFilter;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

public class GuiFragmentControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    public void testGetFragments_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fragments")
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", CORSFilter.ALLOWED_METHODS));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(1)));

        result.andExpect(jsonPath("$.payload[0].widgetType.code", is("login_form")));
        result.andExpect(jsonPath("$.payload[0].widgetType.title", is("Widget di Login")));

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
        restListReq.setPageSize(4);
    }

    @Test
    public void testGetFragments_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc.perform(
                get("/fragments").param("page", "1")
                        .param("pageSize", "4")
                        .param("filter[0].attribute", "code")
                        .param("filter[0].value", "gin")
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(4)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(1)));

        result.andExpect(jsonPath("$.payload[0].widgetType.code", is("login_form")));
        result.andExpect(jsonPath("$.payload[0].widgetType.title", is("Widget di Login")));

        result = mockMvc.perform(
                get("/fragments").param("page", "1")
                        .param("pageSize", "10")
                        .param("filter[0].attribute", "code")
                        .param("filter[0].value", "test")
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(10)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(0)));
    }

    @Test
    public void testGetFragments_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc.perform(
                get("/fragments").param("page", "1")
                        .param("pageSize", "4")
                        .param("filter[0].attribute", "widgetType.code")
                        .param("filter[0].value", "gin")
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(4)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(1)));

        result.andExpect(jsonPath("$.payload[0].widgetType.code", is("login_form")));
        result.andExpect(jsonPath("$.payload[0].widgetType.title", is("Widget di Login")));

        result = mockMvc.perform(
                get("/fragments").param("page", "1")
                        .param("pageSize", "10")
                        .param("filter[0].attribute", "widgetType.code")
                        .param("filter[0].value", "test")
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.pageSize", is(10)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(0)));
    }

    @Test
    public void testGetFragmentUsage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String code = "login_form";

        mockMvc.perform(get("/fragments/{code}/usage".replace("{code}", code))
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.payload.type", is(GuiFragmentController.COMPONENT_ID)))
                .andExpect(jsonPath("$.payload.code", is(code)))
                .andExpect(jsonPath("$.payload.usage", is(0)))
                .andReturn();
    }

}
