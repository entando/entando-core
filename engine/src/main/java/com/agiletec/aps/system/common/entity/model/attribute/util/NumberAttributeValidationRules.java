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
package com.agiletec.aps.system.common.entity.model.attribute.util;

import java.util.List;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * @author E.Santoboni
 */
public class NumberAttributeValidationRules extends AbstractAttributeValidationRules {

	private static final long serialVersionUID = 3174872953012814318L;
	private static final Logger _logger =  LoggerFactory.getLogger(NumberAttributeValidationRules.class);
	
	@Override
    protected void fillJDOMConfigElement(Element configElement) {
        super.fillJDOMConfigElement(configElement);
        String toStringEqualValue = (this.getValue() != null) ? String.valueOf(this.getValue()) : null;
        this.insertJDOMConfigElement("value", this.getValueAttribute(), toStringEqualValue, configElement);

        String toStringStartValue = (this.getRangeStart() != null) ? String.valueOf(this.getRangeStart()) : null;
        this.insertJDOMConfigElement("rangestart", this.getRangeStartAttribute(), toStringStartValue, configElement);

        String toStringEndValue = (this.getRangeEnd() != null) ? String.valueOf(this.getRangeEnd()) : null;
        this.insertJDOMConfigElement("rangeend", this.getRangeEndAttribute(), toStringEndValue, configElement);
    }
    
	@Override
    protected void extractValidationRules(Element validationElement) {
        super.extractValidationRules(validationElement);
        Element valueElement = validationElement.getChild("value");
        if (null != valueElement) {
            this.setValue(this.getIntegerValue(valueElement.getText()));
            this.setValueAttribute(valueElement.getAttributeValue("attribute"));
        }
        Element rangeStartElement = validationElement.getChild("rangestart");
        if (null != rangeStartElement) {
            this.setRangeStart(this.getIntegerValue(rangeStartElement.getText()));
            this.setRangeStartAttribute(rangeStartElement.getAttributeValue("attribute"));
        }
        Element rangeEndElement = validationElement.getChild("rangeend");
        if (null != rangeEndElement) {
            this.setRangeEnd(this.getIntegerValue(rangeEndElement.getText()));
            this.setRangeEndAttribute(rangeEndElement.getAttributeValue("attribute"));
        }
    }
    
    private Integer getIntegerValue(String text) {
        if (null == text || text.trim().length() == 0) {
            return null;
        }
        Integer valueInteger = null;
        try {
            valueInteger = Integer.parseInt(text);
        } catch (NumberFormatException e) {
           _logger.error("Error in parsing number '{}' for extracting attribute roles", text, e);
        }
        return valueInteger;
    }
    
    @Override
	public List<AttributeFieldError> validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager) {
        List<AttributeFieldError> errors = super.validate(attribute, tracer, langManager);
        if (this.isEmpty()) {
			return errors;
		}
        try {
            NumberAttribute numberAttribute = (NumberAttribute) attribute;
            if (null == numberAttribute.getValue()) {
				return errors;
			}
            int attributeValue = numberAttribute.getValue().intValue();
            Integer startValue = (this.getRangeStart() != null) ? (Integer) this.getRangeStart() : this.getOtherAttributeValue(attribute, this.getRangeStartAttribute());
            if (null != startValue && attributeValue < startValue) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.LESS_THAN_ALLOWED, tracer);
                error.setMessage("Number less than " + startValue);
                errors.add(error);
            }
            Integer endValue = (this.getRangeEnd() != null) ? (Integer) this.getRangeEnd() : this.getOtherAttributeValue(attribute, this.getRangeEndAttribute());
            if (null != endValue && attributeValue > endValue) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.GREATER_THAN_ALLOWED, tracer);
                error.setMessage("Number greater than " + endValue);
                errors.add(error);
            }
            Integer value = (this.getValue() != null) ? (Integer) this.getValue() : this.getOtherAttributeValue(attribute, this.getValueAttribute());
            if (null != value && attributeValue != value) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.INVALID, tracer);
                error.setMessage("Number not equals than " + value);
                errors.add(error);
            }
        } catch (Throwable t) {
            _logger.error("Error validating number attribute", t);
            throw new RuntimeException("Error validating number attribute", t);
        }
        return errors;
    }
    
    private Integer getOtherAttributeValue(AttributeInterface attribute, String otherAttributeName) {
        if (null == otherAttributeName) {
			return null;
		}
        AttributeInterface other = (AttributeInterface) attribute.getParentEntity().getAttribute(otherAttributeName);
        if (null != other && (other instanceof NumberAttribute) && ((NumberAttribute) other).getValue() != null) {
            return ((NumberAttribute) other).getValue().intValue();
        }
        return null;
    }
    
}
