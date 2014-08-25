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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "serviceInfo")
@XmlType(propOrder = {"key", "description", "tag", "myEntando", "parameters"})
public class ServiceInfo implements Serializable {
	
	public ServiceInfo() {}
	
	public ServiceInfo(String key, String description, String tag, boolean myEntando) {
		this.setKey(key);
		this.setDescription(description);
		this.setTag(tag);
		this.setMyEntando(myEntando);
	}
	
	@XmlElement(name = "key")
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	@XmlElement(name = "description")
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	@XmlElement(name = "tag")
	public String getTag() {
		return _tag;
	}
	public void setTag(String tag) {
		this._tag = tag;
	}

	@XmlElement(name = "parameter", required = true)
	@XmlElementWrapper(name = "parameters")
	public List<ServiceParameterInfo> getParameters() {
		return _parameters;
	}
	public void addParameter(ServiceParameterInfo parameter) {
		if (null == this._parameters) {
			this._parameters = new ArrayList<ServiceParameterInfo>();
		}
		_parameters.add(parameter);
	}
	
	@XmlElement(name = "myentando")
	public boolean isMyEntando() {
		return _myEntando;
	}
	protected void setMyEntando(boolean myEntando) {
		this._myEntando = myEntando;
	}
	
	private String _key;
	private String _description;
	private String _tag;
	private List<ServiceParameterInfo> _parameters = null;
	private boolean _myEntando;
	
}