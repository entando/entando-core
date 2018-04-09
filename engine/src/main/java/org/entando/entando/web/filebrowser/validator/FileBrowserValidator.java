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
package org.entando.entando.web.filebrowser.validator;

import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author eu
 */
public class FileBrowserValidator extends AbstractPaginationValidator implements Validator {

    public static final String ERRCODE_FOLDER_DOES_NOT_EXIST = "1";

    @Override
    public boolean supports(Class<?> paramClass) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

}
