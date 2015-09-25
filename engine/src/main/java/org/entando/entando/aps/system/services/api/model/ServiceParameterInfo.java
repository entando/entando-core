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
package org.entando.entando.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "serviceParameterInfo")
@XmlType(propOrder = {"key", "description", "required", "defaultValue"})
public class ServiceParameterInfo extends ApiMethodParameter {
	
	public ServiceParameterInfo() {}
	
	public ServiceParameterInfo(ApiMethodParameter parameter) {
		this.setKey(parameter.getKey());
		this.setDescription(parameter.getDescription());
		this.setRequired(parameter.isRequired());
	}
	
	@XmlElement(name = "key", required = true)
	@Override
	public String getKey() {
		return super.getKey();
	}
	
	@XmlElement(name = "description", required = true)
	@Override
	public String getDescription() {
		return super.getDescription();
	}
	
	@XmlElement(name = "required", required = true)
	@Override
	public boolean isRequired() {
		return super.isRequired();
	}
	
	@Override
	public void setRequired(boolean required) {
		super.setRequired(required);
	}
	
	@XmlElement(name = "defaultValue", required = true)
	public String getDefaultValue() {
		return _defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this._defaultValue = defaultValue;
	}

	private String _defaultValue;
	
}