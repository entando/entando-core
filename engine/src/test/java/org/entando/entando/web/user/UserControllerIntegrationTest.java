/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.user;

import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.Date;
import static junit.framework.TestCase.assertNull;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author paddeo
 */
public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    IUserManager userManager;

    @Autowired
    IGroupManager groupManager;

    @Autowired
    IRoleManager roleManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Test
    public void shouldAddUserAuthorities() throws Exception {
        Group group = createGroup(1);
        Role role = createRole(1);
        try {
            this.groupManager.addGroup(group);
            this.roleManager.addRole(role);
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "[{\"group\":\"group1\", \"role\":\"role1\"}]";

            ResultActions result = mockMvc.perform(
                    put("/users/{target}/authorities", "mockuser")
                            .sessionAttr("user", user)
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload[0].group", is("group1")));
        } finally {
            this.authorizationManager.deleteUserAuthorizations("mockuser");
            this.groupManager.removeGroup(group);
            this.roleManager.removeRole(role);
        }
    }

    @Test
    public void shouldAddRomoveUser() throws Exception {
        try {

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "{\n"
                    + "    \"username\": \"newuser\",\n"
                    + "    \"status\": \"active\",\n"
                    + "    \"password\": \"password\"\n"
                    + " }";

            ResultActions result = mockMvc.perform(
                    post("/users")
                            .sessionAttr("user", user)
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));

            String response = result.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + response);
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.username", is("newuser")));

            ResultActions resultDelete = mockMvc.perform(
                    delete("/users/{username}", "newuser")
                            .sessionAttr("user", user)
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));

            resultDelete.andExpect(status().isOk());
            resultDelete.andExpect(jsonPath("$.payload.code", is("newuser")));
            response = resultDelete.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + response);

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            UserDetails user = this.userManager.getUser("newuser");
            assertNull(user);
        }
    }

    private Group createGroup(int i) {
        Group group = new Group();
        group.setDescription("descr" + i);
        group.setName("group" + i);
        return group;
    }

    private Role createRole(int i) {
        Role role = new Role();
        role.setDescription("descr" + i);
        role.setName("role" + i);
        return role;
    }

    private UserDetails createUser(int i) {
        User user = new User();
        user.setUsername("user" + i);
        user.setDisabled(false);
        user.setLastAccess(new Date());
        user.setLastPasswordChange(new Date());
        user.setMaxMonthsSinceLastAccess(2);
        user.setMaxMonthsSinceLastPasswordChange(1);
        user.setPassword("password" + i);
        return user;
    }

}
