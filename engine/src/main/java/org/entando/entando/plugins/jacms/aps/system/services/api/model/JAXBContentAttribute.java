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
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "contentAttribute")
@XmlType(propOrder = {"contentId", "attributeName", "langCode", "value"})
public class JAXBContentAttribute implements Serializable {

	public JAXBContentAttribute() {
		super();
	}

	private String _contentId;
	private String _attributeName;
	private String _langCode;
	private String _value;

	/*
	@XmlElement(name = "key", required = true)
	public String getKey() {
		return _key;
	}

	public void setKey(String key) {
		this._key = key;
	}

	//modifica per redhat
	@XmlElement(name = "labels", required = true)
	//@XmlElementWrapper(name = "labels")
	public List<JAXBLabel> getLabels() {
		return _labels;
	}

	public void setLabels(List<JAXBLabel> labels) {
		this._labels = labels;
	}

	private String _key;
	private List<JAXBLabel> _labels;
	 */
	@XmlElement(name = "contentId", required = true)
	public String getContentId() {
		return _contentId;
	}

	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	@XmlElement(name = "attributeName", required = true)
	public String getAttributeName() {
		return _attributeName;
	}

	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}

	@XmlElement(name = "langCode", required = true)
	public String getLangCode() {
		return _langCode;
	}

	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}

	@XmlElement(name = "value", required = true)
	public String getValue() {
		return _value;
	}

	public void setValue(String value) {
		this._value = value;
	}

}
