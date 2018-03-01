/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.guifragment.validator;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.web.guifragment.model.GuiFragmentRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GuiFragmentValidator implements Validator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String ERRCODE_FRAGMENT_ALREADY_EXISTS = "1";
	public static final String ERRCODE_URINAME_MISMATCH = "2";

	public static final String ERRCODE_FRAGMENT_REFERENCES = "4";

	@Autowired
	private IGuiFragmentManager guiFragmentManager;

	@Override
	public boolean supports(Class<?> paramClass) {
		return GuiFragmentRequestBody.class.equals(paramClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GuiFragmentRequestBody request = (GuiFragmentRequestBody) target;
		String code = request.getCode();
		try {
			if (null != this.guiFragmentManager.getGuiFragment(code)) {
				errors.reject(ERRCODE_FRAGMENT_ALREADY_EXISTS, new String[]{code}, "guifragment.exists");
			}
		} catch (Exception e) {
			logger.error("Error extracting fragment {}", code, e);
			throw new RestServerError("error extracting fragment", e);
		}
	}

	public void validateBodyName(String fragmentCode, GuiFragmentRequestBody guiFragmentRequest, Errors errors) {
		if (!StringUtils.equals(fragmentCode, guiFragmentRequest.getCode())) {
			errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{fragmentCode, guiFragmentRequest.getCode()}, "guifragment.code.mismatch");
		}
	}

}
