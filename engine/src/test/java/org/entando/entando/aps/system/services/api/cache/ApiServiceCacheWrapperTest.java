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
package org.entando.entando.aps.system.services.api.cache;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.entando.entando.aps.system.services.api.IApiCatalogManager;

import org.entando.entando.aps.system.services.api.model.ApiService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class ApiServiceCacheWrapperTest {

	private static final String CACHE_NAME = IApiCatalogManager.API_CATALOG_CACHE_NAME;

	@Mock
	private CacheManager springCacheManager;

	@InjectMocks
	private ApiServiceCacheWrapper cacheWrapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ConcurrentMapCache fakeCache = new ConcurrentMapCache(CACHE_NAME);
		fakeCache.put(IApiServiceCacheWrapper.APICATALOG_SERVICES_CACHE_NAME, new HashMap<String, ApiService>());
		when(springCacheManager.getCache(CACHE_NAME)).thenReturn(fakeCache);
	}

	@Test
	public void should_update_existing_entry() {
		cacheWrapper.removeService("test");
	}

}
