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
package org.entando.entando.web.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.user.annotation.GroupOrRoleNotBlank;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;

/**
 *
 * @author paddeo
 */
public class UserAuthorityValidator implements
        ConstraintValidator<GroupOrRoleNotBlank, UserAuthoritiesRequest> {

    @Override
    public void initialize(GroupOrRoleNotBlank a) {
    }

    @Override
    public boolean isValid(UserAuthoritiesRequest req, ConstraintValidatorContext cvc) {
        return req.stream().anyMatch(authority -> !(StringUtils.isBlank(authority.getGroup()) && StringUtils.isBlank(authority.getRole())));
    }

}
