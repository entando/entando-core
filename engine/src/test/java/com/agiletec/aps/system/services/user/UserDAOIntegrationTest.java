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

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDAOIntegrationTest extends BaseTestCase {

    private IUserDAO userDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
        PasswordEncoder passwordEncoder = (PasswordEncoder) this.getApplicationContext().getBean("compatiblePasswordEncoder");
        UserDAO dao = new UserDAO();
        dao.setDataSource(dataSource);
        dao.setPasswordEncoder(passwordEncoder);
        this.userDao = dao;
    }

    public void testAddDeleteUser() throws Throwable {
        String username = "UserForTest1";
        User user = this.createUserForTest(username);
        try {
            userDao.deleteUser(user);
            assertNull(userDao.loadUser(username));

            userDao.addUser(user);
            UserDetails extractedUser = userDao.loadUser(username);
            assertEquals(user.getUsername(), extractedUser.getUsername());
            assertThat(extractedUser.getPassword()).startsWith("{bcrypt}");
        } catch (Throwable t) {
            throw t;
        } finally {
            userDao.deleteUser(user);
            assertNull(userDao.loadUser(username));
        }
    }

    public void testUpdateUser() throws Throwable {
        String username = "UserForTest2";
        User user = this.createUserForTest(username);
        try {
            userDao.addUser(user);

            user.setPassword("newPassword");
            userDao.updateUser(user);

            UserDetails extractedUser = userDao.loadUser(username);
            assertEquals(user.getUsername(), extractedUser.getUsername());
            assertEquals(user.getPassword(), "newPassword");
        } catch (Throwable t) {
            throw t;
        } finally {
            userDao.deleteUser(user);
            assertNull(userDao.loadUser(username));
        }
    }

    private User createUserForTest(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("temp");
        return user;
    }
}
