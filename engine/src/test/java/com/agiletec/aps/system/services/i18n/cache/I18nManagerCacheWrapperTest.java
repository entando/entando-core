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

import com.agiletec.aps.system.exception.ApsSystemException;
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
import com.agiletec.aps.system.services.i18n.II18nDAO;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.cache.Cache;

public class I18nManagerCacheWrapperTest {

    private static final String CACHE_NAME = I18nManagerCacheWrapper.I18N_MANAGER_CACHE_NAME;

    private static final String TEST_KEY = "LABEL_HELLO";

    @Mock
    private II18nDAO i18nDAO;
    
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
    
    @Test
    public void testInitCache() throws Exception {
        Cache fakeCache = Mockito.mock(Cache.class);
        when(this.springCacheManager.getCache(CACHE_NAME)).thenReturn(fakeCache);
        ApsProperties properties = I18nManagerTest.createLabel("It Label", "En Label");
        Map<String, ApsProperties> labels = new HashMap<>();
        labels.put(TEST_KEY, properties);
        when(this.i18nDAO.loadLabelGroups()).thenReturn(labels);
        this.cacheWrapper.initCache(this.i18nDAO);
        Mockito.verify(fakeCache, Mockito.times(1)).put(Mockito.anyString(), Mockito.any(ApsProperties.class));
    }
    
    @Test(expected = ApsSystemException.class)
    public void testInitCacheWithErrors() throws Exception {
        Cache fakeCache = Mockito.mock(Cache.class);
        when(this.springCacheManager.getCache(CACHE_NAME)).thenReturn(fakeCache);
        Mockito.doThrow(RuntimeException.class).when(fakeCache).get(Mockito.anyString());
        try {
            this.cacheWrapper.initCache(this.i18nDAO);
            Assert.fail();
        } catch (Exception e) {
            throw e;
        } finally {
            Mockito.verify(fakeCache, Mockito.times(0)).put(Mockito.anyString(), Mockito.any(ApsProperties.class));
        }
    }
    
    @Test
    public void getLabelsGroup() throws Exception {
        ApsProperties properties = this.cacheWrapper.getLabelGroup(TEST_KEY);
        Assert.assertNotNull(properties);
        Assert.assertEquals("ciao", properties.get("it"));
    }
    
    @Test
    public void update() {
        cacheWrapper.updateLabelGroup(TEST_KEY, I18nManagerTest.createLabel("si", "yes"));
        ApsProperties properties = this.cacheWrapper.getLabelGroup(TEST_KEY);
        Assert.assertNotNull(properties);
        Assert.assertEquals("yes", properties.get("en"));
    }
    
    @Test(expected = CacheItemNotFoundException.class)
    public void updateInvalidEntry() {
        cacheWrapper.updateLabelGroup("THIS_DO_NOT_EXISTS", I18nManagerTest.createLabel("si", "yes"));
    }
    
    @Test
    public void delete() {
        cacheWrapper.removeLabelGroup(TEST_KEY);
        ApsProperties properties = this.cacheWrapper.getLabelGroup(TEST_KEY);
        Assert.assertNull(properties);
    }

}
