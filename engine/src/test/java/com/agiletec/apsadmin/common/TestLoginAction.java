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
