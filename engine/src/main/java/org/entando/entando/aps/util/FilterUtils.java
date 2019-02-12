package org.entando.entando.aps.util;

import com.agiletec.aps.system.SystemConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
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

    public static boolean filterString(Filter filter, Supplier<String> supplier) {

        FilterOperator operator = getFilterOperator(filter);
        String value = supplier.get();

        boolean result = false;

        for (String filterValue : filter.getFilterValues()) {
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

    public static boolean filterBoolean(Filter filter, Supplier<Boolean> supplier) {

        FilterOperator operator = getFilterOperator(filter);
        boolean value = supplier.get();

        boolean result = false;

        for (boolean filterValue : getTypedValues(filter, v -> Boolean.parseBoolean(v.toLowerCase()))) {

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

        List<Double> filterValues = getTypedValues(filter, value -> {
            try {
                return (double) sdf.parse(value).getTime();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });

        return filterNumber(filter, filterValues, supplier.get().getTime());
    }

    private static boolean filterNumber(Filter filter, double value) {
        return filterNumber(filter, getTypedValues(filter, Double::parseDouble), value);
    }

    private static boolean filterNumber(Filter filter, List<Double> filterValues, double value) {

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

    private static <T> List<T> getTypedValues(Filter filter, Function<String, T> converter) {
        return filter.getFilterValues().stream().map(converter::apply).collect(Collectors.toList());
    }

    private static FilterOperator getFilterOperator(Filter filter) {
        return FilterOperator.parse(filter.getOperator());
    }

    private static String getUnsupportedOperatorMessage(Filter filter) {
        return "Operator '" + filter.getOperator() + "' is not supported";
    }
}
