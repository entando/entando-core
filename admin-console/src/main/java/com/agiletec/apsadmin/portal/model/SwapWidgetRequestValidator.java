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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.portal.PageConfigAction;
import com.opensymphony.xwork2.ActionSupport;

@Component
public class SwapWidgetRequestValidator {

	@Autowired
	private IPageManager pageManager;

	private IPage getPage(String pageCode) {
		IPage page = this.pageManager.getDraftPage(pageCode);
		return page;
	}


	public void validateRequest(SwapWidgetRequest swapWidgetRequest, PageConfigAction pageConfigAction) {
		int framesCount = pageConfigAction.getPage(swapWidgetRequest.getPageCode()).getDraftMetadata().getModel().getFrames().length;
		validate(swapWidgetRequest, pageConfigAction, framesCount);
	}

	protected  void validate(SwapWidgetRequest swapWidgetRequest, ActionSupport action, int framesCount) {
		if (null == swapWidgetRequest) {
			action.addActionError(action.getText("error.request.null"));
			return;
		}
		
		if (StringUtils.isBlank(swapWidgetRequest.getPageCode())) {
			action.addActionError(action.getText("error.request.pageCode.blank"));
			return;
		}
		String pageCode = swapWidgetRequest.getPageCode();
		IPage page = this.getPage(pageCode);
		if (null == page) {
			action.addActionError(action.getText("error.request.page.notFound"));
			return;
		}
		
		if (swapWidgetRequest.getSrc() == swapWidgetRequest.getDest()) {
			action.addActionError(action.getText("error.request.src.dest.equals"));
			return;
		}
		
		//array min max
		if (swapWidgetRequest.getSrc() < 0) {
			action.addActionError(action.getText("error.request.src.invalid"));
			return;
		}
		if (swapWidgetRequest.getDest() < 0) {
			action.addActionError(action.getText("error.request.dest.invalid"));
			return;
		}

		if (swapWidgetRequest.getSrc() > framesCount) {
			action.addActionError(action.getText("error.request.src.invalid"));
			return;
		}
	
		if (swapWidgetRequest.getDest() > framesCount) {
			action.addActionError(action.getText("error.request.dest.invalid"));
			return;
		}
		
		//src cannot be null
		if (null == page.getDraftWidgets()[swapWidgetRequest.getSrc()]) {			
			action.addActionError(action.getText("error.request.src.nullFrame"));
			return;
		}

	}

}
