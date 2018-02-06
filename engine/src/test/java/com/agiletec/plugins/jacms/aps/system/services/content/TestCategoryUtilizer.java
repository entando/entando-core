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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * Test del servizio gestore categorie.
 *
 * @author E.Santoboni
 */
public class TestCategoryUtilizer extends BaseTestCase {

	public void testGetCategoryUtilizers_1() throws Throwable {
		String[] names = this.getApplicationContext().getBeanNamesForType(CategoryUtilizer.class);
		assertTrue(names.length >= 2);
		for (int i = 0; i < names.length; i++) {
			CategoryUtilizer service = (CategoryUtilizer) this.getApplicationContext().getBean(names[i]);
			List utilizers = service.getCategoryUtilizers("evento");
			if (names[i].equals(JacmsSystemConstants.CONTENT_MANAGER)) {
				assertTrue(utilizers.size() == 2);
			}
		}
	}

	public void testGetCategoryUtilizers_2() throws Throwable {
		String[] names = this.getApplicationContext().getBeanNamesForType(CategoryUtilizer.class);
		assertTrue(names.length >= 2);
		for (int i = 0; i < names.length; i++) {
			CategoryUtilizer service = (CategoryUtilizer) this.getApplicationContext().getBean(names[i]);
			List utilizers = service.getCategoryUtilizers("resCat1");
			if (names[i].equals(JacmsSystemConstants.RESOURCE_MANAGER)) {
				assertTrue(utilizers.size() == 1);
			} else {
				assertTrue(utilizers.size() == 0);
			}
		}
	}

}
