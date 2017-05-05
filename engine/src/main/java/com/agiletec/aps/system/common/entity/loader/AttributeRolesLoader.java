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
package com.agiletec.aps.system.common.entity.loader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.parse.AttributeRoleDOM;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;
import org.springframework.beans.BeansException;

/**
 * The Class loader of the extra attribute roles.
 * @author E.Santoboni
 */
public class AttributeRolesLoader {

	private static final Logger _logger =  LoggerFactory.getLogger(AttributeRolesLoader.class);
	
	public Map<String, AttributeRole> extractAttributeRoles(String attributeRolesFileName, BeanFactory beanFactory, IEntityManager entityManager) {
		Map<String, AttributeRole> attributeRoles = new HashMap<>();
		try {
			this.setEntityManager(entityManager);
			this.setBeanFactory(beanFactory);
			this.loadDefaultRoles(attributeRolesFileName, attributeRoles);
			this.loadExtraRoles(attributeRoles);
		} catch (Throwable t) {
			_logger.error("Error loading attribute Roles", t);
		}
		return attributeRoles;
	}
	
	private void loadDefaultRoles(String attributeRolesFileName, Map<String, AttributeRole> attributeRoles) {
		try {
			String xml = this.extractConfigFile(attributeRolesFileName);
			if (null != xml) {
				AttributeRoleDOM dom = new AttributeRoleDOM();
				attributeRoles.putAll(dom.extractRoles(xml, attributeRolesFileName));
			}
		} catch (Throwable t) {
			_logger.error("Error loading attribute Roles : file {}", attributeRolesFileName, t);
		}
	}
	
	private void loadExtraRoles(Map<String, AttributeRole> attributeRoles) {
		try {
			ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
			String[] defNames = factory.getBeanNamesForType(ExtraAttributeRolesWrapper.class);
			for (int i=0; i<defNames.length; i++) {
				try {
					Object loader = this.getBeanFactory().getBean(defNames[i]);
					if (loader != null) {
						((ExtraAttributeRolesWrapper) loader).executeLoading(attributeRoles, this.getEntityManager());
					}
				} catch (BeansException | ApsSystemException t) {
					_logger.error("Error extracting attribute support object : bean {}", defNames[i], t);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading attribute support object", t);
		}
	}
	
	private String extractConfigFile(String fileName) throws Throwable {
		InputStream is = this.getEntityManager().getClass().getResourceAsStream(fileName);
		if (null == is) {
			_logger.debug("{}: there isn't any object to load : file {}", this.getEntityManager().getClass().getName(), fileName);
			return null;
		}
		return FileTextReader.getText(is);
	}
	
	protected IEntityManager getEntityManager() {
		return _entityManager;
	}
	protected void setEntityManager(IEntityManager entityManager) {
		this._entityManager = entityManager;
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	protected void setBeanFactory(BeanFactory beanFactory) {
		this._beanFactory = beanFactory;
	}
	
	private IEntityManager _entityManager;
	private BeanFactory _beanFactory;
	
}