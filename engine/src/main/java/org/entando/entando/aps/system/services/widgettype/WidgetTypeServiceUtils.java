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
package org.entando.entando.aps.system.services.widgettype;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class WidgetTypeServiceUtils {

    private static final String KEY_CODE = "code";
    private static final String KEY_USED = "used";
    private static final String KEY_TYPOLOGY = "typology";
    private static final String KEY_GROUP = "group";
    private static final String KEY_PLUGIN_CODE = "pluginCode";

    public static class Predicates {

        public static Predicate<WidgetDto> byCode(String codeToken) {
            return p -> p.getCode().toLowerCase().contains(codeToken.toLowerCase());
        }

        public static Predicate<WidgetDto> byUsed(Integer used) {
            return p -> p.getUsed() == used;
        }

        public static Predicate<WidgetDto> byTypology(String typologyToken) {
            return p -> p.getTypology().toLowerCase().contains(typologyToken.toLowerCase());
        }

        public static Predicate<WidgetDto> byGroupy(String groupToken) {
            return p -> p.getGroup().toLowerCase().contains(groupToken.toLowerCase());
        }

        public static Predicate<WidgetDto> byPluginCode(String pluginToken) {
            return p -> p.getPluginCode().toLowerCase().contains(pluginToken.toLowerCase());
        }

    }

    public static class Comparators {

        public static Comparator<WidgetDto> byCode() {
            return new Comparator<WidgetDto>() {
                @Override
                public int compare(WidgetDto o1, WidgetDto o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
            };
        }

        public static Comparator<WidgetDto> byUsed() {
            return new Comparator<WidgetDto>() {
                @Override
                public int compare(WidgetDto o1, WidgetDto o2) {
                    return Integer.compare(o1.getUsed(), o2.getUsed());
                }
            };
        }

        public static Comparator<WidgetDto> byTypology() {
            return new Comparator<WidgetDto>() {
                @Override
                public int compare(WidgetDto o1, WidgetDto o2) {
                    return o1.getTypology().compareTo(o2.getTypology());
                }
            };
        }

        public static Comparator<WidgetDto> byGroup() {
            return new Comparator<WidgetDto>() {
                @Override
                public int compare(WidgetDto o1, WidgetDto o2) {
                    return o1.getGroup().compareTo(o2.getGroup());
                }
            };
        }

        public static Comparator<WidgetDto> byPlugin() {
            return new Comparator<WidgetDto>() {
                @Override
                public int compare(WidgetDto o1, WidgetDto o2) {
                    return o1.getPluginCode().compareTo(o2.getPluginCode());
                }
            };
        }

    }

    public static Comparator<WidgetDto> getComparator(String sort) {
        switch (sort) {
            case KEY_CODE:
                return Comparators.byCode();
            case KEY_USED:
                return Comparators.byUsed();
            case KEY_TYPOLOGY:
                return Comparators.byTypology();
            case KEY_GROUP:
                return Comparators.byGroup();
            case KEY_PLUGIN_CODE:
                return Comparators.byPlugin();
            default:
                return Comparators.byCode();
        }
    }

    public static Comparator<WidgetDto> getComparator(String sort, String direction) {
        Comparator<WidgetDto> comparator = getComparator(sort);
        if (null == comparator) {
            return null;
        }
        if (direction.equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    public static Predicate<WidgetDto> getPredicate(Filter filter) {
        if (null == filter) {
            return null;
        }
        switch (filter.getAttribute()) {
            case KEY_CODE:
                return Predicates.byCode(filter.getValue());
            case KEY_USED:
                return Predicates.byUsed(Integer.valueOf(filter.getValue()));
            case KEY_TYPOLOGY:
                return Predicates.byTypology(filter.getValue());
            case KEY_GROUP:
                return Predicates.byGroupy(filter.getValue());
            case KEY_PLUGIN_CODE:
                return Predicates.byPluginCode(filter.getValue());
            default:
                break;
        }
        return null;
    }

    public static List<Predicate<WidgetDto>> getPredicates(RestListRequest restListRequest) {
        List<Predicate<WidgetDto>> predicates = new ArrayList<>();
        if (null == restListRequest || null == restListRequest.getFilters()) {
            return predicates;
        }
        for (Filter f : restListRequest.getFilters()) {
            Predicate<WidgetDto> p = getPredicate(f);
            if (null != p) {
                predicates.add(p);
            }
        }
        return predicates;
    }

}
