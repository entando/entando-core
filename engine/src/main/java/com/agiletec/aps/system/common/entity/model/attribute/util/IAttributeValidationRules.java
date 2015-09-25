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

import java.io.Serializable;
import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * @author E.Santoboni
 */
public interface IAttributeValidationRules extends Serializable {
    
    public IAttributeValidationRules clone();
    
	public boolean isEmpty();
	
    public void setConfig(Element attributeElement);
    
    public Element getJDOMConfigElement();
    
    /**
     * Set up the required (mandatory) condition for the current attribute.
     * @param required True if the attribute is mandatory
     */
    public void setRequired(boolean required);
    
    /**
     * Test whether this attribute is declared mandatory or not.
     * @return True if the attribute is mandatory, false otherwise.
     */
    public boolean isRequired();
    
    public OgnlValidationRule getOgnlValidationRule();

    public void setOgnlValidationRule(OgnlValidationRule ognlValidationRule);
    
    public List<AttributeFieldError> validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager);
    
    public static final String VALIDATIONS_ELEMENT_NAME = "validations";
    
}