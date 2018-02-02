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
package org.entando.entando.aps.system.services.api.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.CacheItemNotFoundException;
import org.entando.entando.aps.system.services.api.IApiCatalogDAO;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;

public class ApiCatalogManagerCacheWrapper extends AbstractCacheWrapper implements IApiCatalogManagerCacheWrapper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getCacheName() {
		return APICATALOG_MANAGER_CACHE_NAME;
	}

	@Override
	public void initCache(Map<String, ApiResource> resources, IApiCatalogDAO apiCatalogDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			apiCatalogDAO.loadApiStatus(resources);
			this.insertObjectsOnCache(cache, resources, apiCatalogDAO);
		} catch (Throwable t) {
			logger.error("Error bootstrapping ApiCatalog cache", t);
			throw new ApsSystemException("Error bootstrapping ApiCatalog cache", t);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ApiResource> getMasterResources() {
		return this.get(this.getCache(), APICATALOG_RESOURCES_CACHE_NAME, Map.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ApiService> getMasterServices() {
		return this.get(this.getCache(), APICATALOG_SERVICES_CACHE_NAME, Map.class);
	}

	@Override
	public void addService(ApiService apiService) {
		this.manage(apiService.getKey(), apiService, Action.ADD);
	}

	@Override
	public void updateService(ApiService apiService) {
		this.manage(apiService.getKey(), apiService, Action.UPDATE);
	}

	@Override
	public void removeService(String key) {
		this.manage(key, null, Action.DELETE);
	}

	protected void insertObjectsOnCache(Cache cache, Map<String, ApiResource> resources, IApiCatalogDAO apiCatalogDAO) throws ApsSystemException {
		cache.put(APICATALOG_RESOURCES_CACHE_NAME, resources);
		Map<String, ApiService> services = new HashMap<>();
		try {
			List<ApiMethod> apiGETMethods = buildApiGetMethods(resources);
			services = apiCatalogDAO.loadServices(apiGETMethods);

			for (Map.Entry<String, ApiService> entry : services.entrySet()) {
				cache.put(APICATALOG_SERVICE_CACHE_NAME_PREFIX + entry.getKey(), entry.getValue());
			}

			cache.put(APICATALOG_SERVICES_CACHE_NAME, services);
		} catch (Throwable t) {
			cache.put(APICATALOG_SERVICES_CACHE_NAME, new HashMap<String, ApiService>());
			this.releaseServiceEntries(cache, APICATALOG_SERVICES_CACHE_NAME, APICATALOG_SERVICE_CACHE_NAME_PREFIX);
			logger.error("Error loading Services definitions", t);
			throw new ApsSystemException("Error loading Services definitions", t);
		}
	}


	protected void releaseCachedObjects(Cache cache) {
		this.releaseServiceEntries(cache, APICATALOG_SERVICES_CACHE_NAME, APICATALOG_SERVICE_CACHE_NAME_PREFIX);
		cache.evict(APICATALOG_RESOURCES_CACHE_NAME);
		cache.evict(APICATALOG_SERVICES_CACHE_NAME);
	}

	@SuppressWarnings("unchecked")
	protected void releaseServiceEntries(Cache cache, String cacheKey, String codePrefix) {
		Map<String, ApiService> services = (Map<String, ApiService>) this.get(cache, cacheKey, Map.class);
		if (null != services) {
			for (Map.Entry<String, ApiService> entry : services.entrySet()) {
				String key = entry.getKey();
				cache.evict(codePrefix + key);
			}
		}
	}

	protected List<ApiMethod> buildApiGetMethods(Map<String, ApiResource> resources) {
		List<ApiMethod> apiGETMethods = new ArrayList<ApiMethod>();
		List<ApiResource> resourceList = new ArrayList<ApiResource>(resources.values());
		for (int i = 0; i < resourceList.size(); i++) {
			ApiResource apiResource = resourceList.get(i);
			if (null != apiResource.getGetMethod()) {
				apiGETMethods.add(apiResource.getGetMethod());
			}
		}
		return apiGETMethods;
	}

	protected void manage(String key, ApiService service, Action operation) {
		if (StringUtils.isBlank(key)) {
			return;
		}
		Cache cache = this.getCache();
		Map<String, ApiService> services = this.getMasterServices();

		if (Action.ADD.equals(operation)) {
			services.put(key, service);
			cache.put(APICATALOG_SERVICES_CACHE_NAME, services);
			cache.put(APICATALOG_SERVICE_CACHE_NAME_PREFIX + key, service);
			logger.trace("executed {} cache entry into {} with key {}", operation, cache.getName(), key);
		} else if (Action.UPDATE.equals(operation)) {
			if (!services.containsKey(key)) {
				throw new CacheItemNotFoundException(key, cache.getName());
			}
			cache.put(APICATALOG_SERVICE_CACHE_NAME_PREFIX + key, service);
			services.put(key, service);
			cache.put(APICATALOG_SERVICES_CACHE_NAME, services);
			logger.trace("executed {} cache entry into {} with key {}", operation, cache.getName(), key);
		} else if (Action.DELETE.equals(operation)) {
			cache.evict(APICATALOG_SERVICE_CACHE_NAME_PREFIX + key);
			services.remove(key);
			cache.put(APICATALOG_SERVICES_CACHE_NAME, services);
			logger.trace("executed {} cache entry from {} with key {}", operation, cache.getName(), key);
		}
	}

}
