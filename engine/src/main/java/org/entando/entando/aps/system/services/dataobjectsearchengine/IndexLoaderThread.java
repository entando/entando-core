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
package org.entando.entando.aps.system.services.dataobjectsearchengine;

import java.util.Date;
import java.util.List;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread Class delegate to load dataobject index, in use on SearchEngine.
 *
 * @author E.Santoboni
 */
public class IndexLoaderThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(IndexLoaderThread.class);

	public IndexLoaderThread(SearchEngineManager searchEngineManager,
			IDataObjectManager dataObjectManager, IIndexerDAO indexerDao) {
		this._dataObjectManager = dataObjectManager;
		this._searchEngineManager = searchEngineManager;
		this._indexerDao = indexerDao;
	}

	@Override
	public void run() {
		LastReloadInfo reloadInfo = new LastReloadInfo();
		try {
			this.loadNewIndex();
			reloadInfo.setResult(LastReloadInfo.ID_SUCCESS_RESULT);
		} catch (Throwable t) {
			reloadInfo.setResult(LastReloadInfo.ID_FAILURE_RESULT);
			_logger.error("error in run", t);
		} finally {
			reloadInfo.setDate(new Date());
			this._searchEngineManager.notifyEndingIndexLoading(reloadInfo, this._indexerDao);
			this._searchEngineManager.sellOfQueueEvents();
		}
	}

	private void loadNewIndex() throws Throwable {
		try {
			List<String> contentsId = this._dataObjectManager.searchId(null);
			for (int i = 0; i < contentsId.size(); i++) {
				String id = contentsId.get(i);
				this.reloadContentIndex(id);
			}
			_logger.info("Indicizzazione effettuata");
		} catch (Throwable t) {
			_logger.error("error in reloadIndex", t);
			throw t;
		}
	}

	private void reloadContentIndex(String id) {
		try {
			DataObject dataObject = this._dataObjectManager.loadDataObject(id, true, false);
			if (dataObject != null) {
				this._indexerDao.add(dataObject);
				_logger.debug("Indexed DataObject {}", dataObject.getId());
			}
		} catch (Throwable t) {
			_logger.error("Error reloading index: DataObject id {}", id, t);
		}
	}

	private SearchEngineManager _searchEngineManager;
	private IDataObjectManager _dataObjectManager;
	private IIndexerDAO _indexerDao;

}
