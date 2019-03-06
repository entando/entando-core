package org.entando.entando.aps.util;

import com.agiletec.aps.system.SystemConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.junit.Test;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class FilterUtilsTest {

    private final TransformingComparator comparator
            = FilterUtils.createCaseInsensitiveComparator();

    @Test
    public void comparatorIsCaseInsensitive() {
        String a = "B1";
        String b = "b2";

        assertThat(comparator.compare(a, b)).isNegative();
        assertThat(comparator.compare(b, a)).isPositive();
    }

    @Test
    public void comparatorWorksWithSameCase() {
        String a = "b1";
        String b = "b2";

        assertThat(comparator.compare(a, b)).isNegative();
        assertThat(comparator.compare(b, a)).isPositive();
    }

    @Test
    public void comparatorFindsEqualStrings() {
        String a = "b1";
        String b = "B1";

        assertThat(comparator.compare(a, b)).isZero();
        assertThat(comparator.compare(b, a)).isZero();
    }

    @Test
    public void comparatorWorksWithObjects() {
        Integer a = 1;
        Integer b = 2;

        assertThat(comparator.compare(a, b)).isNegative();
        assertThat(comparator.compare(b, a)).isPositive();
    }

    @Test
    public void comparatorWorksWithEqualObjects() {
        Integer a = 1;
        Integer b = 1;

        assertThat(comparator.compare(a, b)).isZero();
        assertThat(comparator.compare(b, a)).isZero();
    }

    @Test
    public void shouldFilterStrings() {
        assertTrue(filterString("a", FilterOperator.EQUAL, new String[]{"a", "b"}));
        assertFalse(filterString("a", FilterOperator.EQUAL, new String[]{"A", "B"}));

        assertTrue(filterString("a", FilterOperator.NOT_EQUAL, new String[]{"b", "c"}));
        assertFalse(filterString("a", FilterOperator.NOT_EQUAL, "a"));

        assertTrue(filterString("-A-", FilterOperator.LIKE, new String[]{"a", "b"}));
        assertFalse(filterString("-A-", FilterOperator.LIKE, new String[]{"b", "c"}));

        assertTrue(filterString("c", FilterOperator.GREATER, new String[]{"a", "b"}));
        assertFalse(filterString("a", FilterOperator.GREATER, new String[]{"b", "c"}));

        assertTrue(filterString("a", FilterOperator.LOWER, new String[]{"b", "c"}));
        assertFalse(filterString("c", FilterOperator.LOWER, new String[]{"a", "b"}));
    }

    @Test
    public void shouldFilterNumbers() {
        assertTrue(filterInt(1, FilterOperator.EQUAL, new String[]{"1", "2"}));
        assertFalse(filterInt(1, FilterOperator.EQUAL, new String[]{"2", "3"}));

        assertTrue(filterInt(1, FilterOperator.NOT_EQUAL, new String[]{"2", "3"}));
        assertFalse(filterInt(1, FilterOperator.NOT_EQUAL, "1"));

        assertTrue(filterInt(2, FilterOperator.GREATER, new String[]{"1", "3"}));
        assertFalse(filterInt(1, FilterOperator.GREATER, new String[]{"2", "3"}));

        assertTrue(filterInt(1, FilterOperator.LOWER, new String[]{"2", "3"}));
        assertFalse(filterInt(3, FilterOperator.LOWER, new String[]{"1", "2"}));
    }

    @Test
    public void shouldFilterBooleans() {
        assertTrue(filterBoolean(true, FilterOperator.EQUAL, "true"));
        assertTrue(filterBoolean(false, FilterOperator.EQUAL, "false"));

        assertTrue(filterBoolean(true, FilterOperator.NOT_EQUAL, "false"));
        assertFalse(filterBoolean(true, FilterOperator.NOT_EQUAL, "true"));
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

    private boolean filterString(String valueToFilter, FilterOperator operator, String filterValue) {
        return FilterUtils.filterString(getFilter(operator, filterValue), valueToFilter);
    }

    private boolean filterString(String valueToFilter, FilterOperator operator, String[] allowedValues) {
        return FilterUtils.filterString(getFilter(operator, allowedValues), valueToFilter);
    }

    private boolean filterInt(int valueToFilter, FilterOperator operator, String filterValue) {
        return FilterUtils.filterInt(getFilter(operator, filterValue), valueToFilter);
    }

    private boolean filterInt(int valueToFilter, FilterOperator operator, String[] allowedValues) {
        return FilterUtils.filterInt(getFilter(operator, allowedValues), valueToFilter);
    }

    private boolean filterBoolean(boolean valueToFilter, FilterOperator operator, String filterValue) {
        return FilterUtils.filterBoolean(getFilter(operator, filterValue), valueToFilter);
    }

    private boolean filterDate(Date valueToFilter, FilterOperator operator, String filterValue) {
        return FilterUtils.filterDate(getFilter(operator, filterValue), valueToFilter);
    }

    private Filter getFilter(FilterOperator operator, String value) {
        Filter filter = new Filter();
        filter.setOperator(operator.getValue());
        filter.setValue(value);
        return filter;
    }

    private Filter getFilter(FilterOperator operator, String[] allowedValues) {
        Filter filter = new Filter();
        filter.setOperator(operator.getValue());
        filter.setAllowedValues(allowedValues);
        return filter;
    }
}
