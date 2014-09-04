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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.TimestampAttribute;
import com.agiletec.apsadmin.util.CheckFormatUtil;

/**
 * @author E.Santoboni
 */
public class TimestampAttributeManager extends DateAttributeManager {
	
	@Override
    protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        String value = this.getValueFromForm(attribute, tracer, request);
        if (value != null) {
            if (value.trim().length() == 0) {
                value = null;
            }
            this.setValue(attribute, value);
        }
		String hourValue = this.getValueFromForm(attribute, tracer, "_hour", request);
		this.setValue(attribute, hourValue, VALUE_HOUR);
		String minuteValue = this.getValueFromForm(attribute, tracer, "_minute", request);
		this.setValue(attribute, minuteValue, VALUE_MINUTE);
		String secondValue = this.getValueFromForm(attribute, tracer, "_second", request);
		this.setValue(attribute, secondValue, VALUE_SECOND);
    }
	
	protected void setValue(AttributeInterface attribute, String value, String valueType) {
		TimestampAttribute timestampAttribute = (TimestampAttribute) attribute;
		if (value != null) {
			value = value.trim();
		}
		Integer number = null;
		if (CheckFormatUtil.isValidNumber(value)) {
			try {
				number = Integer.parseInt(value);
			} catch (Throwable ex) {
				this.setError(timestampAttribute, value, valueType);
				throw new RuntimeException("Error while parsing the number - " + value + " -", ex);
			}
			int max = (valueType.equalsIgnoreCase(VALUE_HOUR)) ? 23 : 59;
			if (number > max) {
				this.setError(timestampAttribute, value, valueType);
				return;
			} else {
				this.resetError(timestampAttribute, valueType);
			}
		} else {
			this.setError(timestampAttribute, value, valueType);
			return;
		}
		if (null != number && null != timestampAttribute.getDate()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestampAttribute.getDate());
			if (valueType.equalsIgnoreCase(VALUE_HOUR)) {
				cal.set(Calendar.HOUR_OF_DAY, number);
			} else if (valueType.equalsIgnoreCase(VALUE_MINUTE)) {
				cal.set(Calendar.MINUTE, number);
			} else if (valueType.equalsIgnoreCase(VALUE_SECOND)) {
				cal.set(Calendar.SECOND, number);				
			}
			timestampAttribute.setDate(cal.getTime());
			this.resetError(timestampAttribute, valueType);
		}
	}
	
	private void setError(TimestampAttribute timestampAttribute, String value, String valueType) {
		if (valueType.equalsIgnoreCase(VALUE_HOUR)) {
			timestampAttribute.setFailedHourString(value);
		} else if (valueType.equalsIgnoreCase(VALUE_MINUTE)) {
			timestampAttribute.setFailedMinuteString(value);
		} else if (valueType.equalsIgnoreCase(VALUE_SECOND)) {
			timestampAttribute.setFailedSecondString(value);
		}
	}
    
	private void resetError(TimestampAttribute timestampAttribute, String valueType) {
		if (valueType.equalsIgnoreCase(VALUE_HOUR)) {
			timestampAttribute.setFailedHourString(null);
		} else if (valueType.equalsIgnoreCase(VALUE_MINUTE)) {
			timestampAttribute.setFailedMinuteString(null);
		} else if (valueType.equalsIgnoreCase(VALUE_SECOND)) {
			timestampAttribute.setFailedSecondString(null);
		}
	}
    
	protected String getValueFromForm(AttributeInterface attribute, AttributeTracer tracer, String suffix, HttpServletRequest request) {
        String formFieldName = tracer.getFormFieldName(attribute) + suffix;
        return request.getParameter(formFieldName);
    }
    
	private static final String VALUE_HOUR = "h";
	private static final String VALUE_MINUTE = "m";
	private static final String VALUE_SECOND = "s";
	
}