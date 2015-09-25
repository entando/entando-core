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
package com.agiletec.plugins.jacms.apsadmin.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.entity.type.EntityTypesAction;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;

/**
 * @author E.Santoboni
 */
public class ContentTypesAction extends EntityTypesAction implements IContentReferencesReloadingAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentTypesAction.class);
	
	@Override
	public String reloadContentsIndexes() {
		try {
			this.getSearchEngineManager().startReloadContentsReferences();
			_logger.info("Reload context index started");
		} catch (Throwable t) {
			_logger.error("error in reloadContentsIndexs", t);
			//ApsSystemUtils.logThrowable(t, this, "reloadContentsIndexs");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getSearcherManagerStatus() {
		return this.getSearchEngineManager().getStatus();
	}
	
	protected ICmsSearchEngineManager getSearchEngineManager() {
		return _searchEngineManager;
	}
	public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
		this._searchEngineManager = searchEngineManager;
	}
	
	private ICmsSearchEngineManager _searchEngineManager;
	
}
