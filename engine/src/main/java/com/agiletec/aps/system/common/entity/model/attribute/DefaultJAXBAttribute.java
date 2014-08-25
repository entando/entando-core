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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlType(propOrder = {"name", "description", "type", "value", "roles"})
public class DefaultJAXBAttribute {
    
    @XmlElement(name = "name", required = true)
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }
	
	@XmlElement(name = "description", required = false)
    public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
    
    @XmlElement(name = "type", required = true)
    public String getType() {
        return _type;
    }
    public void setType(String type) {
        this._type = type;
    }
    
    @XmlElement(name = "value", required = false)
    public Object getValue() {
        return _value;
    }
    public void setValue(Object value) {
        this._value = value;
    }
    
    @XmlElement(name = "role", required = false)
    @XmlElementWrapper(name = "roles")
    public List<String> getRoles() {
        return _roles;
    }
    public void setRoles(List<String> roles) {
        this._roles = roles;
    }
    
    private String _name;
	private String _description;
    private String _type;
    private Object _value;
    private List<String> _roles;
    
}