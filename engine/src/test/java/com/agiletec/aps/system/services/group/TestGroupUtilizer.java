/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @author E.Santoboni
 */
public class TestGroupUtilizer extends BaseTestCase {
	
	public void testGetGroupsUtilizers_1() throws Throwable {
    	String[] names = this.getApplicationContext().getBeanNamesForType(GroupUtilizer.class);
    	assertTrue(names.length>=4);
    	for (int i=0; i<names.length; i++) {
			GroupUtilizer service = (GroupUtilizer) this.getApplicationContext().getBean(names[i]);
			List utilizers = service.getGroupUtilizers(Group.FREE_GROUP_NAME);
			if (names[i].equals(SystemConstants.AUTHORIZATION_SERVICE)) {
				assertEquals(0, utilizers.size());
			} else {
				assertTrue(utilizers.size()>0);
			}
		}
    }
	
	public void testGetGroupsUtilizers_2() throws Throwable {
    	String[] names = this.getApplicationContext().getBeanNamesForType(GroupUtilizer.class);
    	assertTrue(names.length>=4);
    	for (int i=0; i<names.length; i++) {
			GroupUtilizer service = (GroupUtilizer) this.getApplicationContext().getBean(names[i]);
			List utilizers = service.getGroupUtilizers("coach");
			if (names[i].equals(SystemConstants.USER_MANAGER)) {
				assertEquals(3, utilizers.size());
			} else if (names[i].equals(SystemConstants.PAGE_MANAGER)) {
				assertEquals(2, utilizers.size());
			} 
		}
    }
	
}
