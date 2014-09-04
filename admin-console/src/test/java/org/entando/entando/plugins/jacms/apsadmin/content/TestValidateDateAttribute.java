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
package org.entando.entando.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestValidateDateAttribute extends AbstractTestContentAttribute {
	
	public void testValidate_Single_1() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			AttributeInterface dateAttribute = (AttributeInterface) content.getAttribute("Date");
			String formFieldName = tracer.getFormFieldName(dateAttribute);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "dateValue");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "25/07/2012");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}
	
	public void testValidate_Single_2() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			AttributeInterface dateAttribute = (AttributeInterface) content.getAttribute("Date2");
			String formFieldName = tracer.getFormFieldName(dateAttribute);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "10/10/2011");//validation: end range 25/11/2010
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "10/10/2009");//validation: end range 25/11/2010
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("Date:Date", "10/12/2009");//validation: start range attribute "Date"
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}
	
	public void testValidate_MonolistElement() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			MonoListAttribute monolist = (MonoListAttribute) content.getAttribute("MonoLDate");
			AttributeInterface attribute = monolist.addAttribute();
			String formFieldPrefix = "Monolist:" + attribute.getType() + ":";
			
			tracer.setListIndex(monolist.getAttributes().size() - 1);
			tracer.setListLang(this.getLangManager().getDefaultLang());
			tracer.setMonoListElement(true);
			
			String formFieldName = tracer.getFormFieldName(attribute);
			assertEquals(formFieldPrefix + "MonoLDate_0", formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "dateValue");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "11/07/1982");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
			
			AttributeInterface attribute2 = monolist.addAttribute();
			tracer.setListIndex(monolist.getAttributes().size() - 1);
			String formFieldName2 = tracer.getFormFieldName(attribute2);
			assertEquals(formFieldPrefix + "MonoLDate_1", formFieldName2);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName2);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.addParameter(formFieldName2, "18/04/1971");
			this.checkFieldErrors(1, formFieldName2);
			
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}
	
	public void testValidate_ListElement() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracerIT = this.getTracer();
			ListAttribute list = (ListAttribute) content.getAttribute("ListDate");
			AttributeInterface attributeIT = list.addAttribute("it");
			assertEquals(0, list.getAttributeList("en").size());
			assertEquals(1, list.getAttributeList("it").size());
			
			tracerIT.setListIndex(list.getAttributeList("it").size() - 1);
			tracerIT.setListLang(this.getLangManager().getLang("it"));
			tracerIT.setListElement(true);
			
			String formFieldItName = tracerIT.getFormFieldName(attributeIT);
			String formFieldPrefix = "List:" + attributeIT.getType() + ":";
			assertEquals(formFieldPrefix + "it_ListDate_0", formFieldItName);
			
			AttributeTracer tracerEN = tracerIT.clone();
			tracerEN.setLang(this.getLangManager().getLang("en"));
			tracerEN.setListLang(this.getLangManager().getLang("en"));
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldItName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldItName, "ListDateElement0Value");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldItName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldItName, "26/11/2007");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldItName);
			
			AttributeInterface attribute2 = list.addAttribute("it");
			tracerIT.setListIndex(list.getAttributes().size() - 1);
			formFieldItName = tracerIT.getFormFieldName(attribute2);
			assertEquals(formFieldPrefix + "it_ListDate_1", formFieldItName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldItName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldItName, "26/11/2007");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldItName);
			
			AttributeInterface attributeEN = list.addAttribute("en");
			String formFieldEnName = tracerEN.getFormFieldName(attributeEN);
			assertEquals(formFieldPrefix + "en_ListDate_0", formFieldEnName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldEnName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldEnName, "06/07/1987");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldEnName);
			
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}
	
	public void testValidate_CompositeElement() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			CompositeAttribute compositeAttribute = (CompositeAttribute) content.getAttribute("Composite");
			AttributeInterface attribute = compositeAttribute.getAttribute("Date");
			
			tracer.setCompositeElement(true);
			tracer.setParentAttribute(compositeAttribute);
			
			String formFieldName = tracer.getFormFieldName(attribute);
			String formFieldPrefix = "Composite:" + attribute.getType() + ":";
			assertEquals(formFieldPrefix + "Composite_Date", formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "wrongDateValue");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "10/10/2011");//validation: end range 10/10/2010
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "10/10/2008");//validation: end range 10/10/2010
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("Date:Date", "10/10/2009");//validation: start range attribute "Date"
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}
	
	public void testValidate_MonolistCompositeElement() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			MonoListAttribute monolist = (MonoListAttribute) content.getAttribute("MonoLCom");
			CompositeAttribute compositeElement = (CompositeAttribute) monolist.addAttribute();
			AttributeInterface attribute = compositeElement.getAttribute("Date");
			String monolistElementFieldPrefix = "Monolist:Composite:";
			String formFieldPrefix = monolistElementFieldPrefix + attribute.getType() + ":";
			
			tracer.setListIndex(monolist.getAttributes().size() - 1);
			tracer.setListLang(this.getLangManager().getDefaultLang());
			tracer.setMonoListElement(true);
			tracer.setCompositeElement(true);
			tracer.setParentAttribute(compositeElement);
			
			String formFieldName = tracer.getFormFieldName(attribute);
			assertEquals(formFieldPrefix + "MonoLCom_Date_0", formFieldName);
			
			String monolistElementName = tracer.getMonolistElementFieldName(compositeElement);
			assertEquals(monolistElementFieldPrefix + "MonoLCom_0", monolistElementName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, monolistElementName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "wrongDateValue");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "24/10/1961");//validation: start range 10/10/1971
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "10/10/2008");//validation: start range 10/10/1971
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("Date:Date", "10/10/1999");//validation: end range attribute "Date"
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);
			
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}
	
}