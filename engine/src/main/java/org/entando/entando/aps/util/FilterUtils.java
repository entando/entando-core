package org.entando.entando.aps.util;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.TransformingComparator;

public class FilterUtils {

    private FilterUtils() {
        // Utility class, not to be instantiated
    }

    public static TransformingComparator createCaseInsensitiveComparator() {
        Transformer caseInsensitiveTransformer = input -> {
            if (input instanceof String) {
                return ((String)input).toLowerCase();
            } else {
                return input;
            }
        };

        return new TransformingComparator(caseInsensitiveTransformer);
    }
}
