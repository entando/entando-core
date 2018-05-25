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
package com.agiletec.aps.system.services.pagemodel;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author M.Diana
 */
public class TestPageModelManager extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testSearch_with_null_empty_filters() throws ApsSystemException {
        List<FieldSearchFilter> filters = null;
        SearcherDaoPaginatedResult<PageModel> result = this._pageModelManager.searchPageModels(filters);
        assertThat(result.getCount(), is(3));
        assertThat(result.getList().size(), is(3));

        filters = new ArrayList<>();
        result = this._pageModelManager.searchPageModels(filters);
        assertThat(result.getCount(), is(3));
        assertThat(result.getList().size(), is(3));
    }

    public void testSearch_with_page_filter() throws ApsSystemException {
        RestListRequest restListRequest = new RestListRequest();
        restListRequest.setPageSize(2);
        restListRequest.setPage(1);

        List<FieldSearchFilter> filters = restListRequest.buildFieldSearchFilters();
        SearcherDaoPaginatedResult<PageModel> result = this._pageModelManager.searchPageModels(filters);
        assertThat(result.getCount(), is(3));
        assertThat(result.getList().size(), is(2));

        restListRequest.addFilter(new Filter("descr", "modello"));
        result = this._pageModelManager.searchPageModels(restListRequest.buildFieldSearchFilters());
        assertThat(result.getCount(), is(2));
        assertThat(result.getList().size(), is(2));

        restListRequest.setPage(0);
        result = this._pageModelManager.searchPageModels(restListRequest.buildFieldSearchFilters());
        assertThat(result.getCount(), is(2));
        assertThat(result.getList().size(), is(2));
    }

    public void testGetPageModel() throws ApsSystemException {
        PageModel pageModel = this._pageModelManager.getPageModel("home");
        String code = pageModel.getCode();
        String descr = pageModel.getDescription();
        assertEquals(code, "home");
        assertEquals(descr, "Modello home page");
        Widget[] widgets = pageModel.getDefaultWidget();
        for (int i = 0; i < widgets.length; i++) {
            Widget widget = widgets[i];
            assertEquals(widget, null);
        }
        String[] frames = pageModel.getFrames();
        assertEquals(frames[0], "Box sinistra alto");
        int mainFrame = pageModel.getMainFrame();
        assertEquals(mainFrame, 3);
    }

    public void testGetPageModels() throws ApsSystemException {
        List<PageModel> pageModels = new ArrayList<>(this._pageModelManager.getPageModels());
        assertEquals(3, pageModels.size());
        for (int i = 0; i < pageModels.size(); i++) {
            PageModel pageModel = pageModels.get(i);
            String code = pageModel.getCode();
            boolean isNotNull = (code != null);
            assertEquals(isNotNull, true);
            if (code.equals("home")) {
                assertEquals("Modello home page", pageModel.getDescription());
            } else if (code.equals("service")) {
                assertEquals("Modello pagine di servizio", pageModel.getDescription());
            }
        }
    }

    public void testGetModel() throws Throwable {
        PageModel model = this._pageModelManager.getPageModel("internal");
        assertNotNull(model);
        assertEquals(9, model.getFrames().length);
        Widget[] defaultWidgets = model.getDefaultWidget();
        assertEquals(model.getFrames().length, defaultWidgets.length);
        for (int i = 0; i < defaultWidgets.length; i++) {
            Widget widget = defaultWidgets[i];
            if (i == 3) {
                assertNotNull(widget);
                WidgetType type = widget.getType();
                assertEquals("leftmenu", type.getCode());
                assertEquals(1, type.getTypeParameters().size());
                assertNull(type.getConfig());
                ApsProperties config = widget.getConfig();
                assertEquals(1, config.size());
                assertEquals("code(homepage).subtree(1)", config.getProperty("navSpec"));
            } else {
                assertNull(widget);
            }
        }
    }

    public void testAddRemoveModel() throws Throwable {
        String testPageModelCode = "test_pagemodel";
        assertNull(this._pageModelManager.getPageModel(testPageModelCode));
        try {
            PageModel mockModel = this.createMockPageModel(testPageModelCode);
            this._pageModelManager.addPageModel(mockModel);
            PageModel extractedMockModel = this._pageModelManager.getPageModel(testPageModelCode);
            assertNotNull(extractedMockModel);
            assertEquals(testPageModelCode, extractedMockModel.getCode());
            assertTrue(extractedMockModel.getDescription().contains(testPageModelCode));
            assertEquals(3, extractedMockModel.getFrames().length);
            Widget[] defaultWidgets = extractedMockModel.getDefaultWidget();
            assertEquals(3, defaultWidgets.length);
            Widget defWidg0 = defaultWidgets[0];
            assertNull(defWidg0);
            Widget defWidg1 = defaultWidgets[1];
            assertNotNull(defWidg1);
            assertEquals("formAction", defWidg1.getType().getCode());
            Widget defWidg2 = defaultWidgets[2];
            assertNotNull(defWidg2);
            assertEquals("login_form", defWidg2.getType().getCode());
            assertNull(defWidg2.getConfig());
            assertEquals("<strong>Freemarker template content</strong>", extractedMockModel.getTemplate());
        } catch (Exception e) {
            throw e;
        } finally {
            this._pageModelManager.deletePageModel(testPageModelCode);
            assertNull(this._pageModelManager.getPageModel(testPageModelCode));
        }
    }

    public void testUpdateModel() throws Throwable {
        String testPageModelCode = "test_pagemodel";
        assertNull(this._pageModelManager.getPageModel(testPageModelCode));
        try {
            PageModel mockModel = this.createMockPageModel(testPageModelCode);
            this._pageModelManager.addPageModel(mockModel);
            PageModel extractedMockModel = this._pageModelManager.getPageModel(testPageModelCode);
            extractedMockModel.setDescription("Modified Description");
            Frame[] configuration = extractedMockModel.getConfiguration();
            Frame[] newConfiguration = new Frame[4];
            for (int i = 0; i < configuration.length; i++) {
                newConfiguration[i] = configuration[i];
            }
            Frame frame3 = new Frame();
            frame3.setPos(3);
            frame3.setDescription("Freme 3");
            Widget defWidg3ToSet = new Widget();
            defWidg3ToSet.setType(this._widgetTypeManager.getWidgetType("entando_apis"));
            frame3.setDefaultWidget(defWidg3ToSet);
            newConfiguration[3] = frame3;
            extractedMockModel.setConfiguration(newConfiguration);
            extractedMockModel.setTemplate("<strong>Modified Freemarker template content</strong>");
            this._pageModelManager.updatePageModel(extractedMockModel);
            extractedMockModel = this._pageModelManager.getPageModel(testPageModelCode);
            assertNotNull(extractedMockModel);
            assertEquals(testPageModelCode, extractedMockModel.getCode());
            assertEquals("Modified Description", extractedMockModel.getDescription());
            assertEquals(4, extractedMockModel.getFrames().length);
            Widget[] defaultWidgets = extractedMockModel.getDefaultWidget();
            assertEquals(4, defaultWidgets.length);

            Widget defWidg0 = defaultWidgets[0];
            assertNull(defWidg0);

            Widget defWidg1 = defaultWidgets[1];
            assertNotNull(defWidg1);
            assertEquals("formAction", defWidg1.getType().getCode());

            Widget defWidg2 = defaultWidgets[2];
            assertNotNull(defWidg2);
            assertEquals("login_form", defWidg2.getType().getCode());
            assertNull(defWidg2.getConfig());

            Widget defWidg3 = defaultWidgets[3];
            assertNotNull(defWidg3);
            assertEquals("entando_apis", defWidg3.getType().getCode());

            assertEquals("<strong>Modified Freemarker template content</strong>", extractedMockModel.getTemplate());

        } catch (Exception e) {
            throw e;
        } finally {
            this._pageModelManager.deletePageModel(testPageModelCode);
            assertNull(this._pageModelManager.getPageModel(testPageModelCode));
        }
    }

    private PageModel createMockPageModel(String code) {
        PageModel model = new PageModel();
        model.setCode(code);
        model.setDescription("Description of model " + code);
        Frame frame0 = new Frame();
        frame0.setPos(0);
        frame0.setDescription("Freme 0");
        frame0.setMainFrame(true);
        Frame frame1 = new Frame();
        frame1.setPos(1);
        frame1.setDescription("Frame 1");
        Widget defWidg1 = new Widget();
        defWidg1.setType(this._widgetTypeManager.getWidgetType("formAction"));
        frame1.setDefaultWidget(defWidg1);
        Frame frame2 = new Frame();
        frame2.setPos(1);
        frame2.setDescription("Freme 2");
        Widget defWidg2 = new Widget();
        defWidg2.setType(this._widgetTypeManager.getWidgetType("login_form"));
        frame2.setDefaultWidget(defWidg2);
        Frame[] configuration = {frame0, frame1, frame2};
        model.setConfiguration(configuration);
        model.setTemplate("<strong>Freemarker template content</strong>");
        return model;
    }

    @SuppressWarnings("unchecked")
    public void testGuiFragmentUtilizer() throws Throwable {
        String testPageModelACode = "test_pagemodelA";
        String testPageModelBCode = "test_pagemodelB";
        String testPageModelCCode = "test_pagemodelC";
        assertNull(this._pageModelManager.getPageModel(testPageModelACode));
        assertNull(this._pageModelManager.getPageModel(testPageModelBCode));
        assertNull(this._pageModelManager.getPageModel(testPageModelCCode));
        try {
            String templateA = "Hello 	<@wp.fragment code=\"CODE_1\" escapeXml=false /><@wp.fragment code=\"CODE_1\" escapeXml=false /><@wp.fragment escapeXml=false code=\"CODE_1\"  /><@wp.fragment code=\"CODE_1\"  />world";
            PageModel mockModelA = this.createMockPageModel(testPageModelACode);
            mockModelA.setTemplate(templateA);
            this._pageModelManager.addPageModel(mockModelA);

            String templateB = "Hello 	<@wp.fragment code=\"CODE_B\" escapeXml=false />\r\n<@wp.fragment escapeXml=false code=\"CODE_X\" /><@wp.fragment escapeXml=false code=\"CODE_1\"  /><@wp.fragment code=\"CODE_1\"  />world";
            PageModel mockModelB = this.createMockPageModel(testPageModelBCode);
            mockModelB.setTemplate(templateB);
            this._pageModelManager.addPageModel(mockModelB);

            String templateC = "Hello\r\n 	<@wp.fragment code=\"CODE_B\" escapeXml=false />\n\t<@wp.fragment code=\"CODE_1\" escapeXml=false /><@wp.fragment escapeXml=false code=\"CODE_C\"  /><@wp.fragment code=\"CODE_1\"  />world";
            PageModel mockModelC = this.createMockPageModel(testPageModelCCode);
            mockModelC.setTemplate(templateC);
            this._pageModelManager.addPageModel(mockModelC);

            String fragment = "CODE";
            List<PageModel> list = ((PageModelManager) this._pageModelManager).getGuiFragmentUtilizers(fragment);
            assertEquals(0, list.size());

            fragment = "CODE_1";
            list = ((PageModelManager) this._pageModelManager).getGuiFragmentUtilizers(fragment);
            assertEquals(3, list.size());

            fragment = "CODE_CC";
            list = ((PageModelManager) this._pageModelManager).getGuiFragmentUtilizers(fragment);
            assertEquals(0, list.size());

            fragment = "CODE_C";
            list = ((PageModelManager) this._pageModelManager).getGuiFragmentUtilizers(fragment);
            assertEquals(1, list.size());

            fragment = "CODE_B";
            list = ((PageModelManager) this._pageModelManager).getGuiFragmentUtilizers(fragment);
            assertEquals(2, list.size());

            fragment = "CODE_X";
            list = ((PageModelManager) this._pageModelManager).getGuiFragmentUtilizers(fragment);
            assertEquals(1, list.size());

        } catch (Exception e) {
            throw e;
        } finally {
            this._pageModelManager.deletePageModel(testPageModelACode);
            this._pageModelManager.deletePageModel(testPageModelBCode);
            this._pageModelManager.deletePageModel(testPageModelCCode);
            assertNull(this._pageModelManager.getPageModel(testPageModelACode));
            assertNull(this._pageModelManager.getPageModel(testPageModelBCode));
            assertNull(this._pageModelManager.getPageModel(testPageModelCCode));
        }
    }

    private void init() throws Exception {
        try {
            this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IWidgetTypeManager _widgetTypeManager;
    private IPageModelManager _pageModelManager = null;

}
