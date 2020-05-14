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
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserGroupPermissions;
import org.entando.entando.aps.system.services.assertionhelper.UserGroupPermissionAssertionHelper;
import org.entando.entando.aps.system.services.mockhelper.AuthorizationMockHelper;
import org.entando.entando.aps.system.services.mockhelper.UserMockHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author paddeo
 */
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    private UserDetails userDetails = UserMockHelper.mockUser();
    private List<Authorization> authorizationList = AuthorizationMockHelper.mockAuthorizationList(3);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getMyGroupPermissionsTest() {

        userDetails.addAuthorizations(authorizationList);

        List<UserGroupPermissions> expectedList = authorizationList.stream()
                .map(authorization -> new UserGroupPermissions(authorization.getGroup().getName(), authorization.getRole().getPermissions()))
                .collect(Collectors.toList());

        List<UserGroupPermissions> actualList = userService.getMyGroupPermissions(userDetails);
        UserGroupPermissionAssertionHelper.assertUserGroupPermissions(expectedList, actualList);
    }


    @Test
    public void getMyGroupPermissionsWithNoAuthorizationsTest() {

        List<UserGroupPermissions> actualList = userService.getMyGroupPermissions(userDetails);
        assertEquals(0, actualList.size());
    }

    @Test
    public void getMyGroupPermissionsWithNullUserShouldReturnEmptyListTest() {

        List<UserGroupPermissions> actualList = userService.getMyGroupPermissions(null);
        assertEquals(0, actualList.size());
    }
}
