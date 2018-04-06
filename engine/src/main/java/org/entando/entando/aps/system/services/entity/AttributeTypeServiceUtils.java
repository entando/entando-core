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
package org.entando.entando.aps.system.services.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class AttributeTypeServiceUtils {

    private static final String KEY_CODE = "code";

    public static class Predicates {

        public static Predicate<AttributeInterface> byTypeCode(String typeCodeToken) {
            return p -> p.getType().toLowerCase().contains(typeCodeToken.toLowerCase());
        }

    }

    public static class Comparators {

        public static Comparator<AttributeInterface> byTypeCode() {
            return new Comparator<AttributeInterface>() {
                @Override
                public int compare(AttributeInterface o1, AttributeInterface o2) {
                    return o1.getType().compareTo(o2.getType());
                }
            };
        }

    }

    public static Comparator<AttributeInterface> getComparator(String sort) {
        switch (sort) {
            case KEY_CODE:
                return Comparators.byTypeCode();
            default:
                return Comparators.byTypeCode();
        }
    }

    public static Comparator<AttributeInterface> getComparator(String sort, String direction) {
        Comparator<AttributeInterface> comparator = getComparator(sort);
        if (null == comparator) {
            return null;
        }
        if (direction.equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    public static Predicate<AttributeInterface> getPredicate(Filter filter) {
        if (null == filter) {
            return null;
        }
        switch (filter.getAttribute()) {
            case KEY_CODE:
                return Predicates.byTypeCode(filter.getValue());
            default:
                break;
        }
        return null;
    }

    public static List<Predicate<AttributeInterface>> getPredicates(RestListRequest restListRequest) {
        List<Predicate<AttributeInterface>> predicates = new ArrayList<>();
        if (null == restListRequest || null == restListRequest.getFilters()) {
            return predicates;
        }
        for (Filter f : restListRequest.getFilters()) {
            Predicate<AttributeInterface> p = getPredicate(f);
            if (null != p) {
                predicates.add(p);
            }
        }
        return predicates;
    }

}
