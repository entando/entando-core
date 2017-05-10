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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.services.mock.MockWidgetsDAO;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author M.Diana, E.Mezzano
 */
public class TestPageManager extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetPage() throws Throwable {
		IPage root = _pageManager.getRoot();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertTrue(root.isOnline());
		assertNotNull(root.getOnlineMetadata());
		assertNotNull(root.getDraftMetadata());
		assertTrue(root.getChildren().length > 2);
		assertEquals(root.getOnlineChildren().length, root.getChildren().length);
		assertEquals(root.getOnlineChildren().length + 1, root.getAllChildren().length);
		
		assertNotNull(_pageManager.getOnlinePage("homepage"));
		assertNotNull(_pageManager.getDraftPage("homepage"));
		
		IPage page1 = _pageManager.getOnlinePage("pagina_1");
		assertNotNull(page1);
		assertTrue(page1.isOnline());
		assertNotNull(page1.getOnlineMetadata());
		assertNotNull(page1.getDraftMetadata());
		assertTrue(page1.getChildren().length > 1);
		assertEquals(page1.getOnlineChildren().length, page1.getChildren().length);
		assertEquals(page1.getOnlineChildren().length, page1.getAllChildren().length);
		assertTrue(page1.getOnlineWidgets().length > 1);
		assertEquals(page1.getOnlineWidgets().length, page1.getWidgets().length);
		assertEquals(page1.getOnlineWidgets().length, page1.getDraftWidgets().length);
		
