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
package com.agiletec.aps.system.common.entity.model;

import java.io.Serializable;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * @author E.Santoboni
 */
public class AttributeFieldError extends FieldError implements Serializable {

    public AttributeFieldError(AttributeInterface attribute, String errorCode, AttributeTracer tracer) {
        super(null, errorCode);
        this.setTracer(tracer);
        this.setAttribute(attribute);
    }
	
	@Override
    public String getFieldCode() {
        String fieldCode = super.getFieldCode();
        if (null == fieldCode) {
            fieldCode = this.getTracer().getFormFieldName(this.getAttribute());
        }
        return fieldCode;
    }

    public String getFullMessage() {
        StringBuilder buffer = new StringBuilder(this.getAttributePositionMessage());
        buffer.append(" : ");
        if (null != this.getMessageKey()) {
            buffer.append(this.getMessageKey());
        } else if (null != this.getMessage()) {
            buffer.append(this.getMessage());
        } else {
            buffer.append(this.getErrorCode());
        }
        return buffer.toString();
    }

    public String getAttributePositionMessage() {
        return this.getTracer().getPositionMessage(this.getAttribute());
    }

    public AttributeTracer getTracer() {
        return _tracer;
    }
    protected void setTracer(AttributeTracer tracer) {
        this._tracer = tracer;
    }

    public AttributeInterface getAttribute() {
        return _attribute;
    }
    protected void setAttribute(AttributeInterface attribute) {
        this._attribute = attribute;
    }

    private AttributeTracer _tracer;
    private AttributeInterface _attribute;

    public static final String OGNL_VALIDATION = "OGNL_VALIDATION";

}