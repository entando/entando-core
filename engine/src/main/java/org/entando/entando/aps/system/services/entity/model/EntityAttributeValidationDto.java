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

/**
 * @author E.Santoboni
 */
public class EntityAttributeValidationDto {

    private EntityAttributeOgnlValidationDto ognlValidation;

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
        EntityAttributeValidationDto validationDto = null;
        if (attribute.isTextAttribute()) {
            TextAttributeValidationRules textValRule = (TextAttributeValidationRules) validationRules;
            /*
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
            /*
            this.setRangeEndDate((Date) dateValRule.getRangeEnd());
            this.setRangeStartDate((Date) dateValRule.getRangeStart());
            this.setEqualDate((Date) dateValRule.getValue());
            this.setRangeEndDateAttribute(dateValRule.getRangeEndAttribute());
            this.setRangeStartDateAttribute(dateValRule.getRangeStartAttribute());
            this.setEqualDateAttribute(dateValRule.getValueAttribute());
             */
        }
        if (attribute instanceof NumberAttribute) {
            NumberAttributeValidationRules nulValRule = (NumberAttributeValidationRules) validationRules;
            /*
            this.setRangeEndNumber((Integer) nulValRule.getRangeEnd());
            this.setRangeStartNumber((Integer) nulValRule.getRangeStart());
            this.setEqualNumber((Integer) nulValRule.getValue());
            this.setRangeEndNumberAttribute(nulValRule.getRangeEndAttribute());
            this.setRangeStartNumberAttribute(nulValRule.getRangeStartAttribute());
            this.setEqualNumberAttribute(nulValRule.getValueAttribute());
             */
        }
        OgnlValidationRule ognlValidationRule = validationRules.getOgnlValidationRule();
        if (null != ognlValidationRule) {
            this.setOgnlValidation(new EntityAttributeOgnlValidationDto(ognlValidationRule));
        }
    }

    public EntityAttributeOgnlValidationDto getOgnlValidation() {
        return ognlValidation;
    }

    public void setOgnlValidation(EntityAttributeOgnlValidationDto ognlValidation) {
        this.ognlValidation = ognlValidation;
    }

}
