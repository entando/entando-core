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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;

/**
 * Manager class for the 'checkbox' attributes.
 * @author E.Santoboni
 */
public class CheckBoxAttributeManager extends AbstractMonoLangAttributeManager {
    
	@Override
    protected void setValue(AttributeInterface attribute, String value) {
        if (null != value) {
            ((BooleanAttribute) attribute).setBooleanValue(Boolean.parseBoolean(value));
        } else {
            ((BooleanAttribute) attribute).setBooleanValue(null);
        }
    }
    
	@Override
    protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        String value = this.getValueFromForm(attribute, tracer, request);
        if (value != null) {
            if (value.trim().length() == 0) {
                value = null;
            }
            this.setValue(attribute, value);
        } else {
            this.setValue(attribute, null);
        }
    }
    
}