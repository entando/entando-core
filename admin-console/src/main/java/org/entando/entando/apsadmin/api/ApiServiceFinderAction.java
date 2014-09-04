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
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.entando.entando.apsadmin.api.model.ApiSelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ApiServiceFinderAction extends AbstractApiFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiServiceFinderAction.class);
	
	public String updateAllStatusOfGroup() {
		try {
			if (this.getServiceGroup() == null) {
				this.addActionMessage(this.getText("error.service.group.required"));
				return INPUT;
			}
			Map<String, List<ApiSelectItem>> groups = this.getServiceFlavours();
			List<ApiSelectItem> group = groups.get(this.getServiceGroup());
			if (group == null) {
				this.addActionMessage(this.getText("error.service.group.invalid"));
				return INPUT;
			}
			for (int i = 0; i < group.size(); i++) {
				ApiSelectItem serviceItem = group.get(i);
				ApiService service = this.getApiCatalogManager().getApiService(serviceItem.getKey());
				boolean activeService = (this.getRequest().getParameter(service.getKey() + "_active") != null);
				boolean publicService = (this.getRequest().getParameter(service.getKey() + "_public") != null);
				if (activeService != service.isActive() || publicService != !service.isHidden()) {
					service.setActive(activeService);
					service.setHidden(!publicService);
					this.getApiCatalogManager().updateService(service);
					this.addActionMessage(this.getText("message.service.status.updated", new String[]{serviceItem.getKey(), serviceItem.getValue()}));
					_logger.info("Updated api service status - Service Key '{}'", serviceItem.getKey());
				}
			}
		} catch (Throwable t) {
			_logger.error("error in updateAllStatusOfGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "updateAllStatusOfGroup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Map<String, List<ApiSelectItem>> getServiceFlavours() {
		Map<String, List<ApiSelectItem>> groups = new HashMap<String, List<ApiSelectItem>>();
		try {
			this.buildServiceGroups(groups);
			Iterator<List<ApiSelectItem>> groupIter = groups.values().iterator();
			while (groupIter.hasNext()) {
				List<ApiSelectItem> group = groupIter.next();
				BeanComparator comparator = new BeanComparator("value");
				Collections.sort(group, comparator);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting Flavours services", t);
			//ApsSystemUtils.logThrowable(t, this, "getServiceFlavours");
			throw new RuntimeException("Error extracting Flavours services", t);
		}
		return groups;
	}
	
	private void buildServiceGroups(Map<String, List<ApiSelectItem>> groups) throws Throwable {
		try {
			Map<String, ApiService> serviceMap = this.getApiCatalogManager().getServices();
			if (null == serviceMap || serviceMap.isEmpty()) return;
			Iterator<ApiService> services = serviceMap.values().iterator();
			while (services.hasNext()) {
				ApiService apiService = services.next();
				if (this.includeServiceIntoMapping(apiService)) {
					ApiMethod masterMethod = apiService.getMaster();
					String pluginCode = masterMethod.getPluginCode();
					if (null != pluginCode && pluginCode.trim().length() > 0) {
						this.addService(pluginCode, apiService, groups);
					} else if (masterMethod.getSource().equals("core")) {
						this.addService(masterMethod.getSource(), apiService, groups);
					} else {
						this.addService("custom", apiService, groups);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in buildServiceGroups", t);
			//ApsSystemUtils.logThrowable(t, this, "buildServiceGroups");
			throw t;
		}
	}
	
	protected boolean includeServiceIntoMapping(ApiService apiService) {
		return true;
	}
	
	private void addService(String groupCode, ApiService apiService, Map<String, List<ApiSelectItem>> groups) {
		List<ApiSelectItem> group = groups.get(groupCode);
		if (null == group) {
			group = new ArrayList<ApiSelectItem>();
			groups.put(groupCode, group);
		}
		String description = super.getTitle(apiService.getKey(), apiService.getDescription());
		ApiSelectItem item = new ApiSelectItem(apiService.getKey(), description, groupCode);
		item.setActiveItem(apiService.isActive());
		item.setPublicItem(!apiService.isHidden());
		group.add(item);
	}
	
	@Override
	protected boolean includeIntoMapping(ApiResource apiResource) {
		ApiMethod GETMethod = apiResource.getGetMethod();
        return (null != GETMethod && GETMethod.isCanSpawnOthers());
    }
	
	public String getServiceGroup() {
		return _serviceGroup;
	}
	public void setServiceGroup(String serviceGroup) {
		this._serviceGroup = serviceGroup;
	}
	
	private String _serviceGroup;
	
}
