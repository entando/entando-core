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

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * @author E.Santoboni
 */
public class BaseAttributeValidationRules implements IAttributeValidationRules {

	private static final Logger _logger =  LoggerFactory.getLogger(BaseAttributeValidationRules.class);
	
    @Override 
    public IAttributeValidationRules clone() {
        BaseAttributeValidationRules clone = null;
        try {
            Class validationConditionClass = Class.forName(this.getClass().getName());
            clone = (BaseAttributeValidationRules) validationConditionClass.newInstance();
            clone.setRequired(this.isRequired());
            if (null != this.getOgnlValidationRule()) {
                clone.setOgnlValidationRule(this.getOgnlValidationRule().clone());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            _logger.error("Error detected while cloning the ValidationRules", e);
            throw new RuntimeException("Error detected while cloning the ValidationRules class '" + this.getClass().getName() + "' ");
        }
        return clone;
    }
    
    @Override 
    public Element getJDOMConfigElement() {
        Element configElement = null;
        try {
            if (this.isEmpty()) {
                return null;
            }
            configElement = new Element(VALIDATIONS_ELEMENT_NAME);
            this.fillJDOMConfigElement(configElement);
        } catch (Throwable t) {
            _logger.error("Error detected while creating jdom element", t);
            throw new RuntimeException("Error detected while creating jdom element", t);
        }
        return configElement;
    }

    protected void fillJDOMConfigElement(Element configElement) {
        if (this.isRequired()) {
            Element element = new Element("required");
            element.setText("true");
            configElement.addContent(element);
        }
        if (null != this.getOgnlValidationRule()) {
            Element exprElement = this.getOgnlValidationRule().getConfigElement();
            if (null != exprElement) {
                configElement.addContent(exprElement);
            }
        }
    }
    
    @Override 
    public void setConfig(Element attributeElement) {
        Element validationElement = attributeElement.getChild(VALIDATIONS_ELEMENT_NAME);
        if (null != validationElement) {
            this.extractValidationRules(validationElement);
        }
    }
    
    protected void extractValidationRules(Element validationElement) {
        String required = this.extractValue(validationElement, "required");
        this.setRequired(null != required && required.equalsIgnoreCase("true"));
        Element expressionElement = validationElement.getChild("expression");
        if (null != expressionElement) {
            OgnlValidationRule validationRule = new OgnlValidationRule(expressionElement);
            this.setOgnlValidationRule(validationRule);
        }
    }

    protected String extractValue(Element validationElements, String qName) {
        Element element = validationElements.getChild(qName);
        if (null != element) {
            return element.getText();
        }
        return null;
    }
	
	@Override
    public boolean isEmpty() {
        return (!this.isRequired() && null == this.getOgnlValidationRule());
    }
	
	@Override 
    public List<AttributeFieldError> validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager) {
        List<AttributeFieldError> errors = new ArrayList<>();
        if (this.isEmpty()) {
			return errors;
		}
        try {
            if (this.isRequired() && attribute.getStatus().equals(AttributeInterface.Status.EMPTY)) {
                AttributeTracer tracerClone = tracer.clone();
                tracerClone.setLang(langManager.getDefaultLang());
                errors.add(new AttributeFieldError(attribute, FieldError.MANDATORY, tracerClone));
            }
            OgnlValidationRule ognlValidationRule = this.getOgnlValidationRule();
            if (null != ognlValidationRule) {
                AttributeFieldError error = ognlValidationRule.validate(attribute, tracer, langManager);
                if (null != error) {
                    errors.add(error);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error validating Attribute '{}'", attribute.getName(), t);
            throw new RuntimeException("Error validating Attribute '" + attribute.getName() + "'", t);
        }
        return errors;
    }
    
    @Override 
    public boolean isRequired() {
        return this._required;
    }
    @Override 
    public void setRequired(boolean required) {
        this._required = required;
    }
    
    @Override 
    public OgnlValidationRule getOgnlValidationRule() {
        return _ognlValidationRule;
    }
    @Override 
    public void setOgnlValidationRule(OgnlValidationRule ognlValidationRule) {
        this._ognlValidationRule = ognlValidationRule;
    }
    
    private boolean _required;
    private OgnlValidationRule _ognlValidationRule;
    
}
