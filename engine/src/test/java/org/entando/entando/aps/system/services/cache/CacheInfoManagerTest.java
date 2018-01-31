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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author E.Santoboni
 */
public class CacheInfoManagerTest {
	
	@Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private Cache.ValueWrapper valueWrapperForExpirationTime;
	
	@Mock
    private Cache.ValueWrapper valueWrapperForGroups;
	
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

	@InjectMocks
    private CacheInfoManager cacheInfoManager;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Map<String, Date> map = new HashMap<String, Date>();
		Mockito.when(valueWrapperForExpirationTime.get()).thenReturn(map);
		Mockito.when(cache.get(Mockito.startsWith(ICacheInfoManager.EXPIRATIONS_CACHE_NAME_PREFIX))).thenReturn(valueWrapperForExpirationTime);
		Map<String, List<String>> groupsMap = new HashMap<String, List<String>>();
		List<String> list_a = Arrays.asList(new String[]{"key_a1", "key_a2", "key_a3"});
		List<String> list_b = Arrays.asList(new String[]{"key_b1", "key_b2", "key_b3", "key_b4"});
		groupsMap.put("group_1", new ArrayList<String>(list_a));
		groupsMap.put("group_2", new ArrayList<String>(list_b));
		Mockito.when(valueWrapperForGroups.get()).thenReturn(groupsMap);
		Mockito.when(cache.get(Mockito.startsWith(ICacheInfoManager.GROUP_CACHE_NAME_PREFIX))).thenReturn(valueWrapperForGroups);
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
		Object requiredMap = cacheInfoManager.getFromCache(ICacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME, 
				ICacheInfoManager.GROUP_CACHE_NAME_PREFIX + ICacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME);
		Assert.assertTrue(requiredMap instanceof Map);
		Assert.assertNotNull(requiredMap);
		Assert.assertEquals(2, ((Map) requiredMap).size());
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
	
	@Test
    public void flushEntry() throws Throwable {
		String targetCache = "targetCacheName3";
		String cacheKey = "testkey3";
		cacheInfoManager.flushEntry(targetCache, cacheKey);
		Mockito.verify(cacheManager, Mockito.times(1)).getCache(Mockito.eq(targetCache));
		Mockito.verify(cache, Mockito.times(1)).evict(Mockito.eq(cacheKey));
	}
	
	@Test
    public void putInCache() throws Throwable {
		String targetCache = "targetCacheName3";
		String cacheKey = "testkey3";
		cacheInfoManager.putInCache(targetCache, cacheKey, "Some value");
		Mockito.verify(cacheManager, Mockito.times(1)).getCache(targetCache);
		Mockito.verify(cache, Mockito.times(1)).put(cacheKey, "Some value");
	}
	
	@Test
    public void putInCacheWithGroups() throws Throwable {
		String targetCache = "targetCacheName3";
		String cacheKey = "testkey3";
		String[] groups = new String[]{"group_1", "group_2"};
		cacheInfoManager.putInCache(targetCache, cacheKey, "Some value", groups);
		Mockito.verify(cacheManager, Mockito.times(1)).getCache(targetCache);
		Mockito.verify(cache, Mockito.times(1)).put(cacheKey, "Some value");
		Mockito.verify(cacheManager, Mockito.times(1)).getCache(ICacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME);
	}
	
	@Test
    public void putInGroup() throws Throwable {
		String targetCache = "targetCacheName4";
		String cacheKey = "testkey4";
		String[] groups = new String[]{"group_1", "group_2"};
		cacheInfoManager.putInGroup(targetCache, cacheKey, groups);
		Mockito.verify(cacheManager, Mockito.times(0)).getCache(targetCache);
		Mockito.verify(cache, Mockito.times(0)).put(cacheKey, Mockito.eq(Mockito.anyString()));
		Mockito.verify(cacheManager, Mockito.times(1)).getCache(ICacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME);
	}
	
	@Test
    public void flushGroup_1() throws Throwable {
		this.flushGroup("group_1", 3);
	}
	
	@Test
    public void flushGroup_2() throws Throwable {
		this.flushGroup("group_2", 4);
	}
	
    private void flushGroup(String groupName, int expectedEvict) throws Throwable {
		String targetCache = "targetCacheName5";
		cacheInfoManager.flushGroup(targetCache, groupName);
		Mockito.verify(cacheManager, Mockito.times(1)).getCache(ICacheInfoManager.CACHE_INFO_MANAGER_CACHE_NAME);
		Mockito.verify(cacheManager, Mockito.times(expectedEvict)).getCache(targetCache);
		Mockito.verify(cache, Mockito.times(expectedEvict)).evict(Mockito.any(Object.class));
		Mockito.verify(cache, Mockito.times(1)).put(Mockito.startsWith(ICacheInfoManager.GROUP_CACHE_NAME_PREFIX), Mockito.any(Object.class));
	}
	
}
