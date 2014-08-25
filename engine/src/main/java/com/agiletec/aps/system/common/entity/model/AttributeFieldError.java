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

    public String getFieldCode() {
        String fieldCode = super.getFieldCode();
        if (null == fieldCode) {
            fieldCode = this.getTracer().getFormFieldName(this.getAttribute());
        }
        return fieldCode;
    }

    public String getFullMessage() {
        StringBuffer buffer = new StringBuffer(this.getAttributePositionMessage());
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