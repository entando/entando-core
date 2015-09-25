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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer;

import java.util.HashMap;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestBaseFilterAction extends ApsAdminBaseTestCase {
	
	public void testNewFilter() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Page/SpecialWidget/ListViewer", "newFilter");
		this.addParameter("pageCode", "homepage");
		this.addParameter("frame", "1");
		this.addParameter("widgetTypeCode", "content_viewer_list");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testSetFilterType() throws Throwable {
		this.setUserOnSession("admin");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("pageCode", "homepage");
		parameters.put("frame", "1");
		parameters.put("widgetTypeCode", "content_viewer_list");
		parameters.put("contentType", "EVN");
		parameters.put("filterKey", "Titolo");
		
		this.initAction("/do/jacms/Page/SpecialWidget/ListViewer", "setFilterType");
		this.addParameters(parameters);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		BaseFilterAction action = (BaseFilterAction) this.getAction();
		assertEquals(BaseFilterAction.TEXT_ATTRIBUTE_FILTER_TYPE, action.getFilterTypeId());
		
		parameters.put("filterKey", "DataInizio");
		this.initAction("/do/jacms/Page/SpecialWidget/ListViewer", "setFilterType");
		this.addParameters(parameters);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		action = (BaseFilterAction) this.getAction();
		assertEquals(BaseFilterAction.DATE_ATTRIBUTE_FILTER_TYPE, action.getFilterTypeId());
	}
	
	public void testSaveFilter() throws Throwable {
		this.setUserOnSession("admin");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("pageCode", "homepage");
		parameters.put("frame", "1");
		parameters.put("widgetTypeCode", "content_viewer_list");
		parameters.put("contentType", "EVN");
		parameters.put("filterKey", "created");
		parameters.put("attributeFilter", "false");
		parameters.put("order", "ASC");
		parameters.put("filters", "(order=DESC;attributeFilter=true;key=DataInizio)");
		
		this.initAction("/do/jacms/Page/SpecialWidget/ListViewer", "saveFilter");
		this.addParameters(parameters);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
}