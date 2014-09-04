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
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.IEntityReferencesReloadingAction;

/**
 * @author E.Santoboni
 */
public class EntityTypesAction extends AbstractEntityConfigAction implements IEntityTypesAction, IEntityReferencesReloadingAction {

	private static final Logger _logger = LoggerFactory.getLogger(EntityTypesAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (!this.hasFieldErrors()) {
			try {
				this.getEntityManager();
			} catch (Throwable t) {
				String[] args = {this.getEntityManagerName()};
				this.addFieldError("entityManagerName", this.getText("error.entityManager.not.valid", args));
			}
		}
	}
	
	@Override
	public String reloadEntityManagerReferences() {
		try {
			String typeCode = this.getEntityTypeCode();
			this.getEntityManager().reloadEntitiesReferences(typeCode);
		} catch (Throwable t) {
			_logger.error("reloadEntityManagerReferences", t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntityManagerReferences");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getEntityManagerStatus(String entityManagerName, String typeCode) {
		IEntityManager entityManager = (IEntityManager) this.getBeanFactory().getBean(entityManagerName);
		return entityManager.getStatus(typeCode);
	}
	
	@Override
	public List<IApsEntity> getEntityPrototypes() {
		List<IApsEntity> entityPrototypes = null;
		try {
			Map<String, IApsEntity> modelMap = this.getEntityManager().getEntityPrototypes();
			entityPrototypes = new ArrayList<IApsEntity>(modelMap.values());
			BeanComparator comparator = new BeanComparator("typeDescr");
			Collections.sort(entityPrototypes, comparator);
		} catch (Throwable t) {
			_logger.error("Error on extracting entity prototypes", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityPrototypes");
			throw new RuntimeException("Error on extracting entity prototypes", t);
		}
		return entityPrototypes;
	}
	
	@Override
	public String initAddEntityType() {
		return SUCCESS;
	}
	
	@Override
	public String initEditEntityType() {
		try {
			if (null == this.getEntityPrototype(this.getEntityTypeCode())) {
				String[] args = {this.getEntityTypeCode()};
				this.addFieldError("entityTypeCode", this.getText("error.entity.type.null", args));
				return "wrongEntityType";
			}
		} catch (Throwable t) {
			_logger.error("error in initEditEntityType", t);
			//ApsSystemUtils.logThrowable(t, this, "initEditEntityType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public IApsEntity getEntityPrototype(String typeCode) {
		IEntityManager entityManager = this.getEntityManager();
		return entityManager.getEntityPrototype(typeCode);
	}
	
	@Override
	public String trashEntityType() {
		try {
			String checkResult = this.checkDelete();
			if (null != checkResult) return checkResult;
		} catch (Throwable t) {
			_logger.error("error in trashEntityType", t);
			//ApsSystemUtils.logThrowable(t, this, "trashEntityType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeEntityType() {
		try {
			String checkResult = this.checkDelete();
			if (null != checkResult) return checkResult;
			IEntityTypesConfigurer entityManager = (IEntityTypesConfigurer) this.getEntityManager();
			entityManager.removeEntityPrototype(this.getEntityTypeCode());
		} catch (Throwable t) {
			_logger.error("error in removeEntityType", t);
			//ApsSystemUtils.logThrowable(t, this, "removeEntityType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private String checkDelete() throws Throwable {
		IApsEntity entityType = this.getEntityPrototype(this.getEntityTypeCode());
		if (null == entityType) {
			String[] args = {this.getEntityTypeCode()};
			this.addFieldError("entityTypeCode", this.getText("error.entityTypeCode.is.null",args));
			return INPUT;
		}
		EntitySearchFilter typeCodeFilter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, entityType.getTypeCode(), false);
		EntitySearchFilter[] filters = {typeCodeFilter};
		List<String> entityIds = this.getEntityManager().searchId(filters);
		if (null != entityIds && !entityIds.isEmpty()) {
			this.setReferences(entityIds);
			return "hasReferences";
		}
		return null;
	}
	
	public List<String> getReferences() {
		return _references;
	}
	public void setReferences(List<String> references) {
		this._references = references;
	}
	
	private List<String> _references;
	
}