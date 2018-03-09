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

import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import java.util.Date;

/**
 * @author E.Santoboni
 */
public class DateAttributeValidationDto extends EntityAttributeValidationDto {

    private Date rangeStartDate;
    private Date rangeEndDate;
    private String rangeStartDateAttribute;
    private String rangeEndDateAttribute;
    private Date equalDate;
    private String equalDateAttribute;

    public DateAttributeValidationDto(IAttributeValidationRules validationRules) {
        super(validationRules);
        if (validationRules instanceof DateAttributeValidationRules) {
            DateAttributeValidationRules dateValRule = (DateAttributeValidationRules) validationRules;
            this.setRangeEndDate((Date) dateValRule.getRangeEnd());
            this.setRangeStartDate((Date) dateValRule.getRangeStart());
            this.setEqualDate((Date) dateValRule.getValue());
            this.setRangeEndDateAttribute(dateValRule.getRangeEndAttribute());
            this.setRangeStartDateAttribute(dateValRule.getRangeStartAttribute());
            this.setEqualDateAttribute(dateValRule.getValueAttribute());
        }
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

}
