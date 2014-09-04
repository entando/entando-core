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
package com.agiletec.apsadmin.system.entity.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * @author E.Santoboni
 */
public class EntityTypeConfigAction extends AbstractEntityConfigAction implements IEntityTypeConfigAction {

	private static final Logger _logger = LoggerFactory.getLogger(EntityTypeConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		IApsEntity entityType = this.updateEntityOnSession();
		if (this.getOperationId() == ApsAdminSystemConstants.ADD && !this.hasFieldErrors()) {
			if (null != this.getEntityPrototype(entityType.getTypeCode())) {
				String[] args = {entityType.getTypeCode()};
				this.addFieldError("entityTypeCode", this.getText("error.entity.alredy.exists", args));
			}
		}
	}
	
	@Override
	public String addEntityType() {
		try {
			String checkEntityManagerResult = this.checkEntityManager();
			if (null != checkEntityManagerResult) return checkEntityManagerResult;
			Class entityClass = this.getEntityManager().getEntityClass();
			IApsEntity entityType = (IApsEntity) entityClass.newInstance();
			this.initSessionParams(entityType, ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in addEntityType", t);
			//ApsSystemUtils.logThrowable(t, this, "addEntityType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String editEntityType() {
		try {
			String checkEntityManagerResult = this.checkEntityManager();
			if (null != checkEntityManagerResult) return checkEntityManagerResult;
			IApsEntity entityType = this.getEntityPrototype(this.getEntityTypeCode());
			if (null == entityType) {
				String[] args = {this.getEntityTypeCode()};
				this.addFieldError("entityTypeCode", this.getText("error.entity.type.null",args));
				return INPUT;
			}
			this.initSessionParams(entityType, ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in editEntityType", t);
			//ApsSystemUtils.logThrowable(t, this, "editEntityType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private String checkEntityManager() {
		try {
			this.getEntityManager();
		} catch (Throwable t) {
			String[] args = {this.getEntityManagerName()};
			this.addFieldError("entityManagerName", this.getText("error.entityManager.invalid", args));
			return "wrongEntityManager";
		}
		return null;
	}
	
	private void initSessionParams(IApsEntity entityType, int operationCode) {
		this.getRequest().getSession().setAttribute(ENTITY_TYPE_MANAGER_SESSION_PARAM, this.getEntityManagerName());
		this.getRequest().getSession().setAttribute(ENTITY_TYPE_OPERATION_ID_SESSION_PARAM, new Integer(operationCode));
		this.getRequest().getSession().setAttribute(ENTITY_TYPE_ON_EDIT_SESSION_PARAM, entityType);
	}
	
	public IApsEntity getEntityType() {
		return (IApsEntity) this.getRequest().getSession().getAttribute(ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
	}
	
	public int getOperationId() {
		return (Integer) this.getRequest().getSession().getAttribute(ENTITY_TYPE_OPERATION_ID_SESSION_PARAM);
	}
	
	protected IApsEntity updateEntityOnSession() {
		IApsEntity entityType = this.getEntityType();
		if (this.getOperationId() == ApsAdminSystemConstants.ADD && null != this.getEntityTypeCode()) {
			entityType.setTypeCode(this.getEntityTypeCode());
		}
		entityType.setTypeDescr(this.getEntityTypeDescription());
		return entityType;
	}
	
	@Override
	public String addAttribute() {
		this.updateEntityOnSession();
		try {
			IEntityManager entityManager = this.getEntityManager();
			Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
			if (null == attributeTypes.get(this.getAttributeTypeCode())) {
				String[] args = {this.getAttributeTypeCode()};
				this.addFieldError("attributeTypeCode", this.getText("error.attribute.not.exists",args));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in addAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "addAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String editAttribute() {
		try {
			IApsEntity entityType = this.updateEntityOnSession();
			if (null == entityType.getAttribute(this.getAttributeName())) {
				String[] args = {this.getAttributeName()};
				this.addFieldError("attributeName", this.getText("error.attribute.not.exists", args));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in editAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "editAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String moveAttribute() {
		IApsEntity entity = this.updateEntityOnSession();
		try {
			int elementIndex = this.getAttributeIndex();
			String movement = this.getMovement();
			List<AttributeInterface> attributes = entity.getAttributeList();
			if (!(elementIndex==0 && movement.equals(ApsAdminSystemConstants.MOVEMENT_UP_CODE)) && 
					!(elementIndex==attributes.size()-1 && movement.equals(ApsAdminSystemConstants.MOVEMENT_DOWN_CODE))) {
				AttributeInterface attributeToMove = attributes.get(elementIndex);
				attributes.remove(elementIndex);
				if (movement.equals(ApsAdminSystemConstants.MOVEMENT_UP_CODE)) {
					attributes.add(elementIndex-1, attributeToMove);
				} 
				if (movement.equals(ApsAdminSystemConstants.MOVEMENT_DOWN_CODE)) {
					attributes.add(elementIndex+1, attributeToMove);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in moveAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "moveAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeAttribute() {
		IApsEntity entity = this.updateEntityOnSession();
		try {
			int elementIndex = this.getAttributeIndex();
			List<AttributeInterface> attributes = entity.getAttributeList();
			AttributeInterface attributeToRemove = attributes.get(elementIndex);
			attributes.remove(elementIndex);
			entity.getAttributeMap().remove(attributeToRemove.getName());
		} catch (Throwable t) {
			_logger.error("error in removeAttribute", t);
			//ApsSystemUtils.logThrowable(t, this, "removeAttribute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String saveEntityType() {
		try {
			IApsEntity entityType = this.getEntityType();
			entityType.setDefaultLang(this.getLangManager().getDefaultLang().getCode());
			if (this.getOperationId() == ApsAdminSystemConstants.ADD) {
				((IEntityTypesConfigurer) this.getEntityManager()).addEntityPrototype(entityType);
			} else {
				((IEntityTypesConfigurer) this.getEntityManager()).updateEntityPrototype(entityType);
			}
			String entityManagerName = (String) this.getRequest().getSession().getAttribute(ENTITY_TYPE_MANAGER_SESSION_PARAM);
			this.setEntityManagerName(entityManagerName);
		} catch (Throwable t) {
			_logger.error("error in saveEntityType", t);
			//ApsSystemUtils.logThrowable(t, this, "saveEntityType");
			return FAILURE;
		} finally {
			this.getRequest().getSession().removeAttribute(ENTITY_TYPE_MANAGER_SESSION_PARAM);
			this.getRequest().getSession().removeAttribute(ENTITY_TYPE_OPERATION_ID_SESSION_PARAM);
			this.getRequest().getSession().removeAttribute(ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		}
		return SUCCESS;
	}
	
	public List<AttributeInterface> getAttributeTypes() {
		List<AttributeInterface> attributes = null;
		try {
			IEntityManager entityManager = this.getEntityManager();
			Map<String, AttributeInterface> attributeTypes = entityManager.getEntityAttributePrototypes();
			attributes = new ArrayList<AttributeInterface>(attributeTypes.values());
			Collections.sort(attributes, new BeanComparator("type"));
		} catch (Throwable t) {
			_logger.error("Error while extracting attribute Types", t);
			//ApsSystemUtils.logThrowable(t, this, "getAttributeTypes");
			throw new RuntimeException("Error while extracting attribute Types", t);
		}
		return attributes;
	}
	
	public boolean isEntityManagerSearchEngineUser() {
		return this.getEntityManager().isSearchEngineUser();
	}
	
	protected IApsEntity getEntityPrototype(String typeCode) {
		IEntityManager entityManager = this.getEntityManager();
		return entityManager.getEntityPrototype(typeCode);
	}
	
	@Override
	public String getEntityManagerName() {
		if (null != super.getEntityManagerName()) {
			return super.getEntityManagerName();
		}
		return (String) this.getRequest().getSession().getAttribute(ENTITY_TYPE_MANAGER_SESSION_PARAM);
	}
	
	public String getEntityTypeDescription() {
		return _entityTypeDescription;
	}
	public void setEntityTypeDescription(String entityTypeDescription) {
		this._entityTypeDescription = entityTypeDescription;
	}
	
	public int getAttributeIndex() {
		return _attributeIndex;
	}
	public void setAttributeIndex(int attributeIndex) {
		this._attributeIndex = attributeIndex;
	}

	public String getMovement() {
		return _movement;
	}
	public void setMovement(String movement) {
		this._movement = movement;
	}
	
	public String getAttributeTypeCode() {
		return _attributeTypeCode;
	}
	public void setAttributeTypeCode(String attributeTypeCode) {
		this._attributeTypeCode = attributeTypeCode;
	}
	
	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}
	
	private String _entityTypeDescription;
	
	private int _attributeIndex;
	private String _movement;
	
	private String _attributeTypeCode;
	private String _attributeName;
	
}