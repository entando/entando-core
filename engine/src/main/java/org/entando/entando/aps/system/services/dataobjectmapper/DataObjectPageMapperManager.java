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
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

/**
 * Servizio gestore della mappa dei datatypes pubblicati nelle pagine. Il
 * servizio carica e gestisce nella mappa esclusivamente i datatypes pubblicati
 * esplicitamente nel frame principale delle pagine.
 *
 * @author W.Ambu
 */
public class DataObjectPageMapperManager extends AbstractService implements IDataObjectPageMapperManager, PageChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectPageMapperManager.class);

	@Override
	public void init() throws Exception {
		this.createDataObjectPageMapper();
		_logger.debug("{} ready.", this.getClass().getName());
	}

	/**
	 * Effettua il caricamento della mappa contenuti pubblicati / pagine
	 *
	 * @throws ApsSystemException
	 */
	@Override
	public void reloadContentPageMapper() throws ApsSystemException {
		this.createDataObjectPageMapper();
	}

	@Override
	public String getPageCode(String contentId) {
		return this.getDataObjectPageMapper().getPageCode(contentId);
	}

	/**
	 * Crea la mappa dei DataObject pubblicati nelle pagine.
	 *
	 * @throws ApsSystemException
	 */
	private void createDataObjectPageMapper() throws ApsSystemException {
		this._dataObjectPageMapper = new DataObjectPageMapper();
		try {
			IPage root = this.getPageManager().getOnlineRoot();
			this.searchPublishedDataObjects(root);
		} catch (Throwable t) {
			_logger.error("Error loading ContentPageMapper", t);
			throw new ApsSystemException("Errore loading ContentPageMapper", t);
		}
	}

	/**
	 * Cerca i DataObject pubblicati e li aggiunge al mapper. Nella ricerca
	 * vengono considerati solamente i DataObject pubblicati nel mainFrame e la
	 * ricerca viene estesa anche alle pagine figlie di quella specificate.
	 *
	 * @param page La pagina nel qual cercare i DataObject pubblicati.
	 */
	private void searchPublishedDataObjects(IPage page) {
		PageModel pageModel = page.getModel();
		if (pageModel != null) {
			int mainFrame = pageModel.getMainFrame();
			Widget[] widgets = page.getWidgets();
			Widget widget = null;
			if (null != widgets && mainFrame != -1) {
				widget = widgets[mainFrame];
			}
			ApsProperties config = (null != widget) ? widget.getConfig() : null;
			String contentId = (null != config) ? config.getProperty("contentId") : null;
			if (null != contentId) {
				this.getDataObjectPageMapper().add(contentId, page.getCode());
			}
			IPage[] children = page.getChildren();
			for (int i = 0; i < children.length; i++) {
				this.searchPublishedDataObjects(children[i]);
			}
		}
	}

	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		try {
			this.reloadContentPageMapper();
			String pagecode = (null != event.getPage()) ? event.getPage().getCode() : "*undefined*";
			_logger.debug("Notified page change event for page '{}'", pagecode);
		} catch (Throwable t) {
			_logger.error("Error notifying event", t);
		}
	}

	/**
	 * Restituisce la mappa dei DataObject pubblicati nelle pagine.
	 *
	 * @return La mappa dei DataObject pubblicati nelle pagine.
	 */
	protected DataObjectPageMapper getDataObjectPageMapper() {
		return _dataObjectPageMapper;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private IPageManager _pageManager;

	/**
	 * La mappa dei DataObject pubblicati nelle pagine.
	 */
	private DataObjectPageMapper _dataObjectPageMapper;

}
