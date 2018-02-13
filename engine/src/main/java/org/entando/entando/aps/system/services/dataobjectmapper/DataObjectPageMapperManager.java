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
package org.entando.entando.aps.system.services.dataobjectmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;

import org.entando.entando.aps.system.services.dataobjectmapper.cache.IDataObjectMapperCacheWrapper;

/**
 * Servizio gestore della mappa dei datatypes pubblicati nelle pagine. Il
 * servizio carica e gestisce nella mappa esclusivamente i datatypes pubblicati
 * esplicitamente nel frame principale delle pagine.
 *
 * @author W.Ambu
 */
public class DataObjectPageMapperManager extends AbstractService implements IDataObjectPageMapperManager, PageChangedObserver {

	private static final Logger logger = LoggerFactory.getLogger(DataObjectPageMapperManager.class);
	
	private IPageManager pageManager;
	private IDataObjectMapperCacheWrapper cacheWrapper;

	@Override
	public void init() throws Exception {
		this.getCacheWrapper().initCache(this.getPageManager());
		logger.debug("{} ready.", this.getClass().getName());
	}

	/**
	 * Effettua il caricamento della mappa contenuti pubblicati / pagine
	 * @throws ApsSystemException
	 */
	@Override
	public void reloadDataObjectPageMapper() throws ApsSystemException {
		this.getCacheWrapper().initCache(this.getPageManager());
	}

	@Override
	public String getPageCode(String dataId) {
		return this.getCacheWrapper().getPageCode(dataId);
	}

	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		try {
			this.reloadDataObjectPageMapper();
			String pagecode = (null != event.getPage()) ? event.getPage().getCode() : "*undefined*";
			logger.debug("Notified page change event for page '{}'", pagecode);
		} catch (Throwable t) {
			logger.error("Error notifying event", t);
		}
	}

	protected IPageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this.pageManager = pageManager;
	}

	protected IDataObjectMapperCacheWrapper getCacheWrapper() {
		return cacheWrapper;
	}

	public void setCacheWrapper(IDataObjectMapperCacheWrapper cacheWrapper) {
		this.cacheWrapper = cacheWrapper;
	}
	
}
