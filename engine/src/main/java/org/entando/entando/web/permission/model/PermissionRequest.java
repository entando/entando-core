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
package org.entando.entando.web.permission.model;

import javax.validation.constraints.Size;

import org.entando.entando.aps.system.services.role.model.PermissionDto;
import org.hibernate.validator.constraints.NotBlank;

public class PermissionRequest extends PermissionDto {

    @Override
    @Size(max = 30, message = "string.size.invalid")
    @NotBlank(message = "permission.code.notBlank")
    public String getCode() {
        return super.getCode();
    }

    @Override
    @Size(max = 59, message = "string.size.invalid")
    @NotBlank(message = "permission.descr.notBlank")
    public String getDescr() {
        return super.getDescr();
    }

}
