/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

/**
 * @version 1.0
 * @author M.Diana
 */
public class TestUserDAO extends BaseTestCase {
	
    protected void setUp() throws Exception {
		super.setUp();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		UserDAO userDao = new UserDAO();
		userDao.setDataSource(dataSource);
		this._userDao = userDao;
	}
    
	public void testAddDeleteUser() throws Throwable {
		String username = "UserForTest1";
		User user = this.createUserForTest(username);
		try {
            _userDao.deleteUser(user);
            assertNull(_userDao.loadUser(username));
            
            _userDao.addUser(user);
            UserDetails extractedUser = _userDao.loadUser(username);
            assertEquals(user.getUsername(), extractedUser.getUsername());
            assertEquals(user.getPassword(), extractedUser.getPassword());
        } catch (Throwable t) {
        	throw t;
        } finally {
            _userDao.deleteUser(user);
            assertNull(_userDao.loadUser(username));
        }
	}
	
	public void testUpdateUser() throws Throwable {
		String username = "UserForTest2";
		User user = this.createUserForTest(username);
		try {
            _userDao.addUser(user);
            
            user.setPassword("newPassword");
            _userDao.updateUser(user);
            
            UserDetails extractedUser = _userDao.loadUser(username);
            assertEquals(user.getUsername(), extractedUser.getUsername());
            assertEquals(user.getPassword(), "newPassword");
        } catch (Throwable t) {
        	throw t;
        } finally {
            _userDao.deleteUser(user);
            assertNull(_userDao.loadUser(username));
        }
	}
	
	private User createUserForTest(String username) {
		User user = new User();
		user.setUsername(username);
        user.setPassword("temp");
        return user;
	}
	
	private IUserDAO _userDao;
	
}
