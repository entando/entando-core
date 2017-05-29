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
package org.entando.entando.apsadmin.portal.rs.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.opensymphony.xwork2.ActionSupport;

public class JoinWidgetResponse extends AbstractPageResponse {

	private Map<String, List<String>> fieldErrors;
	private Collection<String> actionErrors;
	private Collection<String> actionMessages;
	private PageJO page;
	private String redirectLocation;

	public Map<String, List<String>> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, List<String>> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public Collection<String> getActionErrors() {
		return actionErrors;
	}

	public void setActionErrors(Collection<String> actionErrors) {
		this.actionErrors = actionErrors;
	}

	public Collection<String> getActionMessages() {
		return actionMessages;
	}

	public void setActionMessages(Collection<String> actionMessages) {
		this.actionMessages = actionMessages;
	}

	public PageJO getPage() {
		return page;
	}

	public void setPage(PageJO page) {
		this.page = page;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}

	public JoinWidgetResponse() {
		//
	}

	public JoinWidgetResponse(ActionSupport actionSupport, IPage draftPage, IPage onlinePage) {
		super();
		this.setFieldErrors(actionSupport.getFieldErrors());
		this.setActionErrors(actionSupport.getActionErrors());
		this.setActionMessages(actionSupport.getActionMessages());
		this.setPage(this.copyPage(draftPage, onlinePage));
	}

}
