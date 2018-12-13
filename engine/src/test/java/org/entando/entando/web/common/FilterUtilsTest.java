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
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class FilterUtilsTest {

    @Test
    public void shouldFilterStrings() {
        assertTrue(filterString("a", FilterOperator.EQUAL, "a"));
        assertFalse(filterString("a", FilterOperator.EQUAL, "A"));
        assertTrue(filterString("a", FilterOperator.NOT_EQUAL, "b"));
        assertTrue(filterString("a", FilterOperator.LIKE, "-A-"));
        assertTrue(filterString("b", FilterOperator.GREATER, "a"));
        assertTrue(filterString("a", FilterOperator.LOWER, "b"));
    }

    @Test
    public void shouldFilterNumbers() {
        assertTrue(filterInt(1, FilterOperator.EQUAL, 1));
        assertFalse(filterInt(1, FilterOperator.NOT_EQUAL, 1));
        assertTrue(filterInt(2, FilterOperator.GREATER, 1));
        assertTrue(filterInt(1, FilterOperator.LOWER, 2));
    }

    @Test
    public void shouldFilterBooleans() {
        assertTrue(filterBoolean(true, FilterOperator.EQUAL, true));
        assertTrue(filterBoolean(false, FilterOperator.EQUAL, false));
        assertTrue(filterBoolean(true, FilterOperator.NOT_EQUAL, false));
    }

    @Test
    public void shouldFilterDates() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);

        String dayString = "2018-12-01 00:00:00";
        String nextDayString = "2018-12-02 00:00:00";

        Date day = sdf.parse(dayString);
        Date nextDay = sdf.parse(nextDayString);

        assertTrue(filterDate(day, FilterOperator.EQUAL, dayString));
        assertTrue(filterDate(nextDay, FilterOperator.NOT_EQUAL, dayString));
        assertTrue(filterDate(nextDay, FilterOperator.GREATER, dayString));
        assertTrue(filterDate(day, FilterOperator.LOWER, nextDayString));
    }

    private boolean filterString(String value, FilterOperator operator, String filterValue) {
        return FilterUtils.filterString(getFilter(operator, filterValue), () -> value);
    }

    private boolean filterInt(int value, FilterOperator operator, int filterValue) {
        return FilterUtils.filterInt(getFilter(operator, String.valueOf(filterValue)), () -> value);
    }

    private boolean filterBoolean(boolean value, FilterOperator operator, boolean filterValue) {
        return FilterUtils.filterBoolean(getFilter(operator, String.valueOf(filterValue)), () -> value);
    }

    private boolean filterDate(Date value, FilterOperator operator, String filterValue) {
        return FilterUtils.filterDate(getFilter(operator, filterValue), () -> value);
    }

    private Filter getFilter(FilterOperator operator, String value) {
        Filter filter = new Filter();
        filter.setOperator(operator.getValue());
        filter.setValue(value);
        return filter;
    }
}
