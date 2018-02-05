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
package org.entando.entando.aps.system.services.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiMethodRelatedWidget;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.api.cache.IApiResourceCacheWrapper;
import org.entando.entando.aps.system.services.api.cache.IApiServiceCacheWrapper;

/**
 * @author E.Santoboni
 */
public class ApiCatalogManager extends AbstractService implements IApiCatalogManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String DEFAULT_LOCATION_PATTERN = "classpath*:/api/**/aps/apiMethods.xml";

	private IApiServiceCacheWrapper serviceCacheWrapper;

	private IApiResourceCacheWrapper resourceCacheWrapper;

	private String locationPatterns;

	private IApiCatalogDAO apiCatalogDAO;

	protected IApiServiceCacheWrapper getServiceCacheWrapper() {
		return serviceCacheWrapper;
	}

	public void setServiceCacheWrapper(IApiServiceCacheWrapper cacheWrapper) {
		this.serviceCacheWrapper = cacheWrapper;
	}

	protected IApiResourceCacheWrapper getResourceCacheWrapper() {
		return resourceCacheWrapper;
	}

	public void setResourceCacheWrapper(IApiResourceCacheWrapper resourceCacheWrapper) {
		this.resourceCacheWrapper = resourceCacheWrapper;
	}

	public void setLocationPatterns(String locationPatterns) {
		this.locationPatterns = locationPatterns;
	}

	protected IApiCatalogDAO getApiCatalogDAO() {
		return apiCatalogDAO;
	}

	public void setApiCatalogDAO(IApiCatalogDAO apiCatalogDAO) {
		this.apiCatalogDAO = apiCatalogDAO;
	}

	@Override
	public void init() throws Exception {
		ApiResourceLoader loader = new ApiResourceLoader(this.getLocationPatterns());
		Map<String, ApiResource> resources = loader.getResources();
		this.getResourceCacheWrapper().initCache(resources, this.getApiCatalogDAO());
		this.getServiceCacheWrapper().initCache(resources, this.getApiCatalogDAO());
		logger.debug("{} ready.", this.getClass().getName());
	}

	@Override
	public ApiMethod getRelatedMethod(String showletCode) throws ApsSystemException {
		List<ApiMethod> masterMethods = this.getMasterMethods(ApiMethod.HttpMethod.GET);
		for (int i = 0; i < masterMethods.size(); i++) {
			ApiMethod apiMethod = masterMethods.get(i);
			ApiMethodRelatedWidget relatedWidget = apiMethod.getRelatedWidget();
			if (null != relatedWidget && relatedWidget.getWidgetCode().equals(showletCode)) {
				return apiMethod.clone();
			}
		}
		return null;
	}

	@Override
	@Deprecated
	public Map<String, ApiMethod> getRelatedShowletMethods() throws ApsSystemException {
		return this.getRelatedWidgetMethods();
	}

	@Override
	public Map<String, ApiMethod> getRelatedWidgetMethods() throws ApsSystemException {
		Map<String, ApiMethod> mapping = new HashMap<String, ApiMethod>();
		try {
			List<ApiMethod> masterMethods = this.getMasterMethods(ApiMethod.HttpMethod.GET);
			for (int i = 0; i < masterMethods.size(); i++) {
				ApiMethod apiMethod = masterMethods.get(i);
				ApiMethodRelatedWidget relatedWidget = apiMethod.getRelatedWidget();
				if (null != relatedWidget) {
					String widgetCode = relatedWidget.getWidgetCode();
					if (mapping.containsKey(widgetCode)) {
						ApiMethod alreadyMapped = mapping.get(widgetCode);
						logger.error("There is more than one method related whith widget '{}' - Actual mapped '{}'; other method '{}'", widgetCode,
								alreadyMapped.getResourceName(), apiMethod.getResourceName());
					} else {
						mapping.put(widgetCode, apiMethod.clone());
					}
				}
			}
		} catch (Throwable t) {
			logger.error("Error loading related widget methods", t);
			throw new ApsSystemException("Error loading related widget methods", t);
		}
		return mapping;
	}

	@Override
	public void updateMethodConfig(ApiMethod apiMethod) throws ApsSystemException {
		try {
			ApiMethod masterMethod = this.checkMethod(apiMethod);
			this.getApiCatalogDAO().saveApiStatus(apiMethod);
			masterMethod.setStatus(apiMethod.getStatus());
			masterMethod.setHidden(apiMethod.getHidden());
			masterMethod.setRequiredAuth(apiMethod.getRequiredAuth());
			String requiredPermission = apiMethod.getRequiredPermission();
			if (null != requiredPermission && requiredPermission.trim().length() > 0) {
				masterMethod.setRequiredPermission(requiredPermission);
			} else {
				masterMethod.setRequiredPermission(null);
			}
			String resourceCode = ApiResource.getCode(masterMethod.getNamespace(), masterMethod.getResourceName());
			ApiResource resource = this.getResourceCacheWrapper().getMasterResource(resourceCode);
			this.getResourceCacheWrapper().updateResource(resource);
		} catch (Throwable t) {
			logger.error("Error error updating api status : resource '{}' method '{}'", apiMethod.getResourceName(), apiMethod.getHttpMethod(), t);
			throw new ApsSystemException("Error updating api status", t);
		}
	}

	@Override
	public void resetMethodConfig(ApiMethod apiMethod) throws ApsSystemException {
		try {
			ApiMethod masterMethod = this.checkMethod(apiMethod);
			String resourceCode = ApiResource.getCode(masterMethod.getNamespace(), masterMethod.getResourceName());
			this.getApiCatalogDAO().resetApiStatus(resourceCode, masterMethod.getHttpMethod());
			masterMethod.resetConfiguration();
		} catch (Throwable t) {
			logger.error("Error error resetting api status : resource '{}' method '{}'", apiMethod.getResourceName(), apiMethod.getHttpMethod(), t);
			throw new ApsSystemException("Error resetting api status", t);
		}
	}

	private ApiMethod checkMethod(ApiMethod apiMethod) throws ApsSystemException {
		if (null == apiMethod) {
			throw new ApsSystemException("Null api method");
		}
		ApiMethod masterMethod = this.getMasterMethod(apiMethod.getHttpMethod(), apiMethod.getNamespace(), apiMethod.getResourceName());
		if (null == masterMethod) {
			throw new ApsSystemException("Api namespace '" + apiMethod.getNamespace() + "' "
					+ "resource '" + apiMethod.getResourceName() + "' "
					+ "method '" + apiMethod.getHttpMethod() + "' does not exist");
		}
		return masterMethod;
	}

	@Deprecated
	@Override
	public ApiMethod getMethod(String resourceName) throws ApsSystemException {
		return this.getMethod(ApiMethod.HttpMethod.GET, resourceName);
	}

	@Override
	public ApiMethod getMethod(ApiMethod.HttpMethod httpMethod, String resourceName) throws ApsSystemException {
		return this.getMethod(ApiMethod.HttpMethod.GET, null, resourceName);
	}

	@Override
	public ApiMethod getMethod(ApiMethod.HttpMethod httpMethod, String namespace, String resourceName) throws ApsSystemException {
		ApiMethod masterMethod = this.getMasterMethod(httpMethod, namespace, resourceName);
		if (null != masterMethod) {
			return masterMethod.clone();
		}
		return null;
	}

	protected ApiMethod getMasterMethod(ApiMethod.HttpMethod httpMethod, String namespace, String resourceName) throws ApsSystemException {
		try {
			String resourceCode = ApiResource.getCode(namespace, resourceName);
			ApiResource resource = this.getResourceCacheWrapper().getMasterResource(resourceCode);
			if (null != resource) {
				return resource.getMethod(httpMethod);
			}
		} catch (Throwable t) {
			logger.error("Error extracting methods", t);
			throw new ApsSystemException("Error extracting methods", t);
		}
		return null;
	}

	@Deprecated
	@Override
	public Map<String, ApiMethod> getMethods() throws ApsSystemException {
		Map<String, ApiMethod> map = new HashMap<>();
		List<ApiMethod> list = this.getMethods(ApiMethod.HttpMethod.GET);
		for (int i = 0; i < list.size(); i++) {
			ApiMethod apiMethod = list.get(i);
			map.put(apiMethod.getResourceName(), apiMethod);
		}
		return map;
	}

	@Override
	public List<ApiMethod> getMethods(ApiMethod.HttpMethod httpMethod) throws ApsSystemException {
		List<ApiMethod> clonedMethods = new ArrayList<>();
		try {
			List<ApiMethod> masterMethods = this.getMasterMethods(httpMethod);
			for (int i = 0; i < masterMethods.size(); i++) {
				ApiMethod apiMethod = masterMethods.get(i);
				clonedMethods.add(apiMethod.clone());
			}
		} catch (Throwable t) {
			logger.error("Error extracting methods", t);
			throw new ApsSystemException("Error extracting methods", t);
		}
		return clonedMethods;
	}

	protected List<ApiMethod> getMasterMethods(ApiMethod.HttpMethod httpMethod) throws ApsSystemException {
		List<ApiMethod> apiMethods = new ArrayList<>();
		try {
			List<ApiResource> resourceList = new ArrayList<>(this.getResourceCacheWrapper().getMasterResources().values());
			for (int i = 0; i < resourceList.size(); i++) {
				ApiResource apiResource = resourceList.get(i);
				if (null != apiResource.getMethod(httpMethod)) {
					apiMethods.add(apiResource.getMethod(httpMethod));
				}
			}
		} catch (Throwable t) {
			logger.error("Error loading Master Methods definitions", t);
			throw new ApsSystemException("Error loading Master Methods definitions", t);
		}
		return apiMethods;
	}

	@Override
	public Map<String, ApiResource> getResources() throws ApsSystemException {
		Map<String, ApiResource> clonedApiResources = new HashMap<String, ApiResource>();
		try {
			Map<String, ApiResource> resources = this.getResourceCacheWrapper().getMasterResources();
			Iterator<String> iterator = resources.keySet().iterator();
			while (iterator.hasNext()) {
				String resourceFullCode = iterator.next();
				ApiResource resource = resources.get(resourceFullCode);
				clonedApiResources.put(resourceFullCode, resource.clone());
			}
		} catch (Throwable t) {
			logger.error("Error extracting resources", t);
			throw new ApsSystemException("Error extracting resources", t);
		}
		return clonedApiResources;
	}

	@Override
	public ApiResource getResource(String namespace, String resourceName) throws ApsSystemException {
		try {
			String resourceCode = ApiResource.getCode(namespace, resourceName);
			ApiResource apiResource = this.getResourceCacheWrapper().getMasterResource(resourceCode);
			if (null != apiResource) {
				return apiResource.clone();
			}
		} catch (Throwable t) {
			logger.error("Error extracting resource by name '{}'", resourceName, t);
			throw new ApsSystemException("Error extracting resource", t);
		}
		return null;
	}

	@Override
	public ApiService getApiService(String key) throws ApsSystemException {
		ApiService service = this.getServiceCacheWrapper().getMasterServices().get(key);
		if (null == service) {
			return null;
		}
		return service.clone();
	}

	@Override
	public Map<String, ApiService> getServices() throws ApsSystemException {
		Map<String, ApiService> clonedServices = new HashMap<>();
		try {
			if (null != this.getServiceCacheWrapper().getMasterServices()) {
				Iterator<String> servicesIter = this.getServiceCacheWrapper().getMasterServices().keySet().iterator();
				while (servicesIter.hasNext()) {
					String serviceKey = servicesIter.next();
					clonedServices.put(serviceKey, this.getServiceCacheWrapper().getMasterServices().get(serviceKey).clone());
				}
			}
		} catch (Throwable t) {
			logger.error("Error extracting services", t);
			throw new ApsSystemException("Error extracting services", t);
		}
		return clonedServices;
	}

	@Override
	public Map<String, ApiService> getServices(String tag) throws ApsSystemException {
		Map<String, ApiService> services = this.getServices();
		if (null == tag || tag.trim().length() == 0) {
			return services;
		}
		Map<String, ApiService> servicesToReturn = new HashMap<>();
		try {
			Iterator<ApiService> iter = services.values().iterator();
			while (iter.hasNext()) {
				ApiService apiService = iter.next();
				String serviceTag = apiService.getTag();
				boolean tagCheck = (null != serviceTag && serviceTag.toLowerCase().indexOf(tag.trim().toLowerCase()) > -1);
				if (tagCheck) {
					servicesToReturn.put(apiService.getKey(), apiService);
				}
			}
		} catch (Throwable t) {
			logger.error("Error extracting services", t);
			throw new ApsSystemException("Error extracting services", t);
		}
		return servicesToReturn;
	}

	@Override
	public void saveService(ApiService service) throws ApsSystemException {
		try {
			if (null == service) {
				throw new ApsSystemException("Null api service to save");
			}
			ApiMethod master = service.getMaster();
			if (null == master || null == this.getMethod(master.getHttpMethod(), master.getNamespace(), master.getResourceName())) {
				throw new ApsSystemException("null or invalid master method of service to save");
			}
			if (null != this.getServiceCacheWrapper().getMasterServices().get(service.getKey())) {
				this.getApiCatalogDAO().updateService(service);
			} else {
				this.getApiCatalogDAO().addService(service);
			}
			this.getServiceCacheWrapper().addService(service);
		} catch (Throwable t) {
			logger.error("Error saving service", t);
			throw new ApsSystemException("Error saving service", t);
		}
	}

	@Override
	public void deleteService(String key) throws ApsSystemException {
		try {
			this.getApiCatalogDAO().deleteService(key);
			this.getServiceCacheWrapper().removeService(key);
		} catch (Throwable t) {
			logger.error("Error deleting api service by key '{}'", key, t);
			throw new ApsSystemException("Error deleting service '" + key + "'", t);
		}
	}

	@Override
	public void updateService(ApiService service) throws ApsSystemException {
		try {
			if (null == service) {
				throw new ApsSystemException("Null api service to update");
			}
			ApiService masterService = this.getServiceCacheWrapper().getMasterServices().get(service.getKey());
			if (null == masterService) {
				throw new ApsSystemException("Api service '" + service.getKey() + "' does not exist");
			}
			masterService.setActive(service.isActive());
			masterService.setHidden(service.isHidden());
			this.getApiCatalogDAO().updateService(masterService);
			this.getServiceCacheWrapper().updateService(service);
		} catch (Throwable t) {
			logger.error("Error updating api service with key '{}'", service.getKey(), t);
			throw new ApsSystemException("Error updating service '" + service.getKey() + "'", t);
		}
	}

	protected String getLocationPatterns() {
		if (null == this.locationPatterns) {
			return DEFAULT_LOCATION_PATTERN;
		}
		return locationPatterns;
	}

}
