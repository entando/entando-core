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
package com.agiletec.apsadmin.system.entity;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.common.entity.IEntityManager;

/**
 * @author E.Santoboni
 */
public class DefaultApsEntityFinderAction extends AbstractApsEntityFinderAction implements BeanFactoryAware {
	
	@Override
	public String delete() {
		// Operation Not Supported
		return FAILURE;
	}
	
	@Override
	public String trash() {
		// Operation Not Supported
		return FAILURE;
	}
	
	@Override
	protected void deleteEntity(String entityId) throws Throwable {
		// Operation Not Supported
	}
	
	@Override
	protected IEntityManager getEntityManager() {
		String entityManagerName = this.getEntityManagerName();
		return (IEntityManager) this.getBeanFactory().getBean(entityManagerName);
	}
	
	public String getEntityManagerName() {
		String sessionValue = (String) this.getRequest().getSession().getAttribute(ApsEntityActionConstants.ENTITY_TYPE_MANAGER_SESSION_PARAM);
		if (null != sessionValue) return sessionValue;
		return _entityManagerName;
	}
	public void setEntityManagerName(String entityManagerName) {
		this.getRequest().getSession().setAttribute(ApsEntityActionConstants.ENTITY_TYPE_MANAGER_SESSION_PARAM, entityManagerName);
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