/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handler class that interprets the XML defining a 'Hypertext Attribute'
 * @author E.Santoboni
 */
public class HypertextAttributeHandler extends TextAttributeHandler {
	
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("hypertext")) {
			this.startText(attributes, qName);
		}
	}
	
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("hypertext")) {
			this.endText(textBuffer);
		}
	}
	
}
