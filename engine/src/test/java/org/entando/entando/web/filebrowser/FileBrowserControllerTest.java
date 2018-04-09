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

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.storage.FileBrowserService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FileBrowserControllerTest extends AbstractControllerTest {

    @Mock
    private FileBrowserService fileBrowserService;

    @InjectMocks
    private FileBrowserController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void testValidRequest() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/fileBrowser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        Mockito.verify(fileBrowserService, Mockito.times(1)).browseFolder(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(2)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.size()", is(0)));
    }

}
