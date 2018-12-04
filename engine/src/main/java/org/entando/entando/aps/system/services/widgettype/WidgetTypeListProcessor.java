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

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.web.common.model.RestListRequest;

public class WidgetTypeListProcessor extends RequestListProcessor<WidgetDto> {

    private static final String CODE = "code";
    private static final String USED = "used";
    private static final String TYPOLOGY = "typology";
    private static final String GROUP = "group";
    private static final String PLUGIN_CODE = "pluginCode";

    public WidgetTypeListProcessor(RestListRequest restListRequest, List<WidgetDto> items) {
        super(restListRequest, items);
    }

    @Override
    protected BiFunction<String, String, Predicate<WidgetDto>> getPredicates() {
        return (attribute, value) -> {
            switch (attribute) {
                case CODE:
                    return p -> p.getCode().toLowerCase().contains(value.toLowerCase());
                case USED:
                    return p -> p.getUsed() == Integer.parseInt(value);
                case TYPOLOGY:
                    return p -> p.getTypology().toLowerCase().contains(value.toLowerCase());
                case GROUP:
                    return p -> p.getGroup().toLowerCase().contains(value.toLowerCase());
                case PLUGIN_CODE:
                    return p -> p.getPluginCode().toLowerCase().contains(value.toLowerCase());
            }
            return null;
        };
    }

    @Override
    protected Function<String, Comparator<WidgetDto>> getComparators() {
        return sort -> {
            switch (sort) {
                case USED:
                    return (a, b) -> Integer.compare(a.getUsed(), b.getUsed());
                case TYPOLOGY:
                    return (a, b) -> a.getTypology().compareTo(b.getTypology());
                case GROUP:
                    return (a, b) -> a.getGroup().compareTo(b.getGroup());
                case PLUGIN_CODE:
                    return (a, b) -> a.getPluginCode().compareTo(b.getPluginCode());
                case CODE:
                default:
                    return (a, b) -> a.getCode().compareTo(b.getCode());
            }
        };
    }
}
