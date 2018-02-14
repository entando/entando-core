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
package com.agiletec.aps.system.services.baseconfig.cache;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.IConfigItemDAO;
import com.agiletec.aps.util.MapSupportRule;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class ConfigManagerCacheWrapper extends AbstractCacheWrapper implements IConfigManagerCacheWrapper {

	private static final Logger logger = LoggerFactory.getLogger(ConfigManagerCacheWrapper.class);

	@Override
	public void initCache(IConfigItemDAO configItemDAO, String version) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, String> configItems = configItemDAO.loadVersionItems(version);
			String xmlParams = configItems.get(SystemConstants.CONFIG_ITEM_PARAMS);
			Map<String, String> params = this.parseParams(xmlParams);
			this.insertObjectsOnCache(cache, configItems, params);
		} catch (ApsSystemException t) {
			logger.error("Error loading configuration params", t);
			throw new ApsSystemException("Error loading configuration params", t);
		}
	}

	protected Map<String, String> parseParams(String xmlParams) throws ApsSystemException {
		Map<String, String> params = new HashMap<>();
		Digester dig = new Digester();
		Rule rule = new MapSupportRule("name");
		dig.addRule("*/Param", rule);
		dig.push(params);
		try {
			dig.parse(new StringReader(xmlParams));
		} catch (Exception e) {
			logger.error("Error detected while parsing the \"params\" item in the \"sysconfig\" table: verify the \"sysconfig\" table", e);
			throw new ApsSystemException(
					"Error detected while parsing the \"params\" item in the \"sysconfig\" table:"
					+ " verify the \"sysconfig\" table", e);
		}
		return params;
	}

	protected void releaseCachedObjects(Cache cache) {
		this.releaseCachedObjects(cache, CONFIG_PARAMS_CODES_CACHE_NAME, CONFIG_PARAM_CACHE_NAME_PREFIX);
		this.releaseCachedObjects(cache, CONFIG_ITEMS_CODES_CACHE_NAME, CONFIG_ITEM_CACHE_NAME_PREFIX);
	}

	private void releaseCachedObjects(Cache cache, String codesName, String codePrefix) {
		List<String> codes = (List<String>) this.get(cache, codesName, List.class);
		if (null != codes) {
			for (String code : codes) {
				cache.evict(codePrefix + code);
			}
			cache.evict(codesName);
		}
	}

	protected void insertObjectsOnCache(Cache cache, Map<String, String> configItems, Map<String, String> params) {
		this.insertObjectsOnCache(cache, configItems, CONFIG_ITEMS_CODES_CACHE_NAME, CONFIG_ITEM_CACHE_NAME_PREFIX);
		this.insertObjectsOnCache(cache, params, CONFIG_PARAMS_CODES_CACHE_NAME, CONFIG_PARAM_CACHE_NAME_PREFIX);
	}

	private void insertObjectsOnCache(Cache cache, Map<String, String> map, String codesCacheName, String codeCachePrefix) {
		List<String> codes = new ArrayList<>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			cache.put(codeCachePrefix + key, map.get(key));
			codes.add(key);
		}
		cache.put(codesCacheName, codes);
	}

	@Override
	public String getConfigItem(String name) {
		return this.get(CONFIG_ITEM_CACHE_NAME_PREFIX + name, String.class);
	}

	@Override
	public String getParam(String name) {
		return this.get(CONFIG_PARAM_CACHE_NAME_PREFIX + name, String.class);
	}

	@Override
	protected String getCacheName() {
		return CONFIG_MANAGER_CACHE_NAME;
	}

}
