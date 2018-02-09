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
package com.agiletec.plugins.jacms.aps.system.services.contentpagemapper;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * @version 1.0
 * @author M.Casari
 */
public class ContentPageMapperManagerIntegrationTest extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetContentPageMapper() throws ApsSystemException {
		String codePage = _contentPageMapperManager.getPageCode("ART1");
		assertEquals(codePage, "homepage");
	}
	
    public void testReloadContentPageMapper() throws ApsSystemException{   
        _contentPageMapperManager.reloadContentPageMapper();
        String codePage = _contentPageMapperManager.getPageCode("ART1");
        assertEquals(codePage, "homepage");
    }
    
    private void init() throws Exception {
    	try {
    		_contentPageMapperManager = (IContentPageMapperManager) this.getService(JacmsSystemConstants.CONTENT_PAGE_MAPPER_MANAGER);
    	} catch (Throwable t) {
            throw new Exception();
        }
    }
    
    private IContentPageMapperManager _contentPageMapperManager = null;
    
}
