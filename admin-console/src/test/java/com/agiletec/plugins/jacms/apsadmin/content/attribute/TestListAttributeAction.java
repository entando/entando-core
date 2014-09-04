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
package com.agiletec.plugins.jacms.apsadmin.content.attribute;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.entity.attribute.action.list.IListAttributeAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestListAttributeAction extends AbstractBaseTestContentAction {

	public void testAddListElement() throws Throwable {
		String contentOnSessionMarker = this.initEditContent();
		this.initContentAction("/do/jacms/Content", "addListElement", contentOnSessionMarker);
		this.addParameter("attributeName", "Autori");
		this.addParameter("listLangCode", "it");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		MonoListAttribute monoListAttribute = (MonoListAttribute) currentContent.getAttribute("Autori");
		List<AttributeInterface> attributes = monoListAttribute.getAttributes();
		String[] expected = {"Pippo", "Paperino", "Pluto", ""};
		this.verifyText(attributes, expected);
	}

	public void testMoveListElement() throws Throwable {
		String contentOnSessionMarker = this.initEditContent();
		this.initContentAction("/do/jacms/Content", "moveListElement", contentOnSessionMarker);
		this.addParameter("attributeName", "Autori");
		this.addParameter("elementIndex", "1");
		this.addParameter("listLangCode", "it");
		this.addParameter("movement", IListAttributeAction.MOVEMENT_UP_CODE);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		MonoListAttribute monoListAttribute = (MonoListAttribute) currentContent.getAttribute("Autori");
		List<AttributeInterface> attributes = monoListAttribute.getAttributes();
		String[] expected = {"Paperino", "Pippo", "Pluto"};
		this.verifyText(attributes, expected);

		this.initContentAction("/do/jacms/Content", "moveListElement", contentOnSessionMarker);
		this.addParameter("attributeName", "Autori");
		this.addParameter("elementIndex", "1");
		this.addParameter("listLangCode", "it");
		this.addParameter("movement", IListAttributeAction.MOVEMENT_DOWN_CODE);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		currentContent = this.getContentOnEdit(contentOnSessionMarker);
		monoListAttribute = (MonoListAttribute) currentContent.getAttribute("Autori");
		attributes = monoListAttribute.getAttributes();
		String[] expected2 = {"Paperino", "Pluto", "Pippo"};
		this.verifyText(attributes, expected2);
	}

	public void testRemoveListElement() throws Throwable {
		String contentOnSessionMarker = this.initEditContent();
		this.initContentAction("/do/jacms/Content", "removeListElement", contentOnSessionMarker);
		this.addParameter("attributeName", "Autori");
		this.addParameter("elementIndex", "1");
		this.addParameter("listLangCode", "it");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		MonoListAttribute monoListAttribute = (MonoListAttribute) currentContent.getAttribute("Autori");
		List<AttributeInterface> attributes = monoListAttribute.getAttributes();
		String[] expected = {"Pippo", "Pluto"};
		this.verifyText(attributes, expected);
	}

	private String initEditContent() throws Throwable {
		String contentId = "ART1";
		Content content = this.getContentManager().loadContent(contentId, false);
		this.executeEdit(contentId, "admin");
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		MonoListAttribute monoListAttribute = (MonoListAttribute) currentContent.getAttribute("Autori");
		List<AttributeInterface> attributes = monoListAttribute.getAttributes();
		String[] expected = {"Pippo", "Paperino", "Pluto"};
		this.verifyText(attributes, expected);
		return contentOnSessionMarker;
	}

	private void verifyText(List<AttributeInterface> attributes, String[] expected) {
		assertEquals(expected.length, attributes.size());
		for (int i=0; i<attributes.size(); i++) {
			ITextAttribute textAttribute = (ITextAttribute) attributes.get(i);
			assertEquals(expected[i], textAttribute.getText());
		}
	}

	//TODO FARE IL RESTO

}
