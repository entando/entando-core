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
package com.agiletec.aps.system.common.entity.model.attribute;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlType(propOrder = {"attributes"})
public class JAXBCompositeAttribute extends AbstractJAXBAttribute implements Serializable {
	
	@XmlElement(name = "attribute", required = true)
	@XmlElementWrapper(name = "attributes")
	public Map<String, AbstractJAXBAttribute> getAttributes() {
		return _attributes;
	}
	public void setAttributes(Map<String, AbstractJAXBAttribute> attributes) {
		this._attributes = attributes;
	}
	
	private Map<String, AbstractJAXBAttribute> _attributes = null;
	
}