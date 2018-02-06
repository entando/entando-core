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
package org.entando.entando.apsadmin.portal.rs.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.apsadmin.portal.rs.model.SwapWidgetRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.portal.PageConfigAction;
import com.opensymphony.xwork2.ActionSupport;

@Component
@Qualifier(ISwapWidgetRequestValidator.BEAN_NAME)
public class SwapWidgetRequestValidator implements ISwapWidgetRequestValidator {

	@Override
	public void validateRequest(SwapWidgetRequest swapWidgetRequest, PageConfigAction pageConfigAction) {
		if (null == swapWidgetRequest) {
			pageConfigAction.addActionError(pageConfigAction.getText("error.request.null"));
			return;
		}
		if (StringUtils.isBlank(swapWidgetRequest.getPageCode())) {
			pageConfigAction.addActionError(pageConfigAction.getText("error.request.pageCode.blank"));
			return;
		}
		IPage page = pageConfigAction.getPage(swapWidgetRequest.getPageCode());
		if (null == page) {
			pageConfigAction.addActionError(pageConfigAction.getText("error.request.page.notFound"));
		} else {
			int framesCount = page.getMetadata().getModel().getFrames().length;
			validate(page, swapWidgetRequest, pageConfigAction, framesCount);
		}
	}

	protected void validate(IPage page, SwapWidgetRequest swapWidgetRequest, ActionSupport action, int framesCount) {
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
		if (null == page.getWidgets()[swapWidgetRequest.getSrc()]) {
			action.addActionError(action.getText("error.request.src.nullFrame"));
			return;
		}
	}

}
