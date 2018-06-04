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

import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.core.serializer.support.SerializationDelegate;
import org.springframework.lang.Nullable;

/**
 * @author E.Santoboni
 */
public class CustomConcurrentMapCache extends ConcurrentMapCache {

    public CustomConcurrentMapCache(String name) {
        super(name);
    }

    public CustomConcurrentMapCache(String name, boolean allowNullValues) {
        super(name, allowNullValues);
    }

    public CustomConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
        super(name, store, allowNullValues);
    }

    public CustomConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues, SerializationDelegate serialization) {
        super(name, store, allowNullValues, serialization);
    }
    
    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper wrapper = super.get(key);
        if (wrapper == null) {
            return null;
        }
        if (wrapper.get() instanceof Nullable) {
            return new SimpleValueWrapper(null);
        } else {
            return wrapper;
        }
    }
    
}
