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
package org.entando.entando.aps.system.services.dataobject.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.dataobject.api.model.ApiDataObjectListBean;
import org.entando.entando.aps.system.services.dataobject.api.model.JAXBDataObject;
import org.entando.entando.aps.system.services.dataobject.api.model.JAXBDataObjectAttribute;
import org.entando.entando.aps.system.services.dataobject.helper.IDataAuthorizationHelper;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectdispenser.DataObjectRenderizationInfo;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListHelper;
import org.entando.entando.aps.system.services.dataobjectdispenser.IDataObjectDispenser;

/**
 * @author E.Santoboni
 */
public class ApiDataObjectInterface extends AbstractApiDataObjectInterface {

    private static final Logger _logger = LoggerFactory.getLogger(ApiDataObjectInterface.class);

    public List<String> getDataObjects(Properties properties) throws Throwable {
        return this.extractDataObjects(properties);
    }

    protected List<String> extractDataObjects(Properties properties) throws Throwable {
        List<String> contentsId = null;
        try {
            ApiDataObjectListBean bean = this.buildSearchBean(properties);
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            contentsId = this.getDataObjectListHelper().getDataTypesId(bean, user);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("error in extractDataObjects", t);
            throw new ApsSystemException("Error into API method", t);
        }
        return contentsId;
    }

