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
package com.agiletec.plugins.jacms.apsadmin.portal;

import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.portal.PageAction;

public class TestTrashReferencedPage extends ApsAdminBaseTestCase {
	
	public void testTrashReferencedPage() throws Throwable {
		String result = this.executeTrashPage("pagina_11", "admin");
		assertEquals("references", result);
		PageAction action = (PageAction) this.getAction();
		Map utilizers = action.getReferences();
		assertEquals(1, utilizers.size());
		List contentUtilizers = (List) utilizers.get("jacmsContentManagerUtilizers");
		assertEquals(2, contentUtilizers.size());
	}
	
	private String executeTrashPage(String selectedPageCode, String userName) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page", "trash");
		this.addParameter("selectedNode", selectedPageCode);
		String result = this.executeAction();
		return result;
	}
	
}
