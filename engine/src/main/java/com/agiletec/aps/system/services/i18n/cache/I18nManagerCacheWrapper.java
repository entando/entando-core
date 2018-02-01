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
package com.agiletec.aps.system.services.i18n.cache;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.CacheItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nDAO;
import com.agiletec.aps.util.ApsProperties;

public class I18nManagerCacheWrapper extends AbstractCacheWrapper implements II18nManagerCacheWrapper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getCacheName() {
		return I18N_MANAGER_CACHE_NAME;
	}

	@Override
	public void initCache(II18nDAO i18nDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, ApsProperties> labels = i18nDAO.loadLabelGroups();
			this.insertObjectsOnCache(cache, labels);
		} catch (Throwable t) {
			logger.error("Error loading labels", t);
			throw new ApsSystemException("Error loading labels", t);
		}
	}

	private void insertObjectsOnCache(Cache cache, Map<String, ApsProperties> labels) {
		for (Map.Entry<String, ApsProperties> entry : labels.entrySet()) {
			String key = entry.getKey();
			cache.put(I18N_CACHE_NAME_PREFIX + key, entry.getValue());
		}
		cache.put(I18N_CODES_CACHE_NAME, labels);
	}

	protected void releaseCachedObjects(Cache cache) {
		this.releaseCachedObjects(cache, I18N_CODES_CACHE_NAME, I18N_CACHE_NAME_PREFIX);
	}

	@SuppressWarnings("unchecked")
	protected void releaseCachedObjects(Cache cache, String cacheKey, String codePrefix) {
		Map<String, ApsProperties> groupCodes = (Map<String, ApsProperties>) this.get(cache, cacheKey, Map.class);
		if (null != groupCodes) {
			for (Map.Entry<String, ApsProperties> entry : groupCodes.entrySet()) {
				String key = entry.getKey();
				cache.evict(codePrefix + key);
			}
			cache.evict(cacheKey);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ApsProperties> getLabelGroups() {
		return this.get(this.getCache(), I18N_CODES_CACHE_NAME, Map.class);
	}

	@Override
	public void addLabelGroup(String key, ApsProperties labels) {
		this.manage(key, labels, Action.ADD);
	}

	@Override
	public void updateLabelGroup(String key, ApsProperties labels) {
		this.manage(key, labels, Action.UPDATE);
	}

	@Override
	public void removeLabelGroup(String key) {
		this.manage(key, null, Action.DELETE);
	}

	private void manage(String key, ApsProperties labels, Action operation) {
		if (StringUtils.isBlank(key)) {
			return;
		}
		Cache cache = this.getCache();
		Map<String, ApsProperties> codes = this.getLabelGroups();
		if (Action.ADD.equals(operation)) {
			if (!codes.containsKey(key)) {
				codes.put(key, labels);
				cache.put(I18N_CODES_CACHE_NAME, codes);
			}
			cache.put(I18N_CACHE_NAME_PREFIX + key, labels);
		} else if (Action.UPDATE.equals(operation)) {
			if (!codes.containsKey(key)) {
				throw new CacheItemNotFoundException(key, cache.getName());
			}
			cache.put(I18N_CACHE_NAME_PREFIX + key, labels);
			codes.put(key, labels);
			cache.put(I18N_CODES_CACHE_NAME, codes);
		} else if (Action.DELETE.equals(operation)) {
			codes.remove(key);
			cache.evict(I18N_CACHE_NAME_PREFIX + key);
			cache.put(I18N_CODES_CACHE_NAME, codes);
		}
	}
}
