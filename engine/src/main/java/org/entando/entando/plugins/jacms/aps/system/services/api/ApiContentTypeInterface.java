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

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.common.entity.api.ApiEntityTypeInterface;
import org.entando.entando.aps.system.common.entity.api.JAXBEntityType;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.plugins.jacms.aps.system.services.api.model.JAXBContentType;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

/**
 * @author E.Santoboni
 */
public class ApiContentTypeInterface extends ApiEntityTypeInterface {
    
    public JAXBContentType getContentType(Properties properties) throws ApiException, Throwable {
        return (JAXBContentType) super.getEntityType(properties);
    }
	
	@Override
	protected JAXBEntityType createJAXBEntityType(IApsEntity masterEntityType) {
		Content masterContentType = (Content) masterEntityType;
		JAXBContentType jaxbContentType = new JAXBContentType(masterContentType);
		jaxbContentType.setDefaultModelId(this.extractModelId(masterContentType.getDefaultModel()));
		jaxbContentType.setListModelId(this.extractModelId(masterContentType.getListModel()));
		return jaxbContentType;
	}
    
    private Integer extractModelId(String stringModelId) {
        if (null == stringModelId) return null;
        Integer modelId = null;
        try {
            modelId = Integer.parseInt(stringModelId);
        } catch (Throwable t) {
            //nothing to catch
        }
        return modelId;
    }
    
    public StringApiResponse addContentType(JAXBContentType jaxbContentType) throws Throwable {
        return super.addEntityType(jaxbContentType);
    }
	
	@Override
	protected void checkNewEntityType(JAXBEntityType jaxbEntityType, IApsEntity newEntityType, StringApiResponse response) throws ApiException, Throwable {
		JAXBContentType jaxbContentType = (JAXBContentType) jaxbEntityType;
		Content contentType = (Content) newEntityType;
		boolean defaultModelCheck = this.checkContentModel(jaxbContentType.getDefaultModelId(), contentType, response);
		if (defaultModelCheck) {
			contentType.setDefaultModel(String.valueOf(jaxbContentType.getDefaultModelId()));
		}
		boolean listModelCheck = this.checkContentModel(jaxbContentType.getListModelId(), contentType, response);
		if (listModelCheck) {
			contentType.setListModel(String.valueOf(jaxbContentType.getListModelId()));
		}
	}
	
    public StringApiResponse updateContentType(JAXBContentType jaxbContentType) throws Throwable {
        return super.updateEntityType(jaxbContentType);
    }
	
	@Override
	protected void checkEntityTypeToUpdate(JAXBEntityType jaxbEntityType, IApsEntity entityTypeToUpdate, StringApiResponse response) throws ApiException, Throwable {
		JAXBContentType jaxbContentType = (JAXBContentType) jaxbEntityType;
		Content contentType = (Content) entityTypeToUpdate;
		boolean defaultModelCheck = this.checkContentModel(jaxbContentType.getDefaultModelId(), contentType, response);
		if (defaultModelCheck) {
			contentType.setDefaultModel(String.valueOf(jaxbContentType.getDefaultModelId()));
		}
		boolean listModelCheck = this.checkContentModel(jaxbContentType.getListModelId(), contentType, response);
		if (listModelCheck) {
			contentType.setListModel(String.valueOf(jaxbContentType.getListModelId()));
		}
	}
    
    private boolean checkContentModel(Integer modelId, Content contentType, StringApiResponse response) {
        if (null == modelId) {
			return true;
		}
        ContentModel contentModel = this.getContentModelManager().getContentModel(modelId);
        if (null == contentModel) {
            ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, 
					"Content model with id '" + modelId + "' does not exist", Response.Status.ACCEPTED);
            response.addError(error);
            return false;
        }
        if (!contentType.getTypeCode().equals(contentModel.getContentType())) {
            ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, 
					"Content model with id '" + modelId + "' is for contents of type '" + contentModel.getContentType() + "'", Response.Status.ACCEPTED);
            response.addError(error);
            return false;
        }
        return true;
    }
    
    public void deleteContentType(Properties properties) throws ApiException, Throwable {
        super.deleteEntityType(properties);
    }
	
	@Override
	protected String getTypeLabel() {
		return "Content type";
	}
	
	@Override
	protected String getTypeCodeParamName() {
		return "code";
	}
	
	@Override
	protected IEntityManager getEntityManager() {
		return this.getContentManager();
	}
    
    protected IContentManager getContentManager() {
        return _contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }
    
    protected IContentModelManager getContentModelManager() {
        return _contentModelManager;
    }
    public void setContentModelManager(IContentModelManager contentModelManager) {
        this._contentModelManager = contentModelManager;
    }
    
    private IContentManager _contentManager;
    private IContentModelManager _contentModelManager;
	
}
