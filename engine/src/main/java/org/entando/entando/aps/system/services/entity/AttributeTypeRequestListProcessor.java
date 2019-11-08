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

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.util.FilterUtils;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class AttributeTypeRequestListProcessor extends RequestListProcessor<AttributeInterface> {

    private static final String KEY_CODE = "code";

    public AttributeTypeRequestListProcessor(RestListRequest restListRequest, List<AttributeInterface> attributes) {
        super(restListRequest, attributes);
    }

    @Override
    protected Function<Filter, Predicate<AttributeInterface>> getPredicates() {
        return (filter) -> {
            switch (filter.getAttribute()) {
                case KEY_CODE:
                    return p -> FilterUtils.filterString(filter, p.getType());
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<AttributeInterface>> getComparators() {
        return sort -> {
            return (a, b) -> StringUtils.compare(a.getType(), b.getType());
        };
    }
}
