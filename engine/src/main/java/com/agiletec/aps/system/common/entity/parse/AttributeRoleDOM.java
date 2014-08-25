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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Dom class parser of Attribute Role definitions.
 * @author E.Santoboni
 */
public class AttributeRoleDOM extends AbstractAttributeSupportObjectDOM {
	
	public Map<String, AttributeRole> extractRoles(String xml, String definitionPath) throws ApsSystemException {
		this.validate(xml, definitionPath);
		Document document = this.decodeDOM(xml);
		return this.extractRoles(document);
	}
	
	@Override
	protected String getSchemaFileName() {
		return "attributeRoles-4.0.xsd";
	}
	
	private Map<String, AttributeRole> extractRoles(Document document) {
		Map<String, AttributeRole> roles = new HashMap<String, AttributeRole>();
		List<Element> roleElements = document.getRootElement().getChildren("role");
		for (int i=0; i<roleElements.size(); i++) {
			Element roleElement = roleElements.get(i);
			String name = roleElement.getChildText("name");
			String description = roleElement.getChildText("description");
			String allowedTypesCSV = roleElement.getChildText("allowedTypes");
			String[] array = allowedTypesCSV.split(",");
			List<String> allowedTypes = Arrays.asList(array);
			AttributeRole role = new AttributeRole(name, description, allowedTypes);
			String formFieldTypeText = roleElement.getChildText("formFieldType");
			if (null != formFieldTypeText) {
				role.setFormFieldType(Enum.valueOf(AttributeRole.FormFieldTypes.class, formFieldTypeText.toUpperCase()));
			}
			roles.put(name, role);
		}
		return roles;
	}
	
}