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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class EntityManagersAction extends BaseAction implements IEntityReferencesReloadingAction, BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(EntityManagersAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (!this.hasFieldErrors()) {
			try {
				String entityManagerName = this.getEntityManagerName();
				this.getBeanFactory().getBean(entityManagerName);
			} catch (Throwable t) {
				String[] args = {this.getEntityManagerName()};
				this.addFieldError("entityManagerName", this.getText("error.entityManager.not.valid", args));
			}
		}
	}
	
	public List<String> getEntityManagers() {
		List<String> serviceNames = null;
		try {
			ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
			String[] defNames = factory.getBeanNamesForType(IEntityManager.class);
			serviceNames = Arrays.asList(defNames);
		} catch (Throwable t) {
			_logger.error("Error while extracting entity managers", t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityManagers");
			throw new RuntimeException("Error while extracting entity managers", t);
		}
		return serviceNames;
	}
	
	@Override
	public String reloadEntityManagerReferences() {
		try {
			String entityManagerName = this.getEntityManagerName();
			IEntityManager entityManager = (IEntityManager) this.getBeanFactory().getBean(entityManagerName);
			String typeCode = null;
			entityManager.reloadEntitiesReferences(typeCode);
		} catch (Throwable t) {
			_logger.error("reloadEntityManagerReferences", t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntityManagerReferences");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getEntityManagerStatus(String entityManagerName) {
		IEntityManager entityManager = (IEntityManager) this.getBeanFactory().getBean(entityManagerName);
		return entityManager.getStatus();
	}
	
	public String getEntityManagerName() {
		return _entityManagerName;
	}
	public void setEntityManagerName(String entityManagerName) {
		this._entityManagerName = entityManagerName;
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private String _entityManagerName;
	
	private BeanFactory _beanFactory;
	
}