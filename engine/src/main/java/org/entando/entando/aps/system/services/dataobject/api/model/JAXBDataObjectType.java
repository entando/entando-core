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
package org.entando.entando.aps.system.services.dataobject.api.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.common.entity.api.JAXBEntityType;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "contentType")
public class JAXBDataObjectType extends JAXBEntityType {

	public JAXBDataObjectType() {
	}

	public JAXBDataObjectType(DataObject dataObject) {
		super(dataObject);
	}

	public Integer getDefaultModelId() {
		return _defaultModelId;
	}

	public void setDefaultModelId(Integer defaultModelId) {
		this._defaultModelId = defaultModelId;
	}

	public Integer getListModelId() {
		return _listModelId;
	}

	public void setListModelId(Integer listModelId) {
		this._listModelId = listModelId;
	}

	private Integer _defaultModelId;
	private Integer _listModelId;

}
