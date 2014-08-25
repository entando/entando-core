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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;

/**
 * Handler class that interprets the XML defining a Text Attribute which supports multiple languages
 * ('Multi-language Text Attribute').
 * @author E.Santoboni
 */
public class TextAttributeHandler extends AbstractMLAttributeHandler {
	
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("text")) {
			this.startText(attributes, qName);
		}
	}
	
	/**
	 * Do the starting attribute operations. This method is declared protected so
	 * that it can be later utilized by those Entity Attribute that extend in turn the
	 * Text Attribute.
	 * @param attributes The attributes of the XML tag.
	 * @param qName The name of the XML tag.
	 * @throws SAXException if errors are detected parsing the XML.
	 */
	protected void startText(Attributes attributes, String qName) throws SAXException {
		String idLang = this.extractAttribute(attributes, "lang", qName, true);
		this.setCurrentLangId(idLang);
	}
	
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("text")) {
			this.endText(textBuffer);
		}
	}
	
	/**
	 * Do the ending attribute operations. This method is declared protected so
	 * that it can be later utilized by those Entity Attribute that extend in turn the
	 * Text Attribute.
	 * @param textBuffer The text extracted from the XML tag.
	 */
	protected void endText(StringBuffer textBuffer) {
		if (null != textBuffer && null != this.getCurrentAttr()) {
			((TextAttribute) this.getCurrentAttr()).setText(textBuffer.toString(), this.getCurrentLangId());
		}
		this.setCurrentLangId(null);
	}
	
}
