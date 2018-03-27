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
package org.entando.entando.aps.system.services.role.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.role.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

public class RoleDto {

    private String code;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Boolean> permissions = new HashMap<>();

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

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public RoleDto() {

    }

    public RoleDto(Role src) {
        this.setCode(src.getName());
        this.setName(src.getDescription());
    }

    public RoleDto(Role src, List<String> permissionCodes) {
        this(src);
        if (null == src.getPermissions()) {
            permissionCodes.forEach(i -> this.getPermissions().put(i, false));
        } else {
            permissionCodes.forEach(i -> this.getPermissions().put(i, src.getPermissions().contains(i)));
        }
    }
}
