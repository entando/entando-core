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
package org.entando.entando.aps.system.services.cache;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * Classe test del servizio gestore cache.
 *
 * @author E.Santoboni
 */
public class CacheInfoManagerIntegrationTest extends BaseTestCase {

	private static final String DEFAULT_CACHE = CacheInfoManager.DEFAULT_CACHE_NAME;

	private CacheInfoManager cacheInfoManager = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testPutGetFromCache_1() {
		String value = "Stringa prova";
		String key = "Chiave_prova";
		this.cacheInfoManager.putInCache(DEFAULT_CACHE, key, value);
		Object extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertEquals(value, extracted);
		this.cacheInfoManager.flushEntry(DEFAULT_CACHE, key);
		extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNull(extracted);
	}

	public void testPutGetFromCache_2() {
		String key = "Chiave_prova";
		Object extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNull(extracted);
		extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNull(extracted);

		String value = "Stringa prova";
		this.cacheInfoManager.putInCache(DEFAULT_CACHE, key, value);

		extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNotNull(extracted);
		assertEquals(value, extracted);
		this.cacheInfoManager.flushEntry(DEFAULT_CACHE, key);
		extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNull(extracted);
	}

	public void testPutGetFromCacheOnRefreshPeriod() throws Throwable {
		String value = "Stringa prova";
		String key = "Chiave prova";
		this.cacheInfoManager.putInCache(DEFAULT_CACHE, key, value);
		this.cacheInfoManager.setExpirationTime(DEFAULT_CACHE, key, 2l);
		Object extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertEquals(value, extracted);
		synchronized (this) {
			this.wait(3000);
		}
		extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNull(extracted);
	}

	public void testPutGetFromCacheGroup() {
		String value = "Stringa prova";
		String key = "Chiave prova";
		String group1 = "group1";
		String[] groups = {group1};
		cacheInfoManager.putInCache(DEFAULT_CACHE, key, value, groups);
		Object extracted = cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertEquals(value, extracted);
		cacheInfoManager.flushGroup(DEFAULT_CACHE, group1);
		extracted = cacheInfoManager.getFromCache(DEFAULT_CACHE, key);
		assertNull(extracted);
	}

	private void init() throws Exception {
		try {
			cacheInfoManager = (CacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

}
