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
package org.entando.entando.web.common.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;
import java.math.BigDecimal;
import java.util.Date;

public enum FilterType {

    STRING("string") {
        @Override
        public Object parseFilterValue(String value) {
            return value;
        }
    },
    DATE("date") {
        @Override
        public Date parseFilterValue(String value) {
            return DateConverter.parseDate(value, SystemConstants.API_DATE_FORMAT);
        }
    },
    BOOLEAN("boolean") {
        @Override
        public Boolean parseFilterValue(String value) {
            return Boolean.parseBoolean(value.toLowerCase());
        }
    },
    NUMBER("number") {
        @Override
        public BigDecimal parseFilterValue(String value) {
            Double numberInt = Double.parseDouble(value);
            return new BigDecimal(numberInt);
        }
    };

    private final String value;

    private FilterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FilterType parse(String type) {
        for (FilterType value : FilterType.values()) {
            if (value.getValue().equals(type)) {
                return value;
            }
        }
        throw new IllegalArgumentException(type + " is not a supported filter type");
    }

    public abstract Object parseFilterValue(String value);
}
