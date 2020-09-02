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

import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * @author E.Santoboni
 */
public class TimestampAttribute extends DateAttribute {
	
    @Override
    public Element getJDOMElement() {
		Element attributeElement = this.createRootElement("attribute");
        if (null != this.getDate()) {
            Element dateElement = new Element("timestamp");
            dateElement.setText(this.getFormattedDate(SystemConstants.SYSTEM_TIMESTAMP_FORMAT));
            attributeElement.addContent(dateElement);
        }
        return attributeElement;
    }
	
    @Override
    public List<AttributeFieldError> validate(AttributeTracer tracer, ILangManager langManager) {
        List<AttributeFieldError> errors = super.validate(tracer, langManager);
        if (null == this.getDate() && 
				(null != this.getFailedDateString() || null != this.getFailedHourString() || null != this.getFailedMinuteString() || null != this.getFailedSecondString())) {
            errors.add(new AttributeFieldError(this, FieldError.INVALID_FORMAT, tracer));
        }
        return errors;
    }
	
	public String getFailedHourString() {
		return _failedHourString;
	}
	public void setFailedHourString(String failedHourString) {
		this._failedHourString = failedHourString;
	}
	
	public String getFailedMinuteString() {
		return _failedMinuteString;
	}
	public void setFailedMinuteString(String failedMinuteString) {
		this._failedMinuteString = failedMinuteString;
	}
	
	public String getFailedSecondString() {
		return _failedSecondString;
	}
	public void setFailedSecondString(String failedSecondString) {
		this._failedSecondString = failedSecondString;
	}

	@Override
	public boolean isSearchableOptionSupported() {
		return true;
	}

	private String _failedHourString;
    private String _failedMinuteString;
    private String _failedSecondString;
    
}
