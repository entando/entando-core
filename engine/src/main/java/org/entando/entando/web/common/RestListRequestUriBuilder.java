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
package org.entando.entando.web.common;

import java.util.function.Supplier;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.web.util.UriComponentsBuilder;

public class RestListRequestUriBuilder {

    private final RestListRequest request;
    private final UriComponentsBuilder builder;

    public RestListRequestUriBuilder(UriComponentsBuilder builder, RestListRequest request) {
        this.request = request;
        this.builder = builder;
    }

    public String toUriString() {

        addQueryParam("page", () -> request.getPage());
        addQueryParam("pageSize", () -> request.getPageSize());
        addQueryParam("direction", () -> request.getDirection());
        addQueryParam("sort", () -> request.getSort());

        if (request.getFilters() != null) {
            int index = 0;
            for (Filter filter : request.getFilters()) {
                String prefix = String.format("filters[%s].", index);
                addQueryParam(prefix + "attribute", () -> filter.getAttribute());
                addQueryParam(prefix + "entityAttr", () -> filter.getEntityAttr());
                addQueryParam(prefix + "operator", () -> filter.getOperator());
                addQueryParam(prefix + "value", () -> filter.getValue());
                index++;
            }
        }

        // The URL is returned without applying URL encoding, because otherwise 
        // the RestTemplate class would apply it again leading to wrong results.
        return builder.build(false).toUriString();
    }

    private void addQueryParam(String key, Supplier supplier) {

        Object value = supplier.get();

        if (value != null) {
            builder.queryParam(key, value);
        }
    }
}
