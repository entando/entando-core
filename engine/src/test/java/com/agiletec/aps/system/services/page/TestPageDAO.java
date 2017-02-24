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

import java.util.List;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;

/**
 * @author M.Diana, E.Mezzano
 */
public class TestPageDAO extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testLoadPageList() throws Throwable {
		try {
			List<IPage> pages = _pageDao.loadPages();
			IPage home = PageTestUtil.getPageByCode(pages, "homepage");
			assertNotNull(home);
			assertTrue(home.isOnline());
			assertNotNull(home.getOnlineMetadata());
			assertNotNull(home.getDraftMetadata());
			
			IPage draft = PageTestUtil.getPageByCode(pages, "pagina_draft");
			assertNotNull(draft);
			assertFalse(draft.isOnline());
			assertNull(draft.getOnlineMetadata());
			assertNotNull(draft.getDraftMetadata());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testAddUpdateDeletePage() throws Throwable {
		String pageCode = "temp";
		Page newPageForTest = this.createPageForTest(pageCode);
		IPage extractedPage = null;
		try {
			extractedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
			assertNull(extractedPage);

			this._pageDao.addPage(newPageForTest);
			extractedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);

			PageTestUtil.comparePagesFull(newPageForTest, extractedPage, false);
			PageTestUtil.compareWidgets(newPageForTest.getDraftWidgets(), extractedPage.getOnlineWidgets());

			IPage pageToUpdate = this.updatePageForTest(extractedPage, this._pageDao);
			this._pageDao.updatePage(pageToUpdate);
			IPage updatedPage = PageTestUtil.getPageByCode( this._pageDao.loadPages(), "temp");

			PageTestUtil.comparePagesFull(pageToUpdate, updatedPage, true);
		} catch (Throwable t) {
			throw t;
		} finally {
			Page pageToDelete = (null != extractedPage) ? (Page) extractedPage : newPageForTest;
			PageTestUtil.deletePage(pageToDelete, _pageDao);
		}
	}

