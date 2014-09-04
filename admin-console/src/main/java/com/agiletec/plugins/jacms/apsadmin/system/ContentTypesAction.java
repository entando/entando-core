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
