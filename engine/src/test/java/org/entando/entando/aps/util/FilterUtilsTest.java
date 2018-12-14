package org.entando.entando.aps.util;

import org.apache.commons.collections.comparators.TransformingComparator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class FilterUtilsTest {

    private final TransformingComparator comparator =
            FilterUtils.createCaseInsensitiveComparator();

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
}