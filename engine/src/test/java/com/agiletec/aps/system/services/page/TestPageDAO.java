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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static final String TREE_ONLINE = "ONLINE";
	public static final String TREE_DRAFT = "DRAFT";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	protected Map<String, Map<String, IPage>> buildTrees(List<PageRecord> pageRecordList) {
		Map<String, IPage> fullMap = new HashMap<>();
		Map<String, IPage> onlineMap = new HashMap<>();
		for (int i = 0; i < pageRecordList.size(); i++) {
			PageRecord pageRecord = pageRecordList.get(i);
			IPage pageD = pageRecord.createDraftPage();
			fullMap.put(pageD.getCode(), pageD);
			if (pageD.isOnline()) {
				IPage pageO = pageRecord.createOnlinePage();
				onlineMap.put(pageO.getCode(), pageO);
			}
		}
		HashMap<String, Map<String, IPage>> result = new HashMap<>();
		result.put(TREE_DRAFT, fullMap);
		result.put(TREE_ONLINE, onlineMap);
		return result;
	}

	private void refreshTree(List<PageRecord> pageRecordList, Map<String, IPage> draftTree, Map<String, IPage> onlineTree) {
		pageRecordList = _pageDao.loadPageRecords();
		Map<String, Map<String, IPage>> trees = this.buildTrees(pageRecordList);
		draftTree = trees.get(TREE_DRAFT);
		onlineTree = trees.get(TREE_ONLINE);
	}

	public void testLoadPageList() throws Throwable {
		try {
			List<PageRecord> pageRecordList = _pageDao.loadPageRecords();
			Map<String, Map<String, IPage>> trees = this.buildTrees(pageRecordList);
			Map<String, IPage> draftTree = trees.get(TREE_DRAFT);
			Map<String, IPage> onlineTree = trees.get(TREE_ONLINE);

			IPage homeD = PageTestUtil.getPageByCode(draftTree, "homepage");
			assertNotNull(homeD);
			assertTrue(homeD.isOnline());
			assertNotNull(homeD.getMetadata());
			assertNotNull(homeD.getMetadata());

			IPage homeO = PageTestUtil.getPageByCode(onlineTree, "homepage");
			assertNotNull(homeO);
			assertTrue(homeO.isOnline());
			assertNotNull(homeO.getMetadata());

			IPage draftD = PageTestUtil.getPageByCode(draftTree, "pagina_draft");
			assertNotNull(draftD);
			assertFalse(draftD.isOnline());
			assertNotNull(draftD.getMetadata());

			IPage draftO = PageTestUtil.getPageByCode(onlineTree, "pagina_draft");
			assertNull(draftO);

		} catch (Throwable t) {
			throw t;
		}
	}

	public void testAddUpdateDeletePage() throws Throwable {
		String pageCode = "temp";
		Page newPageForTest = this.createPageForTest(pageCode);
		IPage extractedPage = null;
		try {

			// spostare
			List<PageRecord> pageRecordList = _pageDao.loadPageRecords();
			Map<String, Map<String, IPage>> trees = this.buildTrees(pageRecordList);
			Map<String, IPage> draftTree = trees.get(TREE_DRAFT);
			Map<String, IPage> onlineTree = trees.get(TREE_ONLINE);

			extractedPage = PageTestUtil.getPageByCode(draftTree, pageCode);
			assertNull(extractedPage);

			this._pageDao.addPage(newPageForTest);

			// spostare
			pageRecordList = _pageDao.loadPageRecords();
			trees = this.buildTrees(pageRecordList);
			draftTree = trees.get(TREE_DRAFT);
			onlineTree = trees.get(TREE_ONLINE);

			extractedPage = PageTestUtil.getPageByCode(draftTree, pageCode);

			PageTestUtil.comparePagesFull(newPageForTest, extractedPage, false);
			PageTestUtil.compareWidgets(newPageForTest.getWidgets(), extractedPage.getWidgets());

			IPage pageToUpdate = this.updatePageForTest(extractedPage, this._pageDao);
			this._pageDao.updatePage(pageToUpdate);

			// spostare
			pageRecordList = _pageDao.loadPageRecords();
			trees = this.buildTrees(pageRecordList);
			draftTree = trees.get(TREE_DRAFT);
			onlineTree = trees.get(TREE_ONLINE);

			IPage updatedPage = PageTestUtil.getPageByCode(draftTree, "temp");

			PageTestUtil.comparePagesFull(pageToUpdate, updatedPage, true);
		} catch (Throwable t) {
			throw t;
		} finally {
			Page pageToDelete = (null != extractedPage) ? (Page) extractedPage : newPageForTest;
			PageTestUtil.deletePage(pageToDelete, _pageDao);
		}
	}

	/*
	 * 
	 * public void testUpdateWidgetPosition() throws Throwable { String pageCode
	 * = "temp"; Page newPageForTest = this.createPageForTest(pageCode);// W[1]
	 * IPage addedPage = null; try { this._pageDao.addPage(newPageForTest);
	 * addedPage = PageTestUtil.getPageByCode(this._pageDao.loadPages(),
	 * pageCode); PageTestUtil.comparePagesFull(newPageForTest, addedPage,
	 * false);
	 * 
	 * this._pageDao.updateWidgetPosition(pageCode, 2, 1); IPage updatedPage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * PageTestUtil.comparePages(addedPage, updatedPage, true);
	 * PageTestUtil.comparePageMetadata(addedPage.getDraftMetadata(),
	 * updatedPage.getDraftMetadata(), 0);
	 * PageTestUtil.comparePageMetadata(addedPage.getOnlineMetadata(),
	 * updatedPage.getOnlineMetadata(), 0);
	 * PageTestUtil.compareWidgets(addedPage.getOnlineWidgets(),
	 * updatedPage.getOnlineWidgets());
	 * 
	 * Widget[] previousWidgets = addedPage.getDraftWidgets(); Widget[]
	 * newWidgets = updatedPage.getDraftWidgets();
	 * assertEquals(previousWidgets.length, newWidgets.length);
	 * assertEquals(previousWidgets[0], newWidgets[0]);
	 * assertEquals(previousWidgets[1], newWidgets[2]);
	 * assertEquals(previousWidgets[2], newWidgets[1]);
	 * assertEquals(previousWidgets[3], newWidgets[3]); } catch (Throwable t) {
	 * throw t; } finally { PageTestUtil.deletePage(newPageForTest, _pageDao); }
	 * }
	 * 
	 * public void testJoinPublishRemoveWidget() throws Throwable { String
	 * pageCode = "temp"; Page newPageForTest =
	 * this.createPageForTest(pageCode);// W[1] IPage addedPage = null; try {
	 * this._pageDao.addPage(newPageForTest); addedPage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * PageTestUtil.comparePagesFull(newPageForTest, addedPage, false);
	 * 
	 * Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", null,
	 * this._widgetTypeManager); this._pageDao.joinWidget(addedPage,
	 * widgetToAdd, 3); IPage updatedPage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * PageTestUtil.comparePages(addedPage, updatedPage, true);
	 * PageTestUtil.comparePageMetadata(addedPage.getDraftMetadata(),
	 * updatedPage.getDraftMetadata(), 0);
	 * PageTestUtil.comparePageMetadata(addedPage.getOnlineMetadata(),
	 * updatedPage.getOnlineMetadata(), 0);
	 * PageTestUtil.compareWidgets(addedPage.getOnlineWidgets(),
	 * updatedPage.getOnlineWidgets());
	 * 
	 * Widget[] previousWidgets = addedPage.getDraftWidgets(); Widget[]
	 * newWidgets = updatedPage.getDraftWidgets();
	 * assertEquals(previousWidgets.length, newWidgets.length);
	 * assertEquals(previousWidgets[0], newWidgets[0]);
	 * assertEquals(previousWidgets[1], newWidgets[1]);
	 * assertEquals(previousWidgets[2], newWidgets[2]);
	 * assertNull(previousWidgets[3]); assertEquals(widgetToAdd, newWidgets[3]);
	 * 
	 * this._pageDao.setPageOnline(pageCode); IPage onlinePage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * assertFalse(onlinePage.isChanged()); assertTrue(onlinePage.isOnline());
	 * PageTestUtil.comparePageMetadata(updatedPage.getDraftMetadata(),
	 * onlinePage.getDraftMetadata(), null);
	 * PageTestUtil.comparePageMetadata(updatedPage.getOnlineMetadata(),
	 * onlinePage.getOnlineMetadata(), null);
	 * PageTestUtil.compareWidgets(updatedPage.getDraftWidgets(),
	 * onlinePage.getDraftWidgets());
	 * 
	 * previousWidgets = updatedPage.getOnlineWidgets(); newWidgets =
	 * onlinePage.getOnlineWidgets(); assertEquals(previousWidgets.length,
	 * newWidgets.length); assertEquals(previousWidgets[0], newWidgets[0]);
	 * assertEquals(previousWidgets[1], newWidgets[1]);
	 * assertEquals(previousWidgets[2], newWidgets[2]);
	 * assertNull(previousWidgets[3]); assertEquals(widgetToAdd, newWidgets[3]);
	 * 
	 * this._pageDao.removeWidget(updatedPage, 3); updatedPage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * assertTrue(updatedPage.isChanged()); assertTrue(updatedPage.isOnline());
	 * PageTestUtil.comparePages(onlinePage, updatedPage, true);
	 * PageTestUtil.comparePageMetadata(onlinePage.getDraftMetadata(),
	 * updatedPage.getDraftMetadata(), null);
	 * PageTestUtil.comparePageMetadata(onlinePage.getOnlineMetadata(),
	 * updatedPage.getOnlineMetadata(), null);
	 * PageTestUtil.compareWidgets(onlinePage.getOnlineWidgets(),
	 * updatedPage.getOnlineWidgets());
	 * 
	 * previousWidgets = onlinePage.getDraftWidgets(); newWidgets =
	 * updatedPage.getDraftWidgets(); assertEquals(previousWidgets.length,
	 * newWidgets.length); assertEquals(previousWidgets[0], newWidgets[0]);
	 * assertEquals(previousWidgets[1], newWidgets[1]);
	 * assertEquals(previousWidgets[2], newWidgets[2]);
	 * assertNull(newWidgets[3]);
	 * 
	 * this._pageDao.setPageOffline(pageCode); IPage offlinePage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * assertFalse(offlinePage.isChanged());
	 * assertFalse(offlinePage.isOnline());
	 * assertNull(offlinePage.getOnlineMetadata());
	 * assertNull(offlinePage.getOnlineWidgets());
	 * PageTestUtil.comparePageMetadata(updatedPage.getDraftMetadata(),
	 * offlinePage.getDraftMetadata(), null);
	 * PageTestUtil.compareWidgets(updatedPage.getDraftWidgets(),
	 * offlinePage.getDraftWidgets()); } catch (Throwable t) { throw t; }
	 * finally { PageTestUtil.deletePage(newPageForTest, _pageDao); } }
	 * 
	 * public void testMovePage() throws Throwable { String pageCode = "temp";
	 * Page newPageForTest = this.createPageForTest(pageCode);// W[1] IPage
	 * addedPage = null; try { this._pageDao.addPage(newPageForTest); addedPage
	 * = PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * PageTestUtil.comparePagesFull(newPageForTest, addedPage, false);
	 * 
	 * IPage parent = this._pageManager.getPage("homepage");
	 * assertNotNull(parent); assertNotSame(parent.getCode(),
	 * addedPage.getParentCode());
	 * 
	 * this._pageDao.movePage(addedPage, parent); IPage movedPage = addedPage =
	 * PageTestUtil.getPageByCode(this._pageDao.loadPages(), pageCode);
	 * PageTestUtil.comparePages(addedPage, movedPage, false);
	 * PageTestUtil.comparePageMetadata(addedPage.getDraftMetadata(),
	 * movedPage.getDraftMetadata(), 0);
	 * PageTestUtil.comparePageMetadata(addedPage.getOnlineMetadata(),
	 * movedPage.getOnlineMetadata(), 0);
	 * PageTestUtil.compareWidgets(addedPage.getOnlineWidgets(),
	 * movedPage.getOnlineWidgets());
	 * PageTestUtil.compareWidgets(addedPage.getDraftWidgets(),
	 * movedPage.getDraftWidgets()); assertEquals(parent.getCode(),
	 * movedPage.getParentCode()); } catch (Throwable t) { throw t; } finally {
	 * PageTestUtil.deletePage(newPageForTest, _pageDao); } }
	 * 
	 * 
	 * 
	 */

	private IPage updatePageForTest(IPage ipageToUpdate, PageDAO pageDAO) throws Throwable {
		Page pageToUpdate = (Page) ipageToUpdate;

		PageMetadata metadata = pageToUpdate.getMetadata();
		metadata.setTitle("it", "pagina temporanea1");
		metadata.setTitle("en", "temporary page");
		metadata.setShowable(false);
		pageToUpdate.setMetadata(metadata);

		Widget[] modifiesWidgets = new Widget[4];
		ApsProperties config = PageTestUtil.createProperties("temp1", "temp1", "contentId", "ART11");
		modifiesWidgets[2] = PageTestUtil.createWidget("content_viewer", config, this._widgetTypeManager); //
		pageToUpdate.setWidgets(modifiesWidgets);
		return pageToUpdate;
	}

	private Page createPageForTest(String code) {
		Page page = new Page();
		page.setCode(code);
		IPage parentPage = this._pageManager.getDraftPage("service");
		page.setParent(parentPage);
		page.setParentCode(parentPage.getCode());
		page.setPosition(parentPage.getChildren().length + 1);
		page.setGroup("free");

		PageMetadata metadata = PageTestUtil.createPageMetadata("service", true, "pagina temporanea", null, null, false, null, DateConverter
				.parseDate("19700101", "yyyyMMdd"));

		page.setMetadata(metadata);

		Widget[] widgets = new Widget[4];
		ApsProperties config = PageTestUtil.createProperties("temp", "temp", "contentId", "ART1");
		widgets[1] = PageTestUtil.createWidget("content_viewer", config, this._widgetTypeManager);

		page.setWidgets(widgets);
		return page;
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
