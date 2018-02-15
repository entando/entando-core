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
package com.agiletec.aps.system.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.exception.CacheItemNotFoundException;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 * @param <O> The object to manage
 */
public abstract class AbstractGenericCacheWrapper<O extends Object> extends AbstractCacheWrapper {

	protected static enum Action {
		ADD, UPDATE, DELETE
	}

	protected void releaseCachedObjects(Cache cache) {
		List<String> codes = (List<String>) this.get(cache, this.getCodesCacheKey(), List.class);
		if (null != codes) {
			for (String code : codes) {
				cache.evict(this.getCacheKeyPrefix() + code);
			}
			cache.evict(this.getCodesCacheKey());
		}
	}

	protected void insertObjectsOnCache(Cache cache, Map<String, O> objects) {
		List<String> codes = new ArrayList<>();
		Iterator<String> iter = objects.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			cache.put(this.getCacheKeyPrefix() + key, objects.get(key));
			codes.add(key);
		}
		cache.put(this.getCodesCacheKey(), codes);
	}

	protected <O> Map<String, O> getObjectMap() {
		Map<String, O> map = new HashMap<>();
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, this.getCodesCacheKey(), List.class);
		if (null != codes) {
			for (String code : codes) {
				map.put(code, (O) this.get(cache, this.getCacheKeyPrefix() + code, Object.class));
			}
		}
		return map;
	}

	protected void add(String key, O object) {
		this.manage(key, object, Action.ADD);
	}

	protected void update(String key, O object) {
		this.manage(key, object, Action.UPDATE);
	}

	protected void remove(String key, O object) {
		this.manage(key, object, Action.DELETE);
	}

	protected <O> void manage(String key, O object, Action operation) {
		if (null == object) {
			return;
		}
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, this.getCodesCacheKey(), List.class);
		if (Action.ADD.equals(operation)) {
			if (!codes.contains(key)) {
				codes.add(key);
				cache.put(this.getCodesCacheKey(), codes);
			}
			cache.put(this.getCacheKeyPrefix() + key, object);
		} else if (Action.UPDATE.equals(operation)) {
			if (!codes.contains(key)) {
				throw new CacheItemNotFoundException(key, cache.getName());
			}
			cache.put(this.getCacheKeyPrefix() + key, object);
		} else if (Action.DELETE.equals(operation)) {
			codes.remove(key);
			cache.evict(this.getCacheKeyPrefix() + key);
			cache.put(this.getCodesCacheKey(), codes);
		}
	}

	protected abstract String getCodesCacheKey();

	protected abstract String getCacheKeyPrefix();

}
