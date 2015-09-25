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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestIntroNewContentAction extends AbstractBaseTestContentAction {

	public void testOpenNew() throws Throwable {
		String result = this.executeOpenNew("admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executeOpenNew("pageManagerCoach");
		assertEquals("userNotAllowed", result);
	}

	private String executeOpenNew(String currentUserName) throws Throwable {
		this.initAction("/do/jacms/Content", "new");
		this.setUserOnSession(currentUserName);
		return this.executeAction();
	}

	public void testCreateNewVoid() throws Throwable {
		String contentTypeCode = "ART";
		Content prototype = this.getContentManager().createContentType(contentTypeCode);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(prototype, ApsAdminSystemConstants.ADD);

		this.initAction("/do/jacms/Content", "createNewVoid");
		this.setUserOnSession("admin");
		this.addParameter("contentTypeCode", contentTypeCode);
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		assertEquals(1, fieldErrors.get("contentDescription").size());
		assertEquals(1, fieldErrors.get("contentMainGroup").size());

		Content content = super.getContentOnEdit(contentOnSessionMarker);
		assertNull(content);

		this.initAction("/do/jacms/Content", "createNewVoid");
		this.setUserOnSession("admin");
		this.addParameter("contentTypeCode", contentTypeCode);
		this.addParameter("contentDescription", "Descrizione di prova");
		this.addParameter("contentMainGroup", Group.FREE_GROUP_NAME);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		content = super.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(content);
		assertEquals(contentTypeCode, content.getTypeCode());
	}

	public void testCreateNewVoid_2() throws Throwable {
		this.initAction("/do/jacms/Content", "createNewVoid");
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErros = action.getFieldErrors();
		assertEquals(3, fieldErros.size());
	}

	public void testCreateNewVoid_3() throws Throwable {
		this.initAction("/do/jacms/Content", "createNewVoid");
		this.setUserOnSession("admin");
		this.addParameter("contentTypeCode", "ART");
		this.addParameter("contentDescription", "");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErros = action.getFieldErrors();
		assertEquals(2, fieldErros.size());
		assertEquals(1, fieldErros.get("contentDescription").size());
		assertEquals(1, fieldErros.get("contentMainGroup").size());
	}

	public void testCreateNewVoid_4() throws Throwable {
		this.initAction("/do/jacms/Content", "createNewVoid");
		this.setUserOnSession("admin");
		this.addParameter("contentTypeCode", "ART");
		this.addParameter("contentDescription", "Description");
		this.addParameter("contentMainGroup", "invalidGroupCode");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErros = action.getFieldErrors();
		assertEquals(1, fieldErros.size());
		assertEquals(1, fieldErros.get("contentMainGroup").size());
	}

	public void testCreateNewVoid_5() throws Throwable {
		this.setUserOnSession("editorCustomers");
		this.initAction("/do/jacms/Content", "createNewVoid");
		this.addParameter("contentTypeCode", "ART");
		this.addParameter("contentDescription", "Description");
		this.addParameter("contentMainGroup", Group.FREE_GROUP_NAME);
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErros = action.getFieldErrors();
		assertEquals(1, fieldErros.size());
		assertEquals(1, fieldErros.get("contentMainGroup").size());

		this.initAction("/do/jacms/Content", "createNewVoid");
		this.addParameter("contentTypeCode", "ART");
		this.addParameter("contentDescription", "Description");
		this.addParameter("contentMainGroup", "customers");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

}