/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
	
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("number")) {
			this.startNumber(attributes, qName);
		}
	}
	
	private void startNumber(Attributes attributes, String qName) throws SAXException {
		//nothing to do
	}
	
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
