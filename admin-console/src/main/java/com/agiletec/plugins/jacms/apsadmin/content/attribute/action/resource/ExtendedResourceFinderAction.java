/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceFinderAction;

/**
 * Classe action a servizio della gestione attributi risorsa, 
 * estensione della action gestrice delle operazioni di ricerca risorse.
 * @author E.Santoboni
 */
public class ExtendedResourceFinderAction extends ResourceFinderAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(ExtendedResourceFinderAction.class);
	
	public String entryFindResource() {
		this.setCategoryCode(null);
		return SUCCESS;
	}
	
	@Override
	public List<String> getResources() throws Throwable {
		List<String> resourcesId = null;
		try {
			List<String> groupCodes = new ArrayList<String>();
			groupCodes.add(Group.FREE_GROUP_NAME);
			if (null != this.getContent().getMainGroup()) {
				groupCodes.add(this.getContent().getMainGroup());
			}
			resourcesId = this.getResourceManager().searchResourcesId(this.getResourceTypeCode(), 
					this.getText(), this.getCategoryCode(), groupCodes);
		} catch (Throwable t) {
			_logger.error("error in getResources", t);
			throw t;
		}
		return resourcesId;
	}
	
	/**
	 * Restituisce il contenuto in sessione.
	 * @return Il contenuto in sessione.
	 */
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}
	
	/**
	 * Aggiunge una risorsa ad un Attributo.
	 * @return SUCCESS se è andato a buon fine, FAILURE in caso contrario
	 */
	public String joinResource() {
		try {
			String resourceId = this.getResourceId();
			ResourceInterface resource = this.getResourceManager().loadResource(resourceId);
			this.buildEntryContentAnchorDest();
			ResourceAttributeActionHelper.joinResource(resource, this.getRequest());
		} catch (Throwable t) {
			_logger.error("error in joinResource", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void buildEntryContentAnchorDest() {
		HttpSession session = this.getRequest().getSession();
		String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
		this.setEntryContentAnchorDest(anchorDest);
	}
	
	public boolean isOnEditContent() {
		return true;
	}
	
	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}
	
	public String getResourceId() {
		return _resourceId;
	}
	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}
	
	public String getEntryContentAnchorDest() {
		if (null == this._entryContentAnchorDest) {
			this.buildEntryContentAnchorDest();
		}
		return _entryContentAnchorDest;
	}
	protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
		this._entryContentAnchorDest = entryContentAnchorDest;
	}
	
	private String _contentOnSessionMarker;
	
	private String _resourceId;
	private String _entryContentAnchorDest;
	
}