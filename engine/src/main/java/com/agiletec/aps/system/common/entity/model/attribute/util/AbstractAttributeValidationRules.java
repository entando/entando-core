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
package com.agiletec.aps.system.common.entity.model.attribute.util;

import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public abstract class AbstractAttributeValidationRules extends BaseAttributeValidationRules {
	
	@Override
	public IAttributeValidationRules clone() {
		AbstractAttributeValidationRules clone = (AbstractAttributeValidationRules) super.clone();
		clone.setRangeEnd(this.getRangeEnd());
		clone.setRangeEndAttribute(this.getRangeEndAttribute());
		clone.setRangeStart(this.getRangeStart());
		clone.setRangeStartAttribute(this.getRangeStartAttribute());
		clone.setValue(this.getValue());
		clone.setValueAttribute(this.getValueAttribute());
		return clone;
	}
	
	protected void insertJDOMConfigElement(String conditionRuleCode, 
			String attributeName, String toStringValue, Element configElement) {
		if ((toStringValue != null  && toStringValue.trim().length() > 0) || (attributeName != null && attributeName.trim().length() > 0)) {
			Element element = new Element(conditionRuleCode);
			if (toStringValue != null  && toStringValue.trim().length() > 0) {
				element.setText(toStringValue);
			} else {
				element.setAttribute("attribute", attributeName);
			}
			configElement.addContent(element);
		}
	}
	
	@Override
	public boolean isEmpty() {
		return (super.isEmpty() 
				&& (null == this.getRangeStart()) 
				&& (null == this.getRangeEnd()) 
				&& (null == this.getValue()) 
				&& (null == this.getRangeStartAttribute() || this.getRangeStartAttribute().trim().length() == 0) 
				&& (null == this.getRangeEndAttribute() || this.getRangeEndAttribute().trim().length() == 0) 
				&& (null == this.getValueAttribute() || this.getValueAttribute().trim().length() == 0) );
	}
	
	public Object getValue() {
		return _value;
	}
	public void setValue(Object value) {
		this._value = value;
	}
	
	public Object getRangeStart() {
		return _rangeStart;
	}
	public void setRangeStart(Object rangeStart) {
		this._rangeStart = rangeStart;
	}
	
	public Object getRangeEnd() {
		return _rangeEnd;
	}
	public void setRangeEnd(Object rangeEnd) {
		this._rangeEnd = rangeEnd;
	}
	
	public String getValueAttribute() {
		return _valueAttribute;
	}
	public void setValueAttribute(String equalAttribute) {
		this._valueAttribute = equalAttribute;
	}
	
	public String getRangeStartAttribute() {
		return _rangeStartAttribute;
	}
	public void setRangeStartAttribute(String rangeStartAttribute) {
		this._rangeStartAttribute = rangeStartAttribute;
	}
	
	public String getRangeEndAttribute() {
		return _rangeEndAttribute;
	}
	public void setRangeEndAttribute(String rangeEndAttribute) {
		this._rangeEndAttribute = rangeEndAttribute;
	}
	
	private Object _value;
	private Object _rangeStart;
	private Object _rangeEnd;
	private String _valueAttribute;
	private String _rangeStartAttribute;
	private String _rangeEndAttribute;
	
	protected static final String RULE_TYPE = "type";
	
}
