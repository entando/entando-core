/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.api;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.api.server.IResponseBuilder;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @author E.Santoboni
 */
public class ApiBaseTestCase extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
    	super.setUp();
    	this.init();
    }
	
    private void init() throws Exception {
    	try {
    		this._responseBuilder = (IResponseBuilder) this.getApplicationContext().getBean(SystemConstants.API_RESPONSE_BUILDER);
    		this._apiCatalogManager = (IApiCatalogManager) this.getService(SystemConstants.API_CATALOG_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
    @Override
	protected void tearDown() throws Exception {
    	try {
    		ApiTestHelperDAO helperDao = new ApiTestHelperDAO();
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		helperDao.setDataSource(dataSource);
    		helperDao.cleanApiStatus();
    		helperDao.cleanServices();
    		super.tearDown();
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
	}
    
    protected IResponseBuilder getResponseBuilder() {
		return _responseBuilder;
	}
    protected IApiCatalogManager getApiCatalogManager() {
		return _apiCatalogManager;
	}
	
	private IResponseBuilder _responseBuilder;
	private IApiCatalogManager _apiCatalogManager = null;
    
}