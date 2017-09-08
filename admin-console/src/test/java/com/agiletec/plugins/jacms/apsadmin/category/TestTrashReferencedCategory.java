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
package com.agiletec.plugins.jacms.apsadmin.category;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.category.CategoryAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * @author E.Santoboni
 */
public class TestTrashReferencedCategory extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testTrashReferencedCategory() throws Throwable {
		String categoryCode = "evento";
		Category masterCategory = this._categoryManager.getCategory(categoryCode);
		assertNotNull(masterCategory);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/Category", "trash");
			this.addParameter("selectedNode", categoryCode);
			String result = this.executeAction();
			assertEquals("references", result);

			CategoryAction action = (CategoryAction) this.getAction();
			Map<String, List> references = action.getReferences();
			assertEquals(2, references.size());

			List contentReferences = references.get(JacmsSystemConstants.CONTENT_MANAGER + "Utilizers");
			assertEquals(2, contentReferences.size());
			for (int i = 0; i < contentReferences.size(); i++) {
				String contentId = (String) contentReferences.get(i);
				assertTrue(contentId.equals("EVN193") || contentId.equals("EVN192"));
			}
		} catch (Throwable t) {
			throw t;
		}
	}

	private void init() throws Exception {
		this._categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
	}

	private ICategoryManager _categoryManager;

}
