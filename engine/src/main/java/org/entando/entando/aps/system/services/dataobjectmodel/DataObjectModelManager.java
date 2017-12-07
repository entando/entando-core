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
package org.entando.entando.aps.system.services.dataobjectmodel;

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
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;
import org.entando.entando.aps.system.services.dataobjectmodel.event.DataObjectModelChangedEvent;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * Manager dei modelli di datatype.
 *
 * @author S.Didaci - C.Siddi - C.Sirigu
 */
public class DataObjectModelManager extends AbstractService implements IDataObjectModelManager {

    private static final Logger _logger = LoggerFactory.getLogger(DataObjectModelManager.class);

    @Override
    public void init() throws Exception {
        this.loadDataObjectModels();
        _logger.debug("{} ready. Initialized {} dataObject models", this.getClass().getName(), _dataModels.size());
    }

    private void loadDataObjectModels() throws ApsSystemException {
        try {
            this._dataModels = this.getDataModelDAO().loadDataModels();
        } catch (Throwable t) {
            throw new ApsSystemException("Errore in caricamento modelli", t);
        }
    }

    @Override
    public void addDataObjectModel(DataObjectModel model) throws ApsSystemException {
        try {
            this.getDataModelDAO().addDataModel(model);
            Long wrapLongId = new Long(model.getId());
            _dataModels.put(wrapLongId, model);
            this.notifyDataModelChanging(model, DataObjectModelChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error saving a dataObjectModel", t);
            throw new ApsSystemException("Error saving a dataObjectModel", t);
        }
    }

    @Override
    public void removeDataObjectModel(DataObjectModel model) throws ApsSystemException {
        try {
            this.getDataModelDAO().deleteDataModel(model);
            _dataModels.remove(new Long(model.getId()));
            this.notifyDataModelChanging(model, DataObjectModelChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error deleting a dataObject model", t);
            throw new ApsSystemException("Error deleting a dataObject model", t);
        }
    }

    @Override
    public void updateDataObjectModel(DataObjectModel model) throws ApsSystemException {
        try {
            this.getDataModelDAO().updateDataModel(model);
            this._dataModels.put(new Long(model.getId()), model);
            this.notifyDataModelChanging(model, DataObjectModelChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error updating a dataObject model", t);
            throw new ApsSystemException("Error updating a dataObject model", t);
        }
    }

    private void notifyDataModelChanging(DataObjectModel dataObjectModel, int operationCode) throws ApsSystemException {
        DataObjectModelChangedEvent event = new DataObjectModelChangedEvent();
        event.setDataObjectModel(dataObjectModel);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    @Override
    public DataObjectModel getDataObjectModel(long dataObjectModelId) {
        return (DataObjectModel) _dataModels.get(new Long(dataObjectModelId));
    }

    @Override
    public List<DataObjectModel> getDataObjectModels() {
        List<DataObjectModel> models = new ArrayList<DataObjectModel>(this._dataModels.values());
        Collections.sort(models);
        return models;
    }

    @Override
    public List<DataObjectModel> getModelsForDataObjectType(String dataType) {
        List<DataObjectModel> models = new ArrayList<DataObjectModel>();
        Object[] allModels = this._dataModels.values().toArray();
        for (int i = 0; i < allModels.length; i++) {
            DataObjectModel dataObjectModel = (DataObjectModel) allModels[i];
            if (null == dataType || dataObjectModel.getDataType().equals(dataType)) {
                models.add(dataObjectModel);
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
                        String dataId = widget.getConfig().getProperty("dataId");
                        if (null != id && null != dataId) {
                            long longId = new Long(id).longValue();
                            if (modelId == longId) {
                                List<IPage> pages = (List<IPage>) utilizers.get(dataId);
                                if (null == pages) {
                                    pages = new ArrayList<IPage>();
                                }
                                pages.add(page);
                                utilizers.put(dataId, pages);
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
        List<SmallDataType> smallDataTypes = this.getDataObjectManager().getSmallDataTypes();
        for (int i = 0; i < smallDataTypes.size(); i++) {
            SmallDataType smallDataType = (SmallDataType) smallDataTypes.get(i);
            DataObject prototype = this.getDataObjectManager().createDataObject(smallDataType.getCode());
            if ((null != prototype.getListModel() && prototype.getListModel().equals(modelIdString)) || (null != prototype.getDefaultModel()
                    && prototype.getDefaultModel().equals(modelIdString))) {
                return smallDataType;
            }
        }
        return null;
    }

    public IDataObjectModelDAO getDataModelDAO() {
        return _dataModelDAO;
    }

    public void setDataModelDAO(IDataObjectModelDAO dataModelDAO) {
        this._dataModelDAO = dataModelDAO;
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    protected IDataObjectManager getDataObjectManager() {
        return _dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this._dataObjectManager = dataObjectManager;
    }

    private Map<Long, DataObjectModel> _dataModels;

    private IDataObjectModelDAO _dataModelDAO;

    private IPageManager _pageManager;
    private IDataObjectManager _dataObjectManager;

}
