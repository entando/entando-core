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
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestLinkAttributeAction extends AbstractBaseTestContentAction {

	public void testChooseLink_1() throws Throwable {
		String contentId = "ART1";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.executeEdit(contentId, "admin");
		this.initContentAction("/do/jacms/Content", "chooseLink", contentOnSessionMarker);
		this.addParameter("attributeName", "VediAnche");
		this.addParameter("langCode", "it");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		HttpSession session = this.getRequest().getSession();
		assertEquals("VediAnche", session.getAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM));
		assertEquals("it", session.getAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM));
		assertNull(session.getAttribute(ILinkAttributeActionHelper.LIST_ELEMENT_INDEX_SESSION_PARAM));
		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		assertEquals("ART1", currentContent.getId());
		assertEquals("Articolo", currentContent.getDescr());
	}

	public void testChooseLink_2() throws Throwable {
		String contentId = "EVN191";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.executeEdit(contentId, "admin");
		this.initContentAction("/do/jacms/Content", "chooseLink", contentOnSessionMarker);
		this.addParameter("attributeName", "LinkCorrelati");
		this.addParameter("elementIndex", "0");
		this.addParameter("langCode", "it");
		String result = this.executeAction();
		assertEquals(BaseAction.FAILURE, result);//FALLIMENTO PER LISTA VUOTA

		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		MonoListAttribute monoListAttribute = (MonoListAttribute) currentContent.getAttribute("LinkCorrelati");
		List<AttributeInterface> attributes = monoListAttribute.getAttributes();
		assertEquals(0, attributes.size());

		this.initContentAction("/do/jacms/Content", "addListElement", contentOnSessionMarker);
		this.addParameter("attributeName", "LinkCorrelati");
		this.addParameter("listLangCode", "it");
		result = this.executeAction();
		assertEquals("chooseLink", result);

		currentContent = this.getContentOnEdit(contentOnSessionMarker);
		monoListAttribute = (MonoListAttribute) currentContent.getAttribute("LinkCorrelati");
		attributes = monoListAttribute.getAttributes();
		assertEquals(1, attributes.size());

		this.initContentAction("/do/jacms/Content", "chooseLink", contentOnSessionMarker);
		this.addParameter("attributeName", "LinkCorrelati");
		this.addParameter("elementIndex", "0");
		this.addParameter("langCode", "it");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		HttpSession session = this.getRequest().getSession();
		assertEquals("LinkCorrelati", session.getAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM));
		assertEquals("it", session.getAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM));
		assertNotNull(session.getAttribute(ILinkAttributeActionHelper.LIST_ELEMENT_INDEX_SESSION_PARAM));
		assertEquals(new Integer(0), session.getAttribute(ILinkAttributeActionHelper.LIST_ELEMENT_INDEX_SESSION_PARAM));
		currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		assertEquals("EVN191", currentContent.getId());
		assertEquals("Evento 1", currentContent.getDescr());
	}

	public void testRemoveLink() throws Throwable {
		String contentId = "ART102";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.executeEdit(contentId, "admin");

		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		LinkAttribute linkAttribute = (LinkAttribute) currentContent.getAttribute("VediAnche");
		assertNotNull(linkAttribute);
		SymbolicLink symbolicLink = linkAttribute.getSymbolicLink();
		assertNotNull(symbolicLink);
		assertEquals("ART111", symbolicLink.getContentDest());

		this.initContentAction("/do/jacms/Content", "removeLink", contentOnSessionMarker);
		this.addParameter("attributeName", "VediAnche");
		this.addParameter("langCode", "it");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		linkAttribute = (LinkAttribute) currentContent.getAttribute("VediAnche");
		assertNotNull(linkAttribute);
		symbolicLink = linkAttribute.getSymbolicLink();
		assertNull(symbolicLink);
	}

	public void testFailureChooseLinkType_1() throws Throwable {
		String contentId = "ART1";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.executeEdit(contentId, "admin");

		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "VediAnche");
		session.setAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM, "it");

		this.initContentAction("/do/jacms/Content/Link", "configLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("linkType");
		assertEquals(1, typeFieldErrors.size());

		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		assertEquals("ART1", currentContent.getId());
		assertEquals("Articolo", currentContent.getDescr());
	}

	public void testFailureChooseLinkType_2() throws Throwable {
		String contentId = "ART1";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.executeEdit(contentId, "admin");

		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "VediAnche");
		session.setAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM, "it");

		this.initContentAction("/do/jacms/Content/Link", "configLink", contentOnSessionMarker);
		this.addParameter("linkType", "0");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("linkType");
		assertEquals(1, typeFieldErrors.size());

		this.initContentAction("/do/jacms/Content/Link", "configLink", contentOnSessionMarker);
		this.addParameter("linkType", "4");
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		typeFieldErrors = fieldErrors.get("linkType");
		assertEquals(1, typeFieldErrors.size());

		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		assertEquals("ART1", currentContent.getId());
		assertEquals("Articolo", currentContent.getDescr());
	}

	public void testChooseLinkType() throws Throwable {
		String contentId = "ART1";
		String contentOnSessionMarker = this.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.executeEdit(contentId, "admin");

		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "VediAnche");
		session.setAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM, "it");

		this.initContentAction("/do/jacms/Content/Link", "configLink", contentOnSessionMarker);
		this.addParameter("linkType", "1");
		String result = this.executeAction();
		assertEquals("configUrlLink", result);

		this.initContentAction("/do/jacms/Content/Link", "configLink", contentOnSessionMarker);
		this.addParameter("linkType", "2");
		result = this.executeAction();
		assertEquals("configPageLink", result);

		this.initContentAction("/do/jacms/Content/Link", "configLink", contentOnSessionMarker);
		this.addParameter("linkType", "3");
		result = this.executeAction();
		assertEquals("configContentLink", result);

		Content currentContent = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(currentContent);
		assertEquals("ART1", currentContent.getId());
		assertEquals("Articolo", currentContent.getDescr());
	}

}