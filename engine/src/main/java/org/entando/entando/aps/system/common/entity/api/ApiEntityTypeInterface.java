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
package org.entando.entando.aps.system.common.entity.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.model.StringListApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public abstract class ApiEntityTypeInterface {

	private static final Logger _logger = LoggerFactory.getLogger(ApiEntityTypeInterface.class);
	
	public StringListApiResponse getEntityTypes(Properties properties) throws Throwable {
		StringListApiResponse response = new StringListApiResponse();
		try {
			IEntityManager manager = this.getEntityManager();
			Map<String, IApsEntity> prototypes = manager.getEntityPrototypes();
			List<String> codes = new ArrayList<String>();
			codes.addAll(prototypes.keySet());
			Collections.sort(codes);
			response.setResult(codes, null);
		} catch (Throwable t) {
			_logger.error("Error extracting entity type codes", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityTypes");
			throw new ApsSystemException("Error extracting entity type codes", t);
		}
		return response;
	}

	public JAXBEntityType getEntityType(Properties properties) throws Throwable {
		JAXBEntityType jaxbEntityType = null;
		try {
			IEntityManager manager = this.getEntityManager();
			String typeCode = properties.getProperty(this.getTypeCodeParamName());
			IApsEntity masterEntityType = manager.getEntityPrototype(typeCode);
			if (null == masterEntityType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, this.getTypeLabel() + " with code '" + typeCode + "' does not exist");
			}
			jaxbEntityType = this.createJAXBEntityType(masterEntityType);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error extracting entity type", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityType");
			throw new ApsSystemException("Error extracting entity type", t);
		}
		return jaxbEntityType;
	}
	
	protected JAXBEntityType createJAXBEntityType(IApsEntity masterEntityType) {
		return new JAXBEntityType(masterEntityType);
	}

	public StringApiResponse addEntityType(JAXBEntityType jaxbEntityType) throws Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			IEntityManager manager = this.getEntityManager();
			String typeCode = jaxbEntityType.getTypeCode();
			IApsEntity masterEntityType = manager.getEntityPrototype(typeCode);
			if (null != masterEntityType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, this.getTypeLabel() + " with code '" + typeCode + "' already exists");
			}
			if (typeCode == null || typeCode.length() != 3) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Invalid type code - '" + typeCode + "'");
			}
			Map<String, AttributeInterface> attributes = manager.getEntityAttributePrototypes();
			IApsEntity newEntityType = jaxbEntityType.buildEntityType(manager.getEntityClass(), attributes);
			this.checkNewEntityType(jaxbEntityType, newEntityType, response);
			((IEntityTypesConfigurer) manager).addEntityPrototype(newEntityType);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error extracting entity type", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityType");
			throw new ApsSystemException("Error extracting entity type", t);
		}
		return response;
	}
	
	protected void checkNewEntityType(JAXBEntityType jaxbEntityType, 
			IApsEntity newEntityType, StringApiResponse response) throws ApiException, Throwable {
		//Nothing to do
	}
	
	public StringApiResponse updateEntityType(JAXBEntityType jaxbEntityType) throws Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String typeCode = jaxbEntityType.getTypeCode();
			IApsEntity masterUserProfileType = this.getEntityManager().getEntityPrototype(typeCode);
			if (null == masterUserProfileType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, this.getTypeLabel() + " with code '" + typeCode + "' doesn't exist");
			}
			Map<String, AttributeInterface> attributes = this.getEntityManager().getEntityAttributePrototypes();
			IApsEntity entityTypeToUpdate = jaxbEntityType.buildEntityType(this.getEntityManager().getEntityClass(), attributes);
			this.checkEntityTypeToUpdate(jaxbEntityType, entityTypeToUpdate, response);
			((IEntityTypesConfigurer) this.getEntityManager()).updateEntityPrototype(entityTypeToUpdate);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error updating entity type", t);
			//ApsSystemUtils.logThrowable(t, this, "updateEntityType");
			throw new ApsSystemException("Error updating entity type", t);
		}
		return response;
	}
	
	protected void checkEntityTypeToUpdate(JAXBEntityType jaxbEntityType, 
			IApsEntity entityTypeToUpdate, StringApiResponse response) throws ApiException, Throwable {
		//Nothing to do
	}
	
	public void deleteEntityType(Properties properties) throws Throwable {
		try {
			String typeCode = properties.getProperty(this.getTypeCodeParamName());
			IApsEntity masterEntityType = this.getEntityManager().getEntityPrototype(typeCode);
			if (null == masterEntityType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, this.getTypeLabel() + " with code '" + typeCode + "' doesn't exist");
			}
			EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, typeCode, false);
			List<String> entityIds = this.getEntityManager().searchId(new EntitySearchFilter[]{filter});
			if (null != entityIds && !entityIds.isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, this.getTypeLabel() + " '" + typeCode + "' are used into " + entityIds.size() + " entities");
			}
			this.checkEntityTypeToDelete(masterEntityType);
			((IEntityTypesConfigurer) this.getEntityManager()).removeEntityPrototype(typeCode);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error deleting Entity type", t);
			//ApsSystemUtils.logThrowable(t, this, "deleteEntityType");
			throw new ApsSystemException("Error deleting Entity type", t);
		}
	}
	
	protected void checkEntityTypeToDelete(IApsEntity entityTypeToDelete) throws ApiException, Throwable {
		//Nothing to do
	}
	
	protected abstract IEntityManager getEntityManager();
	
	protected String getTypeLabel() {
		return "Entity type";
	}
	
	protected String getTypeCodeParamName() {
		return "typeCode";
	}
	
}