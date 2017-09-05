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
package org.entando.entando.aps.system.services.dataobject.parse;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

public class DataTypeDOM extends EntityTypeDOM {

	@Override
	protected IApsEntity createEntityType(Element contentElem, Class entityClass) throws ApsSystemException {
		DataObject content = (DataObject) super.createEntityType(contentElem, entityClass);
		content.setId(null);
		String viewPage = this.extractXmlAttribute(contentElem, "viewpage", true);
		if (!viewPage.equals(NULL_VALUE)) {
			content.setViewPage(viewPage);
		}
		String listModel = this.extractXmlAttribute(contentElem, "listmodel", true);
		if (!listModel.equals(NULL_VALUE)) {
			content.setListModel(listModel);
		}
		String defaultModel = this.extractXmlAttribute(contentElem, "defaultmodel", true);
		if (!defaultModel.equals(NULL_VALUE)) {
			content.setDefaultModel(defaultModel);
		}
		content.setStatus(DataObject.STATUS_NEW);
		return content;
	}

	@Override
	protected Element createRootTypeElement(IApsEntity currentEntityType) {
		Element typeElement = super.createRootTypeElement(currentEntityType);
		DataObject content = (DataObject) currentEntityType;
		this.setXmlAttribute(typeElement, "viewpage", content.getViewPage());
		this.setXmlAttribute(typeElement, "listmodel", content.getListModel());
		this.setXmlAttribute(typeElement, "defaultmodel", content.getDefaultModel());
		return typeElement;
	}

	private void setXmlAttribute(Element element, String name, String value) {
		String valueToSet = value;
		if (null == value || value.trim().length() == 0) {
			valueToSet = NULL_VALUE;
		}
		element.setAttribute(name, valueToSet);
	}

	@Override
	protected String getEntityTypeRootElementName() {
		return "datatype";
	}

	@Override
	protected String getEntityTypesRootElementName() {
		return "datatypes";
	}

	private static final String NULL_VALUE = "**NULL**";

}
