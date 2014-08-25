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
package org.entando.entando.plugins.jacms.aps.system.services.api;

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
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;

/**
 * @author E.Santoboni
 */
public class ApiContentModelInterface extends AbstractCmsApiInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiContentModelInterface.class);
	
	public StringListApiResponse getModels(Properties properties) throws ApiException, Throwable {
		StringListApiResponse response = new StringListApiResponse();
		try {
			List<ContentModel> models = null;
			String contentTypeParam = properties.getProperty("contentType");
			String contentType = (null != contentTypeParam && contentTypeParam.trim().length() > 0) ? contentTypeParam.trim() : null;
			if (null != contentType && null == this.getContentManager().getSmallContentTypesMap().get(contentType)) {
				ApiError error = new ApiError(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Content Type " + contentType + " does not exist", Response.Status.CONFLICT);
				response.addError(error);
				contentType = null;
			}
			if (null != contentType) {
				models = this.getContentModelManager().getModelsForContentType(contentType);
			} else {
				models = this.getContentModelManager().getContentModels();
			}
			List<String> list = new ArrayList<String>();
			if (null != models) {
				for (int i = 0; i < models.size(); i++) {
					ContentModel model = models.get(i);
					list.add(String.valueOf(model.getId()));
				}
			}
			response.setResult(list, null);
		} catch (Throwable t) {
			_logger.error("Error loading models", t);
            //ApsSystemUtils.logThrowable(t, this, "getModels");
            throw new ApsSystemException("Error loading models", t);
        }
		return response;
	}
	
	public ContentModel getModel(Properties properties) throws ApiException, Throwable {
		String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid number format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        ContentModel model = this.getContentModelManager().getContentModel(id);
        if (null == model) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        return model;
	}
	
	public void addModel(ContentModel model) throws ApiException, Throwable {
		if (null != this.getContentModelManager().getContentModel(model.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id " + model.getId() + " already exists", Response.Status.CONFLICT);
        }
		if (null == this.getContentManager().getSmallContentTypesMap().get(model.getContentType())) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Content Type " + model.getContentType() + " does not exist", Response.Status.CONFLICT);
		}
		try {
            this.getContentModelManager().addContentModel(model);
        } catch (Throwable t) {
        	_logger.error("Error adding model", t);
        	//ApsSystemUtils.logThrowable(t, this, "addModel");
            throw new ApsSystemException("Error adding model", t);
        }
	}
	
	public void updateModel(ContentModel model) throws ApiException, Throwable {
		ContentModel oldModel = this.getContentModelManager().getContentModel(model.getId());
		if (null == oldModel) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id " + model.getId() + " does not exist", Response.Status.CONFLICT);
        }
		if (!oldModel.getContentType().equals(model.getContentType())) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
					"Content Type code can't be changed - it has to be '" + oldModel.getContentType() + "'", Response.Status.CONFLICT);
		}
		try {
            this.getContentModelManager().updateContentModel(model);
        } catch (Throwable t) {
        	_logger.error("Error updating model", t);
            //ApsSystemUtils.logThrowable(t, this, "updateModel");
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
        ContentModel model = this.getContentModelManager().getContentModel(id);
        if (null == model) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Model with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
		try {
            this.getContentModelManager().removeContentModel(model);
        } catch (Throwable t) {
        	_logger.error("Error deleting model", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteModel");
            throw new ApsSystemException("Error deleting model", t);
        }
	}
	
}
