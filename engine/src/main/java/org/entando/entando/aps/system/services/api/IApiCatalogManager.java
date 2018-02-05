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

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public interface IApiCatalogManager {

	public static final String API_CATALOG_CACHE_NAME = "Entando_ApiCatalogManager";

	/**
	 * Return the api related whith the given widget, if exist.
	 *
	 * @param widgetCode The widget code.
	 * @return The api method related.
	 * @throws ApsSystemException In case of error.
	 */
	public ApiMethod getRelatedMethod(String widgetCode) throws ApsSystemException;

	@Deprecated
	public Map<String, ApiMethod> getRelatedShowletMethods() throws ApsSystemException;

	public Map<String, ApiMethod> getRelatedWidgetMethods() throws ApsSystemException;

	/**
	 * Return the map of GET methods indexed by api method name.
	 *
	 * @return The map of GET methods indexed by api method name.
	 * @throws ApsSystemException In case of error
	 * @deprecated use getMethods(ApiMethod.HttpMethod) method
	 */
	public Map<String, ApiMethod> getMethods() throws ApsSystemException;

	public List<ApiMethod> getMethods(ApiMethod.HttpMethod httpMethod) throws ApsSystemException;

	public Map<String, ApiResource> getResources() throws ApsSystemException;

	/**
	 * Return an API resource
	 *
	 * @param namespace The namespace.
	 * @param resourceName The resource name.
	 * @return The resource.
	 * @throws ApsSystemException In case of exception.
	 */
	public ApiResource getResource(String namespace, String resourceName) throws ApsSystemException;

	/**
	 * Return a GET methods by name.
	 *
	 * @param resourceName the resource name
	 * @return a GET methods.
	 * @throws ApsSystemException In case of error
	 * @deprecated use getMethod(ApiMethod.HttpMethod, resourceName) method
	 */
	public ApiMethod getMethod(String resourceName) throws ApsSystemException;

	public ApiMethod getMethod(ApiMethod.HttpMethod httpMethod, String resourceName) throws ApsSystemException;

	public ApiMethod getMethod(ApiMethod.HttpMethod httpMethod, String namespace, String resourceName) throws ApsSystemException;

	public Map<String, ApiService> getServices() throws ApsSystemException;

	public Map<String, ApiService> getServices(String tag/*, Boolean myentando*/) throws ApsSystemException;

	public ApiService getApiService(String key) throws ApsSystemException;

	public void updateMethodConfig(ApiMethod apiMethod) throws ApsSystemException;

	public void resetMethodConfig(ApiMethod apiMethod) throws ApsSystemException;

	public void saveService(ApiService service) throws ApsSystemException;

	public void deleteService(String key) throws ApsSystemException;

	public void updateService(ApiService service) throws ApsSystemException;

}
