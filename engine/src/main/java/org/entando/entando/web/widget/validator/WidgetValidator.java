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
package org.entando.entando.web.widget.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class WidgetValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_WIDGET_NOT_FOUND = "1";

    public static final String ERRCODE_WIDGET_ALREADY_EXISTS = "1";

    public static final String ERRCODE_WIDGET_GROUP_INVALID = "2";

    public static final String ERRCODE_WIDGET_DOES_NOT_EXISTS = "1";
    public static final String ERRCODE_URINAME_MISMATCH = "3";

    public static final String ERRCODE_OPERATION_FORBIDDEN_LOCKED = "1";
    public static final String ERRCODE_CANNOT_DELETE_USED_PAGES = "2";

    public static final String ERRCODE_NOT_BLANK = "52";

    @Override
    public boolean supports(Class<?> paramClass) {
        return WidgetRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (StringUtils.isEmpty(((WidgetRequest) target).getCustomUi())) {
            errors.rejectValue("customUi", ERRCODE_NOT_BLANK, new String[]{}, "widgettype.customUi.notBlank");
        }
    }

    public void validateWidgetCode(String widgetCode, WidgetRequest widgetRequest, Errors errors) {
        if (!StringUtils.equals(widgetCode, widgetRequest.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{widgetCode, widgetRequest.getCode()}, "widgettype.code.mismatch");
        }
    }

}
