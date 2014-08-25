/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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