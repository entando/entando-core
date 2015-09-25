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
package org.entando.entando.aps.system.common.entity.model.attribute;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author E.Santoboni
 */
public class JAXBEnumeratorMapAttribute extends AbstractJAXBAttribute implements Serializable {
	
	@XmlElement(name = "value", required = false)
	public JAXBEnumeratorMapValue getMapValue() {
		return _mapValue;
	}
	public void setMapValue(JAXBEnumeratorMapValue mapValue) {
		this._mapValue = mapValue;
	}
	
	private JAXBEnumeratorMapValue _mapValue;
	
}