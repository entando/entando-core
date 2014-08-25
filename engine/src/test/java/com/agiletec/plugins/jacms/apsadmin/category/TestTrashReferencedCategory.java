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
			assertEquals(1, references.size());
			
			List contentReferences = references.get(JacmsSystemConstants.CONTENT_MANAGER+"Utilizers");
			assertEquals(2, contentReferences.size());
			for (int i=0; i<contentReferences.size(); i++) {
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