		assertNull(_pageManager.getOnlinePage("pagina_draft"));
		IPage draft = _pageManager.getDraftPage("pagina_draft");
		assertFalse(draft.isOnline());
		assertNull(draft.getOnlineMetadata());
		assertNotNull(draft.getDraftMetadata());
		assertEquals(0, draft.getChildren().length);
		assertEquals(0, draft.getOnlineChildren().length);
		assertEquals(0, draft.getAllChildren().length);
	}

	public void testAddUpdateMoveDeletePage() throws Throwable {
		try {
			this.checkAddPage();

			this.checkUpdatePage();

			this.movePage();
			
			this.checkPutOnlineOfflinePage();
			
			this.deletePage();
		} catch (Throwable t) {
			throw t;
		} finally {
			_pageManager.deletePage("temp");
			_pageManager.deletePage("temp1");
			_pageManager.deletePage("temp2");
		}
	}

	public void checkAddPage() throws Throwable {
		IPage parentPage = _pageManager.getDraftPage("service");
		PageModel pageModel = parentPage.getOnlineMetadata().getModel();
		PageMetadata metadata = PageTestUtil.createPageMetadata(
				pageModel.getCode(), true, "pagina temporanea", null, null,
				false, null, null);

		ApsProperties config = PageTestUtil.createProperties("temp", "tempValue", "contentId", "ART11");
		Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config, this._widgetTypeManager);
		Widget[] widgets = { widgetToAdd };
		
		Page pageToAdd = PageTestUtil.createPage("temp", parentPage, "free", metadata, metadata, widgets, widgets);
		_pageManager.addPage(pageToAdd);
		
		IPage addedPage = _pageManager.getDraftPage("temp");
		assertEquals(addedPage, _pageManager.getOnlinePage(addedPage.getCode()));
		PageTestUtil.comparePages(pageToAdd, addedPage, false);
		PageTestUtil.comparePageMetadata(pageToAdd.getOnlineMetadata(), addedPage.getOnlineMetadata(), 0);
		PageTestUtil.comparePageMetadata(pageToAdd.getDraftMetadata(), addedPage.getDraftMetadata(), 0);
		assertEquals(widgetToAdd, addedPage.getOnlineWidgets()[0]);
		assertEquals(widgetToAdd, addedPage.getDraftWidgets()[0]);
		
		parentPage = _pageManager.getDraftPage("service");
		pageToAdd.setParent(parentPage);
		pageToAdd.setCode("temp1");
		_pageManager.addPage(pageToAdd);
		addedPage = _pageManager.getDraftPage("temp1");
		assertEquals(addedPage, _pageManager.getOnlinePage(addedPage.getCode()));
		PageTestUtil.comparePages(pageToAdd, addedPage, false);
		PageTestUtil.comparePageMetadata(pageToAdd.getOnlineMetadata(), addedPage.getOnlineMetadata(), 0);
		PageTestUtil.comparePageMetadata(pageToAdd.getDraftMetadata(), addedPage.getDraftMetadata(), 0);
		assertEquals(widgetToAdd, addedPage.getOnlineWidgets()[0]);
		assertEquals(widgetToAdd, addedPage.getDraftWidgets()[0]);
		
		parentPage = _pageManager.getDraftPage("service");
		pageToAdd.setParent(parentPage);
		pageToAdd.setCode("temp2");
		pageToAdd.setOnlineMetadata(null);
		_pageManager.addPage(pageToAdd);
		addedPage = _pageManager.getDraftPage("temp2");
		assertNull(_pageManager.getOnlinePage(addedPage.getCode()));
		assertNull(pageToAdd.getOnlineMetadata());
		PageTestUtil.comparePages(pageToAdd, addedPage, false);
		assertNull(pageToAdd.getOnlineMetadata());
		PageTestUtil.comparePageMetadata(pageToAdd.getDraftMetadata(), addedPage.getDraftMetadata(), 0);
		assertNull(addedPage.getOnlineWidgets());
		assertEquals(widgetToAdd, addedPage.getDraftWidgets()[0]);
	}

	private void checkUpdatePage() throws Exception {
		Page dbPage = (Page) _pageManager.getDraftPage("temp");
		Page pageToUpdate = PageTestUtil.createPage("temp", dbPage.getParent(), "free", 
				dbPage.getOnlineMetadata().clone(), dbPage.getDraftMetadata().clone(), 
				PageTestUtil.copyArray(dbPage.getOnlineWidgets()), PageTestUtil.copyArray(dbPage.getDraftWidgets()));
		pageToUpdate.setPosition(dbPage.getPosition());
		PageMetadata onlineMetadata = pageToUpdate.getOnlineMetadata();
		onlineMetadata.setTitle("en", "temptitle1");
		onlineMetadata.setShowable(true);

		ApsProperties config = PageTestUtil.createProperties("temp1", "temp1", "contentId", "ART11");
		Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config, this._widgetTypeManager);
		pageToUpdate.getDraftWidgets()[2] = widgetToAdd;
		_pageManager.updatePage(pageToUpdate);
		
		IPage updatedPage = _pageManager.getDraftPage(dbPage.getCode());
		assertNotNull(updatedPage);
		PageTestUtil.comparePages(pageToUpdate, updatedPage, true);
		PageTestUtil.comparePageMetadata(pageToUpdate.getOnlineMetadata(), updatedPage.getOnlineMetadata(), 0);
		PageTestUtil.comparePageMetadata(pageToUpdate.getDraftMetadata(), updatedPage.getDraftMetadata(), 0);
		assertEquals(2, pageToUpdate.getOnlineMetadata().getTitles().size());
		assertEquals(1, pageToUpdate.getDraftMetadata().getTitles().size());
		PageTestUtil.compareWidgets(pageToUpdate.getOnlineWidgets(), updatedPage.getOnlineWidgets());
		PageTestUtil.compareWidgets(pageToUpdate.getDraftWidgets(), updatedPage.getDraftWidgets());
		assertNull(updatedPage.getOnlineWidgets()[2]);
		assertNotNull(updatedPage.getDraftWidgets()[2]);
		assertEquals(widgetToAdd, updatedPage.getDraftWidgets()[2]);
	}

	private void checkPutOnlineOfflinePage() throws Exception {
		String pageCode = "temp2";
		assertNull(_pageManager.getOnlinePage(pageCode));
		Page draftPage = (Page) _pageManager.getDraftPage(pageCode);
		assertNotNull(draftPage);
		assertFalse(draftPage.isOnline());
		assertFalse(draftPage.isChanged());
		
		_pageManager.setPageOnline(pageCode);
		Page onlinePage = (Page) _pageManager.getOnlinePage(pageCode);
		assertNotNull(onlinePage);
		assertTrue(onlinePage.isOnline());
		assertFalse(onlinePage.isChanged());
		PageTestUtil.comparePageMetadata(onlinePage.getOnlineMetadata(), onlinePage.getDraftMetadata(), 0);
		PageTestUtil.compareWidgets(onlinePage.getOnlineWidgets(), onlinePage.getDraftWidgets());
		
		_pageManager.setPageOffline(pageCode);
		assertNull(_pageManager.getOnlinePage(pageCode));
		Page offlinePage = (Page) _pageManager.getDraftPage(pageCode);
		assertNotNull(offlinePage);
		assertFalse(offlinePage.isOnline());
		assertFalse(offlinePage.isChanged());
		assertNull(offlinePage.getOnlineMetadata());
		assertNull(offlinePage.getOnlineWidgets());
	}

	private void movePage() throws Exception {
		int firstPos = 6;
		assertEquals(firstPos, _pageManager.getDraftPage("temp").getPosition());
		assertEquals(firstPos+1, _pageManager.getDraftPage("temp1").getPosition());
		assertEquals(firstPos+2, _pageManager.getDraftPage("temp2").getPosition());
		_pageManager.deletePage("temp");
		assertNull(_pageManager.getDraftPage("temp"));
		
		IPage temp1 = _pageManager.getDraftPage("temp1");
		IPage temp2 = _pageManager.getDraftPage("temp2");
		assertEquals(firstPos, temp1.getPosition());
		assertEquals(firstPos + 1, temp2.getPosition());
		
		_pageManager.movePage("temp2", true);
		IPage movedTemp1 = _pageManager.getDraftPage("temp1");
		IPage movedTemp2 = _pageManager.getDraftPage("temp2");
		assertEquals(firstPos, movedTemp2.getPosition());
		assertEquals(firstPos + 1, movedTemp1.getPosition());
		IPage[] pages = movedTemp2.getParent().getAllChildren();
		assertEquals(pages[pages.length - 2].getCode(), "temp2");
		assertEquals(pages[pages.length - 1].getCode(), "temp1");
		
		_pageManager.movePage("temp2", false);
		movedTemp1 = _pageManager.getDraftPage("temp1");
		movedTemp2 = _pageManager.getDraftPage("temp2");
		assertEquals(firstPos + 1, movedTemp2.getPosition());
		assertEquals(firstPos, movedTemp1.getPosition());
		pages = movedTemp2.getParent().getAllChildren();
		assertEquals(pages[pages.length - 2].getCode(), "temp1");
		assertEquals(pages[pages.length - 1].getCode(), "temp2");
	}

	private void deletePage() throws Throwable {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		MockWidgetsDAO mockWidgetsDAO = new MockWidgetsDAO();
		mockWidgetsDAO.setDataSource(dataSource);
		_pageManager.deletePage("temp");
		_pageManager.deletePage("temp2");
		IPage page = _pageManager.getDraftPage("temp");
		assertNull(page);
		boolean exists = true;
		try {
			exists = mockWidgetsDAO.exists("temp");
			assertEquals(exists, false);
			exists = mockWidgetsDAO.exists("temp2");
			assertEquals(exists, false);
		} catch (Throwable e) {
			throw e;
		}
	}

	public void testFailureJoinWidget_1() throws Throwable {
		String pageCode = "wrongPageCode";
		int frame = 2;
		try {
			Widget widget = this.getWidgetForTest("login", null);
			this._pageManager.joinWidget(pageCode, widget, frame);
			fail();
		} catch (ApsSystemException e) {
			// Errore per pagina inesistente
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testFailureJoinWidget_2() throws Throwable {
		String pageCode = "pagina_1";
		int frame = 6;
		IPage pagina_1 = this._pageManager.getDraftPage(pageCode);
		assertTrue(pagina_1.getDraftWidgets().length <= frame);
		try {
			Widget widget = this.getWidgetForTest("login", null);
			this._pageManager.joinWidget(pageCode, widget, frame);
			fail();
		} catch (ApsSystemException e) {
			// Errore per frame errato in modello
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageManager.updatePage(pagina_1);
		}
	}

	public void testFailureRemoveWidget_1() throws Throwable {
		String pageCode = "wrongPageCode";
		int frame = 2;
		try {
			this._pageManager.removeWidget(pageCode, frame);
			fail();
		} catch (ApsSystemException e) {
			// Errore per pagina inesistente
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testFailureRemoveWidget_2() throws Throwable {
		String pageCode = "pagina_1";
		int frame = 6;
		IPage pagina_1 = this._pageManager.getDraftPage(pageCode);
		assertTrue(pagina_1.getDraftWidgets().length <= frame);
		try {
			this._pageManager.removeWidget(pageCode, frame);
			fail();
		} catch (ApsSystemException e) {
			// Errore per frame errato in modello
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testJoinMoveRemoveWidget() throws Throwable {
		String pageCode = "pagina_1";
		int frame = 1;
		IPage pagina_1 = this._pageManager.getDraftPage(pageCode);
		assertNull(pagina_1.getDraftWidgets()[frame]);
		try {
			Widget widget = this.getWidgetForTest("login_form", null);
			this._pageManager.joinWidget(pageCode, widget, frame);
			
			pagina_1 = this._pageManager.getDraftPage(pageCode);
			assertTrue(pagina_1.isChanged());
			Widget[] onlineWidgets = PageTestUtil.getValuedWidgets(pagina_1.getOnlineWidgets());
			Widget[] draftWidgets = PageTestUtil.getValuedWidgets(pagina_1.getDraftWidgets());
			assertEquals(onlineWidgets.length + 1, draftWidgets.length);
			Widget extracted = pagina_1.getDraftWidgets()[frame];
			assertNotNull(extracted);
			assertEquals("login_form", extracted.getType().getCode());
			
			this._pageManager.moveWidget(pageCode, frame, frame-1);
			pagina_1 = this._pageManager.getDraftPage(pageCode);
			assertTrue(pagina_1.isChanged());
			onlineWidgets = PageTestUtil.getValuedWidgets(pagina_1.getOnlineWidgets());
			draftWidgets = PageTestUtil.getValuedWidgets(pagina_1.getDraftWidgets());
			assertEquals(onlineWidgets.length + 1, draftWidgets.length);
			assertNull(pagina_1.getDraftWidgets()[frame]);
			extracted = pagina_1.getDraftWidgets()[frame-1];
			assertNotNull(extracted);
			assertEquals("login_form", extracted.getType().getCode());
			
			this._pageManager.moveWidget(pageCode, frame-1, frame);
			pagina_1 = this._pageManager.getDraftPage(pageCode);
			assertTrue(pagina_1.isChanged());
			onlineWidgets = PageTestUtil.getValuedWidgets(pagina_1.getOnlineWidgets());
			draftWidgets = PageTestUtil.getValuedWidgets(pagina_1.getDraftWidgets());
			assertEquals(onlineWidgets.length + 1, draftWidgets.length);
			assertNull(pagina_1.getDraftWidgets()[frame-1]);
			extracted = pagina_1.getDraftWidgets()[frame];
			assertNotNull(extracted);
			assertEquals("login_form", extracted.getType().getCode());
			
			this._pageManager.removeWidget(pageCode, frame);
			pagina_1 = this._pageManager.getDraftPage(pageCode);
			assertFalse(pagina_1.isChanged());
			onlineWidgets = PageTestUtil.getValuedWidgets(pagina_1.getOnlineWidgets());
			draftWidgets = PageTestUtil.getValuedWidgets(pagina_1.getDraftWidgets());
			assertEquals(onlineWidgets.length, draftWidgets.length);
			extracted = pagina_1.getDraftWidgets()[frame];
			assertNull(extracted);
		} catch (Throwable t) {
			pagina_1.getDraftWidgets()[frame] = null;
			this._pageManager.updatePage(pagina_1);
			throw t;
		}
	}

	public void testSearchPage() throws Throwable {
		List<String> allowedGroupCodes = new ArrayList<String>();
		allowedGroupCodes.add(Group.ADMINS_GROUP_NAME);
		try {
			List<IPage> pagesFound = this._pageManager.searchPages("aGIna_", allowedGroupCodes);
			assertNotNull(pagesFound);
			assertEquals(5, pagesFound.size());
			String pageCodeToken = "agina";
			pagesFound = this._pageManager.searchPages(pageCodeToken, allowedGroupCodes);
			// verify the result found
			assertNotNull(pagesFound);
			Iterator<IPage> itr = pagesFound.iterator();
			assertEquals(6, pagesFound.size());
			while (itr.hasNext()) {
				IPage currentCode = itr.next();
				assertTrue(currentCode.getCode().contains(pageCodeToken));
			}
			pagesFound = this._pageManager.searchPages("", allowedGroupCodes);
			assertNotNull(pagesFound);
			System.out.println("-----------------------");
			System.out.println(pagesFound);
			System.out.println("-----------------------");
			assertEquals(17, pagesFound.size());
			pagesFound = this._pageManager.searchPages(null, allowedGroupCodes);
			assertNotNull(pagesFound);
			assertEquals(17, pagesFound.size());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testGetWidgetUtilizers() throws Throwable {
		List<IPage> pageUtilizers1 = this._pageManager.getWidgetUtilizers(null);
		assertNotNull(pageUtilizers1);
		assertEquals(0, pageUtilizers1.size());

		List<IPage> pageUtilizers2 = this._pageManager.getWidgetUtilizers("logic_type");
		assertNotNull(pageUtilizers2);
		assertEquals(0, pageUtilizers2.size());

		List<IPage> pageUtilizers3 = this._pageManager.getWidgetUtilizers("leftmenu");
		assertNotNull(pageUtilizers3);
		assertEquals(3, pageUtilizers3.size());
		assertEquals("pagina_1", pageUtilizers3.get(0).getCode());

		List<IPage> onlinePageUtilizers3 = this._pageManager.getOnlineWidgetUtilizers("leftmenu");
		List<IPage> draftPageUtilizers3 = this._pageManager.getDraftWidgetUtilizers("leftmenu");
		assertEquals(pageUtilizers3.size(), 1 + onlinePageUtilizers3.size());
		assertEquals(pageUtilizers3.size(), draftPageUtilizers3.size());

		List<IPage> pageUtilizers4 = this._pageManager.getWidgetUtilizers("content_viewer");
		assertNotNull(pageUtilizers4);
		assertEquals(7, pageUtilizers4.size());
		assertEquals("homepage", pageUtilizers4.get(0).getCode());
		assertEquals("contentview", pageUtilizers4.get(1).getCode());
		assertEquals("pagina_11", pageUtilizers4.get(2).getCode());
		assertEquals("pagina_2", pageUtilizers4.get(3).getCode());
		assertEquals("coach_page", pageUtilizers4.get(4).getCode());
		assertEquals("customers_page", pageUtilizers4.get(5).getCode());
		assertEquals("customer_subpage_2", pageUtilizers4.get(6).getCode());
	}

	public void pageStatusTest() throws ApsSystemException {
		String testCode = "testcode";
		try {
		PagesStatus status = this._pageManager.getPagesStatus();
		System.out.println(status);
		
		IPage parentPage = _pageManager.getDraftPage(this._pageManager.getRoot().getCode());
		
		PageModel pageModel = parentPage.getOnlineMetadata().getModel();
		
		PageMetadata metadata = PageTestUtil.createPageMetadata(
				pageModel.getCode(), true, "pagina temporanea", null, null,
				false, null, null);

		PageMetadata onlineMeta = null;
		PageMetadata draftMeta = metadata;
		
		Page pageToAdd = PageTestUtil.createPage(testCode, parentPage, "free",  onlineMeta, draftMeta, null, null);
		_pageManager.addPage(pageToAdd);
		
		PagesStatus newStatus = this._pageManager.getPagesStatus();
		assertEquals(newStatus.getOnline(), status.getOnline());
		assertEquals(newStatus.getOnlineWithChanges(), status.getOnlineWithChanges());
		assertEquals(newStatus.getDraft(), status.getDraft() + 1);
		assertEquals(newStatus.getTotal(), status.getTotal() + 1);
		
		this._pageManager.setPageOnline(testCode);
		newStatus = this._pageManager.getPagesStatus();
		assertEquals(newStatus.getOnline(), status.getOnline() + 1);
		assertEquals(newStatus.getOnlineWithChanges(), status.getOnlineWithChanges());
		assertEquals(newStatus.getDraft(), status.getDraft());
		assertEquals(newStatus.getTotal(), status.getTotal() + 1);
		
		IPage test = this._pageManager.getPage(testCode);
		test.getDraftMetadata().setTitle("it", "modxxxx");
		this._pageManager.updatePage(test);
		test = this._pageManager.getPage(testCode);
		newStatus = this._pageManager.getPagesStatus();
		
		assertEquals(newStatus.getOnline(), status.getOnline());
		assertEquals(newStatus.getOnlineWithChanges(), status.getOnlineWithChanges() + 1);
		assertEquals(newStatus.getDraft(), status.getDraft());
		assertEquals(newStatus.getTotal(), status.getTotal() + 1);
	
		} finally {
			this._pageManager.deletePage(testCode);
		}
	}
	
	private Widget getWidgetForTest(String widgetTypeCode, ApsProperties config)
			throws Throwable {
		WidgetType type = this._widgetTypeManager.getWidgetType(widgetTypeCode);
		Widget widget = new Widget();
		widget.setType(type);
		if (null != config) {
			widget.setConfig(config);
		}
		return widget;
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
