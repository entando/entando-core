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

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.parse.AttributeDisablingCodesDOM;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * The Class Loader of the extra attribute disabling codes.
 * @author E.Santoboni
 */
public final class AttributeDisablingCodesLoader {

	private static final Logger _logger =  LoggerFactory.getLogger(AttributeDisablingCodesLoader.class);
	
	public Map<String, String> extractDisablingCodes(String attributeDisablingCodesFileName, BeanFactory beanFactory, IEntityManager entityManager) {
		Map<String, String> disablingCodes = new HashMap<>();
		try {
			this.setEntityManager(entityManager);
			this.setBeanFactory(beanFactory);
			this.loadDefaultDisablingCodes(attributeDisablingCodesFileName, disablingCodes);
			this.loadExtraDisablingCodes(disablingCodes);
		} catch (Throwable t) {
			_logger.error("Error loading disabling codes", t);
		}
		return disablingCodes;
	}
	
	private void loadDefaultDisablingCodes(String disablingCodesFileName, Map<String, String> disablingCodes) {
		try {
			String xml = this.extractConfigFile(disablingCodesFileName);
			if (null != xml) {
				AttributeDisablingCodesDOM dom = new AttributeDisablingCodesDOM();
				disablingCodes.putAll(dom.extractDisablingCodes(xml, disablingCodesFileName));
			}
		} catch (Throwable t) {
			_logger.error("Error loading disabling codes from file {}", disablingCodesFileName, t);
		}
	}
	
	private void loadExtraDisablingCodes(Map<String, String> disablingCodes) {
		try {
			ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
			String[] defNames = factory.getBeanNamesForType(ExtraAttributeDisablingCodesWrapper.class);
			for (int i=0; i<defNames.length; i++) {
				try {
					Object loader = this.getBeanFactory().getBean(defNames[i]);
					if (loader != null) {
						((ExtraAttributeDisablingCodesWrapper) loader).executeLoading(disablingCodes, this.getEntityManager());
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
			_logger.debug("{} : there isn't any object to load : file {}", this.getEntityManager().getClass().getName(), fileName);
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