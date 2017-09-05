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
package org.entando.entando.aps.system.services.datatypemodel;

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
@XmlType(propOrder = {"id", "contentType", "description", "contentShape", "stylesheet"})
public class DataModel implements Comparable, Serializable {

	@XmlElement(name = "id", required = true)
	public long getId() {
		return _id;
	}

	public void setId(long id) {
		this._id = id;
	}

	@XmlElement(name = "contentType", required = true)
	public String getContentType() {
		return _contentType;
	}

	public void setContentType(String contentType) {
		this._contentType = contentType;
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
	public String getContentShape() {
		return _contentShape;
	}

	public void setContentShape(String shape) {
		this._contentShape = shape;
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
		int result = this.getContentType().compareTo(((DataModel) model).getContentType());
		if (result == 0) {
			if (this.getId() > (((DataModel) model).getId())) {
				return 1;
			} else {
				return -1;
			}
		}
		return result;
	}

	private long _id;
	private String _contentType;
	private String _description;
	private String _contentShape;
	private String _stylesheet;

}
