/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.label;

import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.aps.util.FilterUtils;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class LabelRequestListProcessor extends RequestListProcessor<LabelDto> {

    private static final String LABEL_KEY_FILTER_KEY = "key";
    private static final String LABEL_KEY_FILTER_VALUE = "value";

    public LabelRequestListProcessor(final RestListRequest restListRequest, final List<LabelDto> items) {
        super(restListRequest, items);
    }

    @Override
    protected Function<Filter, Predicate<LabelDto>> getPredicates() {
        return filter -> {
            switch (filter.getAttribute()) {
                case LABEL_KEY_FILTER_KEY:
                    return label -> FilterUtils.filterString(filter, label.getKey());
                case LABEL_KEY_FILTER_VALUE:
                    return label -> label.getTitles().values().stream().anyMatch(value -> FilterUtils.filterString(filter, value));
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<LabelDto>> getComparators() {
        return prop -> Comparator.comparing(LabelDto::getKey);
    }

}
