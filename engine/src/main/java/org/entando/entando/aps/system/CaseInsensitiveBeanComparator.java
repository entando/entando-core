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

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;

/**
 * A BeanComparator that orders bean properties in a case insensitive way if
 * they are of type String, or in the default BeanComparator way otherwise.
 */
public class CaseInsensitiveBeanComparator extends BeanComparator {

    private static final Comparator CASE_INSENSITIVE_COMPARATOR = String.CASE_INSENSITIVE_ORDER;
    private static final Comparator DEFAULT_COMPARATOR = ComparableComparator.getInstance();
    private static final Comparator COMPARATOR = new CaseInsensitiveOrComparableComparator();

    private static class CaseInsensitiveOrComparableComparator implements Comparator, Serializable {

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof String && o2 instanceof String) {
                return CASE_INSENSITIVE_COMPARATOR.compare(o1, o2);
            } else {
                return DEFAULT_COMPARATOR.compare(o1, o2);
            }
        }
    }

    public CaseInsensitiveBeanComparator(String property) {
        super(property, COMPARATOR);
    }
}
