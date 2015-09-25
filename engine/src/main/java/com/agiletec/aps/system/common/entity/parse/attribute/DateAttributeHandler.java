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

import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.util.DateConverter;

/**
 * Handler class that interprets the XML defining a 'Date Attribute'
 * @author E.Santoboni
 */
public class DateAttributeHandler extends AbstractAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("date")) {
			this.startDate(attributes, qName);
		}
	}
	
	private void startDate(Attributes attributes, String qName) throws SAXException {
		//nothig to do
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("date")) {
			this.endDate(textBuffer);
		}
	}
	
	private void endDate(StringBuffer textBuffer) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			Date date = DateConverter.parseDate(textBuffer.toString(), SystemConstants.SYSTEM_DATE_FORMAT);
			((DateAttribute) this.getCurrentAttr()).setDate(date);
		}
	}

}
