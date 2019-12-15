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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.event.PublicDataChangedEvent;
import org.entando.entando.aps.system.services.dataobject.event.PublicDataChangedObserver;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio detentore delle operazioni di indicizzazione di oggetti ricercabili
 * tramite motore di ricerca.
 *
 * @author M.Diana - E.Santoboni
 */
public class SearchEngineManager extends AbstractService
		implements IDataObjectSearchEngineManager, PublicDataChangedObserver, EntityTypesChangingObserver {

	private static final Logger _logger = LoggerFactory.getLogger(SearchEngineManager.class);

	@Override
	public void init() throws Exception {
		this._indexerDao = this.getFactory().getIndexer();
		this._searcherDao = this.getFactory().getSearcher();
	}

	@Override
	public void refresh() throws Throwable {
		this.release();
		this._lastReloadInfo = null;
		this.getFactory().init();
		this.init();
	}

	@Override
	public void updateFromPublicDataObjectChanged(PublicDataChangedEvent event) {
		if (this.getStatus() == STATUS_RELOADING_INDEXES_IN_PROGRESS) {
			this._publicContentChangedEventQueue.add(0, event);
		} else {
			this.manageEvent(event);
		}
	}

	private void manageEvent(PublicDataChangedEvent event) {
		DataObject dataObject = event.getDataObject();
		try {
			switch (event.getOperationCode()) {
				case PublicDataChangedEvent.INSERT_OPERATION_CODE:
					this.addEntityToIndex(dataObject);
					break;
				case PublicDataChangedEvent.REMOVE_OPERATION_CODE:
					this.deleteIndexedEntity(dataObject.getId());
					break;
				case PublicDataChangedEvent.UPDATE_OPERATION_CODE:
					this.updateIndexedEntity(dataObject);
					break;
			}
		} catch (Throwable t) {
			_logger.error("Error on event notification", t);
		}
	}

	protected void sellOfQueueEvents() {
		int size = this._publicContentChangedEventQueue.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				PublicDataChangedEvent event = (PublicDataChangedEvent) this._publicContentChangedEventQueue.get(0);
				this.manageEvent(event);
				this._publicContentChangedEventQueue.remove(0);
			}
		}
	}

	@Override
	public Thread startReloadDataObjectsReferences() throws ApsSystemException {
		String newTempSubDirectory = "indexdir" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
		return this.startReloadDataObjectsReferences(newTempSubDirectory);
	}

	@Override
	public Thread startReloadDataObjectsReferences(String subDirectory) throws ApsSystemException {
		IndexLoaderThread loaderThread = null;
		if (this.getStatus() == STATUS_READY || this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
			try {
				this._newTempSubDirectory = subDirectory;
				IIndexerDAO newIndexer = this.getFactory().getIndexer(_newTempSubDirectory);
				loaderThread = new IndexLoaderThread(this, this.getDataObjectManager(), newIndexer);
				String threadName = RELOAD_THREAD_NAME_PREFIX + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
				loaderThread.setName(threadName);
				this.setStatus(STATUS_RELOADING_INDEXES_IN_PROGRESS);
				loaderThread.start();
				_logger.info("Reload Contents References job started");
			} catch (Throwable t) {
				throw new ApsSystemException("Error reloading Contents References", t);
			}
		} else {
			_logger.info("Reload Contents References job suspended: current status: {}", this.getStatus());
		}
		return loaderThread;
	}

	@Override
	public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
		if (((IManager) this.getDataObjectManager()).getName().equals(event.getEntityManagerName())) {
			if (this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
				return;
			}
			boolean needToReload = false;
			if (event.getOperationCode() == EntityTypesChangingEvent.INSERT_OPERATION_CODE) {
				return;
			} else if (event.getOperationCode() == EntityTypesChangingEvent.REMOVE_OPERATION_CODE) {
				needToReload = true;
			} else if (event.getOperationCode() == EntityTypesChangingEvent.UPDATE_OPERATION_CODE) {
				needToReload = this.verifyReloadingNeeded(event.getOldEntityType(), event.getNewEntityType());
			}
			if (needToReload == true) {
				this.setStatus(STATUS_NEED_TO_RELOAD_INDEXES);
			}
		}
	}

	protected boolean verifyReloadingNeeded(IApsEntity oldEntityType, IApsEntity newEntityType) {
		List<AttributeInterface> attributes = oldEntityType.getAttributeList();
		for (int i = 0; i < attributes.size(); i++) {
			AttributeInterface oldAttribute = attributes.get(i);
			AttributeInterface newAttribute = (AttributeInterface) newEntityType.getAttribute(oldAttribute.getName());
			boolean isOldAttributeIndexeable = (oldAttribute.getIndexingType() != null && oldAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
			boolean isNewAttributeIndexeable = (newAttribute != null && newAttribute.getIndexingType() != null && newAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
			if ((isOldAttributeIndexeable && !isNewAttributeIndexeable) || (!isOldAttributeIndexeable && isNewAttributeIndexeable)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addEntityToIndex(IApsEntity entity) throws ApsSystemException {
		try {
			this._indexerDao.add(entity);
		} catch (ApsSystemException e) {
			_logger.error("Error saving content to index", e);
			throw e;
		}
	}

	@Override
	public void deleteIndexedEntity(String entityId) throws ApsSystemException {
		try {
			this._indexerDao.delete(IIndexerDAO.DATAOBJECT_ID_FIELD_NAME, entityId);
		} catch (ApsSystemException e) {
			_logger.error("Error deleting content {} from index", entityId, e);
			throw e;
		}
	}

	protected void notifyEndingIndexLoading(LastReloadInfo info, IIndexerDAO newIndexerDAO) {
		try {
			if (info.getResult() == LastReloadInfo.ID_SUCCESS_RESULT) {
				ISearcherDAO newSearcherDAO = this.getFactory().getSearcher(this._newTempSubDirectory);
				this.getFactory().updateSubDir(_newTempSubDirectory);
				this._indexerDao = newIndexerDAO;
				this._searcherDao = newSearcherDAO;
				this._lastReloadInfo = info;
			} else if (null != this._newTempSubDirectory) {
				this.getFactory().deleteSubDirectory(this._newTempSubDirectory);
			}
		} catch (Throwable t) {
			_logger.error("error updating LastReloadInfo", t);
		} finally {
			if (this.getStatus() != STATUS_NEED_TO_RELOAD_INDEXES) {
				this.setStatus(STATUS_READY);
			}
			this._newTempSubDirectory = null;
		}
	}

	@Override
	public LastReloadInfo getLastReloadInfo() {
		return this._lastReloadInfo;
	}

	@Override
	public List<String> searchId(String sectionCode, String langCode,
			String word, Collection<String> allowedGroups) throws ApsSystemException {
		return this.searchEntityId(langCode, word, allowedGroups);
	}

	@Override
	public List<String> searchEntityId(String langCode, String word,
			Collection<String> allowedGroups) throws ApsSystemException {
		SearchEngineFilter[] filters = new SearchEngineFilter[0];
		if (StringUtils.isNotEmpty(langCode) && StringUtils.isNotEmpty(word)) {
			SearchEngineFilter filter = new SearchEngineFilter(langCode, word);
            filter.setFullTextSearch(true);
			filters = this.addFilter(filters, filter);
		}
		return this.searchEntityId(filters, null, allowedGroups);
	}

	private SearchEngineFilter[] addFilter(SearchEngineFilter[] filters, SearchEngineFilter filterToAdd) {
		int len = filters.length;
		SearchEngineFilter[] newFilters = new SearchEngineFilter[len + 1];
		for (int i = 0; i < len; i++) {
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	//@Override
	public List<String> searchId(String sectionCode, SearchEngineFilter[] filters, Collection<String> allowedGroups) throws ApsSystemException {
		return this.searchEntityId(filters, null, allowedGroups);
	}

	//@Override
	public List<String> searchEntityId(SearchEngineFilter[] filters, Collection<ITreeNode> categories, Collection<String> allowedGroups) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = _searcherDao.searchContentsId(filters, categories, allowedGroups);
		} catch (Throwable t) {
			_logger.error("Error searching content id list. ", t);
			throw new ApsSystemException("Error searching content id list", t);
		}
		return contentsId;
	}

	//@Override
	public FacetedContentsResult searchFacetedEntities(SearchEngineFilter[] filters, Collection<ITreeNode> categories, Collection<String> allowedGroups) throws ApsSystemException {
		FacetedContentsResult contentsId = null;
		try {
			contentsId = _searcherDao.searchFacetedContents(filters, categories, allowedGroups);
		} catch (Throwable t) {
			_logger.error("Error searching faceted dataobject", t);
			throw new ApsSystemException("Error searching faceted dataobject", t);
		}
		return contentsId;
	}

	@Override
	public int getStatus() {
		return this._status;
	}

	protected void setStatus(int status) {
		this._status = status;
	}

	@Override
	public void updateIndexedEntity(IApsEntity entity) throws ApsSystemException {
		try {
			this.deleteIndexedEntity(entity.getId());
			this.addEntityToIndex(entity);
		} catch (ApsSystemException e) {
			_logger.error("Error updating content", e);
			throw e;
		}
	}

	protected ISearchEngineDAOFactory getFactory() {
		return _factory;
	}

	public void setFactory(ISearchEngineDAOFactory factory) {
		this._factory = factory;
	}

	public IDataObjectManager getDataObjectManager() {
		return dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager dataObjectManager) {
		this.dataObjectManager = dataObjectManager;
	}

	private ISearchEngineDAOFactory _factory;

	private IIndexerDAO _indexerDao;
	private ISearcherDAO _searcherDao;

	private int _status;
	private LastReloadInfo _lastReloadInfo;
	private List<ApsEvent> _publicContentChangedEventQueue = new ArrayList<ApsEvent>();

	public static final String RELOAD_THREAD_NAME_PREFIX = "RELOAD_INDEX_";

	private String _newTempSubDirectory;

	private IDataObjectManager dataObjectManager;

}
