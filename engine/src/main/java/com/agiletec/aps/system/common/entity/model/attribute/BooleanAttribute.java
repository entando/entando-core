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
package com.agiletec.aps.system.common.entity.model.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This attribute represent an information of type boolean. Obviously this attribute does not
 * support multiple languages.
 * @author E.Santoboni
 */
public class BooleanAttribute extends AbstractAttribute {
	
	@Override
	public Element getJDOMElement() {
		Element attributeElement = this.createRootElement("attribute");
		if (this.saveBooleanJDOMElement()) {
			Element booleanElement = new Element("boolean");
			booleanElement.setText(this.getValue().toString());
			attributeElement.addContent(booleanElement);
		}
		return attributeElement;
	}
	
	protected boolean saveBooleanJDOMElement() {
		return true;
	}
	
	/**
	 * Return the object characterizing the attribute.
	 * @return The boolean
	 */
	@Override
	public Boolean getValue() {
		if (null != this._boolean) {
			return this._boolean.booleanValue();
		}
		return false;
	}

	/**
	 * Return the object characterizing the attribute.
	 * @return The boolean
	 */
	public Boolean getBooleanValue() {
		return _boolean;
	}

	/**
	 * Set up the boolean for the current attribute
	 * @param booleanObject The boolean
	 */
	public void setBooleanValue(Boolean booleanObject) {
		this._boolean = booleanObject;
	}

	@Override
	public boolean isSearchableOptionSupported() {
		return true;
	}
	
	@Override
	public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
		List<AttributeSearchInfo> infos = new ArrayList<AttributeSearchInfo>();
		if (this.addSearchInfo()) {
			AttributeSearchInfo info = new AttributeSearchInfo(String.valueOf(this.getValue()), null, null, null);
			infos.add(info);
		}
		return infos;
	}
	
	protected boolean addSearchInfo() {
		return true;
	}
	
	@Override
	public String getIndexingType() {
		return IndexableAttributeInterface.INDEXING_TYPE_NONE;
	}

	@Override
	protected Object getJAXBValue(String langCode) {
		return (null != this.getBooleanValue()) ? this.getBooleanValue().toString() : "false";
	}

	@Override
	public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
		super.valueFrom(jaxbAttribute);
		String value = (String) jaxbAttribute.getValue();
		if (null != value) {
			this.setBooleanValue(Boolean.valueOf(value));
		}
	}
	
	@Override
	public Status getStatus() {
		return Status.VALUED;
	}
	
	private Boolean _boolean;
	
}