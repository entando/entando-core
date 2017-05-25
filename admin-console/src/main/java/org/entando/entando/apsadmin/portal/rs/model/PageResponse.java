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

public class PageResponse extends AbstractPageResponse {

	public PageResponse() {
		//
	}

	public PageResponse(ActionSupport actionSupport, IPage draftPage, IPage onlinePage) {
		super();
		this.setFieldErrors(actionSupport.getFieldErrors());
		this.setActionErrors(actionSupport.getActionErrors());
		this.setActionMessages(actionSupport.getActionMessages());
		this.setPage(this.copyPage(draftPage, onlinePage));
	}

	public Map<String, List<String>> getFieldErrors() {
		return _fieldErrors;
	}

	public void setFieldErrors(Map<String, List<String>> fieldErrors) {
		this._fieldErrors = fieldErrors;
	}

	public Collection<String> getActionErrors() {
		return _actionErrors;
	}

	public void setActionErrors(Collection<String> actionErrors) {
		this._actionErrors = actionErrors;
	}

	public Collection<String> getActionMessages() {
		return _actionMessages;
	}

	public void setActionMessages(Collection<String> actionMessages) {
		this._actionMessages = actionMessages;
	}

	public Map getReferences() {
		return _references;
	}

	public void setReferences(Map references) {
		this._references = references;
	}

	public PageJO getPage() {
		return _page;
	}

	public void setPage(PageJO page) {
		this._page = page;
	}

	private Map<String, List<String>> _fieldErrors;
	private Collection<String> _actionErrors;
	private Collection<String> _actionMessages;
	private PageJO _page;
	private Map _references;

}
