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
package org.entando.entando.aps.system.services.role;

import java.util.List;

import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.role.model.RoleDto;


public class RoleDtoBuilder extends DtoBuilder<Role, RoleDto> {

    private IRoleManager roleManager;

    public IRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    protected RoleDto toDto(Role src) {
        return new RoleDto(src);
    }

    protected RoleDto toDto(Role src, List<String> permissionsCodes) {
        return new RoleDto(src, permissionsCodes);
    }

}
