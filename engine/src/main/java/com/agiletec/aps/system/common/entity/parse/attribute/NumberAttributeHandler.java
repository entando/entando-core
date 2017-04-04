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
package com.agiletec.aps.system.common.entity.parse.attribute;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;

/**
 * Handler class that interprets the XML defining a Number Attribute.
 * @author E.Santoboni
 */
public class NumberAttributeHandler extends AbstractAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("number")) {
			this.startNumber(attributes, qName);
		}
	}
	
	private void startNumber(Attributes attributes, String qName) throws SAXException {
		//nothing to do
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("number")) {
			this.endNumber(textBuffer);
		}
	}
	
	private void endNumber(StringBuffer textBuffer) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			String bigDecimalString = textBuffer.toString();
			BigDecimal number = new BigDecimal(bigDecimalString);
			((NumberAttribute) this.getCurrentAttr()).setValue(number);
		}
	}

}
