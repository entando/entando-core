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
package com.agiletec.aps.system.services.user;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import org.entando.entando.aps.util.crypto.CompatiblePasswordEncoder;

public class UserManagerIntegrationTest extends BaseTestCase {

    private IUserManager userManager = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testGetUsers() throws Throwable {
        List<UserDetails> users = this.userManager.getUsers();
        assertTrue(users.size() >= 8);
    }

    public void testAdminUserPasswordIsBCrypt() throws Throwable {
        UserDetails admin = this.getUser("admin");
        assertNotNull(admin);
        assertTrue(CompatiblePasswordEncoder.isBCrypt(admin.getPassword()));
    }

    public void testAllUsersPasswordsIsArgon2() throws Throwable {
        for (UserDetails user : userManager.getUsers()) {
            assertTrue(CompatiblePasswordEncoder.isBCrypt(user.getPassword()));
        }
    }

    private void init() throws Exception {
        try {
            this.userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
}
