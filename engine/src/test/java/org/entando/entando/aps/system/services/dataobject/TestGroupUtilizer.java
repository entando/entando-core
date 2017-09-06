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
package org.entando.entando.aps.system.services.dataobject;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

public class TestGroupUtilizer extends BaseTestCase {

	public void testGetGroupsUtilizers() throws Throwable {
		String[] names = this.getApplicationContext().getBeanNamesForType(GroupUtilizer.class);
		assertTrue(names.length >= 4);
		for (int i = 0; i < names.length; i++) {
			GroupUtilizer service = (GroupUtilizer) this.getApplicationContext().getBean(names[i]);
			List utilizers = service.getGroupUtilizers("coach");
			if (names[i].equals(JacmsSystemConstants.CONTENT_MANAGER)) {
				assertEquals(6, utilizers.size());
			} else if (names[i].equals(JacmsSystemConstants.RESOURCE_MANAGER)) {
				assertEquals(0, utilizers.size());
			}
		}
	}
}
