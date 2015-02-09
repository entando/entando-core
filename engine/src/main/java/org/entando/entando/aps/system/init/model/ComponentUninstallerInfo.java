/*
 * Copyright 2013-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.model;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ComponentUninstallerInfo extends AbstractComponentModule implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentUninstallerInfo.class);
	
	public ComponentUninstallerInfo(Element environmentElement, Map<String, String> postProcessClasses) throws Throwable {
		try {
			Element defaultSqlResourcesElement = environmentElement.getChild("sqlResources");
			super.extractSqlResources(defaultSqlResourcesElement);
			Element resourcesElement = environmentElement.getChild("resources");
			this.extractResources(resourcesElement);
			Element postProcessesElement = environmentElement.getChild("postProcesses");
			super.createPostProcesses(postProcessesElement, postProcessClasses);
		} catch (Throwable t) {
			_logger.error("Error creating ComponentUninstallerInfo", t);
			throw new ApsSystemException("Error creating ComponentUninstallerInfo", t);
		}
	}
	
	private void extractResources(Element resourcesElement) throws ApsSystemException {
		if (null != resourcesElement) {
			List<Element> resourceElements = resourcesElement.getChildren("resource");
			for (int j = 0; j < resourceElements.size(); j++) {
				Element resourceElement = resourceElements.get(j);
				String resourcePath = resourceElement.getText().trim();
				this.getResourcesPaths().add(resourcePath);
			}
		}
	}
	
	public List<String> getResourcesPaths() {
		return _resourcesPaths;
	}
	
	private List<String> _resourcesPaths = new ArrayList<String>();
	
}