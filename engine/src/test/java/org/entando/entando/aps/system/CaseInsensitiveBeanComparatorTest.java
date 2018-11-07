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
package org.entando.entando.aps.system;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaseInsensitiveBeanComparatorTest {

    public static class ClassWithString {

        private String value;

        public ClassWithString(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Test
    public void testStringComparison() {
        List<ClassWithString> list = Arrays.asList(new ClassWithString("B"), new ClassWithString("a"));
        Collections.sort(list, new CaseInsensitiveBeanComparator("value"));
        assertEquals("a", list.get(0).getValue());
        assertEquals("B", list.get(1).getValue());
    }

    public static class ClassWithInteger {

        private Integer value;

        public ClassWithInteger(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    @Test
    public void testNonStringComparison() {
        List<ClassWithInteger> list = Arrays.asList(new ClassWithInteger(2), new ClassWithInteger(1));
        Collections.sort(list, new CaseInsensitiveBeanComparator("value"));
        assertEquals(1, (int) list.get(0).getValue());
        assertEquals(2, (int) list.get(1).getValue());
    }
}
