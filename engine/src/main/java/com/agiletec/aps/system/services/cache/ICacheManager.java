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
package com.agiletec.aps.system.services.cache;

import org.entando.entando.aps.system.services.cache.ICacheInfoManager;

/**
 * Interfaccia base per i servizi gestore cache.
 *
 * @author E.Santoboni
 */
@Deprecated
public interface ICacheManager {

	/**
	 * Flush the entire cache immediately.
	 */
	@Deprecated
	public void flushAll();

	/**
	 * Flushes a single cache entry.
	 *
	 * @param key The key entered by the user.
	 */
	@Deprecated
	public void flushEntry(String key);

	/**
	 * Flushes all items that belong to the specified group.
	 *
	 * @param group The name of the group to flush.
	 */
	@Deprecated
	public void flushGroup(String group);

	/**
	 * Put an object in a cache.
	 *
	 * @param key The key entered by the user.
	 * @param obj The object to store.
	 */
	@Deprecated
	public void putInCache(String key, Object obj);

	/**
	 * Put an object in a cache.
	 *
	 * @param key The key entered by the user.
	 * @param obj The object to store.
	 * @param groups The groups that this object belongs to.
	 */
	@Deprecated
	public void putInCache(String key, Object obj, String[] groups);

	/**
	 * Get an object from the cache.
	 *
	 * @param key The key entered by the user.
	 * @return The object from cache.
	 */
	@Deprecated
	public Object getFromCache(String key);

	@Deprecated
	public void putInCacheGroups(String key, String[] groups);

	public void setExpirationTime(String key, long expiresInSeconds);

	@Deprecated
	public static final String CACHE_NAME = ICacheInfoManager.CACHE_NAME;

}
