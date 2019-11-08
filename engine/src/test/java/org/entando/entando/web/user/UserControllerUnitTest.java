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

import com.agiletec.aps.system.common.entity.model.attribute.*;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.*;
import com.agiletec.aps.system.services.role.*;
import com.agiletec.aps.system.services.user.*;
import de.mkammerer.argon2.Argon2Factory;
import org.entando.entando.aps.system.services.user.UserService;
import org.entando.entando.aps.system.services.user.model.*;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.entando.entando.aps.util.crypto.Argon2PasswordEncoder;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.user.model.*;
import org.entando.entando.web.user.validator.UserValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import org.entando.entando.aps.util.crypto.CryptoException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        userValidator.setPasswordEncoder(passwordEncoder);
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
    public void shouldAdd4CharsUsernamePost() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJson = "{\"username\": \"user\",\n"
                + "    \"password\": \"new_password\",\n"
                + "    \"status\": \"active\"\n"
                + "}";

        when(this.userService.addUser(any(UserRequest.class))).thenReturn(this.mockUser());
        ResultActions result = mockMvc.perform(
                post("/users")
                        .sessionAttr("user", user)
                        .content(mockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(jsonPath("$.errors", hasSize(0)));
    }

    @Test
    public void shouldValidateTooShortUsernamePost() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJson = "{\"username\": \"usr\",\n"
                + "    \"password\": \"new_password\",\n"
                + "    \"status\": \"active\"\n"
                + "}";

        when(this.userService.addUser(any(UserRequest.class))).thenReturn(this.mockUser());
        ResultActions result = mockMvc.perform(
                post("/users")
                        .sessionAttr("user", user)
                        .content(mockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(jsonPath("$.errors[0].code", is(UserValidator.ERRCODE_USERNAME_FORMAT_INVALID)));
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

    @Test
    public void shouldGetUsersWithProfileDetails() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(this.userService.getUsers(any(RestListRequest.class), any(String.class))).thenReturn(mockUsersWithProfile());
        ResultActions result = mockMvc.perform(
                get("/users")
                        .param("withProfile", "1")
                        //                        .param("filter[0].attribute", "username")
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
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
            password = passwordEncoder.encode("password");
        } catch (CryptoException ex) {
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
            password = passwordEncoder.encode("password");
        } catch (CryptoException ex) {
            password = "plain_password";
        }
        user1.setPassword(password);
        return user1;
    }

    private PagedMetadata<UserDto> mockUsersWithProfile() {
        List<UserDetails> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("admin");
        user1.setDisabled(false);
        user1.setLastAccess(new Date());
        user1.setLastPasswordChange(new Date());
        user1.setMaxMonthsSinceLastAccess(2);
        user1.setMaxMonthsSinceLastPasswordChange(1);
        UserProfile profile1 = new UserProfile();
        AttributeInterface attribute = new MonoTextAttribute();
        attribute.setName("name");
        attribute.setDescription("Name");
        profile1.addAttribute(attribute);
        user1.setProfile(profile1);
        User user2 = new User();
        user2.setUsername("user2");
        user2.setDisabled(false);
        user2.setLastAccess(new Date());
        user1.setLastPasswordChange(new Date());
        user2.setMaxMonthsSinceLastAccess(2);
        user2.setMaxMonthsSinceLastPasswordChange(1);
        user2.setProfile(profile1);
        User user3 = new User();
        user3.setUsername("user3");
        user3.setDisabled(false);
        user3.setLastAccess(new Date());
        user3.setLastPasswordChange(new Date());
        user3.setMaxMonthsSinceLastAccess(2);
        user3.setMaxMonthsSinceLastPasswordChange(1);
        user3.setProfile(profile1);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        List<UserDto> dtoList = new UserDtoBuilder().convert(users);
        SearcherDaoPaginatedResult<UserDetails> result = new SearcherDaoPaginatedResult<>(users.size(), users);
        PagedMetadata<UserDto> pagedMetadata = new PagedMetadata<>(new RestListRequest(), result);
        pagedMetadata.setBody(dtoList);

        return pagedMetadata;
    }

    @Test(expected = ValidationGenericException.class)
    public void deleteAdminReturnsError() throws ApsSystemException {
        new UserController().deleteUser("admin");
    }
}
