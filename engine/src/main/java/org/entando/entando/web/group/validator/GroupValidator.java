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
package org.entando.entando.web.group.validator;

import com.agiletec.aps.system.services.group.IGroupManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractValidator;
import org.entando.entando.web.group.model.GroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class GroupValidator extends AbstractValidator {

    public static final String ERRCODE_GROUP_NOT_FOUND = "1";
    public static final String ERRCODE_GROUP_ALREADY_EXISTS = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";
    public static final String ERRCODE_CANNOT_DELETE_RESERVED_GROUP = "4";
    public static final String ERRCODE_GROUP_REFERENCES = "5";

    @Autowired
    private IGroupManager groupManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return GroupRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GroupRequest request = (GroupRequest) target;
        String groupCode = request.getCode();
        if (null != groupManager.getGroup(groupCode)) {
            errors.reject(ERRCODE_GROUP_ALREADY_EXISTS, new String[]{groupCode}, "group.exists");
        }
    }

    public void validateBodyName(String groupCode, GroupRequest groupRequest, Errors errors) {
        if (!StringUtils.equals(groupCode, groupRequest.getCode())) {
            errors.rejectValue("name", ERRCODE_URINAME_MISMATCH, new String[]{groupCode, groupRequest.getName()}, "group.code.mismatch");
        }
    }

}
