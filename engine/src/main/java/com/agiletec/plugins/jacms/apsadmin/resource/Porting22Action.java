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
package com.agiletec.plugins.jacms.apsadmin.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;

/**
 * Classe action used to improve the porting from jAPS 2.0.x to version 2.2.x
 * @author E.Santoboni
 */
public class Porting22Action extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(Porting22Action.class);
	
	public String showResources() {
		try {
			List<String> groupCodes = new ArrayList<String>();
			List<Group> systemGroups = this.getGroupManager().getGroups();
			for (int i = 0; i < systemGroups.size(); i++) {
				groupCodes.add(systemGroups.get(i).getName());
			}
			List<String> resourcesId = this.getResourceManager().searchResourcesId(null, null, null, groupCodes);
			this.setResourcesId(resourcesId);
		} catch (Throwable t) {
			_logger.error("error in showResources", t);
			//ApsSystemUtils.logThrowable(t, this, "showResources");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String executeRefresh() {
		try {
			this.getResourceManager().refreshMasterFileNames();
		} catch (Throwable t) {
			_logger.error("error in startReload", t);
			//ApsSystemUtils.logThrowable(t, this, "startReload");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<String> getResourcesId() {
		return _resourcesId;
	}
	public void setResourcesId(List<String> resourcesId) {
		this._resourcesId = resourcesId;
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	private List<String> _resourcesId;
	private IResourceManager _resourceManager;
	private IGroupManager _groupManager;
	
}