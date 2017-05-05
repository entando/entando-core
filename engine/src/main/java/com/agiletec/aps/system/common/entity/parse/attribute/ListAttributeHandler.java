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

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;

/**
 * Handler class that interprets the XML defining a 'List Attribute' that supports several languages.
 * (Multi-Language List Attribute)
 * @author E.Santoboni
 */
public class ListAttributeHandler extends AbstractMLAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("list")) {
			this.startList(attributes, qName);
		} else if (qName.equals("listlang")) {
			this.startListLang(attributes, qName);
		} else {
			this.startAttributeElement(attributes, qName);
		}
	}
	
	private void startList(Attributes attributes, String qName) throws SAXException {
		this._isMonoList = this.getCurrentAttr() instanceof MonoListAttribute;
	}
	
	private void startListLang(Attributes attributes, String qName) throws SAXException{
		String idLang = this.extractAttribute(attributes, "lang", qName, true);
		this.setCurrentLangId(idLang);
	}
	
	private void startAttributeElement(Attributes attributes, String qName) throws SAXException {
		if (null == _elementAttribute) {
			if (_isMonoList) {
				_elementAttribute = ((MonoListAttribute) this.getCurrentAttr()).addAttribute();
			} else {
				_elementAttribute = ((ListAttribute) this.getCurrentAttr()).addAttribute(this.getCurrentLangId());
			}
		} else {
			if (null == _elementAttributeHandler) {
				_elementAttributeHandler = _elementAttribute.getHandler();
				_elementAttributeHandler.setCurrentAttr(_elementAttribute);
			}
			_elementAttributeHandler.startAttribute(attributes, qName);
		}
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("list")) {
			this.endList();
		} else if (qName.equals("listlang")){
			this.endListLang();
		} else {
			if (null == _elementAttributeHandler || _elementAttributeHandler.isEndAttribute(qName)) {
				_elementAttributeHandler = null;
				_elementAttribute = null;
			} else {
				_elementAttributeHandler.endAttribute(qName, textBuffer);
			}
		}
	}
	
	private void endListLang() {
		this.setCurrentLangId(null);
	}
	
	private void endList() {
		//nothing to do
	}
	
	@Override
	public boolean isEndAttribute(String qName) {
		return qName.equals("list");
	}
	
	private AttributeHandlerInterface _elementAttributeHandler;
	private boolean _isMonoList;
	private AttributeInterface _elementAttribute;
	
}
