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
package com.agiletec.plugins.jacms.apsadmin.user.group;

import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.user.group.GroupAction;

public class TestTrashReferencedGroup extends ApsAdminBaseTestCase {
	
	public void testFailureTrashReferencedGroup() throws Throwable {
		String result = this.executeTrash("admin", "customers");
		assertEquals("references", result);
		GroupAction groupAction = (GroupAction) this.getAction();
		Map<String, List<Object>> references = groupAction.getReferences();
		assertNotNull(references);
		assertEquals(4, references.size());
		List contents = references.get("jacmsContentManagerUtilizers");
		assertNotNull(contents);
		assertEquals(5, contents.size());
		List resources = references.get("jacmsResourceManagerUtilizers");
		assertNotNull(resources);
		assertEquals(1, resources.size());
	}
	
	private String executeTrash(String currentUser, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "trash");
		this.addParameter("name", groupName);
		return this.executeAction();
	}
	
}