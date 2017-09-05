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
package org.entando.entando.aps.system.services.datatypemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import org.entando.entando.aps.system.services.datatype.IContentManager;
import org.entando.entando.aps.system.services.datatype.model.DataObject;
import org.entando.entando.aps.system.services.datatype.model.SmallDataType;
import org.entando.entando.aps.system.services.datatypemodel.event.DataModelChangedEvent;

/**
 * Manager dei modelli di datatype.
 *
 * @author S.Didaci - C.Siddi - C.Sirigu
 */
public class DataModelManager extends AbstractService implements IDataModelManager {

	private static final Logger _logger = LoggerFactory.getLogger(DataModelManager.class);

	@Override
	public void init() throws Exception {
		this.loadContentModels();
		_logger.debug("{} ready. Initialized {} content models", this.getClass().getName(), _dataModels.size());
	}

	private void loadContentModels() throws ApsSystemException {
		try {
			this._dataModels = this.getDataModelDAO().loadDataModels();
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in caricamento modelli", t);
		}
	}

	@Override
	public void addContentModel(DataModel model) throws ApsSystemException {
		try {
			this.getDataModelDAO().addDataModel(model);
			Long wrapLongId = new Long(model.getId());
			_dataModels.put(wrapLongId, model);
			this.notifyDataModelChanging(model, DataModelChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error saving a contentModel", t);
			throw new ApsSystemException("Error saving a contentModel", t);
		}
	}

	@Override
	public void removeContentModel(DataModel model) throws ApsSystemException {
		try {
			this.getDataModelDAO().deleteDataModel(model);
			_dataModels.remove(new Long(model.getId()));
			this.notifyDataModelChanging(model, DataModelChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting a content model", t);
			throw new ApsSystemException("Error deleting a content model", t);
		}
	}

	@Override
	public void updateContentModel(DataModel model) throws ApsSystemException {
		try {
			this.getDataModelDAO().updateDataModel(model);
			this._dataModels.put(new Long(model.getId()), model);
			this.notifyDataModelChanging(model, DataModelChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating a content model", t);
			throw new ApsSystemException("Error updating a content model", t);
		}
	}

	private void notifyDataModelChanging(DataModel contentModel, int operationCode) throws ApsSystemException {
		DataModelChangedEvent event = new DataModelChangedEvent();
		event.setContentModel(contentModel);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	@Override
	public DataModel getContentModel(long contentModelId) {
		return (DataModel) _dataModels.get(new Long(contentModelId));
	}

	@Override
	public List<DataModel> getContentModels() {
		List<DataModel> models = new ArrayList<DataModel>(this._dataModels.values());
		Collections.sort(models);
		return models;
	}

	@Override
	public List<DataModel> getModelsForContentType(String contentType) {
		List<DataModel> models = new ArrayList<DataModel>();
		Object[] allModels = this._dataModels.values().toArray();
		for (int i = 0; i < allModels.length; i++) {
			DataModel contentModel = (DataModel) allModels[i];
			if (null == contentType || contentModel.getContentType().equals(contentType)) {
				models.add(contentModel);
			}
		}
		return models;
	}

	@Override
	public Map<String, List<IPage>> getReferencingPages(long modelId) {
		Map<String, List<IPage>> utilizers = new HashMap<String, List<IPage>>();
		IPage root = this.getPageManager().getDraftRoot();
		this.searchReferencingPages(modelId, root, utilizers);
		root = this.getPageManager().getOnlineRoot();
		this.searchReferencingPages(modelId, root, utilizers);
		return utilizers;
	}

	private void searchReferencingPages(long modelId, IPage page, Map<String, List<IPage>> utilizers) {
		this.addReferencingPage(modelId, page, page.getWidgets(), utilizers);
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.searchReferencingPages(modelId, children[i], utilizers);
		}
	}

	private void addReferencingPage(long modelId, IPage page, Widget[] widgets, Map<String, List<IPage>> utilizers) {
		if (null != widgets) {
			for (int i = 0; i < widgets.length; i++) {
				Widget widget = widgets[i];
				if (null != widget) {
					if (null != widget.getConfig()) {
						String id = widget.getConfig().getProperty("modelId");
						String contentId = widget.getConfig().getProperty("contentId");
						if (null != id && null != contentId) {
							long longId = new Long(id).longValue();
							if (modelId == longId) {
								List<IPage> pages = (List<IPage>) utilizers.get(contentId);
								if (null == pages) {
									pages = new ArrayList<IPage>();
								}
								pages.add(page);
								utilizers.put(contentId, pages);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public SmallDataType getDefaultUtilizer(long modelId) {
		String modelIdString = String.valueOf(modelId);
		List<SmallDataType> smallContentTypes = this.getContentManager().getSmallContentTypes();
		for (int i = 0; i < smallContentTypes.size(); i++) {
			SmallDataType smallContentType = (SmallDataType) smallContentTypes.get(i);
			DataObject prototype = this.getContentManager().createContentType(smallContentType.getCode());
			if ((null != prototype.getListModel() && prototype.getListModel().equals(modelIdString)) || (null != prototype.getDefaultModel()
					&& prototype.getDefaultModel().equals(modelIdString))) {
				return smallContentType;
			}
		}
		return null;
	}

	public IDataModelDAO getDataModelDAO() {
		return _dataModelDAO;
	}

	public void setDataModelDAO(IDataModelDAO dataModelDAO) {
		this._dataModelDAO = dataModelDAO;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	private Map<Long, DataModel> _dataModels;

	private IDataModelDAO _dataModelDAO;

	private IPageManager _pageManager;
	private IContentManager _contentManager;

}
