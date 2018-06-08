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
package org.entando.entando.web.label;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class LabelValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_LABELGROUP_NOT_FOUND = "1";

    public static final String ERRCODE_LABELGROUP_EXISTS = "1";

    public static final String ERRCODE_URINAME_MISMATCH = "2";

    public static final String ERRCODE_LABELGROUP_LANGS_DEFAULT_LANG_REQUIRED = "2";

    public static final String ERRCODE_LABELGROUP_LANGS_NOT_ACTIVE_LANG = "3";

    public static final String ERRCODE_LABELGROUP_LANGS_INVALID_LANG = "4";

    public static final String ERRCODE_LABELGROUP_LANGS_TEXT_REQUIRED = "4";

    @Override
    public boolean supports(Class<?> paramClass) {
        return LabelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateBodyName(String labelCode, LabelRequest labelRequest, Errors errors) {
        if (!StringUtils.equals(labelCode, labelRequest.getKey())) {
            errors.rejectValue("key", ERRCODE_URINAME_MISMATCH, new String[]{labelCode, labelRequest.getKey()}, "labelRequest.key.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "key";
    }
}
