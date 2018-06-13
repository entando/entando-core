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

    public void testGetPage_1() throws Throwable {
        IPage root = _pageManager.getDraftRoot();
        assertNotNull(root);
        assertEquals("homepage", root.getCode());
        assertTrue(root.isOnline());
        assertNotNull(root.getMetadata());
        assertNotNull(root.getMetadata());
        assertEquals(7, root.getChildrenCodes().length);

        assertEquals("service", root.getChildrenCodes()[0]);
        assertEquals("pagina_1", root.getChildrenCodes()[1]);
        assertEquals("pagina_2", root.getChildrenCodes()[2]);
        assertEquals("coach_page", root.getChildrenCodes()[3]);
        assertEquals("customers_page", root.getChildrenCodes()[4]);
        assertEquals("administrators_page", root.getChildrenCodes()[5]);
        assertEquals("pagina_draft", root.getChildrenCodes()[6]);

        assertNotNull(_pageManager.getOnlinePage("homepage"));
        assertNotNull(_pageManager.getDraftPage("homepage"));
    }

    public void testGetPage_2() throws Throwable {
        IPage page1 = _pageManager.getOnlinePage("pagina_1");
        assertNotNull(page1);
        assertTrue(page1.isOnline());
        assertNotNull(page1.getMetadata());
        assertNotNull(page1.getMetadata());
        assertEquals(2, page1.getPosition());
        assertTrue(page1.getChildrenCodes().length > 1);
        assertTrue(page1.getWidgets().length > 1);
    }

    public void testGetPage_3() throws Throwable {
        assertNull(_pageManager.getOnlinePage("pagina_draft"));
        IPage draft = _pageManager.getDraftPage("pagina_draft");
        assertFalse(draft.isOnline());
        assertNotNull(draft.getMetadata());
        assertEquals(0, draft.getChildrenCodes().length);
        assertEquals(7, draft.getPosition());
    }

    public void testAddUpdateMoveDeletePage() throws Throwable {
        try {
            assertNull(this._pageManager.getDraftPage("temp"));
            assertNull(this._pageManager.getDraftPage("temp1"));
            assertNull(this._pageManager.getDraftPage("temp2"));
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

    private void checkAddPage() throws Throwable {
        IPage parentPage = _pageManager.getDraftPage("service");
        PageModel pageModel = parentPage.getMetadata().getModel();
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(),
                true, "pagina temporanea", null, null, false, null, null);
        ApsProperties config = PageTestUtil.createProperties("actionPath", "/myJsp.jsp", "param1", "value1");
        Widget widgetToAdd = PageTestUtil.createWidget("formAction", config, this._widgetTypeManager);
        Widget[] widgets = {widgetToAdd};
        Page pageToAdd = PageTestUtil.createPage("temp", parentPage, "free", metadata, widgets);
        _pageManager.addPage(pageToAdd);

        IPage addedPage = _pageManager.getDraftPage("temp");
        assertEquals(addedPage, _pageManager.getDraftPage(addedPage.getCode()));
        PageTestUtil.comparePages(pageToAdd, addedPage, false);
        PageTestUtil.comparePageMetadata(pageToAdd.getMetadata(), addedPage.getMetadata(), 0);
        assertEquals(widgetToAdd, addedPage.getWidgets()[0]);

        parentPage = _pageManager.getDraftPage("service");
        pageToAdd.setParent(parentPage);
        pageToAdd.setCode("temp1");
        _pageManager.addPage(pageToAdd);
        addedPage = _pageManager.getDraftPage("temp1");
        assertEquals(addedPage, _pageManager.getDraftPage(addedPage.getCode()));
        PageTestUtil.comparePages(pageToAdd, addedPage, false);
        PageTestUtil.comparePageMetadata(pageToAdd.getMetadata(), addedPage.getMetadata(), 0);
        assertEquals(widgetToAdd, addedPage.getWidgets()[0]);

        parentPage = _pageManager.getDraftPage("service");
        pageToAdd.setParent(parentPage);
        pageToAdd.setCode("temp2");
        _pageManager.addPage(pageToAdd);
        addedPage = _pageManager.getDraftPage("temp2");
        assertNotNull(_pageManager.getDraftPage(addedPage.getCode()));
        assertNotNull(pageToAdd.getMetadata());
        assertEquals(widgetToAdd, addedPage.getWidgets()[0]);
    }

    private void checkUpdatePage() throws Exception {
        Page dbPage = (Page) _pageManager.getDraftPage("temp");
        Page pageToUpdate = PageTestUtil.createPage("temp", dbPage.getParent(), "free", dbPage.getMetadata().clone(), PageTestUtil
                .copyArray(dbPage.getWidgets()));
        pageToUpdate.setPosition(dbPage.getPosition());
        PageMetadata onlineMetadata = pageToUpdate.getMetadata();
        onlineMetadata.setTitle("en", "temptitle1");
        onlineMetadata.setShowable(true);

        ApsProperties config = PageTestUtil.createProperties("actionPath", "/myJsp.jsp", "param1", "value1");
        Widget widgetToAdd = PageTestUtil.createWidget("formAction", config, this._widgetTypeManager);
        pageToUpdate.getWidgets()[2] = widgetToAdd;
        _pageManager.setPageOnline(pageToUpdate.getCode());

        IPage updatedPage = _pageManager.getOnlinePage(dbPage.getCode());
        pageToUpdate = (Page) _pageManager.getOnlinePage(pageToUpdate.getCode());
        assertNotNull(updatedPage);
        PageTestUtil.comparePages(pageToUpdate, updatedPage, false);
        PageTestUtil.comparePageMetadata(pageToUpdate.getMetadata(), updatedPage.getMetadata(), 0);

        assertEquals(1, pageToUpdate.getMetadata().getTitles().size());
        PageTestUtil.compareWidgets(pageToUpdate.getWidgets(), updatedPage.getWidgets());
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
        PageTestUtil.comparePageMetadata(onlinePage.getMetadata(), onlinePage.getMetadata(), 0);
        PageTestUtil.compareWidgets(onlinePage.getWidgets(), onlinePage.getWidgets());

        _pageManager.setPageOffline(pageCode);
        assertNull(_pageManager.getOnlinePage(pageCode));
        Page offlinePage = (Page) _pageManager.getOnlinePage(pageCode);
        assertNull(offlinePage);
    }

    private void movePage() throws Exception {
        int firstPos = 7;
        assertEquals(firstPos, _pageManager.getDraftPage("temp").getPosition());
        assertEquals(firstPos + 1, _pageManager.getDraftPage("temp1").getPosition());
        assertEquals(firstPos + 2, _pageManager.getDraftPage("temp2").getPosition());
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
        String[] pages = movedTemp2.getParent().getChildrenCodes();
        assertEquals(pages[pages.length - 2], "temp2");
        assertEquals(pages[pages.length - 1], "temp1");

        _pageManager.movePage("temp2", false);
        movedTemp1 = _pageManager.getDraftPage("temp1");
        movedTemp2 = _pageManager.getDraftPage("temp2");
        assertEquals(firstPos + 1, movedTemp2.getPosition());
        assertEquals(firstPos, movedTemp1.getPosition());
        pages = movedTemp2.getParent().getChildrenCodes();
        assertEquals(pages[pages.length - 2], "temp1");
        assertEquals(pages[pages.length - 1], "temp2");
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
        assertTrue(pagina_1.getWidgets().length <= frame);
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
        assertTrue(pagina_1.getWidgets().length <= frame);
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
        assertNull(pagina_1.getWidgets()[frame]);
        try {

            Widget[] onlineWidgets = this._pageManager.getOnlinePage(pageCode).getWidgets();
            Widget[] draftWidgets = this._pageManager.getDraftPage(pageCode).getWidgets();
            onlineWidgets = PageTestUtil.getValuedWidgets(onlineWidgets);
            draftWidgets = PageTestUtil.getValuedWidgets(draftWidgets);
            assertEquals(onlineWidgets.length, draftWidgets.length);

            Widget widget = this.getWidgetForTest("login_form", null);
            this._pageManager.joinWidget(pageCode, widget, frame);

            pagina_1 = this._pageManager.getDraftPage(pageCode);
            assertTrue(pagina_1.isChanged());

            onlineWidgets = this._pageManager.getOnlinePage(pageCode).getWidgets();
            draftWidgets = this._pageManager.getDraftPage(pageCode).getWidgets();
            onlineWidgets = PageTestUtil.getValuedWidgets(onlineWidgets);
            draftWidgets = PageTestUtil.getValuedWidgets(draftWidgets);

            assertEquals(onlineWidgets.length + 1, draftWidgets.length);
            Widget extracted = pagina_1.getWidgets()[frame];
            assertNotNull(extracted);
            assertEquals("login_form", extracted.getType().getCode());

            this._pageManager.moveWidget(pageCode, frame, frame - 1);
            pagina_1 = this._pageManager.getDraftPage(pageCode);
            assertTrue(pagina_1.isChanged());

            onlineWidgets = this._pageManager.getOnlinePage(pageCode).getWidgets();
            draftWidgets = this._pageManager.getDraftPage(pageCode).getWidgets();
            onlineWidgets = PageTestUtil.getValuedWidgets(onlineWidgets);
            draftWidgets = PageTestUtil.getValuedWidgets(draftWidgets);

            assertEquals(onlineWidgets.length + 1, draftWidgets.length);
            assertNull(pagina_1.getWidgets()[frame]);
            extracted = pagina_1.getWidgets()[frame - 1];
            assertNotNull(extracted);
            assertEquals("login_form", extracted.getType().getCode());

            this._pageManager.moveWidget(pageCode, frame - 1, frame);
            pagina_1 = this._pageManager.getDraftPage(pageCode);
            assertTrue(pagina_1.isChanged());

            onlineWidgets = this._pageManager.getOnlinePage(pageCode).getWidgets();
            draftWidgets = this._pageManager.getDraftPage(pageCode).getWidgets();
            onlineWidgets = PageTestUtil.getValuedWidgets(onlineWidgets);
            draftWidgets = PageTestUtil.getValuedWidgets(draftWidgets);

            assertEquals(onlineWidgets.length + 1, draftWidgets.length);
            assertNull(pagina_1.getWidgets()[frame - 1]);
            extracted = pagina_1.getWidgets()[frame];
            assertNotNull(extracted);
            assertEquals("login_form", extracted.getType().getCode());

            this._pageManager.removeWidget(pageCode, frame);
            pagina_1 = this._pageManager.getDraftPage(pageCode);
            assertFalse(pagina_1.isChanged());

            onlineWidgets = this._pageManager.getOnlinePage(pageCode).getWidgets();
            draftWidgets = this._pageManager.getDraftPage(pageCode).getWidgets();
            onlineWidgets = PageTestUtil.getValuedWidgets(onlineWidgets);
            draftWidgets = PageTestUtil.getValuedWidgets(draftWidgets);

            assertEquals(onlineWidgets.length, draftWidgets.length);
            extracted = pagina_1.getWidgets()[frame];
            assertNull(extracted);
        } catch (Throwable t) {
            pagina_1.getWidgets()[frame] = null;
            this._pageManager.updatePage(pagina_1);
            throw t;
        }
    }

    public void testSearchPage() throws Throwable {
        List<String> allowedGroupCodes = new ArrayList<>();
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
            assertEquals(18, pagesFound.size());
            pagesFound = this._pageManager.searchPages(null, allowedGroupCodes);
            assertNotNull(pagesFound);
            assertEquals(18, pagesFound.size());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testGetWidgetUtilizers() throws Throwable {
        List<IPage> pageUtilizers1 = this._pageManager.getDraftWidgetUtilizers(null);
        assertNotNull(pageUtilizers1);
        assertEquals(0, pageUtilizers1.size());

        List<IPage> pageUtilizers2 = this._pageManager.getDraftWidgetUtilizers("login_form");
        assertNotNull(pageUtilizers2);
        assertEquals(2, pageUtilizers2.size());

        List<IPage> pageUtilizers3 = this._pageManager.getDraftWidgetUtilizers("leftmenu");
        assertNotNull(pageUtilizers3);
        assertEquals(3, pageUtilizers3.size());
        assertEquals("pagina_1", pageUtilizers3.get(0).getCode());

        pageUtilizers3 = this._pageManager.getOnlineWidgetUtilizers("leftmenu");
        assertNotNull(pageUtilizers3);
        assertEquals(1, pageUtilizers3.size());
        assertEquals("pagina_1", pageUtilizers3.get(0).getCode());
    }

    public void testPageStatus() throws ApsSystemException {
        String testCode = "testcode";
        try {
            PagesStatus status = this._pageManager.getPagesStatus();
            IPage parentPage = _pageManager.getDraftRoot();
            PageModel pageModel = parentPage.getMetadata().getModel();
            PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel.getCode(), true, "pagina temporanea", null, null, false, null, null);
            PageMetadata draftMeta = metadata;
            Page pageToAdd = PageTestUtil.createPage(testCode, parentPage, "free", draftMeta, null);
            _pageManager.addPage(pageToAdd);
            PagesStatus newStatus = this._pageManager.getPagesStatus();
            assertEquals(newStatus.getOnline(), status.getOnline());
            assertEquals(newStatus.getOnlineWithChanges(), status.getOnlineWithChanges());
            assertEquals(newStatus.getUnpublished(), status.getUnpublished() + 1);
            assertEquals(newStatus.getTotal(), status.getTotal() + 1);
            this._pageManager.setPageOnline(testCode);
            newStatus = this._pageManager.getPagesStatus();
            assertEquals(newStatus.getOnline(), status.getOnline() + 1);
            assertEquals(newStatus.getOnlineWithChanges(), status.getOnlineWithChanges());
            assertEquals(newStatus.getUnpublished(), status.getUnpublished());
            assertEquals(newStatus.getTotal(), status.getTotal() + 1);
            IPage test = this._pageManager.getDraftPage(testCode);
            test.getMetadata().setTitle("it", "modxxxx");
            this._pageManager.updatePage(test);
            test = this._pageManager.getDraftPage(testCode);
            newStatus = this._pageManager.getPagesStatus();
            assertEquals(newStatus.getOnline(), status.getOnline());
            assertEquals(newStatus.getOnlineWithChanges(), status.getOnlineWithChanges() + 1);
            assertEquals(newStatus.getUnpublished(), status.getUnpublished());
            assertEquals(newStatus.getTotal(), status.getTotal() + 1);
        } finally {
            this._pageManager.deletePage(testCode);
        }
    }

    private Widget getWidgetForTest(String widgetTypeCode, ApsProperties config) throws Throwable {
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
