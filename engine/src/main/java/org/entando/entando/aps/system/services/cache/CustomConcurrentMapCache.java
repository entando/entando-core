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
package org.entando.entando.aps.system.services.cache;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * @author E.Santoboni
 */
public class CustomConcurrentMapCache extends ConcurrentMapCache {
    
    private static final Object NULL_HOLDER = new CustomNullHolder();
    
    public CustomConcurrentMapCache(String name) {
        super(name);
    }

    public CustomConcurrentMapCache(String name, boolean allowNullValues) {
        super(name, allowNullValues);
    }

    public CustomConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
        super(name, store, allowNullValues);
    }

    @Override
    public Cache.ValueWrapper get(Object key) {
        Cache.ValueWrapper wrapper = super.get(key);
        if (wrapper == null) {
            return null;
        }
        if (wrapper.get() instanceof CustomNullHolder) {
            return new SimpleValueWrapper(null);
        } else {
            return wrapper;
        }
    }
    
    /**
	 * Convert the given value from the internal store to a user value
	 * returned from the get method (adapting {@code null}).
	 * @param storeValue the store value
	 * @return the value to return to the user
	 */
    @Override
    protected Object fromStoreValue(Object storeValue) {
		if (super.isAllowNullValues() && storeValue == NULL_HOLDER) {
			return null;
		}
		return storeValue;
	}

	/**
	 * Convert the given user value, as passed into the put method,
	 * to a value in the internal store (adapting {@code null}).
	 * @param userValue the given user value
	 * @return the value to store
	 */
    @Override
	protected Object toStoreValue(Object userValue) {
		if (super.isAllowNullValues() && userValue == null) {
			return NULL_HOLDER;
		}
		return userValue;
    }
    
    @SuppressWarnings("serial")
    private static class CustomNullHolder implements Serializable {
    }
    
}
