package com.agiletec.apsadmin.portal.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteWidgetResponse {


	private Map<String, List<String>> fieldErrors;
	private Collection<String> actionErrors;
	private Collection<String> actionMessages;
	private IPage page;

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

	public DeleteWidgetResponse() {
		//
	}

	public DeleteWidgetResponse(ActionSupport actionSupport) {
		super();
		this.setFieldErrors(actionSupport.getFieldErrors());
		this.setActionErrors(actionSupport.getActionErrors());
		this.setActionMessages(actionSupport.getActionMessages());
	}
}
