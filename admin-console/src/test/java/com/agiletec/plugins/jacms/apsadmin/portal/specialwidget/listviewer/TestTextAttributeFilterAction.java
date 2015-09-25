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

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestTextAttributeFilterAction extends TestAbstractAttributeFilterAction {
	
	public void testFailureAddTextFilter() throws Throwable {
		Map<String, String> params = this.getBaseParams("NEW");
		params.put("filterKey", "Title");
		params.put("attributeFilter", "true");
		params.put("filterTypeId", String.valueOf(IContentListFilterAction.TEXT_ATTRIBUTE_FILTER_TYPE));//Tipo Stringa
		params.put("filterOptionId", String.valueOf(IContentListFilterAction.VALUE_FILTER_OPTION));//Opzione "Value"
		params.put("stringValue", "");
		String result = this.executeAddFilter("admin", params, "saveTextFilter");
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> stringValueErrors = fieldsErrors.get("stringValue");
		assertEquals(1, stringValueErrors.size());
	}
	
}
