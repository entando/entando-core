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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

/**
 * @author E.Santoboni
 */
public class TestJaxbPageModel extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testLoadModel() throws Throwable {
		String testPageModelCode = "test_jabx_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			PageModel mockModel = this.createMockPageModel(testPageModelCode);
			this._pageModelManager.addPageModel(mockModel);
			PageModel extractedMockModel = this._pageModelManager.getPageModel(testPageModelCode);
			assertNotNull(extractedMockModel);
			
			String xml = this.createXml(mockModel);
			assertNotNull(xml);
			
			assertTrue(xml.contains("<description>Description of model test_jabx_pagemodel</description>"));
			assertTrue(xml.contains("<description>Freme 0</description>"));
			assertTrue(xml.contains("<description>Freme 1</description>"));
			assertTrue(xml.contains("<description>Freme 2</description>"));
			
		} catch (Exception e) {
			throw e;
		} finally {
			this._pageModelManager.deletePageModel(testPageModelCode);
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		}
	}
	
	protected String createXml(PageModel model) throws Throwable {
		JAXBContext context = JAXBContext.newInstance(PageModel.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter writer = new StringWriter();
		marshaller.marshal(model, writer);
		return writer.toString();
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
		frame1.setDescription("Freme 1");
		Widget defWidg1 = new Widget();
		defWidg1.setType(this._widgetTypeManager.getWidgetType("content_viewer_list"));
		ApsProperties props1 = new ApsProperties();
		props1.setProperty("contentType", "ART");
		defWidg1.setConfig(props1);
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
			this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
    private IPageModelManager _pageModelManager = null;
	private IWidgetTypeManager _widgetTypeManager;
	
}
