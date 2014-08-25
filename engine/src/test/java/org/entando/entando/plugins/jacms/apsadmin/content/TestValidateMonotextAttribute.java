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
public class TestValidateMonotextAttribute extends AbstractTestContentAttribute {

	public void testValidate_Single_1() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			AttributeInterface monotext = (AttributeInterface) content.getAttribute("Monotext");
			String formFieldName = tracer.getFormFieldName(monotext);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "monotextValue");
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
			AttributeInterface monotext = (AttributeInterface) content.getAttribute("Monotext2");
			//Mail attribute (required, min=15, max=30, regex=**mailFormat**)
			String formFieldName = tracer.getFormFieldName(monotext);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "invalidMonotext2Value");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "ii@22.it");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "aabbccddeeffgghh112233@iillmmnnooppqq334455.it");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "aabbccdd@eeffgghhii.com");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);

		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}

	public void testValidate_MonoListElement() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			AttributeTracer tracer = this.getTracer();
			MonoListAttribute monolist = (MonoListAttribute) content.getAttribute("MonoLMonot");
			AttributeInterface attribute = monolist.addAttribute();
			String formFieldPrefix = "Monolist:" + attribute.getType() + ":";

			tracer.setLang(this.getLangManager().getDefaultLang());
			tracer.setListIndex(monolist.getAttributes().size() - 1);
			tracer.setListLang(this.getLangManager().getDefaultLang());
			tracer.setMonoListElement(true);
			tracer.setParentAttribute(monolist);

			String formFieldName = tracer.getFormFieldName(attribute);
			assertEquals(formFieldPrefix + "MonoLMonot_0", formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "MonoLMonotElement0Value");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);

			AttributeInterface attribute2 = monolist.addAttribute();
			tracer.setListIndex(monolist.getAttributes().size() - 1);
			String formFieldName2 = tracer.getFormFieldName(attribute2);
			assertEquals(formFieldPrefix + "MonoLMonot_1", formFieldName2);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
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
			AttributeTracer tracer = this.getTracer();
			ListAttribute list = (ListAttribute) content.getAttribute("ListMonot");
			AttributeInterface attribute = list.addAttribute("it");
			assertEquals(0, list.getAttributeList("en").size());
			assertEquals(1, list.getAttributeList("it").size());

			tracer.setLang(this.getLangManager().getLang("it"));
			tracer.setListIndex(list.getAttributeList("it").size() - 1);
			tracer.setListLang(this.getLangManager().getLang("it"));
			tracer.setListElement(true);
			tracer.setParentAttribute(list);

			String formFieldName = tracer.getFormFieldName(attribute);
			String formFieldPrefix = "List:" + attribute.getType() + ":";
			assertEquals(formFieldPrefix + "it_ListMonot_0", formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "ListMonotElement0Value");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);

			AttributeInterface attribute2 = list.addAttribute("it");
			tracer.setListIndex(list.getAttributes().size() - 1);
			formFieldName = tracer.getFormFieldName(attribute2);
			assertEquals(formFieldPrefix + "it_ListMonot_1", formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, formFieldName);

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
			AttributeInterface attribute = compositeAttribute.getAttribute("Monotext");

			tracer.setCompositeElement(true);
			tracer.setParentAttribute(compositeAttribute);

			String formFieldName = tracer.getFormFieldName(attribute);
			String formFieldPrefix = "Composite:" + attribute.getType() + ":";
			assertEquals(formFieldPrefix + "Composite_Monotext", formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "MonotextValue");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
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
			AttributeInterface attribute = compositeElement.getAttribute("Monotext");
			String monolistElementFieldPrefix = "Monolist:Composite:";
			String formFieldPrefix = monolistElementFieldPrefix + attribute.getType() + ":";

			tracer.setListIndex(monolist.getAttributes().size() - 1);
			tracer.setListLang(this.getLangManager().getDefaultLang());
			tracer.setMonoListElement(true);
			tracer.setCompositeElement(true);
			tracer.setParentAttribute(compositeElement);

			String formFieldName = tracer.getFormFieldName(attribute);
			assertEquals(formFieldPrefix + "MonoLCom_Monotext_0", formFieldName);

			String monolistElementName = tracer.getMonolistElementFieldName(compositeElement);
			assertEquals(monolistElementFieldPrefix + "MonoLCom_0", monolistElementName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(1, monolistElementName);

			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter(formFieldName, "MonotextValue");
			this.executeAction(Action.INPUT);
			this.checkFieldErrors(0, formFieldName);
		} catch (Throwable t) {
			this.deleteTestContent();
			throw t;
		}
	}

}
