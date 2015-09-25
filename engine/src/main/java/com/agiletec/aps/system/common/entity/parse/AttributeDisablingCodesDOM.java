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
package com.agiletec.aps.system.common.entity.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Dom class parser of Attribute disabling codes definitions.
 * @author E.Santoboni
 */
public class AttributeDisablingCodesDOM extends AbstractAttributeSupportObjectDOM {
	
	public Map<String, String> extractDisablingCodes(String xml, String definitionPath) throws ApsSystemException {
		this.validate(xml, definitionPath);
		Document document = this.decodeDOM(xml);
		return this.extractDisablingCodes(document);
	}
	
	@Override
	protected String getSchemaFileName() {
		return "attributeDisablingCodes-4.0.xsd";
	}
	
	private Map<String, String> extractDisablingCodes(Document document) {
		Map<String, String> roles = new HashMap<String, String>();
		List<Element> roleElements = document.getRootElement().getChildren("disablingcode");
		for (int i=0; i<roleElements.size(); i++) {
			Element roleElement = roleElements.get(i);
			String name = roleElement.getChildText("name");
			String description = roleElement.getChildText("description");
			roles.put(name, description);
		}
		return roles;
	}
	
}