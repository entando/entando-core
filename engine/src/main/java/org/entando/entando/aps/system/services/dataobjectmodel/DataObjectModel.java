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
package org.entando.entando.aps.system.services.dataobjectmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.entando.entando.aps.system.services.api.model.CDataXmlTypeAdapter;

/**
 * Rappresenta un modello di datatype.
 *
 * @author E.Santoboni
 */
@XmlRootElement(name = "contentModel")
@XmlType(propOrder = {"id", "dataType", "description", "shape", "stylesheet"})
public class DataObjectModel implements Comparable, Serializable {

	@XmlElement(name = "id", required = true)
	public long getId() {
		return _id;
	}

	public void setId(long id) {
		this._id = id;
	}

	@XmlElement(name = "dataType", required = true)
	public String getDataType() {
		return _dataType;
	}

	public void setDataType(String dataType) {
		this._dataType = dataType;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return _description;
	}

	public void setDescription(String descr) {
		this._description = descr;
	}

	@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
	@XmlElement(name = "shape", required = true)
	public String getShape() {
		return _dataShape;
	}

	public void setShape(String shape) {
		this._dataShape = shape;
	}

	@XmlElement(name = "stylesheet", required = false)
	public String getStylesheet() {
		return _stylesheet;
	}

	public void setStylesheet(String stylesheet) {
		this._stylesheet = stylesheet;
	}

	@Override
	public int compareTo(Object model) {
		int result = this.getDataType().compareTo(((DataObjectModel) model).getDataType());
		if (result == 0) {
			if (this.getId() > (((DataObjectModel) model).getId())) {
				return 1;
			} else {
				return -1;
			}
		}
		return result;
	}

	private long _id;
	private String _dataType;
	private String _description;
	private String _dataShape;
	private String _stylesheet;

}
