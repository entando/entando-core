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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.util.CheckFormatUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Manager class for the 'date' attribute.
 * @author E.Santoboni
 */
public class DateAttributeManager extends AbstractMonoLangAttributeManager {

	@Override
    protected void setValue(AttributeInterface attribute, String value) {
        DateAttribute dateAttribute = (DateAttribute) attribute;
        Date data = null;
        if (value != null) {
            value = value.trim();
        }
        if (CheckFormatUtil.isValidDate(value)) {
            try {
                SimpleDateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");
                data = dataF.parse(value);
                dateAttribute.setFailedDateString(null);
            } catch (ParseException ex) {
                throw new RuntimeException("Error while parsing the date submitted - " + value + " -", ex);
            }
        } else {
            dateAttribute.setFailedDateString(value);
        }
        dateAttribute.setDate(data);
    }

	@Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        AttributeInterface attribute = attributeFieldError.getAttribute();
        DateAttributeValidationRules valRule = (DateAttributeValidationRules) attribute.getValidationRules();
        if (null != valRule) {
            String errorCode = attributeFieldError.getErrorCode();
            if (errorCode.equals(FieldError.GREATER_THAN_ALLOWED)) {
                Date endValue = (valRule.getRangeEnd() != null) ? (Date) valRule.getRangeEnd() : this.getOtherAttributeValue(attribute, valRule.getRangeEndAttribute());
                String[] args = {DateConverter.getFormattedDate(endValue, "dd/MM/yyyy")};
                return action.getText("DateAttribute.fieldError.greaterValue", args);
            } else if (errorCode.equals(FieldError.LESS_THAN_ALLOWED)) {
                Date startValue = (valRule.getRangeStart() != null) ? (Date) valRule.getRangeStart() : this.getOtherAttributeValue(attribute, valRule.getRangeStartAttribute());
                String[] args = {DateConverter.getFormattedDate(startValue, "dd/MM/yyyy")};
                return action.getText("DateAttribute.fieldError.lessValue", args);
            } else if (errorCode.equals(FieldError.NOT_EQUALS_THAN_ALLOWED)) {
                Date value = (valRule.getValue() != null) ? (Date) valRule.getValue() : this.getOtherAttributeValue(attribute, valRule.getValueAttribute());
                String[] args = {DateConverter.getFormattedDate(value, "dd/MM/yyyy")};
                return action.getText("DateAttribute.fieldError.wrongValue", args);
            }
        }
        return action.getText(this.getInvalidAttributeMessage());
    }

    private Date getOtherAttributeValue(AttributeInterface attribute, String otherAttributeName) {
        AttributeInterface other = (AttributeInterface) attribute.getParentEntity().getAttribute(otherAttributeName);
        if (null != other && (other instanceof DateAttribute) && ((DateAttribute) other).getDate() != null) {
            return ((DateAttribute) other).getDate();
        }
        return null;
    }

	@Override
    protected String getInvalidAttributeMessage() {
        return "DateAttribute.fieldError.invalidDate";
    }

}
