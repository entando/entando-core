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

public enum FilterType {

    STRING("string"),
    DATE("date"),
    BOOLEAN("boolean"),
    NUMBER("number");

    private final String value;

    private FilterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FilterType parse(String op) {
        for (FilterType value : FilterType.values()) {
            if (value.getValue().equals(op)) {
                return value;
            }
        }
        throw new IllegalArgumentException(op + " is not a supported filter type");
    }

}
