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

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * Abstract class for those Handler classes that parse the XML codes of single Attribute Types.
 * @author E.Santoboni
 */
public abstract class AbstractAttributeHandler implements AttributeHandlerInterface {
	
	@Override
	public Object getAttributeHandlerPrototype() {
		AttributeHandlerInterface handler = null;
		try {
			Class attributeHandlerClass = Class.forName(this.getClass().getName());
			handler = (AttributeHandlerInterface) attributeHandlerClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error detected while cloning the handler" + this.getClass().getName());
		}
		return handler;
	}
	
	/**
	 * Return the value of a XML attribute.
	 * @param attrs The attributes of the XML tag.
	 * @param attributeName The name of the attribute where to take the value from.
	 * @param qName The name of the XML tag that contains the requested attribute.
	 * @param required If true look for a mandatory attribute.
	 * @return The attribute value contained in the XML.
	 * @throws SAXException if a mandatory attribute is not found.
	 */
	protected String extractAttribute(Attributes attrs, String attributeName,
			String qName, boolean required) throws SAXException {
		int index = attrs.getIndex(attributeName);
		String value = attrs.getValue(index);
		if(required && value == null) {
			throw new SAXException("Attribute '" + attributeName +"' not found in tag <" + qName + ">");
		}
		return value;
	}
	
	public boolean isEndAttribute(String qName) {
		return qName.equals("attribute");
	}
	
	/** 
	 * Return the current attribute, either 'simple' or 'complex'.
	 * @return The current attribute.
	 */
	protected AttributeInterface getCurrentAttr() {
		return _currentAttr;
	}
	
	public void setCurrentAttr(AttributeInterface currentAttr) {
		this._currentAttr = currentAttr;
	}
	
	private AttributeInterface _currentAttr;
	
}
