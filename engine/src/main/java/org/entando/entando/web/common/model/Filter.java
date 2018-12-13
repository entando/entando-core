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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.FieldSearchFilter.LikeOptionType;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringEscapeUtils;

public class Filter {

    private String attribute;
    private String entityAttr;
    private String operator = FilterOperator.EQUAL.getValue();
    private String value;

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
        FieldSearchFilter filter = new FieldSearchFilter(StringEscapeUtils.escapeSql(this.getAttributeName()), StringEscapeUtils.escapeSql(this.getValue()), true, LikeOptionType.COMPLETE);
        return filter;
    }

    @JsonIgnore
    @SuppressWarnings("rawtypes")
    public EntitySearchFilter getEntitySearchFilter() {
        EntitySearchFilter filter = new EntitySearchFilter(StringEscapeUtils.escapeSql(this.getEntityAttr()), true, StringEscapeUtils.escapeSql(this.getValue()), true, LikeOptionType.COMPLETE);
        return filter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
