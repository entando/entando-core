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
package org.entando.entando.web.role.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.role.model.RoleDto;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.role.model.RoleRequest;
import org.springframework.validation.Errors;

public class RoleValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_ROLE_NOT_FOUND = "1";
    public static final String ERRCODE_ROLE_ALREADY_EXISTS = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";
    public static final String ERRCODE_PERMISSON_NOT_FOUND = "4";
    public static final String ERRCODE_ROLE_REFERENCES = "5";

    @Override
    public boolean supports(Class<?> paramClass) {
        return RoleRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateBodyName(String roleCode, RoleRequest roleRequest, Errors errors) {
        if (!StringUtils.equals(roleCode, roleRequest.getCode())) {
            errors.rejectValue("name", ERRCODE_URINAME_MISMATCH, new String[]{roleCode, roleRequest.getName()}, "role.code.mismatch");
        }
    }

}
