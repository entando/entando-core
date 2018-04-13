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
import java.net.URLEncoder;
import java.util.Date;
import static junit.framework.TestCase.assertNull;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
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
    public void testGetUsersDefaultSorting() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/users")
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.sort", is("username")));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
    }

    @Test
    public void testAddUserAuthorities() throws Exception {
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
    public void testAddRemoveUser_1() throws Exception {
        String validUsername = "valid.username_ok";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String invalidBody1 = "{\"username\": \"$invalid%%\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions resultInvalid1 = this.executeUserPost(invalidBody1, accessToken, status().isBadRequest());
            resultInvalid1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid1.andExpect(jsonPath("$.errors[0].code", is("2")));
            resultInvalid1.andExpect(jsonPath("$.metaData.size()", is(0)));

            String invalidBody2 = "{\"username\": \"usernamevelylong_.veryverylong\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions resultInvalid2 = this.executeUserPost(invalidBody2, accessToken, status().isBadRequest());
            resultInvalid2.andExpect(jsonPath("$.errors[0].code", is("2")));

            String invalidBody3 = "{\"username\": \"username with space\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions resultInvalid3 = this.executeUserPost(invalidBody3, accessToken, status().isBadRequest());
            resultInvalid3.andExpect(jsonPath("$.errors[0].code", is("2")));

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions result = this.executeUserPost(mockJson, accessToken, status().isOk());
            String response = result.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + response);
            result.andExpect(jsonPath("$.payload.username", is(validUsername)));

            ResultActions result2 = this.executeUserPost(mockJson, accessToken, status().isConflict());
            System.out.println(result2.andReturn().getResponse().getContentAsString());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("1")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions resultDelete = mockMvc.perform(
                    delete("/users/{username}", URLEncoder.encode(validUsername, "ISO-8859-1"))
                    .content(mockJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken));
            resultDelete.andExpect(status().isOk());
            resultDelete.andExpect(jsonPath("$.payload.code", is(validUsername)));
            System.out.println(resultDelete.andReturn().getResponse().getContentAsString());
        } catch (Throwable e) {
            this.userManager.removeUser(validUsername);
            e.printStackTrace();
            throw e;
        } finally {
            UserDetails user = this.userManager.getUser(validUsername);
            assertNull(user);
        }
    }

    @Test
    public void testAddRemoveUser_2() throws Exception {
        String validUsername = "valid.username_ok";
        String validPassword = "valid.123_ok";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String invalidBody1 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"$invalid%%\"}";
            ResultActions resultInvalid1 = this.executeUserPost(invalidBody1, accessToken, status().isBadRequest());
            resultInvalid1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid1.andExpect(jsonPath("$.errors[0].code", is("3")));
            resultInvalid1.andExpect(jsonPath("$.metaData.size()", is(0)));

            String invalidBody2 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"upasswordvelylong_.veryverylong\"}";
            ResultActions resultInvalid2 = this.executeUserPost(invalidBody2, accessToken, status().isBadRequest());
            resultInvalid2.andExpect(jsonPath("$.errors[0].code", is("3")));

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"" + validPassword + "\"}";
            ResultActions result = this.executeUserPost(mockJson, accessToken, status().isOk());
            String response = result.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + response);
            result.andExpect(jsonPath("$.payload.username", is(validUsername)));

            ResultActions resultDelete = mockMvc.perform(
                    delete("/users/{username}", URLEncoder.encode(validUsername, "ISO-8859-1"))
                    .content(mockJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken));
            resultDelete.andExpect(status().isOk());
            resultDelete.andExpect(jsonPath("$.payload.code", is(validUsername)));
            System.out.println(resultDelete.andReturn().getResponse().getContentAsString());
        } catch (Throwable e) {
            this.userManager.removeUser(validUsername);
            e.printStackTrace();
            throw e;
        } finally {
            UserDetails user = this.userManager.getUser(validUsername);
            assertNull(user);
        }
    }

    @Test
    public void testUpdateUser() throws Exception {
        String validUsername = "test_test";
        String validPassword = "password";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"" + validPassword + "\"}";
            this.executeUserPost(mockJson, accessToken, status().isOk());

            String invalidBody1 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"$invalid%%\"}";
            ResultActions resultInvalid1 = this.executeUserPut(invalidBody1, validUsername, accessToken, status().isBadRequest());
            System.out.println(resultInvalid1.andReturn().getResponse().getContentAsString());
            resultInvalid1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid1.andExpect(jsonPath("$.errors[0].code", is("3")));
            resultInvalid1.andExpect(jsonPath("$.metaData.size()", is(0)));

            String invalidBody2 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"upasswordvelylong_.veryverylong\"}";
            ResultActions resultInvalid2 = this.executeUserPut(invalidBody2, validUsername, accessToken, status().isBadRequest());
            resultInvalid2.andExpect(jsonPath("$.errors[0].code", is("3")));

            String invalidBody3 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"" + validPassword + "\"}";
            ResultActions resultInvalid3 = this.executeUserPut(invalidBody3, "invalidUsername", accessToken, status().isConflict());
            resultInvalid3.andExpect(jsonPath("$.errors[0].code", is("2")));

            String valid = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"12345678\",\"reset\": true}";
            ResultActions result = this.executeUserPut(valid, validUsername, accessToken, status().isOk());
            String response = result.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + response);
            result.andExpect(jsonPath("$.payload.username", is(validUsername)));
        } catch (Throwable e) {
            throw e;
        } finally {
            this.userManager.removeUser(validUsername);
        }
    }

    @Test
    public void testUpdatePassword() throws Exception {
        String validUsername = "valid.username_ok";
        String validPassword = "valid.123_ok";
        String newValidPassword = "valid.1234_ok";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"" + validPassword + "\"}";
            this.executeUserPost(mockJson, accessToken, status().isOk());

            String invalidBody1 = "{\"username\": \"" + validUsername + "\",\"oldPassword\": \"" + validPassword + "\",\"newPassword\": \"$invalid%%\"}";
            ResultActions resultInvalid1 = this.executeUpdatePassword(invalidBody1, validUsername, accessToken, status().isBadRequest());
            resultInvalid1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid1.andExpect(jsonPath("$.errors[0].code", is("3")));
            resultInvalid1.andExpect(jsonPath("$.metaData.size()", is(0)));

            String invalidBody2 = "{\"username\": \"" + validUsername + "\",\"oldPassword\": \"" + validPassword + "\",\"newPassword\": \"upasswordvelylong_.veryverylong\"}";
            ResultActions resultInvalid2 = this.executeUpdatePassword(invalidBody2, validUsername, accessToken, status().isBadRequest());
            resultInvalid2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid2.andExpect(jsonPath("$.errors[0].code", is("3")));
            resultInvalid2.andExpect(jsonPath("$.metaData.size()", is(0)));

            String invalidBody3 = "{\"username\": \"" + validUsername + "\",\"oldPassword\": \"invalid\",\"newPassword\": \"" + newValidPassword + "\"}";
            ResultActions resultInvalid3 = this.executeUpdatePassword(invalidBody3, validUsername, accessToken, status().isBadRequest());
            resultInvalid3.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid3.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid3.andExpect(jsonPath("$.errors[0].code", is("4")));
            resultInvalid3.andExpect(jsonPath("$.metaData.size()", is(0)));

            String invalidBody4 = "{\"username\": \"" + validUsername + "\",\"oldPassword\": \"invalid\",\"newPassword\": \"\"}";
            ResultActions resultInvalid4 = this.executeUpdatePassword(invalidBody4, validUsername, accessToken, status().isBadRequest());
            resultInvalid4.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid4.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid4.andExpect(jsonPath("$.errors[0].code", is("52")));
            resultInvalid4.andExpect(jsonPath("$.metaData.size()", is(0)));

            String validBody = "{\"username\": \"" + validUsername + "\",\"oldPassword\": \"" + validPassword + "\",\"newPassword\": \"" + newValidPassword + "\"}";
            ResultActions resultValid = this.executeUpdatePassword(validBody, validUsername, accessToken, status().isOk());
            resultValid.andExpect(jsonPath("$.payload.username", is(validUsername)));
            resultValid.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            resultValid.andExpect(jsonPath("$.metaData.size()", is(0)));

        } catch (Throwable e) {
            throw e;
        } finally {
            this.userManager.removeUser(validUsername);
        }
    }

    private ResultActions executeUserPost(String body, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeUserPut(String body, String username, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(put("/users/{username}", new Object[]{username})
                        .content(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeUpdatePassword(String body, String username, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/users/{username}/password", new Object[]{username})
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
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
