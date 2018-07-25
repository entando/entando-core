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
package org.entando.entando.plugins.jacms.apsadmin.content;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentPreviewAction extends AbstractBaseTestContentAction {
	
	public void testPreviewNewContent() throws Throwable {
		String insertedDescr = "XXX Prova preview XXX";
		String contentTypeCode = "ART";
		Content prototype = this.getContentManager().createContentType(contentTypeCode);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(prototype, ApsAdminSystemConstants.ADD);

		String result = this.executeCreateNewVoid(contentTypeCode, insertedDescr, Content.STATUS_DRAFT,  Group.FREE_GROUP_NAME, "admin");
		assertEquals(Action.SUCCESS, result);

		Content content = this.getContentOnEdit(contentOnSessionMarker);
		ITextAttribute titleAttribute = (ITextAttribute) content.getAttribute("Titolo");
		assertNull(titleAttribute.getTextForLang("it"));
		assertEquals(content.getDescr(), insertedDescr);

		this.initContentAction("/do/jacms/Content", "preview", contentOnSessionMarker);
		this.addParameter("Text:it_Titolo", "Nuovo titolo di prova");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		content = this.getContentOnEdit(contentOnSessionMarker);
		titleAttribute = (ITextAttribute) content.getAttribute("Titolo");
		assertEquals("Nuovo titolo di prova", titleAttribute.getTextForLang("it"));
	}

	public void testPreviewContent() throws Throwable {
		String contentId = "EVN192";
		Content contentForTest = this.getContentManager().loadContent(contentId, true);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(contentForTest, ApsAdminSystemConstants.EDIT);

		String result = this.executeEdit(contentId, "admin");
		assertEquals(Action.SUCCESS, result);

		Content content = this.getContentOnEdit(contentOnSessionMarker);
		ITextAttribute titleAttribute = (ITextAttribute) content.getAttribute("Titolo");
		assertEquals("Titolo B - Evento 2", titleAttribute.getTextForLang("it"));

		this.initContentAction("/do/jacms/Content", "preview", contentOnSessionMarker);
		this.addParameter("mainGroup", Group.FREE_GROUP_NAME);
		this.addParameter("Text:it_Titolo", "Nuovo titolo di prova");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		content = this.getContentOnEdit(contentOnSessionMarker);
		titleAttribute = (ITextAttribute) content.getAttribute("Titolo");
		assertEquals("Nuovo titolo di prova", titleAttribute.getTextForLang("it"));
	}

	public void testExecutePreviewContent_1() throws Throwable {
		String contentId = "EVN192";
		Content contentForTest = this.getContentManager().loadContent(contentId, true);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(contentForTest, ApsAdminSystemConstants.EDIT);

		String result = this.executeEdit(contentId, "admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executePreviewPage("", contentOnSessionMarker);
		assertNull(result);
	}

	public void testExecutePreviewContent_2() throws Throwable {
		String contentId = "ART187";
		Content contentForTest = this.getContentManager().loadContent(contentId, true);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(contentForTest, ApsAdminSystemConstants.EDIT);

		String result = this.executeEdit(contentId, "admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executePreviewPage(null, contentOnSessionMarker);
		assertNull(result);
	}
	
	public void testExecutePreviewContent_3() throws Throwable {
		String contentId = "ART187";
		Content contentForTest = this.getContentManager().loadContent(contentId, true);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(contentForTest, ApsAdminSystemConstants.EDIT);

		String result = this.executeEdit(contentId, "admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executePreviewPage("pagina_2", contentOnSessionMarker);
		assertNull(result);
	}
	
	public void testFailureExecutePreviewContent() throws Throwable {
		String contentId = "ART187";
		Content contentForTest = this.getContentManager().loadContent(contentId, true);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(contentForTest, ApsAdminSystemConstants.EDIT);

		String result = this.executeEdit(contentId, "admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executePreviewPage("wrongPageCode", contentOnSessionMarker);
		assertEquals(Action.INPUT, result);

		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		assertNotNull(reqCtx);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("previewPageCode").size());
	}
	
	private String executePreviewPage(String pageDest, String contentOnSessionMarker) throws Throwable {
		this.initContentAction("/do/jacms/Content", "executePreview", contentOnSessionMarker);
		this.addParameter("previewPageCode", pageDest);
		return this.executeAction();
	}
	
}
