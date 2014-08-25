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
		List<String> allowedTypes = new ArrayList<String>();
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