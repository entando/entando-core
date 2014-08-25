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
package com.agiletec.plugins.jacms.apsadmin.resource.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseActionHelper;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Classe helper della gestione risorse.
 * @author E.Santoboni
 */
public class ResourceActionHelper extends BaseActionHelper implements IResourceActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceActionHelper.class);
	
	@Override
	public Map<String, List> getReferencingObjects(ResourceInterface resource, HttpServletRequest request) throws ApsSystemException {
		Map<String, List> references = new HashMap<String, List>();
    	if (null != resource) {
    		return this.getReferencingObjects(resource.getId(), request);
    	}
    	return references;
	}
	
	@Override
	public Map<String, List> getReferencingObjects(String resourceId, HttpServletRequest request) throws ApsSystemException {
		Map<String, List> references = new HashMap<String, List>();
    	try {
    		String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(ResourceUtilizer.class);
			for (int i=0; i<defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error in getReferencingObjects", t);
					//ApsSystemUtils.logThrowable(t, this, "getReferencingObjects");
					service = null;
				}
				if (service != null) {
					ResourceUtilizer resourceUtilizer = (ResourceUtilizer) service;
					List utilizers = resourceUtilizer.getResourceUtilizers(resourceId);
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(resourceUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
    	} catch (Throwable t) {
    		_logger.error("Error extracting referencing objects by resource '{}'", resourceId, t);
    		//ApsSystemUtils.logThrowable(t, this, "getReferencingObjects", "Error extracting referencing objects by resource '" + resourceId +"'");
    		throw new ApsSystemException("Errore in getReferencingObjects", t);
    	}
    	return references;
	}
	
	@Override
	public List<Group> getAllowedGroups(UserDetails currentUser) {
		return super.getAllowedGroups(currentUser);
    }
	
	@Override
	public List<String> searchResources(String resourceType, 
			String insertedText, String categoryCode, UserDetails currentUser) throws Throwable {
		return this.searchResources(resourceType, insertedText, null, null, categoryCode, currentUser);
	}
	
	@Override
	public List<String> searchResources(String resourceType, String insertedText, String groupName, 
			String insertedFileName, String categoryCode, UserDetails currentUser) throws Throwable {
		List<String> allowedGroups = new ArrayList<String>();
		if (null != groupName && groupName.trim().length() > 0) {
			if (!this.getAuthorizationManager().isAuthOnGroup(currentUser, groupName)) {
				_logger.info("User '{}' not allowed to manage resources of group '{}'", currentUser.getUsername(), groupName);
				return new ArrayList<String>();
			}
			allowedGroups.add(groupName);
		} else {
			allowedGroups.addAll(super.getAllowedGroupCodes(currentUser));
		}
		List<String> resourcesId = this.getResourceManager().searchResourcesId(resourceType, 
				insertedText, insertedFileName, categoryCode, allowedGroups);
		return resourcesId;
	}
	
    protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	private IResourceManager _resourceManager;
	private ICategoryManager _categoryManager;
	
}
