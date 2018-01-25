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
package com.agiletec.aps.system.services.role.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IPermissionDAO;
import com.agiletec.aps.system.services.role.IRoleDAO;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import java.util.List;

/**
 * @author E.Santoboni
 */
public interface IRoleManagerCacheWrapper {

	public static final String ROLE_MANAGER_CACHE_NAME = "Entando_RoleManager";
	public static final String ROLE_CACHE_NAME_PREFIX = "RoleManager_role_";
	public static final String ROLE_CODES_CACHE_NAME = "RoleManager_roles";

	public static final String PERMISSION_CACHE_NAME_PREFIX = "RoleManager_permission_";
	public static final String PERMISSION_CODES_CACHE_NAME = "RoleManager_permissions";

	public void initCache(IRoleDAO roleDAO, IPermissionDAO permissionDAO) throws ApsSystemException;

	public List<Role> getRoles();
	
	public Role getRole(String code);
	
	public void addRole(Role role);

	public void updateRole(Role role);

	public void removeRole(Role role);
	
	public List<Permission> getPermissions();
	
	public Permission getPermission(String code);

	public void addPermission(Permission permission);

	public void updatePermission(Permission permission);

	public void removePermission(String permissionName);

}
