/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.common.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.FieldSearchFilter.LikeOptionType;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.util.DateConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class Filter {

    private String attribute;
    private String entityAttr;
    private String operator = FilterOperator.LIKE.getValue();
    private String value;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String order;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getEntityAttr() {
        return entityAttr;
    }

    public void setEntityAttr(String entityAttr) {
        this.entityAttr = entityAttr;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Filter() {
    }

    public Filter(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Filter(String attribute, String value, String operator) {
        this(attribute, value);
        this.operator = operator;
    }

    @JsonIgnore
    public String getAttributeName() {
        return this.getAttribute();
    }

    @JsonIgnore
    @SuppressWarnings("rawtypes")
    public FieldSearchFilter getFieldSearchFilter() {
        FieldSearchFilter filter = null;
        String escapedKey = StringEscapeUtils.escapeSql(this.getAttributeName());
        Object objectValue = this.extractFilterValue();
        if (FilterOperator.GREATER.getValue().equalsIgnoreCase(this.getOperator())) {
            filter = new FieldSearchFilter(escapedKey, objectValue, null);
        } else if (FilterOperator.LOWER.getValue().equalsIgnoreCase(this.getOperator())) {
            filter = new FieldSearchFilter(escapedKey, null, objectValue);
        } else {
            filter = new FieldSearchFilter(escapedKey, objectValue, FilterOperator.LIKE.getValue().equalsIgnoreCase(this.getOperator()));
        }
        filter.setOrder(this.getOrder());
        return filter;
    }

    @JsonIgnore
    @SuppressWarnings("rawtypes")
    public EntitySearchFilter getEntitySearchFilter() {
        EntitySearchFilter filter = null;
        boolean isAttributeFilter = (StringUtils.isBlank(this.getAttributeName()));
        String escapedKey = (isAttributeFilter) ? StringEscapeUtils.escapeSql(this.getEntityAttr()) : StringEscapeUtils.escapeSql(this.getAttributeName());
        Object objectValue = this.extractFilterValue();
        if (FilterOperator.GREATER.getValue().equalsIgnoreCase(this.getOperator())) {
            filter = new EntitySearchFilter(escapedKey, isAttributeFilter, objectValue, null);
        } else if (FilterOperator.LOWER.getValue().equalsIgnoreCase(this.getOperator())) {
            filter = new EntitySearchFilter(escapedKey, isAttributeFilter, null, objectValue);
        } else {
            filter = new EntitySearchFilter(escapedKey, isAttributeFilter, objectValue, FilterOperator.LIKE.getValue().equalsIgnoreCase(this.getOperator()), LikeOptionType.COMPLETE);
        }
        filter.setOrder(this.getOrder());
        return filter;
    }

    protected Object extractFilterValue() {
        String escapedValue = StringEscapeUtils.escapeSql(this.getValue());
        Object objectValue = escapedValue;
        if (FilterType.DATE.getValue().equalsIgnoreCase(this.getType())) {
            objectValue = DateConverter.parseDate(escapedValue, SystemConstants.API_DATE_FORMAT);
        } else if (FilterType.NUMBER.getValue().equalsIgnoreCase(this.getType())) {
            Integer numberInt = Integer.parseInt(escapedValue);
            objectValue = new BigDecimal(numberInt);
        }
        return objectValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Filter other = (Filter) obj;
        if (attribute == null) {
            if (other.attribute != null) {
                return false;
            }
        } else if (!attribute.equals(other.attribute)) {
            return false;
        }
        if (operator == null) {
            if (other.operator != null) {
                return false;
            }
        } else if (!operator.equals(other.operator)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Filter{" + "attribute=" + attribute + ", operator=" + operator + ", value=" + value + '}';
    }

}
