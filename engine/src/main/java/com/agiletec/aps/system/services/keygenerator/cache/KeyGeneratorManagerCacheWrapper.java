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
package com.agiletec.aps.system.services.keygenerator.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

public class KeyGeneratorManagerCacheWrapper extends AbstractCacheWrapper implements IKeyGeneratorManagerCacheWrapper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getCacheName() {
		return CACHE_NAME;
	}

	@Override
	public void initCache(IKeyGeneratorDAO keyGeneratorDAO) {
		Integer value = keyGeneratorDAO.getUniqueKey();
		Cache cache = this.getCache();
		this.releaseCachedObjects(cache);
		this.insertObjectsOnCache(cache, value);
	}

	@Override
	public int getUniqueKeyCurrentValue() {
		return this.get(this.getCache(), CURRENT_KEY, Integer.class);
	}

	@Override
	public void updateCurrentKey(int value) {
		this.insertObjectsOnCache(this.getCache(), value);
	}

	private void insertObjectsOnCache(Cache cache, Integer value) {
		cache.put(IKeyGeneratorManagerCacheWrapper.CURRENT_KEY, value);
		logger.trace("current key is now {}", value);
	}

	private void releaseCachedObjects(Cache cache) {
		cache.evict(IKeyGeneratorManagerCacheWrapper.CURRENT_KEY);
	}

}
