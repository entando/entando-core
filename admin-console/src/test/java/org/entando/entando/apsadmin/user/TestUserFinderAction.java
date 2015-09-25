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
package org.entando.entando.apsadmin.user;

import java.util.List;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestUserFinderAction extends ApsAdminBaseTestCase {
	
	public void testListWithUserNotAllowed() throws Throwable {
		String result = this.executeList("developersConf");
		assertEquals("apslogin", result);
	}
	
	public void testList() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		UserProfileFinderAction userFinderAction = (UserProfileFinderAction) this.getAction();
		List<String> users = userFinderAction.getSearchResult();
		assertFalse(users.isEmpty());
		assertTrue(users.size()>=8);
	}
	
    public void testSearchUsers() throws Throwable {
    	String result = this.executeSearch("admin", "ustomer");
    	assertEquals(Action.SUCCESS, result);
    	UserProfileFinderAction userFinderAction = (UserProfileFinderAction) this.getAction();
		List<String> users = userFinderAction.getSearchResult();
		assertEquals(3, users.size());
		
		result = this.executeSearch("admin", "anager");
    	assertEquals(Action.SUCCESS, result);
    	userFinderAction = (UserProfileFinderAction) this.getAction();
		users = userFinderAction.getSearchResult();
		assertEquals(2, users.size());
		
		result = this.executeSearch("admin", "");
		assertEquals(Action.SUCCESS, result);
    	userFinderAction = (UserProfileFinderAction) this.getAction();
		users = userFinderAction.getSearchResult();
		assertTrue(users.size()>=8);
		
		result = this.executeSearch("admin", null);
		assertEquals(Action.SUCCESS, result);
    	userFinderAction = (UserProfileFinderAction) this.getAction();
		users = userFinderAction.getSearchResult();
		assertTrue(users.size()>=8);
    }
	
	private String executeList(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "list");
		return this.executeAction();
	}
	
	private String executeSearch(String currentUser, String text) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "search");
		if (null != text) this.addParameter("username", text);
		return this.executeAction();
	}
	
}