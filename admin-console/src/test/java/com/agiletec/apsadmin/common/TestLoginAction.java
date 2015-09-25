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
package com.agiletec.apsadmin.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestLoginAction extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.initAction("/do", "doLogin");
	}
	
	public void testSuccessfulLogin1() throws Throwable {
		String result = this.executeLogin("admin", "admin");
		assertEquals(Action.SUCCESS, result);
    }
	
	public void testFailedLogin1() throws Throwable {
		String result = this.executeLogin("", "");
		assertEquals(Action.INPUT, result);
		
		Map<String, List<String>> fieldsError = this.getAction().getFieldErrors();
		Collection<String> actionError = this.getAction().getActionErrors();
		assertEquals(2, fieldsError.size());
		assertEquals(0, actionError.size());
    }
	
	public void testFailedLogin2() throws Throwable {
		String result = this.executeLogin("pippo", "");
		assertEquals(Action.INPUT, result);
		
		Map<String, List<String>> fieldsError = this.getAction().getFieldErrors();
		Collection<String> actionError = this.getAction().getActionErrors();
		assertEquals(1, fieldsError.size());
		assertEquals(0, actionError.size());
    }
	
	public void testFailedLogin3() throws Throwable {
		String result = this.executeLogin("admin", "wrongPassword");
		assertEquals(Action.INPUT, result);
		
		Map<String, List<String>> fieldsError = this.getAction().getFieldErrors();
		Collection<String> actionError = this.getAction().getActionErrors();
		assertEquals(0, fieldsError.size());
		assertEquals(1, actionError.size());
    }
	
	public void testFailedLogin4() throws Throwable {
		String result = this.executeLogin("guest", "guest");
		assertEquals(Action.INPUT, result);
		
		Map<String, List<String>> fieldsError = this.getAction().getFieldErrors();
		Collection<String> actionError = this.getAction().getActionErrors();
		assertEquals(0, fieldsError.size());
		assertEquals(1, actionError.size());
    }
	
	private String executeLogin(String username, String password) throws Throwable {
		this.addParameter("username", username);
		this.addParameter("password", password);
		String result = super.executeAction();
		return result;
	}
	
}
