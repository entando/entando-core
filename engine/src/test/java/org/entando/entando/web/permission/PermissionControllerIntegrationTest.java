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
package org.entando.entando.web.permission;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PermissionControllerIntegrationTest extends AbstractControllerIntegrationTest {


    @Test
    public void testGetPermissions() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/permissions")
                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }


    @Test
    public void testGetPermissionsFilterByCode() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/permissions")
                                                                  .param("filter[0].attribute", "code")
                                                                  .param("filter[0].value", "manage")
                                                                  .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(3)));
    }


    @Test
    public void testGetPermissionsFilterByDescr() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/permissions")
                                                            .param("filter[0].attribute", "descr")
                                                            .param("filter[0].value", "Accesso")
                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(1)));
    }

    @Test
    public void testGetPermissionsWithoutPermission() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("normal_user", "0x24").build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/permissions")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isForbidden());
    }

    @Test
    public void testGetPermissionsWithEnterBackEndPermission() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("enter_backend_user", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "admin", Permission.ENTER_BACKEND).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/permissions")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

}
