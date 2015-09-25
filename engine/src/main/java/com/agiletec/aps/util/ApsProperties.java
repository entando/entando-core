/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Estensione di Properties con l'aggiunta di metodi di lettura/scrittura da stringa xml.
 * @author E.Santoboni
 */
public class ApsProperties extends Properties {
	
	/**
	 * Come java.util.Properties
	 */
	public ApsProperties() {
		super();
	}
	
	/**
	 * Come java.util.Properties.
	 * @param defaults Valori di default
	 */
	public ApsProperties(Properties defaults) {
		super(defaults);
	}
	
	@Override
	public ApsProperties clone() {
		ApsProperties clone = new ApsProperties();
		clone.putAll(this);
		return clone;
	}
	
	/**
	 * Setta Properties estraendole dal testo xml inserito.
	 * @param propertyXml L'xml da cui estrarre le Properties
	 * @throws IOException
	 */
	public void loadFromXml(String propertyXml) throws IOException {
		ApsProperties prop = null;
		if(propertyXml != null) {
			ApsPropertiesDOM propDom = new ApsPropertiesDOM();
			prop = propDom.extractProperties(propertyXml);
			this.putAll(prop);
		}
	}
	
	/**
	 * Costruisce l'xml relativo alle properties.
	 * @return La stringa xml.
	 * @throws IOException
	 */
	public String toXml() throws IOException {
		String xml = null;
		ApsProperties prop = (ApsProperties) this.clone();
		if (null != prop && prop.size() > 0) {
			ApsPropertiesDOM propDom = new ApsPropertiesDOM();
			propDom.buildJDOM(prop);
			xml = propDom.getXMLDocument();
		}
		return xml;
	}
	
}
