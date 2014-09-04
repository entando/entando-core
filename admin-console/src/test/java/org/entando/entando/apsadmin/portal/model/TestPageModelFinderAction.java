/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.apsadmin.portal.model;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.opensymphony.xwork2.Action;

import java.util.List;

/**
 * @author E.Santoboni
 */
public class TestPageModelFinderAction extends AbstractTestPageModelAction {
	
	public void testGetPageModels_1() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		PageModelFinderAction pageModelFinderAction = (PageModelFinderAction) this.getAction();
		List<PageModel> models = pageModelFinderAction.getPageModels();
		assertEquals(3, models.size());
		assertEquals("internal", models.get(0).getCode());
		assertEquals("home", models.get(1).getCode());
		assertEquals("service", models.get(2).getCode());
	}
	
	public void testGetPageModels_2() throws Throwable {
		String testPageModelCode = "test_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			PageModel mockModel = this.createMockPageModel(testPageModelCode);
			this._pageModelManager.addPageModel(mockModel);
			
			String result = this.executeList("admin");
			assertEquals(Action.SUCCESS, result);
			PageModelFinderAction pageModelFinderAction = (PageModelFinderAction) this.getAction();
			List<PageModel> models = pageModelFinderAction.getPageModels();
			assertEquals(4, models.size());
			assertEquals(testPageModelCode, models.get(0).getCode());
		} catch (Exception e) {
			throw e;
		} finally {
			this._pageModelManager.deletePageModel(testPageModelCode);
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		}
	}
	
	private String executeList(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/PageModel", "list");
		return this.executeAction();
	}
	
}