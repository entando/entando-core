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

import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author M.Diana - E.Santoboni
 */
public class TestPageModelDAO extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testLoadModels() throws Throwable {
        Map<String, PageModel> models = this._pageModelDAO.loadModels();
        assertTrue(models.containsKey("home"));
        assertTrue(models.containsKey("service"));
    }

    public void testAddRemoveModel() throws Throwable {
        Map<String, PageModel> models = this._pageModelDAO.loadModels();
        String testPageModelCode = "test_pagemodel";
        try {
            assertTrue(!models.containsKey(testPageModelCode));
            PageModel mockModel = this.createMockPageModel(testPageModelCode);
            this._pageModelDAO.addModel(mockModel);
            models = this._pageModelDAO.loadModels();
            PageModel extractedMockModel = models.get(testPageModelCode);
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
            this._pageModelDAO.deleteModel(testPageModelCode);
            models = this._pageModelDAO.loadModels();
            assertTrue(!models.containsKey(testPageModelCode));
        }
    }

    public void testUpdateModel() throws Throwable {
        Map<String, PageModel> models = this._pageModelDAO.loadModels();
        String testPageModelCode = "test_pagemodel";
        try {
            assertTrue(!models.containsKey(testPageModelCode));
            PageModel mockModel = this.createMockPageModel(testPageModelCode);
            this._pageModelDAO.addModel(mockModel);
            models = this._pageModelDAO.loadModels();
            PageModel extractedMockModel = models.get(testPageModelCode);
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
            defWidg3ToSet.setType(this._widgetTypeManager.getWidgetType("formAction"));
            ApsProperties props3 = new ApsProperties();
            props3.setProperty("actionPath", "/myPath");
            defWidg3ToSet.setConfig(props3);
            frame3.setDefaultWidget(defWidg3ToSet);
            newConfiguration[3] = frame3;
            extractedMockModel.setConfiguration(newConfiguration);
            extractedMockModel.setTemplate("<strong>Modified Freemarker template content</strong>");
            this._pageModelDAO.updateModel(extractedMockModel);

            models = this._pageModelDAO.loadModels();
            extractedMockModel = models.get(testPageModelCode);
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
            assertEquals("formAction", defWidg3.getType().getCode());
            assertEquals(1, defWidg3.getConfig().size());
            assertEquals("/myPath", defWidg3.getConfig().get("actionPath"));

            assertEquals("<strong>Modified Freemarker template content</strong>", extractedMockModel.getTemplate());

        } catch (Exception e) {
            throw e;
        } finally {
            this._pageModelDAO.deleteModel(testPageModelCode);
            models = this._pageModelDAO.loadModels();
            assertTrue(!models.containsKey(testPageModelCode));
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

    private void init() throws Exception {
        try {
            this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
            this._pageModelDAO = new PageModelDAO();
            this._pageModelDAO.setDataSource(dataSource);
            this._pageModelDAO.setWidgetTypeManager(this._widgetTypeManager);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private PageModelDAO _pageModelDAO;
    private IWidgetTypeManager _widgetTypeManager;

}
