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
public class TestDateAttributeFilterAction extends TestAbstractAttributeFilterAction {
	
	public void testFailureAddDateValueFilter_1() throws Throwable {
		String result = this.executeSaveValueDateFilter(IContentListFilterAction.NO_DATE_FILTER, "");//Opzione nessuna Data inserita
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> dateValueErrors = fieldsErrors.get("dateValueType");
		assertEquals(1, dateValueErrors.size());
	}
	
	public void testFailureAddDateValueFilter_2() throws Throwable {
		String result = this.executeSaveValueDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "");//Opzione Data inserita
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> dateValueErrors = fieldsErrors.get("dateValue");
		assertEquals(1, dateValueErrors.size());
	}
	
	public void testFailureAddDateValueFilter_3() throws Throwable {
		String result = this.executeSaveValueDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "wrongFormat");//Opzione Data inserita
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> dateValueErrors = fieldsErrors.get("dateValue");
		assertEquals(2, dateValueErrors.size());//Errore in conversione e messaggio campo obbligatorio
	}
	
	public void testSuccessAddDateValueFilter() throws Throwable {
		String result = this.executeSaveValueDateFilter(IContentListFilterAction.CURRENT_DATE_FILTER, "");//Opzione Data corrente
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveValueDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972");//Opzione Data inserita
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeSaveValueDateFilter(int dateValueType, String value) throws Throwable {
		Map<String, String> params = this.getBaseParams("NEW");
		params.put("filterKey", "Date");
		params.put("attributeFilter", "true");
		params.put("filterTypeId", String.valueOf(IContentListFilterAction.DATE_ATTRIBUTE_FILTER_TYPE));//Tipo Data
		params.put("filterOptionId", String.valueOf(IContentListFilterAction.VALUE_FILTER_OPTION));//Opzione "Value"
		params.put("dateValueType", String.valueOf(dateValueType));
		params.put("dateValue", value);
		String result = this.executeAddFilter("admin", params, "saveDateFilter");
		return result;
	}
	
	public void testFailureAddRangeDateFilter_1() throws Throwable {
		String result = this.executeSaveRangeDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "", //Data Start inserita
				IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972");// Data End Inserita
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> dateValueErrors = fieldsErrors.get("dateStart");
		assertEquals(1, dateValueErrors.size());//required dateStart
	}
	
	public void testFailureAddRangeDateFilter_2() throws Throwable {
		String result = this.executeSaveRangeDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972", //Data Start inserita
				IContentListFilterAction.INSERTED_DATE_FILTER, "21/04/1972");// Data End Inserita
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> dateValueErrors = fieldsErrors.get("dateEnd");
		assertEquals(1, dateValueErrors.size());//dateStart.after(dateEnd)
	}
	
	public void testFailureAddRangeDateFilter_3() throws Throwable {
		String result = this.executeSaveRangeDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972", //Data Start inserita
				IContentListFilterAction.INSERTED_DATE_FILTER, "wrongFormat");// Data End Inserita
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldsErrors = action.getFieldErrors();
		assertEquals(1, fieldsErrors.size());
		List<String> dateValueErrors = fieldsErrors.get("dateEnd");
		assertEquals(2, dateValueErrors.size());//dateEnd Wrong Format and dateEnd required
	}
	
	public void testSuccessAddRangeDateFilter() throws Throwable {
		String result = this.executeSaveRangeDateFilter(IContentListFilterAction.NO_DATE_FILTER, "", //Nessuna dateStart inserita
				IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/2002");//Data End Inserita
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972", //Data Start inserita
				IContentListFilterAction.NO_DATE_FILTER, "");//Nessuna dateEnd inserita
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972", //Data Start inserita
				IContentListFilterAction.CURRENT_DATE_FILTER, "");//Data Corrente
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeDateFilter(IContentListFilterAction.NO_DATE_FILTER, "", //Nessuna dateStart inserita
				IContentListFilterAction.CURRENT_DATE_FILTER, "");//Data Corrente
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeSaveRangeDateFilter(IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/1972", //Data Start inserita
				IContentListFilterAction.INSERTED_DATE_FILTER, "25/09/2002");//Data End Inserita
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeSaveRangeDateFilter(int dateStartType, String dateStart, int dateEndType, String dateEnd) throws Throwable {
		Map<String, String> params = this.getBaseParams("NEW");
		params.put("filterKey", "Date");
		params.put("attributeFilter", "true");
		params.put("filterTypeId", String.valueOf(IContentListFilterAction.DATE_ATTRIBUTE_FILTER_TYPE));//Tipo Data
		params.put("filterOptionId", String.valueOf(IContentListFilterAction.RANGE_FILTER_OPTION));//Opzione "Range"
		params.put("dateStartType", String.valueOf(dateStartType));
		params.put("dateEndType", String.valueOf(dateEndType));
		params.put("dateStart", dateStart);
		params.put("dateEnd", dateEnd);
		String result = this.executeAddFilter("admin", params, "saveDateFilter");
		return result;
	}
	
}
