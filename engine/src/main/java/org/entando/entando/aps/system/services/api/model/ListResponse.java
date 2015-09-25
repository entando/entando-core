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