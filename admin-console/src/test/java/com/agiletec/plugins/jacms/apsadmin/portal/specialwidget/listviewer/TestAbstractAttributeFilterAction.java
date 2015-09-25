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

/**
 * @author E.Santoboni
 */
public abstract class TestAbstractAttributeFilterAction extends ApsAdminBaseTestCase {
	
	protected Map<String, String> getBaseParams(String contentTypeCode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "homepage");
		params.put("frame", "1");
		params.put("widgetTypeCode", "advancedListViewer");
		params.put("contentType", contentTypeCode);
		params.put("filters", "");
		return params;
	}
	
	protected String executeAddFilter(String username, Map<String, String> params, String actionName) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jacms/Page/SpecialWidget/ListViewer", actionName);
		this.addParameters(params);
		return this.executeAction();
	}
	
}