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
package org.entando.entando.apsadmin.api;

import org.entando.entando.aps.system.services.api.IApiCatalogManager;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestApiServiceFinderAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testServiceList() throws Throwable {
		String result = this.executeListServices("admin");
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeListServices(null);
		assertEquals("apslogin", result);
	}
	
	private String executeListServices(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Api/Service", "list");
		return this.executeAction();
	}
	
	private void init() throws Exception {
    	try {
    		this._apiCatalogManager = (IApiCatalogManager) this.getService(SystemConstants.API_CATALOG_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IApiCatalogManager _apiCatalogManager = null;
	
}