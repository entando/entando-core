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
package org.entando.entando.aps.system.common.entity.parse.attribute;

import com.agiletec.aps.system.common.entity.parse.attribute.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;

/**
 * Handler class that interprets the XML defining a EnumeratorMap
 * ('EnumeratorMap Attribute')
 * @author E.Santoboni
 */
public class EnumeratorMapAttributeHandler extends AbstractAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("key")) {
			this.startKey(attributes, qName);
		} else if (qName.equals("value")) {
			this.startValue(attributes, qName);
		}
	}
	
	private void startKey(Attributes attributes, String qName) throws SAXException {
		//Nothing to do;
	}
	
	private void startValue(Attributes attributes, String qName) throws SAXException {
		//Nothing to do;
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("key")) {
			this.endKey(textBuffer);
		} else if (qName.equals("value")) {
			this.endValue(textBuffer);
		}
	}
	
	private void endKey(StringBuffer textBuffer) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			((MonoTextAttribute) this.getCurrentAttr()).setText(textBuffer.toString());
		}
	}
	
	private void endValue(StringBuffer textBuffer) {
		//Nothing to do;
	}
	
}
