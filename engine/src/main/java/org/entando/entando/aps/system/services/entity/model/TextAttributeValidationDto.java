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

import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;

/**
 * @author E.Santoboni
 */
public class TextAttributeValidationDto extends EntityAttributeValidationDto {

    private Integer minLength;
    private Integer maxLength;
    private String regex;
    private String rangeStartString;
    private String rangeEndString;
    private String rangeStartStringAttribute;
    private String rangeEndStringAttribute;
    private String equalString;
    private String equalStringAttribute;

    public TextAttributeValidationDto(IAttributeValidationRules validationRules) {
        super(validationRules);
        if (validationRules instanceof TextAttributeValidationRules) {
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

}
