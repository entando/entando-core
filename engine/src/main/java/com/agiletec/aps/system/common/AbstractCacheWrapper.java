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
package com.agiletec.aps.system.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author E.Santoboni
 */
public abstract class AbstractCacheWrapper {

    protected static enum Action {
        ADD,
        UPDATE,
        DELETE
    }

    private CacheManager springCacheManager;

    protected CacheManager getSpringCacheManager() {
        return springCacheManager;
    }
    @Autowired
    public void setSpringCacheManager(CacheManager springCacheManager) {
        this.springCacheManager = springCacheManager;
    }

    protected abstract String getCacheName();

    protected <T> T get(String name, Class<T> requiredType) {
        return this.get(this.getCache(), name, requiredType);
    }

    protected <T> T get(Cache cache, String name, Class<T> requiredType) {
        Object value = cache.get(name);
        if (value instanceof Cache.ValueWrapper) {
            value = ((Cache.ValueWrapper) value).get();
        }
        return (T) value;
    }

    protected Cache getCache() {
        return this.getSpringCacheManager().getCache(this.getCacheName());
    }


}
