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

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IPermissionDAO;
import com.agiletec.aps.system.services.role.IRoleDAO;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class RoleManagerCacheWrapper extends AbstractCacheWrapper implements IRoleManagerCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(RoleManagerCacheWrapper.class);

	@Override
	public void initCache(IRoleDAO roleDAO, IPermissionDAO permissionDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, Role> roles = roleDAO.loadRoles();
			Map<String, Permission> permissions = permissionDAO.loadPermissions();
			this.insertObjectsOnCache(cache, roles, permissions);
		} catch (Throwable t) {
			_logger.error("Error loading roles and permissions", t);
			throw new ApsSystemException("Error loading roles and permissions", t);
		}
	}

	protected void releaseCachedObjects(Cache cache) {

	}

	protected void insertObjectsOnCache(Cache cache, Map<String, Role> roles, Map<String, Permission> permissions) {

	}

	@Override
	protected String getCacheName() {
		return ROLE_MANAGER_CACHE_NAME;
	}

}
