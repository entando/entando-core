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
package org.entando.entando.aps.system.services.api.cache;

import java.util.Map;

import org.entando.entando.aps.system.services.api.model.ApiResource;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.api.IApiCatalogDAO;

public interface IApiResourceCacheWrapper {

	public static final String APICATALOG_RESOURCES_CACHE_NAME = "ApiResourceManager_resources";

	public static final String APICATALOG_RESOURCE_CACHE_NAME_PREFIX = "ApiResourceManager_resource_";

	public void initCache(Map<String, ApiResource> resources, IApiCatalogDAO apiCatalogDAO) throws ApsSystemException;

	public ApiResource getMasterResource(String code);

	public Map<String, ApiResource> getMasterResources();

	public void updateResource(ApiResource resource);

}
