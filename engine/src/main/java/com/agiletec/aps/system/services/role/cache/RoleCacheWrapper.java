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
import com.agiletec.aps.system.services.role.IRoleDAO;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;

/**
 * @author E.Santoboni
 */
public class RoleCacheWrapper extends AbstractGenericCacheWrapper<Role> implements IRoleCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(RoleCacheWrapper.class);

	@Override
	public void initCache(IRoleDAO roleDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, Role> roles = roleDAO.loadRoles();
			this.insertObjectsOnCache(cache, roles);
		} catch (Throwable t) {
			_logger.error("Error loading roles", t);
			throw new ApsSystemException("Error loading roles", t);
		}
	}

	@Override
	public List<Role> getRoles() {
		Map<String, Role> map = super.getObjectMap();
		return new ArrayList<>(map.values());
	}

	@Override
	public Role getRole(String code) {
		return this.get(this.getCache(), ROLE_CACHE_NAME_PREFIX + code, Role.class);
	}

	@Override
	public void addRole(Role role) {
		this.manage(role.getName(), role, Action.ADD);
	}

	@Override
	public void updateRole(Role role) {
		this.manage(role.getName(), role, Action.UPDATE);
	}

	@Override
	public void removeRole(Role role) {
		this.manage(role.getName(), role, Action.DELETE);
	}

	@Override
	protected String getCodesCacheKey() {
		return ROLE_CODES_CACHE_NAME;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return ROLE_CACHE_NAME_PREFIX;
	}

	@Override
	protected String getCacheName() {
		return IRoleManager.ROLE_MANAGER_CACHE_NAME;
	}

}
