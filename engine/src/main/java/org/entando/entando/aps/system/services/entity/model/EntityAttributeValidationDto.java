/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.entity.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import com.agiletec.aps.util.DateConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.entity.validator.EntityTypeValidator;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class EntityAttributeValidationDto {

    private Integer minLength;
    private Integer maxLength;
    private String regex;
    private String rangeStartString;
    private String rangeEndString;
    private String rangeStartStringAttribute;
    private String rangeEndStringAttribute;
    private String equalString;
    private String equalStringAttribute;

    private String rangeStartDate;
    private String rangeEndDate;
    private String rangeStartDateAttribute;
    private String rangeEndDateAttribute;
    private String equalDate;
    private String equalDateAttribute;

    private Integer rangeStartNumber;
    private String rangeStartNumberAttribute;
    private Integer rangeEndNumber;
    private String rangeEndNumberAttribute;
    private Integer equalNumber;
    private String equalNumberAttribute;

    private EntityAttributeOgnlValidationDto ognlValidation;

    public EntityAttributeValidationDto() {

    }

    public EntityAttributeValidationDto(IAttributeValidationRules validationRules) {
        OgnlValidationRule ognlValidationRule = validationRules.getOgnlValidationRule();
        if (null != ognlValidationRule) {
            this.setOgnlValidation(new EntityAttributeOgnlValidationDto(ognlValidationRule));
        }
    }

    public EntityAttributeValidationDto(AttributeInterface attribute) {
        IAttributeValidationRules validationRules = attribute.getValidationRules();
        if (null == validationRules) {
            return;
        }
        if (attribute.isTextAttribute()) {
            TextAttributeValidationRules textValRule = (TextAttributeValidationRules) validationRules;
            if (null != textValRule.getMaxLength() && textValRule.getMaxLength() > -1) {
                this.setMaxLength(textValRule.getMaxLength());
            }
            if (null != textValRule.getMinLength() && textValRule.getMinLength() > -1) {
                this.setMinLength(textValRule.getMinLength());
            }
            this.setRegex(textValRule.getRegexp());
            this.setRangeEndString((String) textValRule.getRangeEnd());
            this.setRangeStartString((String) textValRule.getRangeStart());
            this.setEqualString((String) textValRule.getValue());
            this.setRangeEndStringAttribute(textValRule.getRangeEndAttribute());
            this.setRangeStartStringAttribute(textValRule.getRangeStartAttribute());
            this.setEqualStringAttribute(textValRule.getValueAttribute());
        } else if (attribute instanceof DateAttribute) {
            DateAttributeValidationRules dateValRule = (DateAttributeValidationRules) validationRules;
            if (null != dateValRule.getRangeEnd()) {
                this.setRangeEndDate(DateConverter.getFormattedDate((Date) dateValRule.getRangeEnd(), SystemConstants.API_DATE_FORMAT));
            }
            if (null != dateValRule.getRangeEnd()) {
                this.setRangeStartDate(DateConverter.getFormattedDate((Date) dateValRule.getRangeStart(), SystemConstants.API_DATE_FORMAT));
            }
            if (null != dateValRule.getRangeEnd()) {
                this.setEqualDate(DateConverter.getFormattedDate((Date) dateValRule.getValue(), SystemConstants.API_DATE_FORMAT));
            }
            this.setRangeEndDateAttribute(dateValRule.getRangeEndAttribute());
            this.setRangeStartDateAttribute(dateValRule.getRangeStartAttribute());
            this.setEqualDateAttribute(dateValRule.getValueAttribute());
        } else if (attribute instanceof NumberAttribute) {
            NumberAttributeValidationRules nulValRule = (NumberAttributeValidationRules) validationRules;
            this.setRangeEndNumber((Integer) nulValRule.getRangeEnd());
            this.setRangeStartNumber((Integer) nulValRule.getRangeStart());
            this.setEqualNumber((Integer) nulValRule.getValue());
            this.setRangeEndNumberAttribute(nulValRule.getRangeEndAttribute());
            this.setRangeStartNumberAttribute(nulValRule.getRangeStartAttribute());
            this.setEqualNumberAttribute(nulValRule.getValueAttribute());
        }
        OgnlValidationRule ognlValidationRule = validationRules.getOgnlValidationRule();
        if (null != ognlValidationRule) {
            this.setOgnlValidation(new EntityAttributeOgnlValidationDto(ognlValidationRule));
        }
    }

    public void buildAttributeValidation(String typeCode, AttributeInterface attribute, BindingResult bindingResult) {
        EntityAttributeOgnlValidationDto ognlValidationDto = this.getOgnlValidation();
        if (null != ognlValidationDto) {
            ognlValidationDto.buildAttributeOgnlValidation(typeCode, attribute, bindingResult);
        }
        IAttributeValidationRules validationRules = attribute.getValidationRules();
        if (attribute.isTextAttribute()) {
            TextAttributeValidationRules textValRule = (TextAttributeValidationRules) validationRules;
            if (StringUtils.isEmpty(this.getRegex())) {
                textValRule.setRegexp(this.getRegex());
            }
            if (null != this.getMinLength() && null != this.getMaxLength() && (this.getMinLength() > this.getMaxLength())) {
                this.addError(EntityTypeValidator.ERRCODE_INVALID_TEXT_RANGE, bindingResult,
                        new String[]{typeCode, attribute.getName()}, "entityType.attribute.text.invalidRange");
            } else {
                if (null != this.getMinLength()) {
                    textValRule.setMinLength(this.getMinLength());
                }
                if (null != this.getMinLength()) {
                    textValRule.setMaxLength(this.getMaxLength());
                }
            }
            if (StringUtils.isEmpty(this.getRangeStartString()) || StringUtils.isEmpty(this.getRangeEndString())
                    || StringUtils.isEmpty(this.getRangeStartStringAttribute()) || StringUtils.isEmpty(this.getRangeEndStringAttribute())) {
                textValRule.setRangeEnd(this.getRangeEndString());
                textValRule.setRangeStart(this.getRangeStartString());
                textValRule.setRangeEndAttribute(this.getRangeEndStringAttribute());
                textValRule.setRangeStartAttribute(this.getRangeStartStringAttribute());
            } else {
                textValRule.setValue(this.getEqualString());
                textValRule.setValueAttribute(this.getEqualStringAttribute());
            }
        } else if (attribute instanceof DateAttribute) {
            DateAttributeValidationRules dateValRule = (DateAttributeValidationRules) validationRules;
            dateValRule.setRangeStart(this.extractDate(this.getRangeStartDate(), typeCode, attribute.getName(),
                    EntityTypeValidator.ERRCODE_INVALID_DATE_RANGE_START, "entityType.attribute.date.invalidRangeStart", bindingResult));
            dateValRule.setRangeEnd(this.extractDate(this.getRangeEndDate(), typeCode, attribute.getName(),
                    EntityTypeValidator.ERRCODE_INVALID_DATE_RANGE_END, "entityType.attribute.date.invalidRangeEnd", bindingResult));
            if (null != dateValRule.getRangeStart() && null != dateValRule.getRangeEnd()
                    && ((Date) dateValRule.getRangeEnd()).before((Date) dateValRule.getRangeStart())) {
                this.addError(EntityTypeValidator.ERRCODE_INVALID_NUMBER_RANGE,
                        bindingResult, new String[]{typeCode, attribute.getName()}, "entityType.attribute.date.invalidRange");
            }
            dateValRule.setValue(this.extractDate(this.getEqualDate(), typeCode, attribute.getName(),
                    EntityTypeValidator.ERRCODE_INVALID_DATE_VALUE, "entityType.attribute.date.invalidValue", bindingResult));
            dateValRule.setRangeStartAttribute(this.getRangeStartDateAttribute());
            dateValRule.setRangeEndAttribute(this.getRangeEndDateAttribute());
            dateValRule.setValueAttribute(this.getEqualDateAttribute());
        } else if (attribute instanceof NumberAttribute) {
            NumberAttributeValidationRules nulValRule = (NumberAttributeValidationRules) validationRules;
            nulValRule.setRangeEnd(this.getRangeEndNumber());
            nulValRule.setRangeStart(this.getRangeStartNumber());
            if (null != this.getRangeStartNumber() && null != this.getRangeEndNumber()
                    && (this.getRangeEndNumber() < this.getRangeStartNumber())) {
                this.addError(EntityTypeValidator.ERRCODE_INVALID_NUMBER_RANGE,
                        bindingResult, new String[]{typeCode, attribute.getName()}, "entityType.attribute.number.invalidRange");
            }
            nulValRule.setValue(this.getEqualNumber());
            nulValRule.setRangeEndAttribute(this.getRangeEndNumberAttribute());
            nulValRule.setRangeStartAttribute(this.getRangeStartNumberAttribute());
            nulValRule.setValueAttribute(nulValRule.getValueAttribute());
        }
    }

    private Date extractDate(String dateString, String typeCode, String attributeName,
            String errorCode, String errorMessage, BindingResult bindingResult) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
        Date date = null;
        try {
            date = format.parse(dateString.trim());
        } catch (ParseException ex) {
            this.addError(errorCode, bindingResult, new String[]{typeCode, attributeName}, errorMessage);
        }
        return date;
    }

    protected void addError(String errorCode, BindingResult bindingResult, String[] args, String message) {
        bindingResult.reject(errorCode, args, message);
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRangeStartString() {
        return rangeStartString;
    }

    public void setRangeStartString(String rangeStartString) {
        this.rangeStartString = rangeStartString;
    }

    public String getRangeEndString() {
        return rangeEndString;
    }

    public void setRangeEndString(String rangeEndString) {
        this.rangeEndString = rangeEndString;
    }

    public String getRangeStartStringAttribute() {
        return rangeStartStringAttribute;
    }

    public void setRangeStartStringAttribute(String rangeStartStringAttribute) {
        this.rangeStartStringAttribute = rangeStartStringAttribute;
    }

    public String getRangeEndStringAttribute() {
        return rangeEndStringAttribute;
    }

    public void setRangeEndStringAttribute(String rangeEndStringAttribute) {
        this.rangeEndStringAttribute = rangeEndStringAttribute;
    }

    public String getEqualString() {
        return equalString;
    }

    public void setEqualString(String equalString) {
        this.equalString = equalString;
    }

    public String getEqualStringAttribute() {
        return equalStringAttribute;
    }

    public void setEqualStringAttribute(String equalStringAttribute) {
        this.equalStringAttribute = equalStringAttribute;
    }

    public String getRangeStartDate() {
        return rangeStartDate;
    }

    public void setRangeStartDate(String rangeStartDate) {
        this.rangeStartDate = rangeStartDate;
    }

    public String getRangeEndDate() {
        return rangeEndDate;
    }

    public void setRangeEndDate(String rangeEndDate) {
        this.rangeEndDate = rangeEndDate;
    }

    public String getRangeStartDateAttribute() {
        return rangeStartDateAttribute;
    }

    public void setRangeStartDateAttribute(String rangeStartDateAttribute) {
        this.rangeStartDateAttribute = rangeStartDateAttribute;
    }

    public String getRangeEndDateAttribute() {
        return rangeEndDateAttribute;
    }

    public void setRangeEndDateAttribute(String rangeEndDateAttribute) {
        this.rangeEndDateAttribute = rangeEndDateAttribute;
    }

    public String getEqualDate() {
        return equalDate;
    }

    public void setEqualDate(String equalDate) {
        this.equalDate = equalDate;
    }

    public String getEqualDateAttribute() {
        return equalDateAttribute;
    }

    public void setEqualDateAttribute(String equalDateAttribute) {
        this.equalDateAttribute = equalDateAttribute;
    }

    public Integer getRangeStartNumber() {
        return rangeStartNumber;
    }

    public void setRangeStartNumber(Integer rangeStartNumber) {
        this.rangeStartNumber = rangeStartNumber;
    }

    public String getRangeStartNumberAttribute() {
        return rangeStartNumberAttribute;
    }

    public void setRangeStartNumberAttribute(String rangeStartNumberAttribute) {
        this.rangeStartNumberAttribute = rangeStartNumberAttribute;
    }

    public Integer getRangeEndNumber() {
        return rangeEndNumber;
    }

    public void setRangeEndNumber(Integer rangeEndNumber) {
        this.rangeEndNumber = rangeEndNumber;
    }

    public String getRangeEndNumberAttribute() {
        return rangeEndNumberAttribute;
    }

    public void setRangeEndNumberAttribute(String rangeEndNumberAttribute) {
        this.rangeEndNumberAttribute = rangeEndNumberAttribute;
    }

    public Integer getEqualNumber() {
        return equalNumber;
    }

    public void setEqualNumber(Integer equalNumber) {
        this.equalNumber = equalNumber;
    }

    public String getEqualNumberAttribute() {
        return equalNumberAttribute;
    }

    public void setEqualNumberAttribute(String equalNumberAttribute) {
        this.equalNumberAttribute = equalNumberAttribute;
    }

    public EntityAttributeOgnlValidationDto getOgnlValidation() {
        return ognlValidation;
    }

    public void setOgnlValidation(EntityAttributeOgnlValidationDto ognlValidation) {
        this.ognlValidation = ognlValidation;
    }

}
