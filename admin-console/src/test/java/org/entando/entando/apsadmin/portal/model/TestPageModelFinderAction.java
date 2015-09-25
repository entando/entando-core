/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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