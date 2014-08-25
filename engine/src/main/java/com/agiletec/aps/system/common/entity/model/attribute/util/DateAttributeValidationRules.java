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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class DateAttributeValidationRules extends AbstractAttributeValidationRules {

	private static final Logger _logger =  LoggerFactory.getLogger(DateAttributeValidationRules.class);
	
	@Override
    protected void fillJDOMConfigElement(Element configElement) {
        super.fillJDOMConfigElement(configElement);
        String toStringEqualValue = this.toStringValue(this.getValue());
        this.insertJDOMConfigElement("value", this.getValueAttribute(), toStringEqualValue, configElement);
        String toStringStartValue = this.toStringValue(this.getRangeStart());
        this.insertJDOMConfigElement("rangestart", this.getRangeStartAttribute(), toStringStartValue, configElement);
        String toStringEndValue = this.toStringValue(this.getRangeEnd());
        this.insertJDOMConfigElement("rangeend", this.getRangeEndAttribute(), toStringEndValue, configElement);
    }
    
    private String toStringValue(Object value) {
        if (null == value) {
			return null;
		}
        Date date = null;
        if (value instanceof XMLGregorianCalendar) {
            XMLGregorianCalendar grCal = (XMLGregorianCalendar) value;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, grCal.getDay());
            calendar.set(Calendar.MONTH, grCal.getMonth()-1);
            calendar.set(Calendar.YEAR, grCal.getYear());
            date = calendar.getTime();
        } else if (value instanceof Date) {
            date = (Date) value;
        }
        if (null != date) {
            return DateConverter.getFormattedDate(date, DATE_PATTERN);
        }
        return null;
    }
    
	@Override
    protected void extractValidationRules(Element validationElement) {
        super.extractValidationRules(validationElement);
        Element valueElement = validationElement.getChild("value");
        if (null != valueElement) {
            this.setValue(DateConverter.parseDate(valueElement.getText(), DATE_PATTERN));
            this.setValueAttribute(valueElement.getAttributeValue("attribute"));
        }
        Element rangeStartElement = validationElement.getChild("rangestart");
        if (null != rangeStartElement) {
            this.setRangeStart(DateConverter.parseDate(rangeStartElement.getText(), DATE_PATTERN));
            this.setRangeStartAttribute(rangeStartElement.getAttributeValue("attribute"));
        }
        Element rangeEndElement = validationElement.getChild("rangeend");
        if (null != rangeEndElement) {
            this.setRangeEnd(DateConverter.parseDate(rangeEndElement.getText(), DATE_PATTERN));
            this.setRangeEndAttribute(rangeEndElement.getAttributeValue("attribute"));
        }
    }
    
	@Override
    public List<AttributeFieldError> validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager) {
        List<AttributeFieldError> errors = super.validate(attribute, tracer, langManager);
        if (this.isEmpty()) {
			return errors;
		}
        try {
            Date attributeValue = ((DateAttribute) attribute).getDate();
            if (null == attributeValue) {
				return errors;
			}
            Date startValue = (this.getRangeStart() != null) ? (Date) this.getRangeStart() : this.getOtherAttributeValue(attribute, this.getRangeStartAttribute());
            if (null != startValue && attributeValue.before(startValue)) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.LESS_THAN_ALLOWED, tracer);
                String allowedDate = DateConverter.getFormattedDate(startValue, DATE_PATTERN);
                error.setMessage("Date less than " + allowedDate);
                errors.add(error);
            }
            Date endValue = (this.getRangeEnd() != null) ? (Date) this.getRangeEnd() : this.getOtherAttributeValue(attribute, this.getRangeEndAttribute());
            if (null != endValue && attributeValue.after(endValue)) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.GREATER_THAN_ALLOWED, tracer);
                String allowedDate = DateConverter.getFormattedDate(endValue, DATE_PATTERN);
                error.setMessage("Date greater than " + allowedDate);
                errors.add(error);
            }
            Date value = (this.getValue() != null) ? (Date) this.getValue() : this.getOtherAttributeValue(attribute, this.getValueAttribute());
            if (null != value && !attributeValue.equals(value)) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.NOT_EQUALS_THAN_ALLOWED, tracer);
                String allowedDate = DateConverter.getFormattedDate(value, DATE_PATTERN);
                error.setMessage("Date not equals than " + allowedDate);
                errors.add(error);
            }
        } catch (Throwable t) {
        	_logger.error("Error validating Attribute '{}'", attribute.getName(), t);
           // ApsSystemUtils.logThrowable(t, this, "validate", "Error validating Attribute '" + attribute.getName() + "'");
            throw new RuntimeException("Error validating Attribute '" + attribute.getName() + "'", t);
        }
        return errors;
    }
    
    private Date getOtherAttributeValue(AttributeInterface attribute, String otherAttributeName) {
		if (null == otherAttributeName) {
			return null;
		}
        AttributeInterface other = (AttributeInterface) attribute.getParentEntity().getAttribute(otherAttributeName);
        if (null != other && (other instanceof DateAttribute) && ((DateAttribute) other).getDate() != null) {
            return ((DateAttribute) other).getDate();
        }
        return null;
    }
    
    
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    
}
