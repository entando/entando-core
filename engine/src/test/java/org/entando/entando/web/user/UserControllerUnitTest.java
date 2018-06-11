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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.IApsEncrypter;
import de.mkammerer.argon2.Argon2Factory;
import org.entando.entando.aps.system.services.user.UserService;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.aps.system.services.user.model.UserDtoBuilder;
import org.entando.entando.aps.util.argon2.Argon2Encrypter;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.validator.UserValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author paddeo
 */
public class UserControllerUnitTest extends AbstractControllerTest {

    @Mock
    IUserManager userManager;

    @Mock
    IGroupManager groupManager;

    @Mock
    IRoleManager roleManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
        UserValidator userValidator = new UserValidator();
        userValidator.setUserManager(userManager);
        userValidator.setGroupManager(groupManager);
        userValidator.setRoleManager(roleManager);
        userValidator.setEncrypter(this.getEncrypter());
        this.controller.setUserValidator(userValidator);
    }

    @Test
    public void shouldGetUsersList() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(this.userService.getUsers(any(RestListRequest.class), any(String.class))).thenReturn(mockUsers());
        ResultActions result = mockMvc.perform(
                get("/users")
                        .param("withProfile", "0")
                        .param("sort", "username")
                        .param("filter[0].attribute", "username")
                        .param("filter[0].operator", "like")
                        .param("filter[0].value", "user")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println("result: " + result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());

    }

    @Test
    public void shouldGetUserDetails() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(this.userService.getUser(any(String.class))).thenReturn(mockUser());
        ResultActions result = mockMvc.perform(
                get("/users/{username}", "user")
                        .param("filter[0].attribute", "username")
                        .param("filter[0].operator", "like")
                        .param("filter[0].value", "user")
                        //.sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

    }

    @Test
    public void shouldExecuteUserPut() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String mockJson = "{\n"
                + "    \"username\": \"username_test\",\n"
                + "    \"status\": \"inactive\",\n"
                + "    \"password\": \"different_password\"\n"
                + " }";
        when(this.userManager.getUser("username_test")).thenReturn(this.mockUserDetails("username_test"));
        ResultActions result = mockMvc.perform(
                put("/users/{username}", "username_test")
                        //.sessionAttr("user", user)
                        .content(mockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("users: " + response);
        result.andExpect(status().isOk());
    }

    @Test
    public void shouldValidateUserPut() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJson = "{\n"
                + "    \"username\": \"username_test\",\n"
                + "    \"status\": \"inactive\",\n"
                + "    \"password\": \"invalid spaces\"\n"
                + " }";

        when(this.userManager.getUser(any(String.class))).thenReturn(this.mockUserDetails("username_test"));
        ResultActions result = mockMvc.perform(
                put("/users/{target}", "mismach")
                        .sessionAttr("user", user)
                        .content(mockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("users: " + response);
        result.andExpect(status().isConflict());
    }

    @Test
    public void shouldValidatePasswordsUserPasswordPost() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJson = "{\"username\": \"username_test\",\n"
                + "    \"oldPassword\": \"same_password\",\n"
                + "    \"newPassword\": \"same_password\"\n"
                + "}";

        when(this.userManager.getUser(any(String.class))).thenReturn(this.mockUserDetails("username_test"));
        ResultActions result = mockMvc.perform(
                post("/users/{username}/password", "username_test")
                        .sessionAttr("user", user)
                        .content(mockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(jsonPath("$.errors[0].code", is(UserValidator.ERRCODE_NEW_PASSWORD_EQUALS)));
    }

    @Test
    public void shouldAddUserAuthorities() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJson = "[{\"group\":\"group1\", \"role\":\"role1\"},{\"group\":\"group2\", \"role\":\"role2\"}]";
        List<UserAuthorityDto> authorities = (List<UserAuthorityDto>) this.createMetadata(mockJson, List.class);

        when(this.controller.getUserValidator().getGroupManager().getGroup(any(String.class))).thenReturn(mockedGroup());
        when(this.controller.getUserValidator().getRoleManager().getRole(any(String.class))).thenReturn(mockedRole());
        when(this.controller.getUserService().addUserAuthorities(any(String.class), any(UserAuthoritiesRequest.class))).thenReturn(authorities);
        ResultActions result = mockMvc.perform(
                put("/users/{target}/authorities", "mockuser")
                        .sessionAttr("user", user)
                        .content(mockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());
    }

    private Group mockedGroup() {
        Group group = new Group();
        group.setDescription("descr1");
        group.setName("group1");
        return group;
    }

    private Role mockedRole() {
        Role role = new Role();
        role.setDescription("descr1");
        role.setName("role1");
        return role;
    }

    private PagedMetadata<UserDto> mockUsers() {
        List<UserDetails> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("admin");
        user1.setDisabled(false);
        user1.setLastAccess(new Date());
        user1.setLastPasswordChange(new Date());
        user1.setMaxMonthsSinceLastAccess(2);
        user1.setMaxMonthsSinceLastPasswordChange(1);
        User user2 = new User();
        user2.setUsername("user2");
        user2.setDisabled(false);
        user2.setLastAccess(new Date());
        user1.setLastPasswordChange(new Date());
        user2.setMaxMonthsSinceLastAccess(2);
        user2.setMaxMonthsSinceLastPasswordChange(1);
        User user3 = new User();
        user3.setUsername("user3");
        user3.setDisabled(false);
        user3.setLastAccess(new Date());
        user3.setLastPasswordChange(new Date());
        user3.setMaxMonthsSinceLastAccess(2);
        user3.setMaxMonthsSinceLastPasswordChange(1);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        List<UserDto> dtoList = new UserDtoBuilder().convert(users);
        SearcherDaoPaginatedResult<UserDetails> result = new SearcherDaoPaginatedResult<>(users.size(), users);
        PagedMetadata<UserDto> pagedMetadata = new PagedMetadata<>(new RestListRequest(), result);
        pagedMetadata.setBody(dtoList);

        return pagedMetadata;
    }

    private UserDto mockUser() {
        User user1 = new User();
        user1.setUsername("user");
        user1.setDisabled(false);
        user1.setLastAccess(new Date());
        user1.setLastPasswordChange(new Date());
        user1.setMaxMonthsSinceLastAccess(2);
        user1.setMaxMonthsSinceLastPasswordChange(1);
        String password;
        try {
            password = this.getEncrypter().encrypt("password");
        } catch (ApsSystemException ex) {
            password = "plain_password";
        }
        user1.setPassword(password);
        return new UserDto(user1);
    }

    private UserDetails mockUserDetails(String username) {
        User user1 = new User();
        user1.setUsername(username);
        user1.setDisabled(false);
        user1.setLastAccess(new Date());
        user1.setLastPasswordChange(new Date());
        user1.setMaxMonthsSinceLastAccess(2);
        user1.setMaxMonthsSinceLastPasswordChange(1);
        String password;
        try {
            password = this.getEncrypter().encrypt("password");
        } catch (ApsSystemException ex) {
            password = "plain_password";
        }
        user1.setPassword(password);
        return user1;
    }

    private IApsEncrypter getEncrypter() {
        Argon2Encrypter encrypter = new Argon2Encrypter();
        encrypter.setType(Argon2Factory.Argon2Types.ARGON2i);
        encrypter.setHashLen(32);
        encrypter.setSaltLen(16);
        encrypter.setIterations(4);
        encrypter.setMemory(65536);
        encrypter.setParallelism(4);
        return encrypter;
    }
}
