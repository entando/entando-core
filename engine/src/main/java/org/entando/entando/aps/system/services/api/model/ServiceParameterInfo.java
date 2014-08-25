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