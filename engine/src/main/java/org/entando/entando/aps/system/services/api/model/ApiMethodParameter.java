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

import java.io.Serializable;

import org.entando.entando.aps.system.services.api.ApiResourcesDefDOM;
import org.jdom.Element;

/**
 * The representation of an API parameter
 * @author E.Santoboni
 */
public class ApiMethodParameter implements Serializable {
	
	protected ApiMethodParameter() {}
	
	protected ApiMethodParameter(Element element) {
		this.setKey(element.getAttributeValue(ApiResourcesDefDOM.PARAMETER_KEY_ATTRIBUTE_NAME));
		this.setRequired(Boolean.parseBoolean(element.getAttributeValue(ApiResourcesDefDOM.PARAMETER_REQUIRED_ATTRIBUTE_NAME)));
		this.setOverridable(Boolean.parseBoolean(element.getAttributeValue(ApiResourcesDefDOM.PARAMETER_OVERRIDABLE_ATTRIBUTE_NAME)));
		this.setDescription(element.getChildText(ApiResourcesDefDOM.PARAMETER_DESCRIPTION_ELEMENT_NAME));
	}
	
	@Override
	public ApiMethodParameter clone() {
		ApiMethodParameter parameter = new ApiMethodParameter();
		parameter.setDescription(this.getDescription());
		parameter.setKey(this.getKey());
		parameter.setOverridable(this.isOverridable());
		parameter.setRequired(this.isRequired());
		return parameter; 
	}
	
	public String getKey() {
		return _key;
	}
	protected void setKey(String key) {
		this._key = key;
	}
	
	public boolean isRequired() {
		return _required;
	}
	protected void setRequired(boolean required) {
		this._required = required;
	}
	
	public String getDescription() {
		return _description;
	}
	protected void setDescription(String description) {
		this._description = description;
	}
	
	public boolean isOverridable() {
		return _overridable;
	}
	protected void setOverridable(boolean overridable) {
		this._overridable = overridable;
	}
	
	private String _key;
	private boolean _required;
	private String _description;
	private boolean _overridable;
	
}