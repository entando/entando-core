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
package com.agiletec.apsadmin.system.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public abstract class AbstractApsEntityFinderAction extends BaseAction implements IApsEntityFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractApsEntityFinderAction.class);
	
	@Override
	public String execute() {
		try {
			this.createBaseFilters();
		} catch (Throwable t) {
			_logger.error("error in execute", t);
			//ApsSystemUtils.logThrowable(t, this, "execute");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void createBaseFilters() {
		try {
			int initSize = this.getFilters().length;
			EntitySearchFilter[] roleFilters = this.getEntityActionHelper().getRoleFilters(this);
			this.addFilters(roleFilters);
			IApsEntity prototype = this.getEntityPrototype();
			if (null != prototype) {
				EntitySearchFilter filterToAdd = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, prototype.getTypeCode(), false);
				this.addFilter(filterToAdd);
				EntitySearchFilter[] filters = this.getEntityActionHelper().getAttributeFilters(this, prototype);
				this.addFilters(filters);
			}
			this.setAddedAttributeFilter(this.getFilters().length > initSize);
		} catch (Throwable t) {
			_logger.error("Error while creating entity filters", t);
			//ApsSystemUtils.logThrowable(t, this, "createBaseFilters");
			throw new RuntimeException("Error while creating entity filters", t);
		}
	}
	
	@Override
	public List<String> getSearchResult() {
		List<String> result = null;
		try {
			IEntityManager entityManager = this.getEntityManager();
			result = entityManager.searchId(this.getFilters());
		} catch (Throwable t) {
			_logger.error("Error while searching entity Ids", t);
			//ApsSystemUtils.logThrowable(t, this, "getSearchResult");
			throw new RuntimeException("Error while searching entity Ids", t);
		}
		return result;
	}
	
	protected void addFilters(EntitySearchFilter[] filters) {
		for (int i = 0; i < filters.length; i++) {
			EntitySearchFilter filterToAdd = filters[i];
			this.addFilter(filterToAdd);
		}
	}
	
	protected void addFilter(EntitySearchFilter filterToAdd) {
		EntitySearchFilter[] filters = this.getFilters();
		int len = filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		this.setFilters(newFilters);
	}
	
	@Override
	public String trash() {
		try {
			String checkResult = this.checkDeletingEntity();
			if (null != checkResult) return checkResult;
		} catch (Throwable t) {
			_logger.error("Error while trashing entity", t);
			//ApsSystemUtils.logThrowable(t, this, "trash");
			throw new RuntimeException("Error while trashing entity", t);
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			String checkResult = this.checkDeletingEntity();
			if (null != checkResult) return checkResult;
			this.deleteEntity(this.getEntityId());
		} catch (Throwable t) {
			_logger.error("Error while deleting entity", t);
			//ApsSystemUtils.logThrowable(t, this, "delete");
			throw new RuntimeException("Error while deleting entity", t);
		}
		return SUCCESS;
	}
	
	protected abstract void deleteEntity(String entityId) throws Throwable;
	
	protected String checkDeletingEntity() throws Throwable {
		IApsEntity entity = this.getEntity(this.getEntityId());
		if (null == entity) {
			String[] args = {this.getEntityId()};
			this.addFieldError("entityId", this.getText("error.entity.null",args));
			return INPUT;
		}
		return null;
	}
	
	public IApsEntity getEntityPrototype() {
		IEntityManager entityManager = this.getEntityManager();
		return entityManager.getEntityPrototype(this.getEntityTypeCode());
	}
	
	public List<IApsEntity> getEntityPrototypes() {
		List<IApsEntity> entityPrototypes = null;
		try {
			Map<String, IApsEntity> modelMap = this.getEntityManager().getEntityPrototypes();
			entityPrototypes = new ArrayList<IApsEntity>(modelMap.values());
			BeanComparator comparator = new BeanComparator("typeDescr");
			Collections.sort(entityPrototypes, comparator);
		} catch (Throwable t) {
			_logger.error("Error while extracting entity prototypes", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityPrototypes");
			throw new RuntimeException("Error while extracting entity prototypes", t);
		}
		return entityPrototypes;
	}
	
	protected IApsEntity getEntity(String entityId) {
		IApsEntity entity = null;
		try {
			IEntityManager entityManager = this.getEntityManager();
			entity = entityManager.getEntity(entityId);
		} catch (Throwable t) {
			_logger.error("Error while extracting entity", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntity");
			throw new RuntimeException("Error while extracting entity", t);
		}
		return entity;
	}
	
	public String getSearchFormFieldValue(String inputFieldName) {
		return this.getRequest().getParameter(inputFieldName);
	}
	
	@Deprecated
	public List<AttributeInterface> getSearcheableAttributes() {
		return this.getSearchableAttributes();
	}
	
	public List<AttributeInterface> getSearchableAttributes() {
		List<AttributeInterface> searchableAttributes = new ArrayList<AttributeInterface>();
		IApsEntity prototype = this.getEntityPrototype();
		if (null == prototype) {
			return searchableAttributes;
		}
		List<AttributeInterface> contentAttributes = prototype.getAttributeList();
		for (int i=0; i<contentAttributes.size(); i++) {
			AttributeInterface attribute = contentAttributes.get(i);
			if (attribute.isActive() && attribute.isSearchable()) {
				searchableAttributes.add(attribute);
			}
		}
		return searchableAttributes;
	}
	
	public List<AttributeRole> getAttributeRoles() {
		return this.getEntityManager().getAttributeRoles();
	}
	
	protected abstract IEntityManager getEntityManager();
	
	public String getEntityId() {
		return _entityId;
	}
	public void setEntityId(String entityId) {
		this._entityId = entityId;
	}
	
	public String getEntityTypeCode() {
		return _entityTypeCode;
	}
	public void setEntityTypeCode(String entityTypeCode) {
		this._entityTypeCode = entityTypeCode;
	}
	
	protected boolean isAddedAttributeFilter() {
		return _addedAttributeFilter;
	}
	protected void setAddedAttributeFilter(boolean addedAttributeFilter) {
		this._addedAttributeFilter = addedAttributeFilter;
	}
	
	protected IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}
	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}
	
	protected EntitySearchFilter[] getFilters() {
		return _filters;
	}
	protected void setFilters(EntitySearchFilter[] filters) {
		this._filters = filters;
	}
	
	private String _entityId;
	private String _entityTypeCode;
	
	private boolean _addedAttributeFilter;
	
	private IEntityActionHelper _entityActionHelper;
	
	private EntitySearchFilter[] _filters = new EntitySearchFilter[0];
	
} 