	public void testUpdateWidgetPosition() throws Throwable {
		String pageCode = "temp";
		Page newPageForTest = this.createPageForTest(pageCode);// W[1]
		IPage addedPage = null;
		try {
			this._pageDao.addPage(newPageForTest);
			addedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePagesFull(newPageForTest, addedPage, false);

			this._pageDao.updateWidgetPosition(pageCode, 2, 1);
			IPage updatedPage = PageTestUtil.getPageByCode( this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePages(addedPage, updatedPage, true);
			PageTestUtil.comparePageMetadata(addedPage.getDraftMetadata(), updatedPage.getDraftMetadata(), 0);
			PageTestUtil.comparePageMetadata(addedPage.getOnlineMetadata(), updatedPage.getOnlineMetadata(), 0);
			PageTestUtil.compareWidgets(addedPage.getOnlineWidgets(), updatedPage.getOnlineWidgets());

			Widget[] previousWidgets = addedPage.getDraftWidgets();
			Widget[] newWidgets = updatedPage.getDraftWidgets();
			assertEquals(previousWidgets.length, newWidgets.length);
			assertEquals(previousWidgets[0], newWidgets[0]);
			assertEquals(previousWidgets[1], newWidgets[2]);
			assertEquals(previousWidgets[2], newWidgets[1]);
			assertEquals(previousWidgets[3], newWidgets[3]);
		} catch (Throwable t) {
			throw t;
		} finally {
			PageTestUtil.deletePage(newPageForTest, _pageDao);
		}
	}

	public void testJoinPublishRemoveWidget() throws Throwable {
		String pageCode = "temp";
		Page newPageForTest = this.createPageForTest(pageCode);// W[1]
		IPage addedPage = null;
		try {
			this._pageDao.addPage(newPageForTest);
			addedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePagesFull(newPageForTest, addedPage, false);

			Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", null, this._widgetTypeManager);
			this._pageDao.joinWidget(addedPage, widgetToAdd, 3);
			IPage updatedPage = PageTestUtil.getPageByCode(
					this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePages(addedPage, updatedPage, true);
			PageTestUtil.comparePageMetadata(addedPage.getDraftMetadata(), updatedPage.getDraftMetadata(), 0);
			PageTestUtil.comparePageMetadata(addedPage.getOnlineMetadata(), updatedPage.getOnlineMetadata(), 0);
			PageTestUtil.compareWidgets(addedPage.getOnlineWidgets(), updatedPage.getOnlineWidgets());

			Widget[] previousWidgets = addedPage.getDraftWidgets();
			Widget[] newWidgets = updatedPage.getDraftWidgets();
			assertEquals(previousWidgets.length, newWidgets.length);
			assertEquals(previousWidgets[0], newWidgets[0]);
			assertEquals(previousWidgets[1], newWidgets[1]);
			assertEquals(previousWidgets[2], newWidgets[2]);
			assertNull(previousWidgets[3]);
			assertEquals(widgetToAdd, newWidgets[3]);

			this._pageDao.setPageOnline(pageCode);
			IPage publishedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePages(updatedPage, updatedPage, true);
			PageTestUtil.comparePageMetadata(updatedPage.getDraftMetadata(), publishedPage.getDraftMetadata(), null);
			PageTestUtil.comparePageMetadata(updatedPage.getOnlineMetadata(), publishedPage.getOnlineMetadata(), null);
			PageTestUtil.compareWidgets(updatedPage.getDraftWidgets(), publishedPage.getDraftWidgets());

			previousWidgets = updatedPage.getOnlineWidgets();
			newWidgets = publishedPage.getOnlineWidgets();
			assertEquals(previousWidgets.length, newWidgets.length);
			assertEquals(previousWidgets[0], newWidgets[0]);
			assertEquals(previousWidgets[1], newWidgets[1]);
			assertEquals(previousWidgets[2], newWidgets[2]);
			assertNull(previousWidgets[3]);
			assertEquals(widgetToAdd, newWidgets[3]);

			this._pageDao.removeWidget(updatedPage, 3);
			updatedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePages(publishedPage, updatedPage, true);
			PageTestUtil.comparePageMetadata(publishedPage.getDraftMetadata(), updatedPage.getDraftMetadata(), null);
			PageTestUtil.comparePageMetadata(publishedPage.getOnlineMetadata(), updatedPage.getOnlineMetadata(), null);
			PageTestUtil.compareWidgets(publishedPage.getOnlineWidgets(), updatedPage.getOnlineWidgets());

			previousWidgets = publishedPage.getDraftWidgets();
			newWidgets = updatedPage.getDraftWidgets();
			assertEquals(previousWidgets.length, newWidgets.length);
			assertEquals(previousWidgets[0], newWidgets[0]);
			assertEquals(previousWidgets[1], newWidgets[1]);
			assertEquals(previousWidgets[2], newWidgets[2]);
			assertNull(newWidgets[3]);
		} catch (Throwable t) {
			throw t;
		} finally {
			PageTestUtil.deletePage(newPageForTest, _pageDao);
		}
	}

	public void testMovePage() throws Throwable {
		String pageCode = "temp";
		Page newPageForTest = this.createPageForTest(pageCode);// W[1]
		IPage addedPage = null;
		try {
			this._pageDao.addPage(newPageForTest);
			addedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePagesFull(newPageForTest, addedPage, false);

			IPage parent = PageTestUtil.getPageByCode(
					this._pageDao.loadPages(), "homepage");
			assertNotNull(parent);
			assertNotSame(parent.getCode(), addedPage.getParentCode());

			this._pageDao.movePage(addedPage, parent);
			IPage movedPage = addedPage = PageTestUtil.getPageByCode(
					this._pageDao.loadPages(), pageCode);
			PageTestUtil.comparePages(addedPage, movedPage, false);
			PageTestUtil.comparePageMetadata(addedPage.getDraftMetadata(), movedPage.getDraftMetadata(), 0);
			PageTestUtil.comparePageMetadata(addedPage.getOnlineMetadata(), movedPage.getOnlineMetadata(), 0);
			PageTestUtil.compareWidgets(addedPage.getOnlineWidgets(), movedPage.getOnlineWidgets());
			PageTestUtil.compareWidgets(addedPage.getDraftWidgets(), movedPage.getDraftWidgets());
			assertEquals(parent.getCode(), movedPage.getParentCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			PageTestUtil.deletePage(newPageForTest, _pageDao);
		}
	}

	private Page createPageForTest(String code) {
		Page page = new Page();
		page.setCode(code);
		IPage parentPage = this._pageManager.getDraftPage("service");
		page.setParent(parentPage);
		page.setParentCode("service");
		page.setGroup("free");

		PageMetadata metadata = PageTestUtil.createPageMetadata("service", true, "pagina temporanea", 
				null, null, false, null, DateConverter.parseDate("19700101", "yyyyMMdd"));
		page.setOnlineMetadata(metadata);
		page.setDraftMetadata(metadata);

		Widget[] widgets = new Widget[4];
		ApsProperties config = PageTestUtil.createProperties("temp", "temp", "contentId", "ART1");
		widgets[1] = PageTestUtil.createWidget("content_viewer", config, this._widgetTypeManager);

		page.setOnlineWidgets(widgets);
		page.setDraftWidgets(widgets);
		return page;
	}

	private IPage updatePageForTest(IPage ipageToUpdate, PageDAO pageDAO)
			throws Throwable {
		Page pageToUpdate = (Page) ipageToUpdate;

		PageMetadata metadata = pageToUpdate.getDraftMetadata();
		metadata.setTitle("it", "pagina temporanea1");
		metadata.setTitle("en", "temporary page");
		metadata.setShowable(false);
		pageToUpdate.setOnlineMetadata(metadata);
		
		Widget[] modifiesWidgets = new Widget[4];
		ApsProperties config = PageTestUtil.createProperties("temp1", "temp1", "contentId", "ART11");
		modifiesWidgets[2] = PageTestUtil.createWidget("content_viewer", config, this._widgetTypeManager);
		// pageToUpdate.setOnlineWidgets(modifiesWidgets);
		pageToUpdate.setDraftWidgets(modifiesWidgets);
		return pageToUpdate;
	}
	
	private void init() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			this._pageDao = new PageDAO();
			this._pageDao.setDataSource(dataSource);
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
			IPageModelManager pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
			IWidgetTypeManager widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
			this._pageDao.setPageModelManager(pageModelManager);
			this._pageDao.setWidgetTypeManager(widgetTypeManager);
			this._widgetTypeManager = widgetTypeManager;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private PageDAO _pageDao;
	private IPageManager _pageManager;
	private IWidgetTypeManager _widgetTypeManager;

}