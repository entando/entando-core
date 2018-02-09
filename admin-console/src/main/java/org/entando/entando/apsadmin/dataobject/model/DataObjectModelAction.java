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
package org.entando.entando.apsadmin.dataobject.model;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;

/**
 * Classe action delegata alle operazioni sugli oggetti modelli di
 * DataObjectModel.
 *
 * @author E.Santoboni
 */
public class DataObjectModelAction extends BaseAction {

    private static final Logger _logger = LoggerFactory.getLogger(DataObjectModelAction.class);

    @Override
    public void validate() {
        super.validate();
        if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
            this.checkModelId();
        }
    }

    private void checkModelId() {
        if (null == this.getModelId()) {
            return;
        }
        DataObjectModel dummyModel = this.getDataObjectModelManager().getDataObjectModel(this.getModelId());
        if (dummyModel != null) {
            this.addFieldError("modelId", this.getText("error.dataObjectModel.modelId.alreadyPresent"));
        }
        SmallDataType utilizer = this.getDataObjectModelManager().getDefaultUtilizer(this.getModelId());
        if (null != utilizer && !utilizer.getCode().equals(this.getDataType())) {
            String[] args = {this.getModelId().toString(), utilizer.getDescr()};
            this.addFieldError("modelId", this.getText("error.dataObjectModel.modelId.wrongUtilizer", args));
        }
    }

    public String newModel() {
        this.setStrutsAction(ApsAdminSystemConstants.ADD);
        return SUCCESS;
    }

    public String edit() {
        this.setStrutsAction(ApsAdminSystemConstants.EDIT);
        try {
            long modelId = this.getModelId().longValue();
            DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(modelId);
            this.setFormValues(model);
        } catch (Throwable t) {
            _logger.error("error in edit", t);
            //ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String save() {
        try {
            DataObjectModel model = this.getBeanFromForm();
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                this.getDataObjectModelManager().addDataObjectModel(model);
            } else {
                this.getDataObjectModelManager().updateDataObjectModel(model);
            }
        } catch (Throwable t) {
            _logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    private String checkDelete() {
        String check = null;
        long modelId = this.getModelId().longValue();
        Map<String, List<IPage>> referencingPages = this.getDataObjectModelManager().getReferencingPages(modelId);
        if (!referencingPages.isEmpty()) {
            List<String> referencedDataObjects = new ArrayList<String>(referencingPages.keySet());
            this.setReferencingPages(referencingPages);
            Collections.sort(referencedDataObjects);
            this.setReferencedDatasOnPages(referencedDataObjects);
            check = "references";
        }
        return check;
    }

    public String trash() {
        try {
            String check = this.checkDelete();
            if (null != check) {
                return check;
            }
            long modelId = this.getModelId().longValue();
            DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(modelId);
            if (null != model) {
                this.setDescription(model.getDescription());
                this.setDataType(model.getDataType());
            } else {
                return "noModel";
            }
        } catch (Throwable t) {
            _logger.error("error in trash", t);
            //ApsSystemUtils.logThrowable(t, this, "trash");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String delete() {
        try {
            String check = this.checkDelete();
            if (null != check) {
                return check;
            }
            long modelId = this.getModelId().longValue();
            DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(modelId);
            this.getDataObjectModelManager().removeDataObjectModel(model);
        } catch (Throwable t) {
            _logger.error("error in delete", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public List<SmallEntityType> getSmallDataTypes() {
        return this.getDataObjectManager().getSmallEntityTypes();
    }

    public Map<String, SmallDataType> getSmallDataTypesMap() {
        return this.getDataObjectManager().getSmallDataTypesMap();
    }

    public SmallDataType getSmallDataType(String typeCode) {
        return this.getSmallDataTypesMap().get(typeCode);
    }

    public DataObjectModel getDataObjectModel(long modelId) {
        return this.getDataObjectModelManager().getDataObjectModel(modelId);
    }

    private DataObjectModel getBeanFromForm() {
        DataObjectModel dataObjectModel = new DataObjectModel();
        dataObjectModel.setId(this.getModelId());
        dataObjectModel.setShape(this.getDataObjectShape());
        dataObjectModel.setDataType(this.getDataType());
        dataObjectModel.setDescription(this.getDescription());
        if (null != this.getStylesheet() && this.getStylesheet().trim().length() > 0) {
            dataObjectModel.setStylesheet(this.getStylesheet());
        }
        if (getStrutsAction() == ApsAdminSystemConstants.EDIT) {
            dataObjectModel.setId(new Long(this.getModelId()).longValue());
        }
        return dataObjectModel;
    }

    private void setFormValues(DataObjectModel model) {
        this.setModelId(new Integer(String.valueOf(model.getId())));
        this.setDescription(model.getDescription());
        this.setDataType(model.getDataType());
        this.setDataObjectShape(model.getShape());
        this.setStylesheet(model.getStylesheet());
    }

    /**
     * Restituisce il contenuto vo in base all'identificativo. Metodo a servizio
     * dell'interfaccia di visualizzazione contenuti in lista.
     *
     * @param dataId L'identificativo del contenuto.
     * @return Il contenuto vo cercato.
     */
    public DataObjectRecordVO getDataObjectVo(String dataId) {
        DataObjectRecordVO dataObjectVo = null;
        try {
            dataObjectVo = this.getDataObjectManager().loadDataObjectVO(dataId);
        } catch (Throwable t) {
            _logger.error("error loading getDataObjectVo for {}", dataId, t);
            throw new RuntimeException("Errore in caricamento DataObject vo", t);
        }
        return dataObjectVo;
    }

    public DataObject getDataObjectPrototype() {
        if (null == this.getDataType()) {
            return null;
        }
        return (DataObject) this.getDataObjectManager().getEntityPrototype(this.getDataType());
    }

    public List<String> getAllowedAttributeMethods(DataObject prototype, String attributeName) {
        List<String> methods = new ArrayList<String>();
        try {
            AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(attributeName);
            if (null == attribute) {
                throw new ApsSystemException("Null Attribute '" + attributeName + "' for Data Type '"
                        + prototype.getTypeCode() + "' - '" + prototype.getTypeDescr());
            }
            String methodsString = this.getAllowedPublicAttributeMethods().getProperty(attribute.getType());
            if (null != methodsString) {
                String[] methodsArray = methodsString.split(";");
                methods = Arrays.asList(methodsArray);
            } else {
                BeanInfo beanInfo = Introspector.getBeanInfo(attribute.getClass(), AbstractAttribute.class);
                PropertyDescriptor[] prDescrs = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < prDescrs.length; i++) {
                    PropertyDescriptor propertyDescriptor = prDescrs[i];
                    if (null != propertyDescriptor.getReadMethod()) {
                        methods.add(propertyDescriptor.getDisplayName());
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("error in getAllowedAttributeMethods", t);
        }
        return methods;
    }

    public int getStrutsAction() {
        return _strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this._strutsAction = strutsAction;
    }

    public Integer getModelId() {
        return _modelId;
    }

    public void setModelId(Integer modelId) {
        this._modelId = modelId;
    }

    public String getDataType() {
        return _dataType;
    }

    public void setDataType(String dataType) {
        this._dataType = dataType;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getDataObjectShape() {
        return _dataObjectShape;
    }

    public void setDataObjectShape(String dataObjectShape) {
        this._dataObjectShape = dataObjectShape;
    }

    public String getStylesheet() {
        return _stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this._stylesheet = stylesheet;
    }

    public List<String> getAllowedPublicDataObjectMethods() {
        return _allowedPublicDataObjectMethods;
    }

    public void setAllowedPublicDataObjectMethods(List<String> allowedPublicDataObjectMethods) {
        this._allowedPublicDataObjectMethods = allowedPublicDataObjectMethods;
    }

    public Properties getAllowedPublicAttributeMethods() {
        return _allowedPublicAttributeMethods;
    }

    public void setAllowedPublicAttributeMethods(Properties allowedPublicAttributeMethods) {
        this._allowedPublicAttributeMethods = allowedPublicAttributeMethods;
    }

    public Map getReferencingPages() {
        return _referencingPages;
    }

    protected void setReferencingPages(Map referencingPages) {
        this._referencingPages = referencingPages;
    }

    public List<String> getReferencedDatasOnPages() {
        return _referencedDatasOnPages;
    }

    protected void setReferencedDatasOnPages(List<String> referencedDatasOnPages) {
        this._referencedDatasOnPages = referencedDatasOnPages;
    }

    protected IDataObjectModelManager getDataObjectModelManager() {
        return _dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
        this._dataObjectModelManager = dataObjectModelManager;
    }

    protected IDataObjectManager getDataObjectManager() {
        return _dataObjectManager;
    }

    public void setDataObjectManager(IDataObjectManager dataObjectManager) {
        this._dataObjectManager = dataObjectManager;
    }

    private int _strutsAction;
    private Integer _modelId;
    private String _dataType;
    private String _description;
    private String _dataObjectShape;
    private String _stylesheet;

    private List<String> _allowedPublicDataObjectMethods;
    private Properties _allowedPublicAttributeMethods;

    private Map _referencingPages;
    private List<String> _referencedDatasOnPages;

    private IDataObjectModelManager _dataObjectModelManager;
    private IDataObjectManager _dataObjectManager;

}
