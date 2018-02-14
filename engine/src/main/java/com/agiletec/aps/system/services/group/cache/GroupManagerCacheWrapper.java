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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.agiletec.aps.system.common.AbstractGenericCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupDAO;

/**
 * @author E.Santoboni
 */
public class GroupManagerCacheWrapper extends AbstractGenericCacheWrapper<Group> implements IGroupManagerCacheWrapper {

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

	@Override
	public Map<String, Group> getGroups() {
		return super.getObjectMap();
	}

	@Override
	public Group getGroup(String code) {
		return this.get(this.getCache(), GROUP_CACHE_NAME_PREFIX + code, Group.class);
	}

	@Override
	public void addGroup(Group group) {
		this.add(group.getName(), group);
	}

	@Override
	public void updateGroup(Group group) {
		this.update(group.getName(), group);
	}

	@Override
	public void removeGroup(Group group) {
		this.remove(group.getName(), group);
	}

	@Override
	protected String getCodesCacheKey() {
		return GROUP_CODES_CACHE_NAME;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return GROUP_CACHE_NAME_PREFIX;
	}

	@Override
	protected String getCacheName() {
		return GROUP_MANAGER_CACHE_NAME;
	}

}
