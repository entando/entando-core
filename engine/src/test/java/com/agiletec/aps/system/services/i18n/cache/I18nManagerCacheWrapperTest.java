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
package com.agiletec.aps.system.services.i18n.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.entando.entando.aps.system.exception.CacheItemNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import com.agiletec.aps.system.services.i18n.I18nManagerTest;
import java.util.ArrayList;
import java.util.List;

public class I18nManagerCacheWrapperTest {

	private static final String CACHE_NAME = I18nManagerCacheWrapper.I18N_MANAGER_CACHE_NAME;

	private static final String TEST_KEY = "LABEL_HELLO";

	@Mock
	private CacheManager springCacheManager;

	@InjectMocks
	private I18nManagerCacheWrapper cacheWrapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ConcurrentMapCache fakeCache = new ConcurrentMapCache(CACHE_NAME);
		List<String> codes = new ArrayList<>();
		codes.add(TEST_KEY);
		fakeCache.put(I18nManagerCacheWrapper.I18N_CODES_CACHE_NAME, codes);
		fakeCache.put(I18nManagerCacheWrapper.I18N_CACHE_NAME_PREFIX + TEST_KEY, I18nManagerTest.createLabel("ciao", "hello"));
		when(springCacheManager.getCache(CACHE_NAME)).thenReturn(fakeCache);
	}

	@Test(expected = CacheItemNotFoundException.class)
	public void should_raise_exception_on_update_invalid_entry() {
		cacheWrapper.updateLabelGroup("THIS_DO_NOT_EXISTS", I18nManagerTest.createLabel("si", "yes"));
	}

	@Test
	public void should_update_existing_entry() {
		cacheWrapper.updateLabelGroup(TEST_KEY, I18nManagerTest.createLabel("ciao", "Hello world!"));
		assertEquals("Hello world!", cacheWrapper.getLabelGroups().get(TEST_KEY).get("en"));
	}

}
