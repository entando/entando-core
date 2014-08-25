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