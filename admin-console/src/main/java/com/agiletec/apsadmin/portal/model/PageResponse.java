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
package com.agiletec.apsadmin.portal.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.opensymphony.xwork2.ActionSupport;

public class PageResponse {

	private Map<String, List<String>> fieldErrors;
	private Collection<String> actionErrors;
	private Collection<String> actionMessages;
	private IPage page;
	private Map _references;

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
	
	public Map getReferences() {
		return _references;
	}
	public void setReferences(Map references) {
		this._references = references;
	}

	public IPage getPage() {
		return page;
	}

	public void setPage(IPage src) {
		if (null == src) {
			return;
		}
		Page page = new Page();
		page.setCode(src.getCode());
		page.setDraftMetadata(src.getDraftMetadata());
		page.setDraftWidgets(src.getDraftWidgets());
		page.setParentCode(page.getParentCode());
		page.setGroup(src.getGroup());
		this.page = page;
	}

	public PageResponse() {
		//
	}

	public PageResponse(ActionSupport actionSupport) {
		super();
		this.setFieldErrors(actionSupport.getFieldErrors());
		this.setActionErrors(actionSupport.getActionErrors());
		this.setActionMessages(actionSupport.getActionMessages());
	}
	
}
