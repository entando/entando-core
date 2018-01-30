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
package com.agiletec.aps.system.services.cache;

import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import org.entando.entando.aps.system.services.cache.CacheInfoManager;

/**
 * Manager of the System Cache
 *
 * @author E.Santoboni
 */
public class OldCacheManager extends AbstractService implements ICacheManager {

	private static final Logger _logger = LoggerFactory.getLogger(OldCacheManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} (cache service initialized) ready", this.getClass().getName());
	}

	@Override
	public void flushAll() {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).flushAll(ICacheInfoManager.DEFAULT_CACHE_NAME);
		}
	}

	@Override
	public void flushEntry(String key) {
		this.getCacheInfoManager().flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, key);
	}

	@Override
	public void flushGroup(String group) {
		this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, group);
	}

	@Override
	@Deprecated
	public Object getFromCache(String key, int myRefreshPeriod) {
		return this.getFromCache(key);
	}

	@Override
	public Object getFromCache(String key) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			return ((CacheInfoManager) this.getCacheInfoManager()).getFromCache(ICacheInfoManager.DEFAULT_CACHE_NAME, key);
		}
		return null;
	}

	@Override
	public void putInCache(String key, Object obj, String[] groups) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).putInCache(ICacheInfoManager.DEFAULT_CACHE_NAME, key, obj, groups);
		}
	}

	@Override
	public void putInCacheGroups(String key, String[] groups) {
		this.getCacheInfoManager().putInGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, key, groups);
	}

	@Override
	public void putInCache(String key, Object obj) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).putInCache(ICacheInfoManager.DEFAULT_CACHE_NAME, key, obj);
		}
	}

	@Override
	public void setExpirationTime(String key, long expiresInSeconds) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).setExpirationTime(ICacheInfoManager.DEFAULT_CACHE_NAME, key, expiresInSeconds);
		}
	}

	protected ICacheInfoManager getCacheInfoManager() {
		return _cacheInfoManager;
	}

	public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
		this._cacheInfoManager = cacheInfoManager;
	}

	private ICacheInfoManager _cacheInfoManager;

}
