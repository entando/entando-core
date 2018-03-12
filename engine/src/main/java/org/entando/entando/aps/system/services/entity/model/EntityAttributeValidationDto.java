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

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import java.util.Date;

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

    private Date rangeStartDate;
    private Date rangeEndDate;
    private String rangeStartDateAttribute;
    private String rangeEndDateAttribute;
    private Date equalDate;
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
            /*
            if (attribute instanceof EnumeratorAttribute) {
                EnumeratorAttribute enumeratorAttribute = (EnumeratorAttribute) attribute;
                this.setEnumeratorStaticItems(enumeratorAttribute.getStaticItems());
                this.setEnumeratorStaticItemsSeparator(enumeratorAttribute.getCustomSeparator());
                this.setEnumeratorExtractorBean(enumeratorAttribute.getExtractorBeanName());
            }
             */
        }
        if (attribute instanceof DateAttribute) {
            DateAttributeValidationRules dateValRule = (DateAttributeValidationRules) validationRules;
            this.setRangeEndDate((Date) dateValRule.getRangeEnd());
            this.setRangeStartDate((Date) dateValRule.getRangeStart());
            this.setEqualDate((Date) dateValRule.getValue());
            this.setRangeEndDateAttribute(dateValRule.getRangeEndAttribute());
            this.setRangeStartDateAttribute(dateValRule.getRangeStartAttribute());
            this.setEqualDateAttribute(dateValRule.getValueAttribute());
        }
        if (attribute instanceof NumberAttribute) {
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

    public Date getRangeStartDate() {
        return rangeStartDate;
    }

    public void setRangeStartDate(Date rangeStartDate) {
        this.rangeStartDate = rangeStartDate;
    }

    public Date getRangeEndDate() {
        return rangeEndDate;
    }

    public void setRangeEndDate(Date rangeEndDate) {
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

    public Date getEqualDate() {
        return equalDate;
    }

    public void setEqualDate(Date equalDate) {
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
