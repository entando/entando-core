/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.role;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.role.RoleService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.role.model.RoleRequest;
import org.entando.entando.web.role.validator.RoleValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerUnitTest extends AbstractControllerTest {

    @Mock
    private RoleService roleService;

    @Spy
    private RoleValidator roleValidator;

    @InjectMocks
    private RoleController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void testAddWithEmptyPayload() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ObjectMapper mapper = new ObjectMapper();
        RoleRequest request = new RoleRequest();
        request.setCode("");
        //request.setCode(code);

        String payload = mapper.writeValueAsString(request);
        ResultActions result = mockMvc.perform(
                post("/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testAddWithInvalidCodeAndName() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ObjectMapper mapper = new ObjectMapper();
        RoleRequest request = new RoleRequest();
        request.setCode(StringUtils.repeat("a", 100));
        request.setName(StringUtils.repeat("b", 100));
        //request.setCode(code);

        String payload = mapper.writeValueAsString(request);
        ResultActions result = mockMvc.perform(
                post("/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
    }

}
