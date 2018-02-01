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

	protected void insertObjectsOnCache(Cache cache, Map<String, ApiResource> resources, IApiCatalogDAO apiCatalogDAO) throws ApsSystemException {
		Map<String, ApiService> services = new HashMap<>();
		try {
			List<ApiMethod> apiGETMethods = buildApiGetMethods(resources);
			services = apiCatalogDAO.loadServices(apiGETMethods);
		} catch (Throwable t) {
			services = new HashMap<String, ApiService>();
			logger.error("Error loading Services definitions", t);
			throw new ApsSystemException("Error loading Services definitions", t);
		}
		cache.put(APICATALOG_RESOURCES_CACHE_NAME, resources);
		cache.put(APICATALOG_SERVICES_CACHE_NAME, services);
	}


	protected void releaseCachedObjects(Cache cache) {
		cache.evict(APICATALOG_RESOURCES_CACHE_NAME);
		cache.evict(APICATALOG_SERVICES_CACHE_NAME);
	}


	private List<ApiMethod> buildApiGetMethods(Map<String, ApiResource> resources) {
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


}
