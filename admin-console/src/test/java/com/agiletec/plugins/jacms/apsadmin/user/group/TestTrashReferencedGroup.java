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
		assertEquals(5, references.size());
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
