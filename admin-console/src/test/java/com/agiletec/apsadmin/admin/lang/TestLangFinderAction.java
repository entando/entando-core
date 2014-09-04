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
package com.agiletec.apsadmin.admin.lang;

import java.util.List;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Mezzano
 */
public class TestLangFinderAction extends ApsAdminBaseTestCase {
	
	public void testList() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		LangFinderAction langFinderAction = (LangFinderAction) this.getAction();
		
		List<Lang> langs = langFinderAction.getLangs();
		assertEquals(langs.size(), 2);
		List<Lang> assignableLangs = langFinderAction.getAssignableLangs();
		assertFalse(assignableLangs.isEmpty());
	}
	
	private String executeList(String userName) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Lang", "list");
		String result = this.executeAction();
		return result;
	}
	
}