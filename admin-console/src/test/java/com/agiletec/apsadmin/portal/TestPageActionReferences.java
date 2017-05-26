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
package com.agiletec.apsadmin.portal;

import java.util.Collection;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author E.Santoboni
 */
public class TestPageActionReferences extends ApsAdminBaseTestCase {

	private static final String TEST_PAGE_CODE = "delete_me_001";
	private static final String CONTENT_ID = "ART1";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		Page testPage = createPage(TEST_PAGE_CODE);
		this._pageManager.addPage(testPage);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		Content content = this.contentManager.loadContent(CONTENT_ID, false);
		this.contentManager.insertOnLineContent(content);

		this._pageManager.deletePage(TEST_PAGE_CODE);
	}

	public void test1() throws Throwable {
		int frame = 5;
		String contentId = CONTENT_ID;
		String modelId = "1";
		IPage pagina_1 = this._pageManager.getDraftPage(TEST_PAGE_CODE);
		try {
			// set online content on draftPage
			assertNull(pagina_1.getWidgets()[frame]);
			String result = this.setContentViewer(TEST_PAGE_CODE, frame, contentId, modelId, "admin");
			assertEquals("configure", result);

			// set the content offLine
			Content content = this.contentManager.loadContent(CONTENT_ID, true);
			this.contentManager.removeOnLineContent(content);

			result = this.setPageOnline(TEST_PAGE_CODE, "admin");
			assertEquals("pageTree", result);
			PageAction action = (PageAction) this.getAction();
			assertTrue(action.hasErrors());
			Collection<String> errors = action.getActionErrors();
			assertEquals(1, errors.size());

		} catch (Throwable t) {
			throw t;
		}
	}

	private String setContentViewer(String pageCode, int frame, String contentId, String modelId, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jacms/Page/SpecialWidget/Viewer", "saveViewerConfig");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", String.valueOf(frame));
		this.addParameter("widgetTypeCode", "content_viewer");
		this.addParameter("contentId", contentId);
		this.addParameter("modelId", modelId);
		return this.executeAction();
	}

	private String setPageOnline(String pageCode, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/rs/Page", "setOnline");
		this.addParameter("pageCode", pageCode);

		return this.executeAction();
	}

	private Page createPage(String code) {
		Page page = new Page();
		Page parent = (Page) this._pageManager.getDraftRoot();
		page.setParentCode(parent.getCode());
		page.setParent(parent);
		page.setTitle("it", code);
		page.setTitle("en", code);
		page.setGroup(Group.FREE_GROUP_NAME);
		page.setModel(this.pageModelManager.getPageModel("internal"));
		page.setCode(code);
		return page;

	}

	private void init() throws Exception {
		try {
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
			this.pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
			this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
			this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);

		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IPageManager _pageManager = null;
	private IPageModelManager pageModelManager = null;
	private IContentManager contentManager;
	private IWidgetTypeManager _widgetTypeManager;

}
