/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

/**
 * @author E.Santoboni
 */
public class EntandoCacheManager extends AbstractCacheManager {

    private Collection<Cache> caches = Collections.emptySet();

    @Autowired(required = false)
    private List<ExternalCachesContainer> externalCachesContainers;

    @Override
    public void afterPropertiesSet() {
        if (null != this.getExternalCachesContainers()) {
            for (ExternalCachesContainer container : this.getExternalCachesContainers()) {
                if (null != container.getCaches()) {
                    this.caches.addAll(container.getCaches());
                }
            }
        }
        super.afterPropertiesSet();
    }

    /**
     * Specify the collection of Cache instances to use for this CacheManager.
     *
     * @param caches the caches to set
     */
    public void setCaches(Collection<Cache> caches) {
        this.caches = caches;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    protected List<ExternalCachesContainer> getExternalCachesContainers() {
        return externalCachesContainers;
    }

    protected void setExternalCachesContainers(List<ExternalCachesContainer> externalCachesContainers) {
        this.externalCachesContainers = externalCachesContainers;
    }

}
