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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.api.IApiCatalogManager;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public abstract class AbstractApiFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractApiFinderAction.class);
	
    public List<List<ApiResource>> getResourceFlavours() {
        List<List<ApiResource>> group = new ArrayList<List<ApiResource>>();
        try {
			List<String> pluginCodes = new ArrayList<String>();
            Map<String, List<ApiResource>> mapping = this.getResourceFlavoursMapping(pluginCodes);
            this.addResourceGroup("core", mapping, group);
            for (int i = 0; i < pluginCodes.size(); i++) {
                String pluginCode = pluginCodes.get(i);
                this.addResourceGroup(pluginCode, mapping, group);
            }
            this.addResourceGroup("custom", mapping, group);
        } catch (Throwable t) {
        	_logger.error("Error extracting Flavours resources", t);
            //ApsSystemUtils.logThrowable(t, this, "getResourceFlavours");
            throw new RuntimeException("Error extracting Flavours resources", t);
        }
        return group;
    }
    
    private void addResourceGroup(String groupCode, Map<String, List<ApiResource>> mapping, List<List<ApiResource>> group) {
        List<ApiResource> singleGroup = mapping.get(groupCode);
        if (null != singleGroup) {
            BeanComparator comparator = new BeanComparator("resourceName");
            Collections.sort(singleGroup, comparator);
            group.add(singleGroup);
        }
    }
    
    private Map<String, List<ApiResource>> getResourceFlavoursMapping(List<String> pluginCodes) throws Throwable {
        Map<String, List<ApiResource>> finalMapping = new HashMap<String, List<ApiResource>>();
        try {
            Map<String, ApiResource> masterResources = this.getApiCatalogManager().getResources();
            Iterator<ApiResource> resourcesIter = masterResources.values().iterator();
            while (resourcesIter.hasNext()) {
                ApiResource apiResource = resourcesIter.next();
				if (this.includeIntoMapping(apiResource)) {
					List<ApiResource> resources = finalMapping.get(apiResource.getSectionCode());
					if (null == resources) {
						resources = new ArrayList<ApiResource>();
						finalMapping.put(apiResource.getSectionCode(), resources);
					}
					resources.add(apiResource);
					String pluginCode = apiResource.getPluginCode();
					if (null != pluginCode && !pluginCodes.contains(pluginCode)) {
						pluginCodes.add(pluginCode);
					}
				}
            }
            Collections.sort(pluginCodes);
        } catch (Throwable t) {
        	_logger.error("Error extracting resource Flavours mapping", t);
            //ApsSystemUtils.logThrowable(t, this, "getResourceFlavoursMapping");
            throw new RuntimeException("Error extracting resource Flavours mapping", t);
        }
        return finalMapping;
    }
    
    protected abstract boolean includeIntoMapping(ApiResource apiResource);
	
    protected IApiCatalogManager getApiCatalogManager() {
        return _apiCatalogManager;
    }
    public void setApiCatalogManager(IApiCatalogManager apiCatalogManager) {
        this._apiCatalogManager = apiCatalogManager;
    }
    
    private IApiCatalogManager _apiCatalogManager;
    
}