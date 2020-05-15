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

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.MockMvcHelper;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private IAuthenticationProviderManager authenticationProviderManager;

    private MockMvcHelper mockMvcHelper;

    @Test
    public void testGetUsersDefaultSorting() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/users")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.sort", is("username")));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
    }

    @Test
    public void testGetUsersWithProfile() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/users")
                        .param("withProfile", "1")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(Matchers.greaterThan(0))));
        result.andExpect(jsonPath("$.metaData.additionalParams.withProfile", is("1")));
        System.out.println("with profile: " + result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetUsersWithoutProfile() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/users")
                        .param("withProfile", "0")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        System.out.println("with no profile: " + result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.metaData.additionalParams.withProfile", is("0")));
    }

    @Test
    public void testGetUsersWithProfileAndProfileAttributesFilters() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/users")
                        .param("withProfile", "1")
                        .param("filter[0].entityAttr", "fullname")
                        .param("filter[0].operator", "like")
                        .param("filter[0].value", "s")
                        .param("filter[1].attribute", "profileType")
                        .param("filter[1].operator", "eq")
                        .param("filter[1].value", "All")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        System.out.println("with profile attr: " + result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(Matchers.greaterThan(0))));
        result.andExpect(jsonPath("$.metaData.additionalParams.withProfile", is("1")));
        result.andExpect(jsonPath("$.payload[0].profileAttributes.fullname", Matchers.containsString("s")));

    }

    @Test
    public void testAddUserAuthorities_1() throws Exception {
        Group group = createGroup(1);
        Role role = createRole(1);
        UserDetails mockuser = this.createUser("mockuser");
        try {
            this.groupManager.addGroup(group);
            this.roleManager.addRole(role);
            this.userManager.addUser(mockuser);
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            String mockJson = "[{\"group\":\"group1\", \"role\":\"role1\"}]";
            ResultActions result1 = mockMvc.perform(
                    put("/users/{target}/authorities", "wrongUser")
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result1.andExpect(status().isNotFound());
            ResultActions result2 = mockMvc.perform(
                    put("/users/{target}/authorities", "mockuser")
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result2.andExpect(status().isOk());
            result2.andExpect(jsonPath("$.payload[0].group", is("group1")));

            ResultActions result3 = mockMvc.perform(
                    get("/users/{target}/authorities", "wrongUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result3.andExpect(status().isNotFound());

            ResultActions result4 = mockMvc.perform(
                    get("/users/{target}/authorities", "mockuser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result4.andExpect(status().isOk());
            result4.andExpect(jsonPath("$.payload", Matchers.hasSize(1)));
            result4.andExpect(jsonPath("$.payload[0].group", is("group1")));
            result4.andExpect(jsonPath("$.payload[0].role", is("role1")));
        } finally {
            this.authorizationManager.deleteUserAuthorizations("mockuser");
            this.groupManager.removeGroup(group);
            this.roleManager.removeRole(role);
            this.userManager.removeUser("mockuser");
        }
    }

    @Test
    public void testAddUserAuthorities_2() throws Exception {
        Group group = createGroup(1);
        Role role = createRole(1);
        String username = "mockuser_1";
        UserDetails mockuser = this.createUser(username);
        try {
            this.groupManager.addGroup(group);
            this.roleManager.addRole(role);
            this.userManager.addUser(mockuser);
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            String mockJson1 = "[{\"group\":\"group1\", \"role\":\"role1\"}]";
            ResultActions result1 = mockMvc.perform(
                    post("/users/{target}/authorities", username)
                            .content(mockJson1).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result1.andExpect(status().isOk());
            result1.andExpect(jsonPath("$.payload[0].group", is("group1")));

            List<Authorization> auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(1, auths.size());

            String mockJson2 = "[{\"group\":\"customers\", \"role\":\"supervisor\"}]";
            ResultActions result2 = mockMvc.perform(
                    post("/users/{target}/authorities", username)
                            .content(mockJson2).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result2.andExpect(status().isOk());
            result2.andExpect(jsonPath("$.payload[0].group", is("customers")));

            auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(2, auths.size());

            ResultActions result3 = mockMvc.perform(
                    get("/users/{target}/authorities", username)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result3.andExpect(status().isOk());
            result3.andExpect(jsonPath("$.payload", Matchers.hasSize(2)));

            String mockJson4 = "[{\"group\":\"helpdesk\", \"role\":\"pageManager\"}]";

            ResultActions result4 = mockMvc.perform(
                    put("/users/{target}/authorities", username)
                            .content(mockJson4).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result4.andExpect(status().isOk());
            result4.andExpect(jsonPath("$.payload[0].group", is("helpdesk")));

            auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(1, auths.size());
            Assert.assertEquals("helpdesk", auths.get(0).getGroup().getName());

            String mockJson5 = "[{\"group\":\"wrong_group\", \"role\":\"pageManager\"}]";
            ResultActions result5 = mockMvc.perform(
                    put("/users/{target}/authorities", username)
                            .content(mockJson5).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result5.andExpect(status().isBadRequest());
            result5.andExpect(jsonPath("$.errors.size()", is(1)));
            result5.andExpect(jsonPath("$.errors[0].code", is("2")));

            auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(1, auths.size());
            Assert.assertEquals("helpdesk", auths.get(0).getGroup().getName());

        } finally {
            this.authorizationManager.deleteUserAuthorizations(username);
            this.groupManager.removeGroup(group);
            this.roleManager.removeRole(role);
            this.userManager.removeUser(username);
        }
    }

    @Test
    public void testAddUserAuthorities_3() throws Exception {
        Group group = this.createGroup(100);
        Role role = this.createRole(100);
        String username = "mockuser_2";
        UserDetails mockuser = this.createUser(username);
        try {
            this.groupManager.addGroup(group);
            this.roleManager.addRole(role);
            this.userManager.addUser(mockuser);
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            String mockJson1 = "[{\"group\":null, \"role\":\"role100\"}]";
            ResultActions result1 = mockMvc.perform(
                    post("/users/{target}/authorities", username)
                            .content(mockJson1).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result1.andExpect(status().isOk());
            result1.andExpect(jsonPath("$.payload[0].role", is("role100")));

            List<Authorization> auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(1, auths.size());

            String mockJson2 = "[{\"group\":\"customers\", \"role\":null}]";
            ResultActions result2 = mockMvc.perform(
                    post("/users/{target}/authorities", username)
                            .content(mockJson2).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result2.andExpect(status().isOk());
            result2.andExpect(jsonPath("$.payload[0].group", is("customers")));

            auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(2, auths.size());

            ResultActions result3 = mockMvc.perform(
                    get("/users/{target}/authorities", username)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result3.andExpect(status().isOk());
            result3.andExpect(jsonPath("$.payload", Matchers.hasSize(2)));

            String mockJson4 = "[{\"group\":null, \"role\":null}]";

            ResultActions result4 = mockMvc.perform(
                    put("/users/{target}/authorities", username)
                            .content(mockJson4).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result4.andExpect(status().isBadRequest());
            result4.andExpect(jsonPath("$.errors.size()", is(1)));

            auths = this.authorizationManager.getUserAuthorizations(username);
            Assert.assertEquals(2, auths.size());

        } finally {
            this.authorizationManager.deleteUserAuthorizations(username);
            this.groupManager.removeGroup(group);
            this.roleManager.removeRole(role);
            this.userManager.removeUser(username);
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

            String invalidBody2 = "{\"username\": \"usernamevelylong_.veryveryveryveryveryveryveryveryveryveryveryveryveryveryverylong\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions resultInvalid2 = this.executeUserPost(invalidBody2, accessToken, status().isBadRequest());
            resultInvalid2.andExpect(jsonPath("$.errors[0].code", is("2")));

            String invalidBody3 = "{\"username\": \"username with space\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions resultInvalid3 = this.executeUserPost(invalidBody3, accessToken, status().isBadRequest());
            resultInvalid3.andExpect(jsonPath("$.errors[0].code", is("2")));

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions result = this.executeUserPost(mockJson, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.username", is(validUsername)));

            ResultActions result2 = this.executeUserPost(mockJson, accessToken, status().isConflict());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("1")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions resultDelete = mockMvc.perform(
                    delete("/users/{username}", URLEncoder.encode(validUsername, "ISO-8859-1"))
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            resultDelete.andExpect(status().isOk());
            resultDelete.andExpect(jsonPath("$.payload.code", is(validUsername)));
        } catch (Throwable e) {
            this.userManager.removeUser(validUsername);
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
            result.andExpect(jsonPath("$.payload.username", is(validUsername)));

            ResultActions resultDelete = mockMvc.perform(
                    delete("/users/{username}", URLEncoder.encode(validUsername, "ISO-8859-1"))
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            resultDelete.andExpect(status().isOk());
            resultDelete.andExpect(jsonPath("$.payload.code", is(validUsername)));
        } catch (Throwable e) {
            this.userManager.removeUser(validUsername);
            throw e;
        } finally {
            UserDetails user = this.userManager.getUser(validUsername);
            assertNull(user);
        }
    }

    @Test
    public void testAddUserWithLongName() throws Exception {
        String validUsername = "valid.username_with_very_long_name_with_a_total_of_80_characters_maximum_allowed";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"password\"}";
            ResultActions result = this.executeUserPost(mockJson, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.username", is(validUsername)));

            ResultActions result2 = this.executeUserPost(mockJson, accessToken, status().isConflict());
            result2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            result2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            result2.andExpect(jsonPath("$.errors[0].code", is("1")));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions resultDelete = mockMvc.perform(
                    delete("/users/{username}", URLEncoder.encode(validUsername, "ISO-8859-1"))
                            .content(mockJson)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            resultDelete.andExpect(status().isOk());
            resultDelete.andExpect(jsonPath("$.payload.code", is(validUsername)));
        } catch (Throwable e) {
            this.userManager.removeUser(validUsername);
            throw e;
        } finally {
            UserDetails user = this.userManager.getUser(validUsername);
            assertNull(user);
        }
    }

    @Test
    public void testAddUserWithNameTooLong() throws Exception {
        String invalidUsername = "invalid.username_with_too_many_characters_81_one_more_than_the_maximum_allowed_80";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "{\"username\": \"" + invalidUsername + "\",\"status\": \"active\",\"password\": \"password\"}";
            this.executeUserPost(mockJson, accessToken, status().isBadRequest());

        } catch (Throwable e) {
            this.userManager.removeUser(invalidUsername);
            throw e;
        } finally {
            UserDetails user = this.userManager.getUser(invalidUsername);
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

            String valid1 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"reset\": true}";
            ResultActions resultValid1 = this.executeUserPut(valid1, validUsername, accessToken, status().isOk());
            String responseValid1 = resultValid1.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseValid1);
            resultValid1.andExpect(jsonPath("$.payload.username", is(validUsername)));
            UserDetails authUser = this.authenticationProviderManager.getUser(validUsername, validPassword);
            Assert.assertNotNull(authUser);

            String valid2 = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"12345678\",\"reset\": true}";
            ResultActions resultValid2 = this.executeUserPut(valid2, validUsername, accessToken, status().isOk());
            String responseValid2 = resultValid2.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseValid2);
            resultValid2.andExpect(jsonPath("$.payload.username", is(validUsername)));
            authUser = this.authenticationProviderManager.getUser(validUsername, validPassword);
            Assert.assertNull(authUser);
            authUser = this.authenticationProviderManager.getUser(validUsername, "12345678");
            Assert.assertNotNull(authUser);
        } catch (Throwable e) {
            throw e;
        } finally {
            this.userManager.removeUser(validUsername);
        }
    }

    @Test
    public void testUpdatePassword_1() throws Exception {
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

    @Test
    public void testUpdatePassword_2() throws Exception {
        String validUsername = "valid_ok.2";
        String validPassword = "valid.123_ok";
        String newValidPassword = "valid.1234_ok";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String mockJson = "{\"username\": \"" + validUsername + "\",\"status\": \"active\",\"password\": \"" + validPassword + "\"}";
            this.executeUserPost(mockJson, accessToken, status().isOk());

            String invalidBody1 = "{\"username\": \"" + validUsername + "\",\"oldPassword\": \"" + validPassword + "\",\"newPassword\": \"" + newValidPassword + "\"}";
            ResultActions resultInvalid1 = this.executeUpdatePassword(invalidBody1, "no_same_username", accessToken, status().isConflict());
            resultInvalid1.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid1.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid1.andExpect(jsonPath("$.errors[0].code", is("2")));
            resultInvalid1.andExpect(jsonPath("$.metaData.size()", is(0)));

            String noExistingUser = "test12345";
            String invalidBody2 = "{\"username\": \"" + noExistingUser + "\",\"oldPassword\": \"" + validPassword + "\",\"newPassword\": \"" + newValidPassword + "\"}";
            ResultActions resultInvalid2 = this.executeUpdatePassword(invalidBody2, noExistingUser, accessToken, status().isNotFound());
            resultInvalid2.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
            resultInvalid2.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
            resultInvalid2.andExpect(jsonPath("$.errors[0].code", is("1")));
            resultInvalid2.andExpect(jsonPath("$.metaData.size()", is(0)));

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

    @Test
    public void testUserPagination() throws Exception {
        String userPrefix = "test_pager_";
        for (int i = 0; i < 20; i++) {
            UserDetails user = this.createUser(userPrefix + i);
            this.userManager.addUser(user);
        }
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            ResultActions result = mockMvc
                    .perform(get("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(28)));
            result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.metaData.size()", is(8)));
            result.andExpect(jsonPath("$.metaData.page", is(1)));
            result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(28)));

            result = mockMvc
                    .perform(get("/users")
                            .param("pageSize", "10").param("page", "2")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(10)));
            result.andExpect(jsonPath("$.payload[0].username", is("test_pager_10")));
            result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.metaData.size()", is(8)));
            result.andExpect(jsonPath("$.metaData.page", is(2)));
            result.andExpect(jsonPath("$.metaData.pageSize", is(10)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(28)));

            result = mockMvc
                    .perform(get("/users")
                            .param("pageSize", "10").param("page", "2")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(10)));
            result.andExpect(jsonPath("$.payload[0].username", is("test_pager_10")));
            result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.metaData.size()", is(8)));
            result.andExpect(jsonPath("$.metaData.page", is(2)));
            result.andExpect(jsonPath("$.metaData.pageSize", is(10)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(3)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(28)));

            result = mockMvc
                    .perform(get("/users")
                            .param("pageSize", "5").param("page", "4")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload", Matchers.hasSize(5)));
            result.andExpect(jsonPath("$.payload[0].username", is("test_pager_15")));
            result.andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
            result.andExpect(jsonPath("$.metaData.size()", is(8)));
            result.andExpect(jsonPath("$.metaData.page", is(4)));
            result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(6)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(28)));

        } catch (Throwable e) {
            throw e;
        } finally {
            for (int i = 0; i < 20; i++) {
                this.userManager.removeUser(userPrefix + i);
            }
        }
    }

    private ResultActions executeUserPost(String body, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeUserPut(String body, String username, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(put("/users/{username}", new Object[]{username})
                        .content(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeUpdatePassword(String body, String username, String accessToken, ResultMatcher expected) throws Exception {
        ResultActions result = mockMvc
                .perform(post("/users/{username}/password", new Object[]{username})
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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

    private UserDetails createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setDisabled(false);
        user.setLastAccess(new Date());
        user.setLastPasswordChange(new Date());
        user.setMaxMonthsSinceLastAccess(2);
        user.setMaxMonthsSinceLastPasswordChange(1);
        user.setPassword("password");
        return user;
    }


    @Test
    public void getMyGroupPermissionsWithLoggedUserShouldReturnPermissions() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        this.mockMvcHelper = new MockMvcHelper(mockMvc, accessToken);
        this.mockMvcHelper.getMockMvc("/users/userProfiles/myGroupPermissions")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload", hasSize(1)))
                .andExpect(jsonPath("$.payload[0].group", is(Group.ADMINS_GROUP_NAME)))
                .andExpect(jsonPath("$.payload[0].permissions", hasSize(1)))
                .andExpect(jsonPath("$.payload[0].permissions[0]", is(Permission.SUPERUSER)));
    }


    @Test
    public void getMyGroupPermissionsWithInvalidAccessTokenShouldReturnUnauthorized() throws Exception {

        this.mockMvcHelper = new MockMvcHelper(mockMvc, "not_existing");
        this.mockMvcHelper.getMockMvc("/users/userProfiles/myGroupPermissions")
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getMyGroupPermissionsWithNOTLoggedUserShouldReturn401() throws Exception {

        this.mockMvcHelper = new MockMvcHelper(mockMvc);
        this.mockMvcHelper.getMockMvc("/users/userProfiles/myGroupPermissions")
                .andExpect(status().isUnauthorized());
    }
}
