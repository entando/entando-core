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
package org.entando.entando.aps.system.services.user;

import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.mockhelper.AuthorizationMockHelper;
import org.entando.entando.aps.system.services.mockhelper.UserMockHelper;
import org.entando.entando.aps.system.services.role.IRoleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 *
 * @author paddeo
 */
public class UserServiceTest {

    @Mock
    private IRoleService roleService;
    @Mock
    private IUserManager userManager;
    @Mock
    private IAuthorizationManager authorizationManager;

    @InjectMocks
    private IUserService userService;

    private UserDetails userDetails = UserMockHelper.mockUser();
    private List<Authorization> authorizationList = AuthorizationMockHelper.mockAuthorizationList(3);

    @Before
    protected void setUp() throws Exception {
        when(userManager.getUser(anyString())).thenReturn(userDetails);
    }


    @Test
    public void getCurrentUserPermissionsTest() throws Throwable {
        when(authorizationManager.getUserAuthorizations(anyString())).thenReturn(authorizationList);

//        authorizationList.stream()
//                .forEach(authorization -> when(roleService.getRole(authorization.getRole().getName())).thenReturn());
    }
}
