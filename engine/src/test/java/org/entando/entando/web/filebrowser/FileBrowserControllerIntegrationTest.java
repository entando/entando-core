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
package org.entando.entando.web.filebrowser;

import org.entando.entando.web.dataobject.*;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FileBrowserControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private DataTypeController controller;

    @Test
    public void testCheckRequest() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testBrowseFolder_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(2)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(3)));
        result.andExpect(jsonPath("$.metaData.currentPath", is("")));
        result.andExpect(jsonPath("$.metaData.prevPath", is(CoreMatchers.nullValue())));
        result.andExpect(jsonPath("$.metaData.protectedFolder", is(false)));
    }

    @Test
    public void testBrowseFolder_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser").param("currentPath", "conf")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(3)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(3)));
        result.andExpect(jsonPath("$.metaData.currentPath", is("conf")));
        result.andExpect(jsonPath("$.metaData.prevPath", is("")));
        result.andExpect(jsonPath("$.metaData.protectedFolder", is(false)));
    }

    @Test
    public void testBrowseFolder_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser").param("currentPath", "conf/unexisting")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

    @Test
    public void testGetFile_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser/file").param("currentPath", "conf/systemParams.properties")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload.protectedFolder", is(false)));
        result.andExpect(jsonPath("$.payload.filename", Matchers.is("systemParams.properties")));
        result.andExpect(jsonPath("$.payload.path", Matchers.is("conf/systemParams.properties")));
        result.andExpect(jsonPath("$.payload.base64", Matchers.notNullValue()));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(1)));
        result.andExpect(jsonPath("$.metaData.prevPath", is("conf")));
    }

    @Test
    public void testGetFile_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser/file").param("currentPath", "conf/unexisting.txt")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

}
