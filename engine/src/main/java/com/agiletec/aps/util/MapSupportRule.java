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
package com.agiletec.aps.util;

import java.util.Map;

import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Regola per il Digester, per alimentare un Map.
 * Il tag deve avere un attributo che specifica la chiave, ed il
 * valore nel corpo del tag. Il nome dell'attributo è impostato
 * nel costruttore della regola. 
 * @author W.Ambu
 */
public class MapSupportRule extends Rule {

	/**
	 * Costruttore che consente di definire il nome dell'attributo
	 * del tag in cui è specificata la chiave.
	 * @param keyAttrName Il nome dell'attributo
	 */
	public MapSupportRule(String keyAttrName) {
		super();
		_keyAttrName = keyAttrName;
	}

	public void begin(String namespace, String name, Attributes attributes)
			throws Exception{
		_key = attributes.getValue(_keyAttrName);
	}
	
	public void body(String namespace, String name, String text)
			throws Exception{
		Map map = (Map) getDigester().peek();
		map.put(_key, text);
	}
	
	public void finish() throws Exception {
		_key = null;
	}
	
	private String _key;
	private String _keyAttrName;
}
