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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class RoleManagerCacheWrapper extends AbstractCacheWrapper implements IRoleManagerCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(RoleManagerCacheWrapper.class);
	private static enum Action {ADD, UPDATE, DELETE}
	
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
		this.releaseCachedObjects(cache, ROLE_CODES_CACHE_NAME, ROLE_CACHE_NAME_PREFIX);
		this.releaseCachedObjects(cache, PERMISSION_CODES_CACHE_NAME, PERMISSION_CACHE_NAME_PREFIX);
	}

	private void releaseCachedObjects(Cache cache, String codesName, String codePrefix) {
		List<String> codes = (List<String>) this.get(cache, codesName, List.class);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				cache.evict(codePrefix + code);
			}
			cache.evict(codesName);
		}
	}

	protected void insertObjectsOnCache(Cache cache, Map<String, Role> roles, Map<String, Permission> permissions) {
		List<String> roleCodes = new ArrayList<String>();
		Iterator<String> iterRoles = roles.keySet().iterator();
		while (iterRoles.hasNext()) {
			String key = iterRoles.next();
			cache.put(ROLE_CACHE_NAME_PREFIX + key, roles.get(key));
			roleCodes.add(key);
		}
		cache.put(ROLE_CODES_CACHE_NAME, roleCodes);
		List<String> permissionCodes = new ArrayList<String>();
		Iterator<String> iterPermiss = permissions.keySet().iterator();
		while (iterPermiss.hasNext()) {
			String key = iterPermiss.next();
			cache.put(PERMISSION_CACHE_NAME_PREFIX + key, permissions.get(key));
			permissionCodes.add(key);
		}
		cache.put(PERMISSION_CODES_CACHE_NAME, permissionCodes);
	}
	
	@Override
	public List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, ROLE_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				roles.add(this.get(cache, ROLE_CACHE_NAME_PREFIX + code, Role.class));
			}
		}
		return roles;
	}
	
	@Override
	public Role getRole(String code) {
		return this.get(this.getCache(), ROLE_CACHE_NAME_PREFIX + code, Role.class);
	}
	
	@Override
	public void addRole(Role role) {
		this.manageRole(role, Action.ADD);
	}

	@Override
	public void updateRole(Role role) {
		this.manageRole(role, Action.UPDATE);
	}

	@Override
	public void removeRole(Role role) {
		this.manageRole(role, Action.DELETE);
	}
	
	private void manageRole(Role role, Action operation) {
		if (null == role) {
			return;
		}
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, ROLE_CODES_CACHE_NAME, List.class);
		if (Action.ADD.equals(operation)) {
			if (!codes.contains(role.getName())) {
				codes.add(role.getName());
				cache.put(ROLE_CODES_CACHE_NAME, codes);
			}
			cache.put(ROLE_CACHE_NAME_PREFIX + role.getName(), role);
		} else if (Action.UPDATE.equals(operation) && codes.contains(role.getName())) {
			cache.put(ROLE_CACHE_NAME_PREFIX + role.getName(), role);
		} else if (Action.DELETE.equals(operation)) {
			codes.remove(role.getName());
			cache.evict(ROLE_CACHE_NAME_PREFIX + role.getName());
			cache.put(ROLE_CODES_CACHE_NAME, codes);
		}
	}

	@Override
	public List<Permission> getPermissions() {
		List<Permission> permissions = new ArrayList<Permission>();
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, PERMISSION_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				permissions.add(this.get(cache, PERMISSION_CACHE_NAME_PREFIX + code, Permission.class));
			}
		}
		return permissions;
	}

	@Override
	public Permission getPermission(String code) {
		return this.get(this.getCache(), PERMISSION_CACHE_NAME_PREFIX + code, Permission.class);
	}
	
	@Override
	public void addPermission(Permission permission) {
		this.managePermission(permission, Action.ADD);
	}

	@Override
	public void updatePermission(Permission permission) {
		this.managePermission(permission, Action.UPDATE);
	}

	@Override
	public void removePermission(String permissionName) {
		Permission permission = this.getPermission(permissionName);
		this.managePermission(permission, Action.DELETE);
	}
	
	private void managePermission(Permission permission, Action operation) {
		if (null == permission) {
			return;
		}
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, PERMISSION_CODES_CACHE_NAME, List.class);
		if (Action.ADD.equals(operation)) {
			if (!codes.contains(permission.getName())) {
				codes.add(permission.getName());
				cache.put(PERMISSION_CODES_CACHE_NAME, codes);
			}
			cache.put(PERMISSION_CACHE_NAME_PREFIX + permission.getName(), permission);
		} else if (Action.UPDATE.equals(operation) && codes.contains(permission.getName())) {
			cache.put(PERMISSION_CACHE_NAME_PREFIX + permission.getName(), permission);
		} else if (Action.DELETE.equals(operation)) {
			codes.remove(permission.getName());
			cache.evict(PERMISSION_CACHE_NAME_PREFIX + permission.getName());
			cache.put(PERMISSION_CODES_CACHE_NAME, codes);
		}
	}

	@Override
	protected String getCacheName() {
		return ROLE_MANAGER_CACHE_NAME;
	}

}
