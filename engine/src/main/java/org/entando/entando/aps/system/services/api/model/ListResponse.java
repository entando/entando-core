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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "items")
@XmlType(propOrder = {"size", "_entity"})
public class ListResponse<T> {
	
	public ListResponse() {}
	
	public ListResponse(List<T> entity) {
		this._entity = entity;
	}
	
	@XmlElement(name = "size", required =false)
	public String getSize() {
		if (null != this._entity) {
			return String.valueOf(this._entity.size());
		}
		return null;
	}
	
	@XmlElement(name = "item", required = false)
	private List<T> _entity;
	
}