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
package com.agiletec.aps.system.services.group.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class GroupManagerCacheWrapper extends AbstractCacheWrapper implements IGroupManagerCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(GroupManagerCacheWrapper.class);

	@Override
	public void initCache(IGroupDAO groupDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, Group> groups = groupDAO.loadGroups();
			this.insertObjectsOnCache(cache, groups);
		} catch (Throwable t) {
			_logger.error("Error loading groups", t);
			throw new ApsSystemException("Error loading groups", t);
		}
	}

	protected void releaseCachedObjects(Cache cache) {
		this.releaseCachedObjects(cache, GROUP_CODES_CACHE_NAME, GROUP_CACHE_NAME_PREFIX);
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

	protected void insertObjectsOnCache(Cache cache, Map<String, Group> groups) {
		List<String> groupCodes = new ArrayList<String>();
		Iterator<String> iterRoles = groups.keySet().iterator();
		while (iterRoles.hasNext()) {
			String key = iterRoles.next();
			cache.put(GROUP_CACHE_NAME_PREFIX + key, groups.get(key));
			groupCodes.add(key);
		}
		cache.put(GROUP_CODES_CACHE_NAME, groupCodes);
	}

	@Override
	public Map<String, Group> getGroups() {
		Map<String, Group> groups = new HashMap<String, Group>();
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, GROUP_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				groups.put(code, this.get(cache, GROUP_CACHE_NAME_PREFIX + code, Group.class));
			}
		}
		return groups;
	}

	@Override
	public Group getGroup(String code) {
		return this.get(this.getCache(), GROUP_CACHE_NAME_PREFIX + code, Group.class);
	}

	@Override
	public void addGroup(Group group) {
		this.manage(group, Action.ADD);
	}

	@Override
	public void updateGroup(Group group) {
		this.manage(group, Action.UPDATE);
	}

	@Override
	public void removeGroup(Group group) {
		this.manage(group, Action.DELETE);
	}

	private void manage(Group group, Action operation) {
		if (null == group) {
			return;
		}
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, GROUP_CODES_CACHE_NAME, List.class);
		if (Action.ADD.equals(operation)) {
			if (!codes.contains(group.getName())) {
				codes.add(group.getName());
				cache.put(GROUP_CODES_CACHE_NAME, codes);
			}
			cache.put(GROUP_CACHE_NAME_PREFIX + group.getName(), group);
		} else if (Action.UPDATE.equals(operation) && codes.contains(group.getName())) {
			cache.put(GROUP_CACHE_NAME_PREFIX + group.getName(), group);
		} else if (Action.DELETE.equals(operation)) {
			codes.remove(group.getName());
			cache.evict(GROUP_CACHE_NAME_PREFIX + group.getName());
			cache.put(GROUP_CODES_CACHE_NAME, codes);
		}
	}

	@Override
	protected String getCacheName() {
		return GROUP_MANAGER_CACHE_NAME;
	}

}
