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
package com.agiletec.aps.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe per la lettura e scrittura delle Properties da xml.
 * Il formato dell'xml è: 
 * 
<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="Chiave1">Valore 1</property>
	<property key="Chiave2">Valore 2</property>
	....
</properties>
 * @author E.Santoboni
 */
public class ApsPropertiesDOM {

	private static final Logger _logger = LoggerFactory.getLogger(ApsPropertiesDOM.class);
	
	/**
	 * Costruisce il Jdom Document in base alle properties inserite.
	 * @param prop le properties da cui ricavare il Documento Jdom
	 */
	public void buildJDOM(ApsProperties prop) {
		this._doc = new Document();
		this._rootElement = new Element(ROOT);
		Iterator<Object> titlesIter = prop.keySet().iterator();
		while (titlesIter.hasNext()) {
			String attributeValue = (String) titlesIter.next();
			String value = (String) prop.get(attributeValue);
			if (null != value && value.trim().length() > 0) {
				Element tag = new Element(TAB_ELEMENT);
				tag.setAttribute(ATTRIBUTE_KEY, attributeValue);
				tag.setText(value);
				this._rootElement.addContent(tag);
			}
		}
		this._doc.setRootElement(this._rootElement);
	}
	
	/**
	 * Restituisce il formato xml delle Properties.
	 * @return String Formato xml delle Properties.
	 */
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("");
		out.setFormat(format);
		return out.outputString(_doc);
	}
	
	public Element getRootElement() {
		return (Element) this._doc.getRootElement().clone();
	}
	
	private void decodeDOM(String xmlText) {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
			_rootElement = _doc.getRootElement();
		} catch (Throwable t) {
			_logger.error("Parsing error. xml: {}", xmlText, t);
			throw new RuntimeException("Error detected while parsing the XML", t);
		}
	}
	
	private ApsProperties extractProperties() {
		ApsProperties prop = new ApsProperties();
		List<Element> elements = this._rootElement.getChildren(TAB_ELEMENT);
		Iterator<Element> elementsIter = elements.iterator();
		while (elementsIter.hasNext()) {
			Element currentElement = (Element) elementsIter.next();
			String key = currentElement.getAttributeValue(ATTRIBUTE_KEY);
			String value = currentElement.getText();
			prop.put(key, value);
		}
		return prop;
	}
	
	/**
	 * Estrae le Properies dal testo xml immesso.
	 * @param xml L'xml da cui estrarre le properties.
	 * @return Le properties cercate.
	 * @throws IOException
	 */
	public ApsProperties extractProperties(String xml) throws IOException {
		ApsProperties prop = null;
		this.decodeDOM(xml);
		prop = this.extractProperties();
		return prop;
	}
	
	private Document _doc;
	private Element _rootElement;
	private final String ROOT = "properties";
	private final String TAB_ELEMENT = "property";
	private final String ATTRIBUTE_KEY = "key";
	
}
