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

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringListApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;

/**
 * @author E.Santoboni
 */
public class ApiDataObjectModelInterface extends AbstractApiDataObjectInterface {

	private static final Logger _logger = LoggerFactory.getLogger(ApiDataObjectModelInterface.class);

	public StringListApiResponse getModels(Properties properties) throws ApiException, Throwable {
		StringListApiResponse response = new StringListApiResponse();
		try {
			List<DataObjectModel> models = null;
			String dataTypeParam = properties.getProperty("dataType");
			String dataType = (null != dataTypeParam && dataTypeParam.trim().length() > 0) ? dataTypeParam.trim() : null;
			if (null != dataType && null == this.getDataObjectManager().getSmallDataTypesMap().get(dataType)) {
				ApiError error = new ApiError(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Content Type " + dataType + " does not exist", Response.Status.CONFLICT);
				response.addError(error);
				dataType = null;
			}
			if (null != dataType) {
				models = this.getDataObjectModelManager().getModelsForContentType(dataType);
			} else {
				models = this.getDataObjectModelManager().getContentModels();
			}
			List<String> list = new ArrayList<String>();
			if (null != models) {
				for (int i = 0; i < models.size(); i++) {
					DataObjectModel model = models.get(i);
					list.add(String.valueOf(model.getId()));
				}
			}
			response.setResult(list, null);
		} catch (Throwable t) {
			_logger.error("Error loading models", t);
			throw new ApsSystemException("Error loading models", t);
		}
		return response;
	}

	public DataObjectModel getModel(Properties properties) throws ApiException, Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid number format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		DataObjectModel model = this.getDataObjectModelManager().getContentModel(id);
		if (null == model) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id '" + idString + "' does not exist", Response.Status.CONFLICT);
		}
		return model;
	}

	public void addModel(DataObjectModel model) throws ApiException, Throwable {
		if (null != this.getDataObjectModelManager().getContentModel(model.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id " + model.getId() + " already exists", Response.Status.CONFLICT);
		}
		if (null == this.getDataObjectManager().getSmallDataTypesMap().get(model.getDataType())) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Content Type " + model.getDataType() + " does not exist", Response.Status.CONFLICT);
		}
		try {
			this.getDataObjectModelManager().addContentModel(model);
		} catch (Throwable t) {
			_logger.error("Error adding model", t);
			throw new ApsSystemException("Error adding model", t);
		}
	}

	public void updateModel(DataObjectModel model) throws ApiException, Throwable {
		DataObjectModel oldModel = this.getDataObjectModelManager().getContentModel(model.getId());
		if (null == oldModel) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id " + model.getId() + " does not exist", Response.Status.CONFLICT);
		}
		if (!oldModel.getDataType().equals(model.getDataType())) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
					"DataObject Type code can't be changed - it has to be '" + oldModel.getDataType() + "'", Response.Status.CONFLICT);
		}
		try {
			this.getDataObjectModelManager().updateContentModel(model);
		} catch (Throwable t) {
			_logger.error("Error updating model", t);
			throw new ApsSystemException("Error updating model", t);
		}
	}

	public void deleteModel(Properties properties) throws ApiException, Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid number format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		DataObjectModel model = this.getDataObjectModelManager().getContentModel(id);
		if (null == model) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id '" + idString + "' does not exist", Response.Status.CONFLICT);
		}
		try {
			this.getDataObjectModelManager().removeContentModel(model);
		} catch (Throwable t) {
			_logger.error("Error deleting model", t);
			throw new ApsSystemException("Error deleting model", t);
		}
	}

}
