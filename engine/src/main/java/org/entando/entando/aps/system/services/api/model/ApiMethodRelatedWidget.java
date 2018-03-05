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
package org.entando.entando.aps.system.services.api.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.api.ApiResourcesDefDOM;
import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public class ApiMethodRelatedWidget implements Serializable {
	
	protected ApiMethodRelatedWidget() {}
	
	protected ApiMethodRelatedWidget(Element element) {
		this.setWidgetCode(element.getAttributeValue(ApiResourcesDefDOM.RELATED_WIDGET_CODE_ATTRIBUTE_NAME));
		this.setMapping(new ApsProperties());
		List<Element> mappingsElements = element.getChildren(ApiResourcesDefDOM.RELATED_WIDGET_MAP_PARAMETER_ELEMENT_NAME);
		for (int i = 0; i < mappingsElements.size(); i++) {
			Element mappingsElement = mappingsElements.get(i);
			String showletParam = mappingsElement.getAttributeValue(ApiResourcesDefDOM.RELATED_WIDGET_MAP_PARAMETER_WIDGET_ATTRIBUTE_NAME);
			String methodParam = mappingsElement.getAttributeValue(ApiResourcesDefDOM.RELATED_WIDGET_MAP_PARAMETER_METHOD_ATTRIBUTE_NAME);
			this.getMapping().put(showletParam, methodParam);
		}
	}
	
	@Override
	public ApiMethodRelatedWidget clone() {
		ApiMethodRelatedWidget clone = new ApiMethodRelatedWidget();
		clone.setWidgetCode(this.getWidgetCode());
		clone.setMapping(new ApsProperties());
		Iterator<Object> keyIter = this.getMapping().keySet().iterator();
		while (keyIter.hasNext()) {
			Object key = keyIter.next();
			clone.getMapping().put(key, this.getMapping().get(key));
		}
		return clone;
	}
	
	@Deprecated
	public String getShowletCode() {
		return this.getWidgetCode();
	}
	@Deprecated
	public void setShowletCode(String showletCode) {
		this.setWidgetCode(showletCode);
	}
	
	public String getWidgetCode() {
		return _widgetCode;
	}
	public void setWidgetCode(String widgetCode) {
		this._widgetCode = widgetCode;
	}
	
	public ApsProperties getMapping() {
		return _mapping;
	}
	public void setMapping(ApsProperties mapping) {
		this._mapping = mapping;
	}
	
	private String _widgetCode;
	private ApsProperties _mapping;
	
}