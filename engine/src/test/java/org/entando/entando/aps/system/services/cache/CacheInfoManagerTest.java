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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.aspectj.lang.ProceedingJoinPoint;
import static org.entando.entando.aps.system.services.cache.ICacheInfoManager.GROUP_CACHE_NAME_PREFIX;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author E.Santoboni
 */
@RunWith(MockitoJUnitRunner.class)
public class CacheInfoManagerTest {
	
	@Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private Cache.ValueWrapper valueWrapper;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

	@InjectMocks
    private CacheInfoManager cacheInfoManager;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Map<String, Date> map = new HashMap<String, Date>();
		Mockito.when(valueWrapper.get()).thenReturn(map);
		Mockito.when(cache.get(Mockito.any())).thenReturn(valueWrapper);
		Mockito.when(cacheManager.getCache(Mockito.anyString())).thenReturn(this.cache);
	}
	
	@Test(expected = ApsSystemException.class)
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
	
    @Test(expected = ApsSystemException.class)
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
        //Mockito.verify(cacheInfoManager, Mockito.times(2))
		//		.flushGroup(Mockito.any(String.class), Mockito.any(String.class));
    }
	
    @Test
    public void setExpirationTimeInMinutes() throws Throwable {
		String targetCache = "targetCacheName1";
		String cacheKey = "testkey1";
		cacheInfoManager.putInCache(targetCache, cacheKey, "Some value");
        cacheInfoManager.setExpirationTime(targetCache, cacheKey, 1);
		boolean expired = cacheInfoManager.isExpired(targetCache, cacheKey);
		Assert.assertFalse(expired);
    }
	
    @Test
    public void setExpirationTimeInSeconds() throws Throwable {
		String targetCache = "targetCacheName2";
		String cacheKey = "testkey2";
		cacheInfoManager.putInCache(targetCache, cacheKey, "Some other value");
        cacheInfoManager.setExpirationTime(targetCache, cacheKey, 1l);
		boolean expired = cacheInfoManager.isExpired(targetCache, cacheKey);
		Assert.assertFalse(expired);
		synchronized (this) {
    		this.wait(2000);
		}
		boolean expired2 = cacheInfoManager.isExpired(targetCache, cacheKey);
		Assert.assertTrue(expired2);
    }
	
    @Test
    public void updateFromPageChanged() throws Throwable {
		PageChangedEvent event = new PageChangedEvent();
		Page page = new Page();
		page.setCode("code");
		event.setPage(page);
        cacheInfoManager.updateFromPageChanged(event);
		Mockito.verify(cache, Mockito.times(1)).get(Mockito.anyString());
		Mockito.verify(cache, Mockito.times(1)).put(Mockito.anyString(), Mockito.any(Map.class));
		Object requiredMap = cacheInfoManager.getFromCache(CacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME, 
				GROUP_CACHE_NAME_PREFIX + CacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME);
		Assert.assertTrue(requiredMap instanceof Map);
		Assert.assertNotNull(requiredMap);
		Assert.assertEquals(0, ((Map) requiredMap).size());
		/*
        Mockito.verify(cacheInfoManager, Mockito.times(1)).flushGroup(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cacheInfoManager, Mockito.times(1)).accessOnGroupMapping(Mockito.anyString(), 
				Mockito.any(int.class), Mockito.any(String[].class), Mockito.anyString());
        Mockito.verify(cacheInfoManager, Mockito.times(1)).flushEntry(Mockito.anyString(), Mockito.anyString());
		*/
    }
	
	@Test
    public void destroy() throws Throwable {
		cacheInfoManager.destroy();
		Mockito.verify(cacheManager, Mockito.times(0)).getCacheNames();
		Mockito.verify(cacheManager, Mockito.times(4)).getCache(Mockito.anyString());
		Mockito.verify(cache, Mockito.times(2)).clear();
	}
	
	@Test
    public void flushAll() throws Throwable {
		cacheInfoManager.flushAll();
		Mockito.verify(cacheManager, Mockito.times(1)).getCacheNames();
		Mockito.verify(cacheManager, Mockito.times(0)).getCache(Mockito.anyString());
		Mockito.verify(cache, Mockito.times(0)).clear();
	}
	
	@Test
    public void flushAllWithCaches() throws Throwable {
		List<String> cacheNames = new ArrayList<String>();
		cacheNames.add("cache1");
		cacheNames.add("cache2");
		Mockito.when(cacheManager.getCacheNames()).thenReturn(cacheNames);
		cacheInfoManager.flushAll();
		Mockito.verify(cacheManager, Mockito.times(1)).getCacheNames();
		Mockito.verify(cacheManager, Mockito.times(4)).getCache(Mockito.anyString());
		Mockito.verify(cache, Mockito.times(2)).clear();
	}
	
}
