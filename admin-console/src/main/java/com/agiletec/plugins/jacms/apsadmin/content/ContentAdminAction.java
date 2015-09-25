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
package com.agiletec.plugins.jacms.apsadmin.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.LastReloadInfo;

/**
 * Classi Action delegata alla esecuzione delle operazioni 
 * di amministrazione dei contenuti.
 * @author E.Santoboni
 */
public class ContentAdminAction extends BaseAction implements IContentAdminAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentAdminAction.class);
	
	@Override
	public String reloadContentsIndex() {
		try {
			this.getSearchEngineManager().startReloadContentsReferences();
			_logger.info("Reload contents index started");
		} catch (Throwable t) {
			_logger.error("error in reloadContentsIndex", t);
			//ApsSystemUtils.logThrowable(t, this, "reloadContentsIndex");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String reloadContentsReference() {
		try {
			String typeCode = null;
			this.getContentManager().reloadEntitiesReferences(typeCode);
			_logger.info("Reload contents reference started");
		} catch (Throwable t) {
			_logger.error("error in reloadContentsReference", t);
			//ApsSystemUtils.logThrowable(t, this, "reloadContentsReference");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getContentManagerStatus() {
		return this.getContentManager().getStatus();
	}
	
	public int getSearcherManagerStatus() {
		return this.getSearchEngineManager().getStatus();
	}
	
	/**
	 * @deprecated From jAPS 2.0 version 2.0.9. Use getContentManagerStatus() method
	 */
	public int getContentManagerState() {
		return this.getContentManagerStatus();
	}
	
	/**
	 * @deprecated From jAPS 2.0 version 2.0.9. Use getSearcherManagerStatus() method
	 */
	public int getSearcherManagerState() {
		return this.getSearcherManagerStatus();
	}
	
	public LastReloadInfo getLastReloadInfo() {
		return this.getSearchEngineManager().getLastReloadInfo();
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected ICmsSearchEngineManager getSearchEngineManager() {
		return _searchEngineManager;
	}
	public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
		this._searchEngineManager = searchEngineManager;
	}
	
	private IContentManager _contentManager;
	private ICmsSearchEngineManager _searchEngineManager;
	
}
