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

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceManagerCacheWrapper extends AbstractCacheWrapper implements IResourceManagerCacheWrapper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void initCache() {
        this.initCache(IResourceManager.STATUS_READY);
    }

    @Override
    public void initCache(Integer status) {
        this.getCache().evict(CACHE_NAME_METADATA_MAPPING);
        this.updateStatus(status);
    }

    @Override
    public Integer getStatus() {
        return this.get(CACHE_NAME_STATUS, Integer.class);
    }

    @Override
    public void updateStatus(Integer status) {
        this.getCache().put(CACHE_NAME_STATUS, status);
        logger.trace("status set to {}", status);
    }

    @Override
    public Map<String, List<String>> getMetadataMapping() {
        return this.get(CACHE_NAME_METADATA_MAPPING, Map.class);
    }

    @Override
    public void updateMetadataMapping(Map<String, List<String>> mapping) {
        this.getCache().put(CACHE_NAME_METADATA_MAPPING, mapping);
    }

    @Override
    protected String getCacheName() {
        return CACHE_NAME;
    }

}
