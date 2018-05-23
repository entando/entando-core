/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.group.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class GroupRequest {

    @Size(max = 20, message = "string.size.invalid")
    @NotBlank(message = "group.code.notBlank")
    private String code;

    @Size(max = 50, message = "string.size.invalid")
    @NotBlank(message = "group.name.notBlank")
    private String name;

    public GroupRequest() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
