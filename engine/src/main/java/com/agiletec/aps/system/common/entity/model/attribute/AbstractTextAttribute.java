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
package com.agiletec.aps.system.common.entity.model.attribute;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.commons.lang.StringUtils;

/**
 * This abstract class is the base for the 'Text' Attributes.
 * @author E.Santoboni
 */
public abstract class AbstractTextAttribute extends AbstractAttribute implements IndexableAttributeInterface, ITextAttribute {
	
	@Override
	public boolean isTextAttribute() {
		return true;
	}
	
	@Override
	@Deprecated (/** to guaranted compatibility with previsous version of jAPS 2.0.12 */)
	public void setAttributeConfig(Element attributeElement) throws ApsSystemException {
		super.setAttributeConfig(attributeElement);
		String maxLength = this.extractXmlAttribute(attributeElement, "maxlength", false);
		if (null != maxLength) {
			this.setMaxLength(Integer.parseInt(maxLength));
		}
		String minLength = this.extractXmlAttribute(attributeElement, "minlength", false);
		if (null != minLength) {
			this.setMinLength(Integer.parseInt(minLength));
		}
		Element regexpElement = attributeElement.getChild("regexp");
		if (null != regexpElement) {
			String regexp = regexpElement.getText();
			if (null != regexp && regexp.trim().length() > 0) {
				this.setRegexp(regexp);
			}
		}
	}
	
	@Override
	protected IAttributeValidationRules getValidationRuleNewIntance() {
		return new TextAttributeValidationRules();
	}
	
	@Override
	public int getMaxLength() {
		TextAttributeValidationRules validationRule = (TextAttributeValidationRules) this.getValidationRules();
		if (null != validationRule && null != validationRule.getMaxLength()) {
			return validationRule.getMaxLength();
		}
		return -1;
	}
	
	@Override
	@Deprecated (/** to guaranted compatibility with previsous version of jAPS 2.0.12 */)
	public void setMaxLength(int maxLength) {
		((TextAttributeValidationRules) this.getValidationRules()).setMaxLength(maxLength);
	}
	
	@Override
	public int getMinLength() {
		TextAttributeValidationRules validationRule = (TextAttributeValidationRules) this.getValidationRules();
		if (null != validationRule && null != validationRule.getMinLength()) {
			return validationRule.getMinLength();
		}
		return -1;
	}
	
	@Override
	@Deprecated (/** to guaranted compatibility with previsous version of jAPS 2.0.12 */)
	public void setMinLength(int minLength) {
		((TextAttributeValidationRules) this.getValidationRules()).setMinLength(minLength);
		//this._minLength = minLength;
	}
	
	@Override
	public String getRegexp() {
		return ((TextAttributeValidationRules) this.getValidationRules()).getRegexp();
	}
	
	@Override
	@Deprecated (/** to guaranted compatibility with previsous version of jAPS 2.0.12 */)
	public void setRegexp(String regexp) {
		((TextAttributeValidationRules) this.getValidationRules()).setRegexp(regexp);
	}
	
	@Override
	protected AbstractJAXBAttribute getJAXBAttributeInstance() {
		return new JAXBTextAttribute();
	}
	
	@Override
	public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
		JAXBTextAttribute jaxbTextAttribute = (JAXBTextAttribute) super.createJAXBAttribute(langCode);
		if (null == jaxbTextAttribute) return null;
		String text = this.getTextForLang(langCode);
		if (StringUtils.isNotEmpty(text)) {
			jaxbTextAttribute.setText(text);
		}
		return jaxbTextAttribute;
	}
    
}