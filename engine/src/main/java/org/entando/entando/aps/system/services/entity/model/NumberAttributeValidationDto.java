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
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;

/**
 * @author E.Santoboni
 */
public class NumberAttributeValidationDto extends EntityAttributeValidationDto {

    private Integer rangeStartNumber;
    private String rangeStartNumberAttribute;
    private Integer rangeEndNumber;
    private String rangeEndNumberAttribute;
    private Integer equalNumber;
    private String equalNumberAttribute;

    public NumberAttributeValidationDto(IAttributeValidationRules validationRules) {
        super(validationRules);
        if (validationRules instanceof NumberAttributeValidationRules) {
            NumberAttributeValidationRules nulValRule = (NumberAttributeValidationRules) validationRules;
            this.setRangeEndNumber((Integer) nulValRule.getRangeEnd());
            this.setRangeStartNumber((Integer) nulValRule.getRangeStart());
            this.setEqualNumber((Integer) nulValRule.getValue());
            this.setRangeEndNumberAttribute(nulValRule.getRangeEndAttribute());
            this.setRangeStartNumberAttribute(nulValRule.getRangeStartAttribute());
            this.setEqualNumberAttribute(nulValRule.getValueAttribute());
        }
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

}
