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

import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.TimestampAttribute;
import com.agiletec.aps.util.DateConverter;

/**
 * Handler class that interprets the XML defining a 'Timestamp Attribute'
 * @author E.Santoboni
 */
public class TimestampAttributeHandler extends AbstractAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("timestamp")) {
			this.startTimestamp(attributes, qName);
		}
	}
	
	private void startTimestamp(Attributes attributes, String qName) throws SAXException {
		//nothig to do
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("timestamp")) {
			this.endTimestamp(textBuffer);
		}
	}
	
	private void endTimestamp(StringBuffer textBuffer) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			Date date = DateConverter.parseDate(textBuffer.toString(), SystemConstants.SYSTEM_TIMESTAMP_FORMAT);
			((TimestampAttribute) this.getCurrentAttr()).setDate(date);
		}
	}
	
}