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
package com.agiletec.aps.system.services.page;

import java.util.Arrays;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @author M.Diana, E.Mezzano
 */
public class TestPageManagerNew extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetDraftPage_should_load_draftPages() {
		String onlyDraftPageCode = "pagina_draft";
		IPage page = this._pageManager.getDraftPage(onlyDraftPageCode);
		assertNotNull(page);
		assertFalse(page.isOnline());
	}

	public void testGetDraftPage_should_load_onlinePages() {
		String onlinePageCode = "pagina_1";
		IPage page = this._pageManager.getDraftPage(onlinePageCode);
		assertNotNull(page);
		assertTrue(page.isOnline());
	}

	public void testGetOnlinePage_should_ignore_draftPages() {
		String onlyDraftPageCode = "pagina_draft";
		IPage page = this._pageManager.getOnlinePage(onlyDraftPageCode);
		assertNull(page);
	}

	public void testGetOnlinePage_should_load_onlinePages() {
		String onlinePageCode = "pagina_1";
		IPage page = this._pageManager.getOnlinePage(onlinePageCode);
		assertNotNull(page);
		assertTrue(page.isOnline());
		List<IPage> childs = Arrays.asList(page.getChildren());
		assertEquals(2, childs.size());
	}

	public void testGetOnlinePage_should_ignore_draftPageChildren() {
		String onlyDraftPageCode = "pagina_draft";
		String onlinePageCode = "homepage";
		IPage page = this._pageManager.getOnlinePage(onlinePageCode);
		assertNotNull(page);
		assertTrue(page.isOnline());
		List<IPage> childs = Arrays.asList(page.getChildren());
		for (IPage child : childs) {
			String code = child.getCode();
			assertFalse(code.equalsIgnoreCase(onlyDraftPageCode));
			assertTrue(child.isOnlineInstance());
		}
	}

	public void testGetDraftPage_should_load_draftPageChildren() {
		String onlyDraftPageCode = "pagina_draft";
		String onlinePageCode = "homepage";
		IPage page = this._pageManager.getDraftPage(onlinePageCode);
		assertNotNull(page);
		assertTrue(page.isOnline());
		List<IPage> childs = Arrays.asList(page.getChildren());
		boolean found = false;
		for (IPage child : childs) {
			String code = child.getCode();
			found = code.equals(onlyDraftPageCode);
		}
		assertTrue(found);
	}

	private void init() throws Exception {
		try {
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
			this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IPageManager _pageManager = null;
	private IWidgetTypeManager _widgetTypeManager;

}
