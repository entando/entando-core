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

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class CustomConcurrentMapCacheFactoryBean implements FactoryBean<CustomConcurrentMapCache>, BeanNameAware, InitializingBean {

	private String name = "";

	@Nullable
	private ConcurrentMap<Object, Object> store;

	private boolean allowNullValues = true;

	@Nullable
	private CustomConcurrentMapCache cache;


	/**
	 * Specify the name of the cache.
	 * <p>Default is "" (empty String).
     * @param name the name of the cache
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Specify the ConcurrentMap to use as an internal store
	 * (possibly pre-populated).
	 * <p>Default is a standard {@link java.util.concurrent.ConcurrentHashMap}.
     * @param store The store to set
	 */
	public void setStore(ConcurrentMap<Object, Object> store) {
		this.store = store;
	}

	/**
	 * Set whether to allow {@code null} values
	 * (adapting them to an internal null holder value).
	 * <p>Default is "true".
     * @param allowNullValues the value to set
	 */
	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	@Override
	public void setBeanName(String beanName) {
		if (!StringUtils.hasLength(this.name)) {
			setName(beanName);
		}
	}

	@Override
	public void afterPropertiesSet() {
		this.cache = (this.store != null ? new CustomConcurrentMapCache(this.name, this.store, this.allowNullValues) :
				new CustomConcurrentMapCache(this.name, this.allowNullValues));
	}
    
	@Override
	@Nullable
	public CustomConcurrentMapCache getObject() {
		return this.cache;
	}

	@Override
	public Class<?> getObjectType() {
		return ConcurrentMapCache.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
