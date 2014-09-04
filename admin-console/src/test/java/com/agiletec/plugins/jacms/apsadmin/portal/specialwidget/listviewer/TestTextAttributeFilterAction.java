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