    protected ApiDataObjectListBean buildSearchBean(Properties properties) throws ApiException, Throwable {
        ApiDataObjectListBean bean = null;
        try {
            String dataType = properties.getProperty("dataType");
            if (null == this.getDataObjectManager().getSmallDataTypesMap().get(dataType)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "DataObject Type '" + dataType + "' does not exist", Response.Status.CONFLICT);
            }
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            String filtersParam = properties.getProperty("filters");
            EntitySearchFilter[] filters = this.getDataObjectListHelper().getFilters(dataType, filtersParam, langCode);
            String[] categoryCodes = null;
            String categoriesParam = properties.getProperty("categories");
            if (null != categoriesParam && categoriesParam.trim().length() > 0) {
                categoryCodes = categoriesParam.split(IDataTypeListHelper.CATEGORIES_SEPARATOR);
            }
            bean = new ApiDataObjectListBean(dataType, filters, categoryCodes);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("error in buildSearchBean", t);
            throw new ApsSystemException("Error into API method", t);
        }
        return bean;
    }

    public String getDataObjectsToHtml(Properties properties) throws Throwable {
        StringBuilder render = new StringBuilder();
        try {
            String modelId = properties.getProperty("modelId");
            if (null == modelId || modelId.trim().length() == 0) {
                return null;
            }
            String dataType = properties.getProperty("dataType");
            DataObject prototype = (DataObject) this.getDataObjectManager().getEntityPrototype(dataType);
            Integer modelIdInteger = this.checkModel(modelId, prototype);
            if (null == modelIdInteger) {
                return null;
            }
            List<String> dataObjectsId = this.extractDataObjects(properties);
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            render.append(this.getItemsStartElement());
            for (int i = 0; i < dataObjectsId.size(); i++) {
                render.append(this.getItemStartElement());
                String renderedData = this.getRenderedDataObject(dataObjectsId.get(i), modelIdInteger, langCode);
                if (null != renderedData) {
                    render.append(renderedData);
                }
                render.append(this.getItemEndElement());
            }
            render.append(this.getItemsEndElement());
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("error in getDataObjectsToHtml", t);
            throw new ApsSystemException("Error into API method", t);
        }
        return render.toString();
    }

    public JAXBDataObject getDataObject(Properties properties) throws ApiException, Throwable {
        JAXBDataObject jaxbDataObject = null;
        String id = properties.getProperty("id");
        try {
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            DataObject mainDataObject = this.getPublicDataObject(id);
            jaxbDataObject = this.getJAXBDataObjectInstance(mainDataObject, langCode);
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
            if (!this.getDataObjectAuthorizationHelper().isAuth(user, mainDataObject)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Required DataObject '" + id + "' does not allowed", Response.Status.FORBIDDEN);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("error in getDataObject", t);
            throw new ApsSystemException("Error into API method", t);
        }
        return jaxbDataObject;
    }

    protected JAXBDataObject getJAXBDataObjectInstance(DataObject mainDataObject, String langCode) {
        return new JAXBDataObject(mainDataObject, langCode);
    }

    public String getDataObjectToHtml(Properties properties) throws ApiException, Throwable {
        String render = null;
        String id = properties.getProperty("id");
        String modelId = properties.getProperty("modelId");
        try {
            if (null == modelId || modelId.trim().length() == 0) {
                return null;
            }
            DataObject mainDataObject = this.getPublicDataObject(id);
            Integer modelIdInteger = this.checkModel(modelId, mainDataObject);
            if (null != modelIdInteger) {
                String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
                render = this.getRenderedDataObject(id, modelIdInteger, langCode);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("error in getDataObjectToHtml", t);
            throw new ApsSystemException("Error into API method", t);
        }
        return render;
    }

    protected String getRenderedDataObject(String id, int modelId, String langCode) {
        String renderedData = null;
        DataObjectRenderizationInfo renderizationInfo = this.getDataObjectDispenser().getRenderizationInfo(id, modelId, langCode, null, true);
        if (null != renderizationInfo) {
            renderedData = renderizationInfo.getRenderedDataobject();
        }
        return renderedData;
    }

    protected DataObject getPublicDataObject(String id) throws ApiException, Throwable {
        DataObject dataObject = null;
        try {
            dataObject = this.getDataObjectManager().loadDataObject(id, true);
            if (null == dataObject) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Null DataObject by id '" + id + "'", Response.Status.CONFLICT);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("Error extracting DataObject by id '{}'", id, t);
            throw new ApsSystemException("Error extracting DataObject by id '" + id + "'", t);
        }
        return dataObject;
    }

    protected Integer checkModel(String modelId, DataObject dataObject) throws ApiException, Throwable {
        Integer modelIdInteger = null;
        try {
            if (null == modelId || modelId.trim().length() == 0) {
                return null;
            }
            if (modelId.equals("default")) {
                if (null == dataObject.getDefaultModel()) {
                    throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
                            "Invalid 'default' system model for DataObject type '" + dataObject.getTypeCode() + "' - Contact the administrators",
                            Response.Status.ACCEPTED);
                }
                modelIdInteger = Integer.parseInt(dataObject.getDefaultModel());
            } else if (modelId.equals("list")) {
                if (null == dataObject.getListModel()) {
                    throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
                            "Invalid 'list' system model for DataObject type '" + dataObject.getTypeCode() + "' - Contact the administrators",
                            Response.Status.ACCEPTED);
                }
                modelIdInteger = Integer.parseInt(dataObject.getListModel());
            } else {
                try {
                    modelIdInteger = Integer.parseInt(modelId);
                } catch (Throwable t) {
                    throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
                            "The model id must be an integer or 'default' or 'list' - '" + modelId + "'",
                            Response.Status.ACCEPTED);
                }
            }
            DataObjectModel model = this.getDataObjectModelManager().getDataObjectModel(modelIdInteger);
            if (model == null) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "The DataObject model with id '" + modelId + "' does not exist", Response.Status.ACCEPTED);
            } else if (!dataObject.getTypeCode().equals(model.getDataType())) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
                        "The DataObject model with id '" + modelId + "' does not match with DataObject of type '" + dataObject.getTypeDescription() + "' ",
                        Response.Status.ACCEPTED);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("Error checking model id '{}'", modelId, t);
            throw new ApsSystemException("Error checking model id '" + modelId + "'", t);
        }
        return modelIdInteger;
    }

    public StringApiResponse addDataObject(JAXBDataObject jaxbDataObject, Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String typeCode = jaxbDataObject.getTypeCode();
            DataObject prototype = (DataObject) this.getDataObjectManager().getEntityPrototype(typeCode);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "DataObject type with code '" + typeCode + "' does not exist", Response.Status.CONFLICT);
            }
            DataObject dataObject = (DataObject) jaxbDataObject.buildEntity(prototype, this.getCategoryManager());
            if (null != dataObject.getId()) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "You cannot specify DataObject Id", Response.Status.CONFLICT);
            }
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            dataObject.setFirstEditor((null != user) ? user.getUsername() : SystemConstants.GUEST_USER_NAME);
            response = this.validateAndSaveDataObject(dataObject, properties);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            _logger.error("Error adding DataObject", t);
            throw new ApsSystemException("Error adding DataObject", t);
        }
        return response;
    }

    public StringApiResponse updateDataObject(JAXBDataObject jaxbDataObject, Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String typeCode = jaxbDataObject.getTypeCode();
            DataObject prototype = (DataObject) this.getDataObjectManager().getEntityPrototype(typeCode);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "DataObject type with code '" + typeCode + "' does not exist", Response.Status.CONFLICT);
            }
            DataObject content = (DataObject) jaxbDataObject.buildEntity(prototype, this.getCategoryManager());
            DataObject masterData = this.getDataObjectManager().loadDataObject(content.getId(), false);
            if (null == masterData) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "DataObject with code '" + content.getId() + "' does not exist", Response.Status.CONFLICT);
            } else if (!masterData.getMainGroup().equals(content.getMainGroup())) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Invalid main group " + content.getMainGroup() + " not equals then master " + masterData.getMainGroup(),
                        Response.Status.CONFLICT);
            }
            response = this.validateAndSaveDataObject(content, properties);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            _logger.error("Error updating DataObject", t);
            throw new ApsSystemException("Error updating DataObject", t);
        }
        return response;
    }

    protected StringApiResponse validateAndSaveDataObject(DataObject dataObject, Properties properties) throws ApiException, Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
            if (!this.getDataObjectAuthorizationHelper().isAuth(user, dataObject)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "DataObject groups makes the new content not allowed for user " + user.getUsername(),
                        Response.Status.FORBIDDEN);
            }
            List<ApiError> errors = this.validate(dataObject);
            if (errors.size() > 0) {
                response.addErrors(errors);
                response.setResult(IResponseBuilder.FAILURE, null);
                return response;
            }
            String insertString = properties.getProperty("insert");
            boolean insert = (null != insertString) ? Boolean.parseBoolean(insertString) : false;
            if (!insert) {
                this.getDataObjectManager().saveDataObject(dataObject);
            } else {
                this.getDataObjectManager().insertDataObject(dataObject);
            }
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            _logger.error("error in validateAndSaveDataObject", t);
            throw new ApsSystemException("Error adding dataObject", t);
        }
        return response;
    }

    private List<ApiError> validate(DataObject dataObject) throws ApsSystemException {
        List<ApiError> errors = new ArrayList<ApiError>();
        try {
            if (null == dataObject.getMainGroup()) {
                errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, "Main group null", Response.Status.CONFLICT));
            }
            List<FieldError> fieldErrors = dataObject.validate(this.getGroupManager());
            if (null != fieldErrors) {
                for (int i = 0; i < fieldErrors.size(); i++) {
                    FieldError fieldError = fieldErrors.get(i);
                    if (fieldError instanceof AttributeFieldError) {
                        AttributeFieldError attributeError = (AttributeFieldError) fieldError;
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, attributeError.getFullMessage(), Response.Status.CONFLICT));
                    } else {
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, fieldError.getMessage(), Response.Status.CONFLICT));
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("Error validating DataObject", t);
            throw new ApsSystemException("Error validating DataObject", t);
        }
        return errors;
    }

    public StringApiResponse deleteDataObject(Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String id = properties.getProperty("id");
            DataObject masterDataObject = this.getDataObjectManager().loadDataObject(id, false);
            if (null == masterDataObject) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "DataObject with code '" + id + "' does not exist", Response.Status.CONFLICT);
            }
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
            if (!this.getDataObjectAuthorizationHelper().isAuth(user, masterDataObject)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "DataObject groups makes the new dataObject not allowed for user " + user.getUsername(),
                        Response.Status.FORBIDDEN);
            }
            if (masterDataObject.isOnLine()) {
                this.getDataObjectManager().removeDataObject(masterDataObject);
            }
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            _logger.error("Error deleting DataObject", t);
            throw new ApsSystemException("Error deleting DataObject", t);
        }
        return response;
    }

    public void updateDataObjectText(JAXBDataObjectAttribute jaxbDataObjectAttribute, Properties properties) throws ApiException, Throwable {
        try {
            String dataId = jaxbDataObjectAttribute.getDataId();
            DataObject masterDataObject = this.getDataObjectManager().loadDataObject(jaxbDataObjectAttribute.getDataId(), true);
            if (null == masterDataObject) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "DataObject with code '" + dataId + "' does not exist", Response.Status.CONFLICT);
            }
            String attributeName = jaxbDataObjectAttribute.getAttributeName();
            AttributeInterface attribute = (AttributeInterface) masterDataObject.getAttribute(attributeName);
            if (null == attribute) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "DataObject Attribute with code '" + attributeName + "' does not exist into DataObject " + dataId,
                        Response.Status.CONFLICT);
            } else if (!(attribute instanceof ITextAttribute)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "DataObject Attribute with code '" + attributeName + "' isn't a Text Atttribute",
                        Response.Status.CONFLICT);
            }
            String langCode = jaxbDataObjectAttribute.getLangCode();
            String value = jaxbDataObjectAttribute.getValue();
            if (StringUtils.isEmpty(langCode) || StringUtils.isEmpty(value)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "LangCode or value is Empty", Response.Status.CONFLICT);
            }
            ((ITextAttribute) attribute).setText(value, langCode);
            this.getDataObjectManager().insertDataObject(masterDataObject);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            _logger.error("Error updating DataObject attribute", t);
            throw new ApsSystemException("Error updating DataObject attribute", t);
        }
    }

    protected IUserManager getUserManager() {
        return _userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }

    protected ICategoryManager getCategoryManager() {
        return _categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this._categoryManager = categoryManager;
    }

    protected IGroupManager getGroupManager() {
        return _groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    public IDataTypeListHelper getDataObjectListHelper() {
        return _dataObjectListHelper;
    }

    public void setDataObjectListHelper(IDataTypeListHelper dataObjectListHelper) {
        this._dataObjectListHelper = dataObjectListHelper;
    }

    public IDataAuthorizationHelper getDataObjectAuthorizationHelper() {
        return _dataObjectAuthorizationHelper;
    }

    public void setDataObjectAuthorizationHelper(IDataAuthorizationHelper dataObjectAuthorizationHelper) {
        this._dataObjectAuthorizationHelper = dataObjectAuthorizationHelper;
    }

    public IDataObjectDispenser getDataObjectDispenser() {
        return _dataObjectDispenser;
    }

    public void setDataObjectDispenser(IDataObjectDispenser dataObjectDispenser) {
        this._dataObjectDispenser = dataObjectDispenser;
    }

    public String getItemsStartElement() {
        return _itemsStartElement;
    }

    public void setItemsStartElement(String itemsStartElement) {
        this._itemsStartElement = itemsStartElement;
    }

    public String getItemStartElement() {
        return _itemStartElement;
    }

    public void setItemStartElement(String itemStartElement) {
        this._itemStartElement = itemStartElement;
    }

    public String getItemEndElement() {
        return _itemEndElement;
    }

    public void setItemEndElement(String itemEndElement) {
        this._itemEndElement = itemEndElement;
    }

    public String getItemsEndElement() {
        return _itemsEndElement;
    }

    public void setItemsEndElement(String itemsEndElement) {
        this._itemsEndElement = itemsEndElement;
    }

    private IDataTypeListHelper _dataObjectListHelper;
    private IUserManager _userManager;
    private ICategoryManager _categoryManager;
    private IGroupManager _groupManager;
    private IPageManager _pageManager;
    private IDataAuthorizationHelper _dataObjectAuthorizationHelper;
    private IDataObjectDispenser _dataObjectDispenser;
    private String _itemsStartElement = "<ul>";
    private String _itemStartElement = "<li>";
    private String _itemEndElement = "</li>";
    private String _itemsEndElement = "</ul>";

}
