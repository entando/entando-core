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
package org.entando.entando.web.language.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.language.LanguageDto;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.language.model.LanguageRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class LanguageValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_LANGUAGE_CANNOT_DISABLE_DEFAULT = "1";
    public static final String ERRCODE_LANGUAGE_DOES_NOT_EXISTS = "2";

    @Override
    public boolean supports(Class<?> paramClass) {
        return LanguageRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

}
