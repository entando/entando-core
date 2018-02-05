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
package org.entando.entando.aps.system.services.api;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiService;


/**
 * @author E.Santoboni
 */
public class ApiCatalogManagerIntegrationTest extends ApiBaseTestCase {
	
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