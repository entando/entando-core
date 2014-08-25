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
package com.agiletec.aps.system.common.entity.model.attribute;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This class describes a numeric information common for all the languages.
 * @author W.Ambu - S.Didaci - E.Santoboni
 */
public class NumberAttribute extends AbstractAttribute {
    
    /**
     * Return the number in the format used for the current language.
     * @return The formatted number
     */
    public String getNumber() {
        if (null != this.getValue()) {
            return this.getValue().toString();
        }
        return null;
    }

    /**
     * Return the number in the format used for the current language,
     * expressed in form of percentage.
     * Using this method, a fractional number like. eg., 0.53 is displayed as 53%. 
     * @return The formatted number.
     */
    public String getPercentNumber() {
        String number = "";
        if (null != this.getNumber()) {
            NumberFormat numberInstance =
                    NumberFormat.getPercentInstance(new Locale(getRenderingLang(), ""));
            number = numberInstance.format(this.getNumber());
        }
        return number;
    }
    
    public Element getJDOMElement() {
		Element attributeElement = this.createRootElement("attribute");
        String number = this.getNumber();
        if (null != number && number.trim().length() > 0) {
            Element numberElement = new Element("number");
            numberElement.setText(number);
            attributeElement.addContent(numberElement);
        }
        return attributeElement;
    }

    /**
     * Return the number held by the attribute.
     * @return The number held by the attribute.
     */
    public BigDecimal getValue() {
        return _number;
    }

    /**
     * Associate the given number to the current attribute.
     * @param number The number to associate to the current attribute.
     */
    public void setValue(BigDecimal number) {
        this._number = number;
    }
    
    public boolean isSearchableOptionSupported() {
        return true;
    }
    
    public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
        if (this.getValue() != null) {
            List<AttributeSearchInfo> infos = new ArrayList<AttributeSearchInfo>();
            AttributeSearchInfo info = new AttributeSearchInfo(null, null, this.getValue(), null);
            infos.add(info);
            return infos;
        }
        return null;
    }
    
    protected IAttributeValidationRules getValidationRuleNewIntance() {
        return new NumberAttributeValidationRules();
    }

    /**
     * Associate the (numeric) string submitted in the back-office form to the current attribute.
     * This method is only invoked by the entity handling routines within the back-office area.
     * @param failedNumberString The numeric string submitted in the back-office form.
     */
    public void setFailedNumberString(String failedNumberString) {
        this._failedNumberString = failedNumberString;
    }

    /**
     * Return the numeric string inserted in the back-office form; this method
     * is only invoked by the entity handling routines within the back-office area. 
     * @return The requested numeric string.
     */
    public String getFailedNumberString() {
        return _failedNumberString;
    }
    
    protected Object getJAXBValue(String langCode) {
        return this.getValue();
    }
    
    public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
        super.valueFrom(jaxbAttribute);
        this.setValue((BigDecimal) jaxbAttribute.getValue());
    }
    
    public Status getStatus() {
        if (null != this.getValue() || null != this.getFailedNumberString()) {
            return Status.VALUED;
        }
        return Status.EMPTY;
    }
    
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        if (null == this.getValue() && null != this.getFailedNumberString()) {
            errors.add(new AttributeFieldError(this, FieldError.INVALID_FORMAT, tracer));
        }
        return errors;
    }
    
    private BigDecimal _number;
    private String _failedNumberString;
    
}
