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
package com.agiletec.plugins.jacms.aps.system.services.resource.cache;

import java.util.List;
import java.util.Map;

public interface IResourceManagerCacheWrapper {

    public static final String CACHE_NAME = "Entando_ResourceManager";

    public static final String CACHE_NAME_STATUS = "Entando_ResourceManager_status";

    public static final String CACHE_NAME_METADATA_MAPPING = "Entando_ResourceManager_metadataMapping";

    public void initCache();

    public void initCache(Integer status);

    public Integer getStatus();

    public void updateStatus(Integer status);

    public Map<String, List<String>> getMetadataMapping();

    public void updateMetadataMapping(Map<String, List<String>> mapping);

}
