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
import org.entando.entando.web.common.validator.AbstractValidator;
import org.entando.entando.web.guifragment.model.GuiFragmentRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class GuiFragmentValidator extends AbstractValidator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //GET //PUT
    public static final String ERRCODE_FRAGMENT_DOES_NOT_EXISTS = "1";

    //POST
    public static final String ERRCODE_FRAGMENT_ALREADY_EXISTS = "3";
    public static final String ERRCODE_FRAGMENT_INVALID_CODE = "1";
    //POST PUT
    public static final String ERRCODE_FRAGMENT_INVALID_GUI_CODE = "2";

    //PUT
    public static final String ERRCODE_URINAME_MISMATCH = "2";

    //DELETE
    public static final String ERRCODE_FRAGMENT_REFERENCES = "1";
    public static final String ERRCODE_FRAGMENT_LOCKED = "2";

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
                errors.rejectValue("code", ERRCODE_FRAGMENT_ALREADY_EXISTS, new String[]{code}, "guifragment.exists");
            } else if (code.length() > 50) {
                errors.rejectValue("code", ERRCODE_FRAGMENT_INVALID_CODE, new String[]{}, "guifragment.code.invalid");
            } else if (!code.matches("^[a-zA-Z0-9_]*$")) {
                errors.rejectValue("code", ERRCODE_FRAGMENT_INVALID_CODE, new String[]{}, "guifragment.code.invalid");
            }
            this.validateGuiCode(request, errors);
        } catch (Exception e) {
            logger.error("Error extracting fragment {}", code, e);
            throw new RestServerError("error extracting fragment", e);
        }
    }

    public int validateBody(String fragmentCode, GuiFragmentRequestBody request, Errors errors) {
        if (!StringUtils.equals(fragmentCode, request.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{fragmentCode, request.getCode()}, "guifragment.code.mismatch");
            return 400;
        }
        return this.validateGuiCode(request, errors);
    }

    private int validateGuiCode(GuiFragmentRequestBody request, Errors errors) {
        if (StringUtils.isEmpty(request.getGuiCode())) {
            errors.rejectValue("guiCode", ERRCODE_FRAGMENT_INVALID_GUI_CODE, new String[]{}, "guifragment.gui.notBlank");
            return 400;
        }
        return 0;
    }

}
