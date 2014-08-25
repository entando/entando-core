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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Servizio detentore delle operazioni di indicizzazione 
 * di oggetti ricercabili tramite motore di ricerca.
 * @author W.Ambu - M.Diana - E.Santoboni
 */
public class SearchEngineManager extends AbstractService 
		implements ICmsSearchEngineManager, PublicContentChangedObserver, EntityTypesChangingObserver {

	private static final Logger _logger = LoggerFactory.getLogger(SearchEngineManager.class);
	
	@Override
	public void init() throws Exception {
		this._indexerDao = this.getFactory().getIndexer(false);
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
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		if (this.getStatus() == STATUS_RELOADING_INDEXES_IN_PROGRESS) {
			this._publicContentChangedEventQueue.add(0, event);
		} else {
			this.manageEvent(event);
		}
	}
    
	private void manageEvent(PublicContentChangedEvent event) {
		Content content = event.getContent();
		try {
			switch (event.getOperationCode()) {
			case PublicContentChangedEvent.INSERT_OPERATION_CODE:
				this.addEntityToIndex(content);
				break;
			case PublicContentChangedEvent.REMOVE_OPERATION_CODE:
				this.deleteIndexedEntity(content.getId());
				break;
			case PublicContentChangedEvent.UPDATE_OPERATION_CODE:
				this.updateIndexedEntity(content);
				break;
			}
		} catch (Throwable t) {
			_logger.error("Errore in notificazione evento");
		}
	}
	
	protected void sellOfQueueEvents() {
		int size = this._publicContentChangedEventQueue.size();
		if (size>0) {
			for (int i=0; i<size; i++) {
				PublicContentChangedEvent event = (PublicContentChangedEvent) this._publicContentChangedEventQueue.get(0);
				this.manageEvent(event);
				this._publicContentChangedEventQueue.remove(0);
			}
		}
	}
	
	@Override
	public Thread startReloadContentsReferences() throws ApsSystemException {
		IndexLoaderThread loaderThread = null;
    	if (this.getStatus() == STATUS_READY || this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
    		try {
    			this._newTempSubDirectory = "indexdir" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
    			IIndexerDAO newIndexer = this.getFactory().getIndexer(true, _newTempSubDirectory);
    			loaderThread = new IndexLoaderThread(this, this.getContentManager(), newIndexer);
    			String threadName = RELOAD_THREAD_NAME_PREFIX + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
    			loaderThread.setName(threadName);
    			this.setStatus(STATUS_RELOADING_INDEXES_IN_PROGRESS);
    			loaderThread.start();
                _logger.info("Reload Contents References job started");
    		} catch (Throwable t) {
    			throw new ApsSystemException("Errore in aggiornamento referenze", t);
    		}
    	} else {
    		_logger.info("Reload Contents References job suspended: current status: {}", this.getStatus());
    	}
    	return loaderThread;
	}
	
	@Override
	public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
		if (((IManager) this.getContentManager()).getName().equals(event.getEntityManagerName())) {
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
        	//ApsSystemUtils.logThrowable(e, this, "addEntityToIndex", "Error saving content to index");
            throw e;
        }
	}
	
	@Override
	public void deleteIndexedEntity(String entityId) throws ApsSystemException {
		try {
            this._indexerDao.delete(IIndexerDAO.CONTENT_ID_FIELD_NAME, entityId);
        } catch (ApsSystemException e) {
        	_logger.error("Error deleting content {} from index", entityId, e);
        	//ApsSystemUtils.logThrowable(e, this, "deleteIndexedEntity", "Errore nella cancellazione di un contenuto");
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
			//ApsSystemUtils.logThrowable(t, this, "notifyEndingIndexLoading", "errore in aggiornamento LastReloadInfo");
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
		List<String> contentsId = new ArrayList<String>();
    	try {
    		contentsId = _searcherDao.searchContentsId(langCode, word, allowedGroups);
    	} catch (ApsSystemException e) {
    		_logger.error("Error searching content id list. lang:{}, word:{}", langCode, word, e);
    		//ApsSystemUtils.logThrowable(e, this, "searchContentsId", "Errore in ricerca lista identificativi contenuto");
    		throw e;
    	}
    	return contentsId;
	}
	
	/**
	 * @deprecated From jAPS 2.0 version 2.0.9. Use getStatus() method
	 */
	@Override
	public int getState() {
		return this.getStatus();
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
        	//ApsSystemUtils.logThrowable(e, this, "update", "Errore nell'aggiornamento di un contenuto");
            throw e;
        }
	}
	
	protected ISearchEngineDAOFactory getFactory() {
		return _factory;
	}
	public void setFactory(ISearchEngineDAOFactory factory) {
		this._factory = factory;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private ISearchEngineDAOFactory _factory;
	
	private IIndexerDAO _indexerDao;
    private ISearcherDAO _searcherDao;
    
    private int _status;
    private LastReloadInfo _lastReloadInfo;
    private List<ApsEvent> _publicContentChangedEventQueue = new ArrayList<ApsEvent>();
    
    public static final String RELOAD_THREAD_NAME_PREFIX = "RELOAD_INDEX_";
    
    private String _newTempSubDirectory;
    
    private IContentManager _contentManager;
	
}