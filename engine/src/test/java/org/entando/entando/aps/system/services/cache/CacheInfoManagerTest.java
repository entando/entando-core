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

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;

/**
 * @author E.Santoboni
 */
public class CacheInfoManagerTest {
	
	@Mock
    private CacheManager cacheManager;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

	@InjectMocks
    private CacheInfoManager cacheInfoManager;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
    @Test
    public void testAroundCacheableMethod() throws Throwable {
		CacheableInfo cacheableInfo = new CacheableInfo() {
			@Override
			public int expiresInMinute() {
				return 1;
			}
			@Override
			public String groups() {
				return "testGroup";
			}
			@Override
			public Class<? extends Annotation> annotationType() {
				return CacheableInfo.class;
			}
		};
        cacheInfoManager.aroundCacheableMethod(proceedingJoinPoint, cacheableInfo);
        Mockito.verify(proceedingJoinPoint, Mockito.times(1)).getTarget();
        Mockito.verify(cacheInfoManager, Mockito.never())
				.putInGroup(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String[].class));
        Mockito.verify(cacheInfoManager, Mockito.never())
				.setExpirationTime(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(Date.class));
    }
	
    @Test
    public void testAroundCacheInfoEvictMethod() throws Throwable {
		CacheInfoEvict cacheInfoEvict = new CacheInfoEvict() {
			@Override
			public String[] value() {
				return new String[]{ICacheInfoManager.DEFAULT_CACHE_NAME};
			}
			@Override
			public String groups() {
				return "testCacheGroup2,testCacheGroup2";
			}
			@Override
			public Class<? extends Annotation> annotationType() {
				return CacheInfoEvict.class;
			}
		};
        cacheInfoManager.aroundCacheInfoEvictMethod(proceedingJoinPoint, cacheInfoEvict);
        Mockito.verify(proceedingJoinPoint, Mockito.times(1)).getTarget();
        Mockito.verify(cacheInfoManager, Mockito.times(2))
				.flushGroup(Mockito.any(String.class), Mockito.any(String.class));
    }
	
    @Test
    public void setExpirationTimeInMinutes() throws Throwable {
		Map<String, Date> map = new HashMap<String, Date>();
		Mockito.when(cacheInfoManager.get(Mockito.anyString(), Map.class)).thenReturn(map);
        cacheInfoManager.setExpirationTime("targetCacheName1", "testkey2", 1);
        Mockito.verify(cacheInfoManager, Mockito.times(1)).getCache(Mockito.anyString());
    }
	
    @Test
    public void setExpirationTimeInSeconds() throws Throwable {
		Map<String, Date> map = new HashMap<String, Date>();
		Mockito.when(cacheInfoManager.get(Mockito.anyString(), Map.class)).thenReturn(map);
        cacheInfoManager.setExpirationTime("targetCacheName2", "testkey2", 2l);
        Mockito.verify(cacheInfoManager, Mockito.times(1)).getCache(Mockito.anyString());
    }
	
}
