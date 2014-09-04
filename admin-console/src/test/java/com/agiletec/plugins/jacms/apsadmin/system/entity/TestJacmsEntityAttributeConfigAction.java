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
package com.agiletec.plugins.jacms.apsadmin.system.entity;

import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestJacmsEntityAttributeConfigAction extends ApsAdminBaseTestCase {
	
	public void testValidateAttribute_1() throws Throwable {
		this.executeEditEntityAttributePrototype("ART", JacmsSystemConstants.CONTENT_MANAGER, "Data");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "Data");
		this.addParameter("ognlValidationRule.expression", "date.after(new java.util.Date())");
		this.addParameter("ognlValidationRule.evalExpressionOnlyWhenAttributeIsValued", "false");
		this.addParameter("ognlValidationRule.errorMessage", "the date has to be after tomorrow");
		this.addParameter("ognlValidationRule.helpMessageKey", "HELP_LABEL_KEY");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testValidateAttribute_2() throws Throwable {
		this.executeEditEntityAttributePrototype("ART", JacmsSystemConstants.CONTENT_MANAGER, "Data");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "Data");
		this.addParameter("ognlValidationRule.expression", "date.after(new java.util.Date())");
		this.addParameter("ognlValidationRule.evalExpressionOnlyWhenAttributeIsValued", "false");
		this.addParameter("ognlValidationRule.errorMessage", "the date has to be after tomorrow");
		this.addParameter("ognlValidationRule.errorMessageKey", "errorMessageKey");
		this.addParameter("ognlValidationRule.helpMessage", "");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		assertEquals(1, fieldErrors.get("ognlValidationRule.expression").size());
		assertEquals(1, fieldErrors.get("ognlValidationRule.errorMessage").size());
	}
	
	public void testValidateAttribute_3() throws Throwable {
		this.executeEditEntityAttributePrototype("ART", JacmsSystemConstants.CONTENT_MANAGER, "Data");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "Data");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testValidateAttribute_4() throws Throwable {
		this.executeEditEntityAttributePrototype("ART", JacmsSystemConstants.CONTENT_MANAGER, "Data");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "Data");
		this.addParameter("rangeStartDate", "12/07/2010");
		this.addParameter("rangeEndDate", "12/08/2010");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testValidateAttribute_5() throws Throwable {
		this.executeEditEntityAttributePrototype("ART", JacmsSystemConstants.CONTENT_MANAGER, "Data");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "Data");
		this.addParameter("rangeStartDate", "12/0777/2010");
		this.addParameter("rangeEndDate", "gggggggg");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(2, action.getFieldErrors().size());
		assertEquals(1, action.getFieldErrors().get("rangeStartDate").size());
		assertEquals(1, action.getFieldErrors().get("rangeEndDate").size());
	}

	public void testValidateAttribute_6() throws Throwable {
		this.executeEditEntityAttributePrototype("ART", JacmsSystemConstants.CONTENT_MANAGER, "Data");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "Data");
		this.addParameter("rangeStartDate", "12/07/2010");
		this.addParameter("rangeEndDate", "12/04/2010");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(1, action.getFieldErrors().size());
		assertEquals(1, action.getFieldErrors().get("rangeEndDate").size());
	}
	
	public void testValidateAttribute_7() throws Throwable {
		this.executeEditEntityAttributePrototype("EVN", JacmsSystemConstants.CONTENT_MANAGER, "DataInizio");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "DataInizio");
		this.addParameter("rangeStartDate", "12/07/2004");
		this.addParameter("rangeEndDateAttribute", "DataFine");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testValidateAttribute_8() throws Throwable {
		this.executeEditEntityAttributePrototype("EVN", JacmsSystemConstants.CONTENT_MANAGER, "DataInizio");
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeTypeCode", "Date");
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", "DataInizio");
		this.addParameter("rangeStartDate", "12/07/2004");
		this.addParameter("rangeEndDate", "12/04/2010");
		this.addParameter("rangeEndDateAttribute", "DataFine");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(1, action.getFieldErrors().size());
		assertEquals(1, action.getFieldErrors().get("rangeEndDate").size());
	}
	
	private void executeEditEntityAttributePrototype(String entityTypeCode, String entityManagerName, String attributeName) throws Throwable {
		this.setUserOnSession("admin");
		String result = this.executeEditEntityPrototype(entityTypeCode, entityManagerName);
		assertEquals(Action.SUCCESS, result);
		this.initAction("/do/Entity/Attribute", "editAttribute");
		this.addParameter("entityManagerName", entityManagerName);
		this.addParameter("entityTypeCode", entityTypeCode);
		this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
		this.addParameter("attributeName", attributeName);
		String result1 = this.executeAction();
		assertEquals(Action.SUCCESS, result1);
	}
	
	private String executeEditEntityPrototype(String entityTypeCode, String entityManagerName) throws Throwable {
		this.initAction("/do/jacms/Entity", "editEntityType");
		this.addParameter("entityManagerName", entityManagerName);
		this.addParameter("entityTypeCode", entityTypeCode);
		return this.executeAction();
	}
	
}
