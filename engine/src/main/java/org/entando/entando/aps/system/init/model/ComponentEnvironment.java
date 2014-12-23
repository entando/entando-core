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
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ComponentEnvironment extends AbstractComponentModule implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentEnvironment.class);
	
	public ComponentEnvironment(Element environmentElement, Map<String, String> postProcessClasses) throws Throwable {
		try {
			String environmentCode = environmentElement.getAttributeValue("code");
			this.setCode(environmentCode);
			Element defaultSqlResourcesElement = environmentElement.getChild("defaultSqlResources");
			super.extractSqlResources(defaultSqlResourcesElement);
			Element postProcessesElement = environmentElement.getChild("postProcesses");
			super.createPostProcesses(postProcessesElement, postProcessClasses);
		} catch (Throwable t) {
			_logger.error("Error creating ComponentEnvironment", t);
			throw new ApsSystemException("Error creating ComponentEnvironment", t);
		}
	}
	
	public String getCode() {
		return _code;
	}
	protected void setCode(String code) {
		this._code = code;
	}
	
	public Map<String, String> getDefaultSqlResourcesPaths() {
		return super.getSqlResourcesPaths();
	}
	protected void setDefaultSqlResourcesPaths(Map<String, String> defaultSqlResourcesPaths) {
		super.setSqlResourcesPaths(defaultSqlResourcesPaths);
	}
	
	private String _code;
	
}
