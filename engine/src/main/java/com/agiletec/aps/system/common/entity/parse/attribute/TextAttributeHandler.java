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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;

/**
 * Handler class that interprets the XML defining a Text Attribute which supports multiple languages
 * ('Multi-language Text Attribute').
 * @author E.Santoboni
 */
public class TextAttributeHandler extends AbstractMLAttributeHandler {
	
	@Override
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
	
	@Override
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
