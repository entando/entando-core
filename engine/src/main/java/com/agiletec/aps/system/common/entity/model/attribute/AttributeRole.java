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
package com.agiletec.aps.system.common.entity.model.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The attribute role class.
 * @author E.Santoboni
 */
public class AttributeRole implements Serializable {
	
	public AttributeRole(String name, String description, List<String> allowedTypes) {
		this._name = name;
		this._description = description;
		this._allowedAttributeTypes = allowedTypes;
	}
	
	@Override
	public AttributeRole clone() {
		List<String> allowedTypes = new ArrayList<>();
		allowedTypes.addAll(this.getAllowedAttributeTypes());
		AttributeRole clone = new AttributeRole(this.getName(), this.getDescription(), allowedTypes);
		clone.setFormFieldType(this.getFormFieldType());
		return clone;
	}
	
	public String getName() {
		return _name;
	}
	public String getDescription() {
		return _description;
	}
	public List<String> getAllowedAttributeTypes() {
		return _allowedAttributeTypes;
	}
	
	public FormFieldTypes getFormFieldType() {
		return _formFieldType;
	}
	public void setFormFieldType(FormFieldTypes formFieldType) {
		this._formFieldType = formFieldType;
	}
	
	private String _name;
	private String _description;
	private List<String> _allowedAttributeTypes;
	private FormFieldTypes _formFieldType = FormFieldTypes.TEXT;
	
	public enum FormFieldTypes {TEXT, DATE, NUMBER, BOOLEAN}
	
}