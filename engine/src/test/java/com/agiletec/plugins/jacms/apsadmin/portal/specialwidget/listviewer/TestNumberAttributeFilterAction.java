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
public class TestNumberAttributeFilterAction extends TestAbstractAttributeFilterAction {
	
	public void testFailureAddNumberValueFilter_1() throws Throwable {
		String result = this.executeSaveNumberValueFilter("");
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> numberValueErrors = fieldsErrors.get("numberValue");
		assertEquals(1, numberValueErrors.size());
	}
	
	public void testFailureAddNumberValueFilter_2() throws Throwable {
		String result = this.executeSaveNumberValueFilter("wrongFormat");
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> numberValueErrors = fieldsErrors.get("numberValue");
		assertEquals(2, numberValueErrors.size());//Errore in conversione e messaggio campo obbligatorio
	}
	
	public void testSuccessAddNumberValueFilter() throws Throwable {
		String result = this.executeSaveNumberValueFilter("9");
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeSaveNumberValueFilter(String numberValue) throws Throwable {
		Map<String, String> params = this.getBaseParams("ART");
		params.put("filterKey", "Date");
		params.put("attributeFilter", "true");
		params.put("filterTypeId", String.valueOf(IContentListFilterAction.NUMBER_ATTRIBUTE_FILTER_TYPE));//Tipo Number
		params.put("filterOptionId", String.valueOf(IContentListFilterAction.VALUE_FILTER_OPTION));//Opzione "Value"
		params.put("numberValue", numberValue);
		String result = this.executeAddFilter("admin", params, "saveNumberFilter");
		return result;
	}
	
	public void testFailureAddRangeNumberFilter_1() throws Throwable {
		String result = this.executeSaveRangeNumberFilter("", "");
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> numberValueErrors = fieldsErrors.get("numberEnd");
		assertEquals(1, numberValueErrors.size());//richiesto uno dei campi
	}
	
	public void testFailureAddRangeNumberFilter_2() throws Throwable {
		String result = this.executeSaveRangeNumberFilter("56", "6");
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> numberValueErrors = fieldsErrors.get("numberEnd");
		assertEquals(1, numberValueErrors.size());//numberStart > numberEnd
	}
	
	public void testFailureAddRangeNumberFilter_3() throws Throwable {
		String result = this.executeSaveRangeNumberFilter("6", "wrongFormat");
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> numberValueErrors = fieldsErrors.get("numberEnd");
		assertEquals(1, numberValueErrors.size());//numberEnd wrong Format
	}
	
	public void testSuccesAddRangeNumberFilter() throws Throwable {
		String result = this.executeSaveRangeNumberFilter("6", "9");
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeNumberFilter("9", "9");
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeNumberFilter("6", "");
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeNumberFilter("", "90");
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeSaveRangeNumberFilter(String numberStart, String numberEnd) throws Throwable {
		Map<String, String> params = this.getBaseParams("ART");
		params.put("filterKey", "Numero");
		params.put("attributeFilter", "true");
		params.put("filterTypeId", String.valueOf(IContentListFilterAction.NUMBER_ATTRIBUTE_FILTER_TYPE));//Tipo Number
		params.put("filterOptionId", String.valueOf(IContentListFilterAction.RANGE_FILTER_OPTION));//Opzione "Range"
		params.put("numberStart", numberStart);
		params.put("numberEnd", numberEnd);
		String result = this.executeAddFilter("admin", params, "saveNumberFilter");
		return result;
	}
	
}
