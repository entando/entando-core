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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceAction;

/**
 * Classe action a servizio della gestione attributi risorsa, 
 * estensione della action gestrice delle operazioni sulle risorse.
 * La classe ha il compito di permettere l'aggiunta diretta di una nuova risorsa 
 * sia nell'archivio (corrispondente al tipo) che nel contenuto che si stà editando.
 * @author E.Santoboni
 */
public class ExtendedResourceAction extends ResourceAction {

	private static final Logger _logger = LoggerFactory.getLogger(ExtendedResourceAction.class);
	
	public String entryFindResource() {
		this.setCategoryCode(null);
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				ResourceInterface resource = this.getResourceManager().addResource(this);
				this.buildEntryContentAnchorDest();
				ResourceAttributeActionHelper.joinResource(resource, this.getRequest());
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void buildEntryContentAnchorDest() {
		HttpSession session = this.getRequest().getSession();
		String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
		this.setEntryContentAnchorDest(anchorDest);
	}
	
	protected List<String> getGroupCodesForSearch() {
		List<Group> groups = this.getAllowedGroups();
		List<String> codesForSearch = new ArrayList<String>();
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			codesForSearch.add(group.getName());
		}
		return codesForSearch;
	}
	
	@Override
	public List<Group> getAllowedGroups() {
		List<Group> groups = new ArrayList<Group>();
		if (this.isCurrentUserMemberOf(Group.FREE_GROUP_NAME)) {
			groups.add(this.getGroupManager().getGroup(Group.FREE_GROUP_NAME));
		}
		String contentMainGroup = this.getContent().getMainGroup();
		if (contentMainGroup != null && !contentMainGroup.equals(Group.FREE_GROUP_NAME)) {
			groups.add(this.getGroupManager().getGroup(contentMainGroup));
		}
		return groups;
	}
	
	/**
	 * Restituisce il contenuto in sesione.
	 * @return Il contenuto in sesione.
	 */
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
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
	
	private String _entryContentAnchorDest;
	
}
