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
package org.entando.entando.aps.system.services.api.model;

import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.services.api.ApiResourcesDefDOM;
import org.jdom.Element;

import com.agiletec.aps.util.ApsProperties;

/**
 * @author E.Santoboni
 */
public class ApiMethodRelatedWidget {
	
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