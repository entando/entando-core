/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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

import java.math.BigDecimal;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.apsadmin.util.CheckFormatUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Manager class for the 'Number' Attribute
 * @author E.Santoboni
 */
public class NumberAttributeManager extends AbstractMonoLangAttributeManager {

	@Override
    protected void setValue(AttributeInterface attribute, String value) {
        NumberAttribute numberAttribute = (NumberAttribute) attribute;
        BigDecimal number = null;
        if (value != null) {
            value = value.trim();
        }
        if (CheckFormatUtil.isValidNumber(value)) {
            try {
                number = new BigDecimal(value);
                numberAttribute.setFailedNumberString(null);
            } catch (NumberFormatException e) {
                throw new RuntimeException("The submitted string is not recognized as a valid number - " + value + " -");
            }
        } else {
            numberAttribute.setFailedNumberString(value);
        }
        numberAttribute.setValue(number);
    }

	@Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        AttributeInterface attribute = attributeFieldError.getAttribute();
        NumberAttributeValidationRules valRules = (NumberAttributeValidationRules) attribute.getValidationRules();
        if (null != valRules) {
            String errorCode = attributeFieldError.getErrorCode();
            if (errorCode.equals(FieldError.LESS_THAN_ALLOWED)) {
                Integer startValue = (valRules.getRangeStart() != null) ? (Integer) valRules.getRangeStart() : this.getOtherAttributeValue(attribute, valRules.getRangeStartAttribute());
                String[] args = {startValue.toString()};
                return action.getText("NumberAttribute.fieldError.lessValue", args);
            } else if (errorCode.equals(FieldError.GREATER_THAN_ALLOWED)) {
                Integer endValue = (valRules.getRangeEnd() != null) ? (Integer) valRules.getRangeEnd() : this.getOtherAttributeValue(attribute, valRules.getRangeEndAttribute());
                String[] args = {endValue.toString()};
                return action.getText("NumberAttribute.fieldError.greaterValue", args);
            } else if (errorCode.equals(FieldError.NOT_EQUALS_THAN_ALLOWED)) {
                Integer value = (valRules.getValue() != null) ? (Integer) valRules.getValue() : this.getOtherAttributeValue(attribute, valRules.getValueAttribute());
                String[] args = {value.toString()};
                return action.getText("NumberAttribute.fieldError.wrongValue", args);
            }
        }
        return action.getText(this.getInvalidAttributeMessage());
    }

    private Integer getOtherAttributeValue(AttributeInterface attribute, String otherAttributeName) {
        AttributeInterface other = (AttributeInterface) attribute.getParentEntity().getAttribute(otherAttributeName);
        if (null != other && (other instanceof NumberAttribute) && ((NumberAttribute) other).getValue() != null) {
            return ((NumberAttribute) other).getValue().intValue();
        }
        return null;
    }

	@Override
    protected String getInvalidAttributeMessage() {
        return "NumberAttribute.fieldError.invalidNumber";
    }

}