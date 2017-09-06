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
package org.entando.entando.aps.system.services.dataobject.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.DataTypeUtilizer;

public class DataObjectHelper implements IDataTypeHelper, ApplicationContextAware {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectHelper.class);

	@Override
	public Map<String, List<?>> getReferencingObjects(DataObject content) throws ApsSystemException {
		Collection<DataTypeUtilizer> contentUtilizers = this.getDataTypeUtilizers();
		Map<String, List<?>> references = this.getReferencingObjects(content, contentUtilizers);
		return references;
	}

	@Override
	public Map<String, List<?>> getReferencingObjects(DataObject content, Collection<DataTypeUtilizer> contentUtilizers) throws ApsSystemException {
		Map<String, List<?>> references = new HashMap<String, List<?>>();
		try {
			for (DataTypeUtilizer contentUtilizer : contentUtilizers) {
				if (contentUtilizer != null) {
					List<?> utilizers = contentUtilizer.getDataTypeUtilizers(content.getId());
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(contentUtilizer.getName() + "Utilizers", utilizers);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting referencing object", t);
			throw new ApsSystemException("Error searching content referencing objects", t);
		}
		return references;
	}

	@Override
	public List<DataTypeUtilizer> getDataTypeUtilizers() {
		ApplicationContext applicationContext = this.getApplicationContext();
		String[] defNames = applicationContext.getBeanNamesForType(DataTypeUtilizer.class);
		List<DataTypeUtilizer> contentUtilizers = new ArrayList<DataTypeUtilizer>(defNames.length);
		for (String defName : defNames) {
			Object service = null;
			try {
				service = applicationContext.getBean(defName);
				if (service != null) {
					DataTypeUtilizer contentUtilizer = (DataTypeUtilizer) service;
					contentUtilizers.add(contentUtilizer);
				}
			} catch (Throwable t) {
				_logger.error("error loading ReferencingObject {}", defName, t);
			}
		}
		return contentUtilizers;
	}

	protected ApplicationContext getApplicationContext() throws BeansException {
		return _applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this._applicationContext = applicationContext;
	}

	private ApplicationContext _applicationContext;

}
