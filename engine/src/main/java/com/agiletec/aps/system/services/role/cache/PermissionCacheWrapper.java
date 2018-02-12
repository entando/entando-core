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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.agiletec.aps.system.common.AbstractGenericCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IPermissionDAO;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;

/**
 * @author E.Santoboni
 */
public class PermissionCacheWrapper extends AbstractGenericCacheWrapper<Permission> implements IPermissionCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(PermissionCacheWrapper.class);

	@Override
	public void initCache(IPermissionDAO permissionDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, Permission> permissions = permissionDAO.loadPermissions();
			this.insertObjectsOnCache(cache, permissions);
		} catch (Throwable t) {
			_logger.error("Error loading permissions", t);
			throw new ApsSystemException("Error loading permissions", t);
		}
	}

	@Override
	public List<Permission> getPermissions() {
		Map<String, Permission> map = super.getObjectMap();
		return new ArrayList<>(map.values());
	}

	@Override
	public Permission getPermission(String code) {
		return this.get(this.getCache(), PERMISSION_CACHE_NAME_PREFIX + code, Permission.class);
	}

	@Override
	public void addPermission(Permission permission) {
		this.manage(permission.getName(), permission, Action.ADD);
	}

	@Override
	public void updatePermission(Permission permission) {
		this.manage(permission.getName(), permission, Action.UPDATE);
	}

	@Override
	public void removePermission(String permissionName) {
		Permission permission = this.getPermission(permissionName);
		if (null != permission) {
			this.manage(permission.getName(), permission, Action.DELETE);
		}
	}

	@Override
	protected String getCacheName() {
		return IRoleManager.ROLE_MANAGER_CACHE_NAME;
	}

	@Override
	protected String getCodesCacheKey() {
		return PERMISSION_CODES_CACHE_NAME;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return PERMISSION_CACHE_NAME_PREFIX;
	}

}
