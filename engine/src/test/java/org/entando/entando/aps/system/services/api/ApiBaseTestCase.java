/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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