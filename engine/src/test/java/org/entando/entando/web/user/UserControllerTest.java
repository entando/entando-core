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
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.List;
import org.entando.entando.aps.system.services.user.UserService;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.validator.UserValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author paddeo
 */
public class UserControllerTest extends AbstractControllerTest {

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
        this.controller.setUserValidator(userValidator);
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
}
