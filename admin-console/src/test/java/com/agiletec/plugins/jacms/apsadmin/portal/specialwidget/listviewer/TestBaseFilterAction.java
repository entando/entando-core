/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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