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
package org.entando.entando.web.common;

import com.agiletec.aps.system.SystemConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;

public class FilterUtils {

    public static boolean filterString(Filter filter, Supplier<String> supplier) {

        String value = supplier.get();
        String filterValue = filter.getValue();

        switch (getFilterOperator(filter)) {
            case EQUAL:
                return value.equals(filterValue);
            case NOT_EQUAL:
                return !value.equals(filterValue);
            case LIKE:
                return value.toLowerCase().equals(filterValue.toLowerCase());
            case GREATER:
                return value.compareTo(filterValue) >= 0;
            case LOWER:
                return value.compareTo(filterValue) <= 0;
            default:
                throw new UnsupportedOperationException(getUnsupportedOperatorMessage(filter));
        }
    }

    public static boolean filterBoolean(Filter filter, Supplier<Boolean> supplier) {

        boolean value = supplier.get();
        boolean filterValue = Boolean.parseBoolean(filter.getValue().toLowerCase());

        switch (getFilterOperator(filter)) {
            case EQUAL:
                return value == filterValue;
            case NOT_EQUAL:
                return value != filterValue;
            default:
                throw new UnsupportedOperationException(getUnsupportedOperatorMessage(filter));
        }
    }

    public static boolean filterInt(Filter filter, Supplier<Integer> supplier) {
        return filterNumber(filter, supplier.get().doubleValue());
    }

    public static boolean filterDouble(Filter filter, Supplier<Double> supplier) {
        return filterNumber(filter, supplier.get());
    }

    public static boolean filterLong(Filter filter, Supplier<Long> supplier) {
        return filterNumber(filter, supplier.get().doubleValue());
    }

    public static boolean filterDate(Filter filter, Supplier<Date> supplier) {
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
        double filterValue;
        try {
            filterValue = sdf.parse(filter.getValue()).getTime();
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        return filterNumber(filter, filterValue, supplier.get().getTime());
    }

    private static boolean filterNumber(Filter filter, double value) {
        return filterNumber(filter, Double.parseDouble(filter.getValue()), value);
    }

    private static boolean filterNumber(Filter filter, double filterValue, double value) {

        switch (getFilterOperator(filter)) {
            case EQUAL:
                return value == filterValue;
            case NOT_EQUAL:
                return value != filterValue;
            case GREATER:
                return value >= filterValue;
            case LOWER:
                return value <= filterValue;
            default:
                throw new UnsupportedOperationException(getUnsupportedOperatorMessage(filter));
        }
    }

    private static FilterOperator getFilterOperator(Filter filter) {
        return FilterOperator.parse(filter.getOperator());
    }

    private static String getUnsupportedOperatorMessage(Filter filter) {
        return "Operator '" + filter.getOperator() + "' is not supported";
    }
}
