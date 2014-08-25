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
package com.agiletec.apsadmin.user.role;

import java.util.List;

import com.agiletec.aps.system.services.role.Role;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Mezzano
 */
public class TestRoleFinderAction extends ApsAdminBaseTestCase {
	
	public void testListWithUserNotAllowed() throws Throwable {
		String result = this.executeList("developersConf");
		assertEquals("apslogin", result);
	}
	
	public void testList() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		IRoleFinderAction roleFinderAction = (IRoleFinderAction) this.getAction();
		List<Role> roles = roleFinderAction.getRoles();
		assertFalse(roles.isEmpty());
	}
	
	private String executeList(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Role", "list");
		return this.executeAction();
	}
	
}