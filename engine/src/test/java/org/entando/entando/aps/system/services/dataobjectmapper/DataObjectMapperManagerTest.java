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
package org.entando.entando.aps.system.services.dataobjectmapper;

import org.entando.entando.aps.system.services.dataobjectmapper.cache.*;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class DataObjectMapperManagerTest {
	
    @Mock
    private IPageManager pageManager;
	
	@Mock
    private DataObjectMapperCacheWrapper cacheWrapper;
	
	@InjectMocks
    private DataObjectPageMapperManager pageMapperManager;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public void testInit() throws Throwable {
        pageMapperManager.init();
		Mockito.verify(cacheWrapper, Mockito.times(1)).initCache(pageManager);
    }
	
	@Test
    public void testReload() throws Throwable {
        pageMapperManager.reloadDataObjectPageMapper();
		Mockito.verify(cacheWrapper, Mockito.times(1)).initCache(pageManager);
    }
	
	@Test
    public void testGetPage() throws Throwable {
		Mockito.when(cacheWrapper.getPageCode(Mockito.anyString())).thenReturn("pageCode");
        String pageCode = this.pageMapperManager.getPageCode("dataId");
		Assert.assertNotNull(pageCode);
		Assert.assertEquals("pageCode", pageCode);
    }
	
	@Test
    public void testUpdate() throws Throwable {
        pageMapperManager.updateFromPageChanged(Mockito.any(PageChangedEvent.class));
		Mockito.verify(cacheWrapper, Mockito.times(1)).initCache(pageManager);
    }
	
}
