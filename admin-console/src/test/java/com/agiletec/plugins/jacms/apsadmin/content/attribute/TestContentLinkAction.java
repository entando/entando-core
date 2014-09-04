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

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.ContentLinkAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentLinkAction extends AbstractBaseTestContentAction {

	public void testFindContent_1() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("ART1", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");//Contenuto del gruppo Free

		this.initContentAction("/do/jacms/Content/Link", "configContentLink", contentOnSessionMarker);
		this.addParameter("linkType", "3");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		ContentLinkAction action = (ContentLinkAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(14, contentIds.size());//Contenuti pubblici liberi o non liberi con free gruppo extra
		assertTrue(contentIds.contains("EVN25"));//Contenuto coach abilitato al gruppo free
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
	}

	public void testFindContent_2() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("ART120", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("admin", "ART120", "VediAnche", "it");//Contenuto del gruppo degli amministratori

		this.initContentAction("/do/jacms/Content/Link", "configContentLink", contentOnSessionMarker);
		this.addParameter("linkType", "3");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		ContentLinkAction action = (ContentLinkAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(23, contentIds.size());//Tutti i contenuti pubblici
	}

	public void testFindContent_3() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("ART102", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("editorCustomers", "ART102", "VediAnche", "it");//Contenuto del gruppo customers

		this.initContentAction("/do/jacms/Content/Link", "configContentLink", contentOnSessionMarker);
		this.addParameter("linkType", "3");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		ContentLinkAction action = (ContentLinkAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(19, contentIds.size());// Contenuti pubblici liberi, o del gruppo customers o altri con customers gruppo extra
		assertTrue(contentIds.contains("ART122"));//Contenuto del gruppo "administrators" abilitato al gruppo customers
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
		assertTrue(contentIds.contains("EVN25"));//Contenuto del gruppo "coach" abilitato al gruppo free
		assertTrue(contentIds.contains("ART111"));//Contenuto del gruppo "coach" abilitato al gruppo customers
	}

	public void testFindContent_4() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("EVN25", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("admin", "EVN25", "LinkCorrelati", "it");//Contenuto del gruppo coach

		this.initContentAction("/do/jacms/Content/Link", "configContentLink", contentOnSessionMarker);
		this.addParameter("linkType", "3");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		ContentLinkAction action = (ContentLinkAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(19, contentIds.size());// Contenuti pubblici liberi, o del gruppo coach o altri con coach gruppo extra
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo coach
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
		assertTrue(contentIds.contains("EVN25"));//Contenuto del gruppo "coach" abilitato al gruppo free
		assertTrue(contentIds.contains("ART111"));//Contenuto del gruppo "coach" abilitato al gruppo customers
	}

	public void testFailureJoinContentLink_1() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("ART1", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");

		this.initContentAction("/do/jacms/Content/Link", "joinContentLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("contentId");
		assertEquals(1, typeFieldErrors.size());
	}

	public void testFailureJoinContentLink_2() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("ART1", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");

		this.initContentAction("/do/jacms/Content/Link", "joinContentLink", contentOnSessionMarker);
		this.addParameter("contentId", "ART78");//CONTENUTO INESISTENTE
		String result = this.executeAction();
		assertEquals(BaseAction.FAILURE, result);
	}

	public void testFailureJoinContentLink_3() throws Throwable {
		this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");

		this.initAction("/do/jacms/Content/Link", "joinContentLink");
		this.addParameter("contentId", "ART179");//CONTENUTO NON PUBBLICO
		String result = this.executeAction();
		assertEquals(BaseAction.FAILURE, result);
	}

	public void testJoinContentLink_1() throws Throwable {
		String contentOnSessionMarker = this.extractSessionMarker("ART1", ApsAdminSystemConstants.EDIT);
		this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");

		this.initContentAction("/do/jacms/Content/Link", "joinContentLink", contentOnSessionMarker);
		this.addParameter("contentId", "ART1");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		Content content = this.getContentOnEdit(contentOnSessionMarker);
		LinkAttribute attribute = (LinkAttribute) content.getAttribute("VediAnche");
		SymbolicLink symbolicLink = attribute.getSymbolicLink();
		assertNotNull(symbolicLink);
		assertNull(symbolicLink.getPageDest());
		assertEquals("ART1", symbolicLink.getContentDest());
	}

	public void testJoinContentLink_2() throws Throwable {
		this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");
		this.initAction("/do/jacms/Content/Link", "joinContentLink");
		this.addParameter("contentId", "ART1");
		this.addParameter("contentOnPageType", "true");
		String result = this.executeAction();
		assertEquals("configContentOnPageLink", result);
	}

	private void initJoinLinkTest(String username, String contentId, String simpleLinkAttributeName, String langCode) throws Throwable {
		this.executeEdit(contentId, username);
		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, simpleLinkAttributeName);
		session.setAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM, langCode);
	}

}