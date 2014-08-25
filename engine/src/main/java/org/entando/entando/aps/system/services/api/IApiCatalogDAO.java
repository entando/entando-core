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
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;

/**
 * Interfrace for resource and service Api Objects
 * @author E.Santoboni
 */
public interface IApiCatalogDAO {
    
    public void loadApiStatus(Map<String, ApiResource> resources);
    
    public void resetApiStatus(String resourceCode, ApiMethod.HttpMethod httpMethod);
    
    public void saveApiStatus(ApiMethod method);
    
    @Deprecated
    public Map<String, ApiService> loadServices(Map<String, ApiMethod> methods);
    
    public Map<String, ApiService> loadServices(List<ApiMethod> methods);
    
    public void addService(ApiService service);
	
    public void updateService(ApiService service);
	
    public void deleteService(String key);
    
}