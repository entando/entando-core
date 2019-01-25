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

import com.fasterxml.jackson.databind.JsonNode;
import org.entando.entando.aps.system.services.role.model.PermissionDto;
import org.entando.entando.aps.system.services.role.model.RoleDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.role.model.RoleRequest;

public interface IRoleService {

    public String BEAN_NAME = "RoleService";

    public PagedMetadata<RoleDto> getRoles(RestListRequest requestList);

    public RoleDto updateRole(RoleRequest roleRequest);

    public RoleDto addRole(RoleRequest roleRequest);

    public void removeRole(String roleCode);

    public RoleDto getRole(String roleCode);

    public RoleDto getPatchedRole(String roleCode, JsonNode jsonPatch);

    public PagedMetadata<UserDto> getRoleReferences(String roleCode, RestListRequest requestList);

    public PagedMetadata<PermissionDto> getPermissions(RestListRequest requestList);

}
