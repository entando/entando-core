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

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiService;


/**
 * @author E.Santoboni
 */
public class TestApiCatalogManager extends ApiBaseTestCase {
	
    public void testGetMethod() throws Throwable {
    	ApiMethod method = this.getApiCatalogManager().getMethod(ApiMethod.HttpMethod.GET, "getService");
    	assertNotNull(method);
    	assertTrue(method.isActive());
    }
    
    public void testGetMethods() throws Throwable {
    	List<ApiMethod> methods = this.getApiCatalogManager().getMethods(ApiMethod.HttpMethod.GET);
    	assertNotNull(methods);
    	assertTrue(methods.size() > 0);
    }
    
    public void testUpdateMethodStatus() throws Throwable {
    	ApiMethod method = this.getApiCatalogManager().getMethod(ApiMethod.HttpMethod.GET, "getService");
    	method.setStatus(false);
    	this.getApiCatalogManager().updateMethodConfig(method);
    	method = this.getApiCatalogManager().getMethod(ApiMethod.HttpMethod.GET, "getService");
    	assertFalse(method.isActive());
    }
    
    public void testGetServices() throws Throwable {
    	Map<String, ApiService> services = this.getApiCatalogManager().getServices();
    	assertNotNull(services);
    	assertTrue(services.size() == 0);
    }
    
}