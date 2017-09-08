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

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.common.entity.api.ApiEntityTypeInterface;
import org.entando.entando.aps.system.common.entity.api.JAXBEntityType;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import org.entando.entando.aps.system.services.dataobject.api.model.JAXBDataObjectType;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * @author E.Santoboni
 */
public class ApiDataObjectTypeInterface extends ApiEntityTypeInterface {

	public JAXBDataObjectType getDataObjectType(Properties properties) throws ApiException, Throwable {
		return (JAXBDataObjectType) super.getEntityType(properties);
	}

	@Override
	protected JAXBEntityType createJAXBEntityType(IApsEntity masterEntityType) {
		DataObject masterDataObjectType = (DataObject) masterEntityType;
		JAXBDataObjectType jaxbDataObjectType = new JAXBDataObjectType(masterDataObjectType);
		jaxbDataObjectType.setDefaultModelId(this.extractModelId(masterDataObjectType.getDefaultModel()));
		jaxbDataObjectType.setListModelId(this.extractModelId(masterDataObjectType.getListModel()));
		return jaxbDataObjectType;
	}

	private Integer extractModelId(String stringModelId) {
		if (null == stringModelId) {
			return null;
		}
		Integer modelId = null;
		try {
			modelId = Integer.parseInt(stringModelId);
		} catch (Throwable t) {
			//nothing to catch
		}
		return modelId;
	}

	public StringApiResponse addDataObjectType(JAXBDataObjectType jaxbDataObjectType) throws Throwable {
		return super.addEntityType(jaxbDataObjectType);
	}

	@Override
	protected void checkNewEntityType(JAXBEntityType jaxbEntityType, IApsEntity newEntityType, StringApiResponse response) throws ApiException, Throwable {
		JAXBDataObjectType jaxbDataObjectType = (JAXBDataObjectType) jaxbEntityType;
		DataObject dataType = (DataObject) newEntityType;
		boolean defaultModelCheck = this.checkDataObjectModel(jaxbDataObjectType.getDefaultModelId(), dataType, response);
		if (defaultModelCheck) {
			dataType.setDefaultModel(String.valueOf(jaxbDataObjectType.getDefaultModelId()));
		}
		boolean listModelCheck = this.checkDataObjectModel(jaxbDataObjectType.getListModelId(), dataType, response);
		if (listModelCheck) {
			dataType.setListModel(String.valueOf(jaxbDataObjectType.getListModelId()));
		}
	}

	public StringApiResponse updateDataObjectType(JAXBDataObjectType jaxbDataObjectType) throws Throwable {
		return super.updateEntityType(jaxbDataObjectType);
	}

	@Override
	protected void checkEntityTypeToUpdate(JAXBEntityType jaxbEntityType, IApsEntity entityTypeToUpdate, StringApiResponse response) throws ApiException, Throwable {
		JAXBDataObjectType jaxbDataObjectType = (JAXBDataObjectType) jaxbEntityType;
		DataObject dataType = (DataObject) entityTypeToUpdate;
		boolean defaultModelCheck = this.checkDataObjectModel(jaxbDataObjectType.getDefaultModelId(), dataType, response);
		if (defaultModelCheck) {
			dataType.setDefaultModel(String.valueOf(jaxbDataObjectType.getDefaultModelId()));
		}
		boolean listModelCheck = this.checkDataObjectModel(jaxbDataObjectType.getListModelId(), dataType, response);
		if (listModelCheck) {
			dataType.setListModel(String.valueOf(jaxbDataObjectType.getListModelId()));
		}
	}

	private boolean checkDataObjectModel(Integer modelId, DataObject dataObjectType, StringApiResponse response) {
		if (null == modelId) {
			return true;
		}
		DataObjectModel contentModel = this.getDataObjectModelManager().getContentModel(modelId);
		if (null == contentModel) {
			ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR,
					"DataObject model with id '" + modelId + "' does not exist", Response.Status.ACCEPTED);
			response.addError(error);
			return false;
		}
		if (!dataObjectType.getTypeCode().equals(contentModel.getDataType())) {
			ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR,
					"DataObject model with id '" + modelId + "' is for DataObjects of type '" + contentModel.getDataType() + "'", Response.Status.ACCEPTED);
			response.addError(error);
			return false;
		}
		return true;
	}

	public void deleteDataObjectType(Properties properties) throws ApiException, Throwable {
		super.deleteEntityType(properties);
	}

	@Override
	protected String getTypeLabel() {
		return "DataObject type";
	}

	@Override
	protected String getTypeCodeParamName() {
		return "code";
	}

	@Override
	protected IEntityManager getEntityManager() {
		return this.getDataObjectManager();
	}

	public IDataObjectModelManager getDataObjectModelManager() {
		return _dataObjectModelManager;
	}

	public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
		this._dataObjectModelManager = dataObjectModelManager;
	}

	public IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager _dataObjectManager) {
		this._dataObjectManager = _dataObjectManager;
	}

	private IDataObjectManager _dataObjectManager;
	private IDataObjectModelManager _dataObjectModelManager;

}
