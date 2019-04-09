package org.entando.entando.aps.util;

import com.agiletec.aps.system.SystemConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;

public class FilterUtils {

    private FilterUtils() {
        // Utility class, not to be instantiated
    }

    public static TransformingComparator createCaseInsensitiveComparator() {
        Transformer caseInsensitiveTransformer = input -> {
            if (input instanceof String) {
                return ((String) input).toLowerCase();
            } else {
                return input;
            }
        };

        return new TransformingComparator(caseInsensitiveTransformer);
    }

    public static boolean filterString(Filter filter, String value) {

        FilterOperator operator = getFilterOperator(filter);
        if (value == null) {
            return false;
        }

        boolean result = false;

        for (String filterValue : getFilterValues(filter)) {
            switch (operator) {
                case EQUAL:
                    result |= value.equals(filterValue);
                    break;
                case NOT_EQUAL:
                    result |= !value.equals(filterValue);
                    break;
                case LIKE:
                    result |= value.toLowerCase().contains(filterValue.toLowerCase());
                    break;
                case GREATER:
                    result |= value.compareTo(filterValue) >= 0;
                    break;
                case LOWER:
                    result |= value.compareTo(filterValue) <= 0;
                    break;
                default:
                    throw new UnsupportedOperationException(getUnsupportedOperatorMessage(filter));
            }
        }

        return result;
    }

    public static boolean filterBoolean(Filter filter, boolean value) {

        FilterOperator operator = getFilterOperator(filter);

        boolean result = false;

        for (boolean filterValue : getTypedAllowedValues(filter, v -> Boolean.parseBoolean(v.toLowerCase()))) {

            switch (operator) {
                case EQUAL:
                case LIKE:
                    result |= value == filterValue;
                    break;
                case NOT_EQUAL:
                    result |= value != filterValue;
                    break;
                default:
                    throw new UnsupportedOperationException(getUnsupportedOperatorMessage(filter));
            }
        }

        return result;
    }

    public static boolean filterInt(Filter filter, Integer value) {
        return filterDouble(filter, value.doubleValue());
    }

    public static boolean filterLong(Filter filter, Long value) {
        return filterDouble(filter, value.doubleValue());
    }

    public static boolean filterDate(Filter filter, Date value) {
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);

        List<Double> filterValues = getTypedAllowedValues(filter, v -> {
            try {
                return (double) sdf.parse(v).getTime();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });

        return filterDouble(filter, filterValues, value.getTime());
    }

    public static boolean filterDouble(Filter filter, double value) {
        return filterDouble(filter, getTypedAllowedValues(filter, Double::parseDouble), value);
    }

    private static boolean filterDouble(Filter filter, List<Double> filterValues, double value) {

        FilterOperator operator = getFilterOperator(filter);

        boolean result = false;

        for (double filterValue : filterValues) {
            switch (operator) {
                case EQUAL:
                case LIKE:
                    result |= value == filterValue;
                    break;
                case NOT_EQUAL:
                    result |= value != filterValue;
                    break;
                case GREATER:
                    result |= value >= filterValue;
                    break;
                case LOWER:
                    result |= value <= filterValue;
                    break;
                default:
                    throw new UnsupportedOperationException(getUnsupportedOperatorMessage(filter));
            }
        }

        return result;
    }

    /**
     * Handles the conversion from String to the desired type.
     */
    private static <T> List<T> getTypedAllowedValues(Filter filter, Function<String, T> converter) {
        return getFilterValues(filter).stream().map(converter::apply).collect(Collectors.toList());
    }

    private static FilterOperator getFilterOperator(Filter filter) {
        return FilterOperator.parse(filter.getOperator());
    }

    private static String getUnsupportedOperatorMessage(Filter filter) {
        return "Operator '" + filter.getOperator() + "' is not supported";
    }
    
    private static List<String> getFilterValues(Filter filter) {
        if (filter.getAllowedValues() == null || filter.getAllowedValues().length == 0) {
            List<String> values = new ArrayList<>();
            if (filter.getValue() != null) {
                values.add(filter.getValue());
            }
            return values;
        }
        return Arrays.asList(filter.getAllowedValues());
    }
}
