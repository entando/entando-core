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
package org.entando.entando.aps.system.services.digitalexchange.client;

import java.util.Map;
import java.util.TreeMap;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * Represents the information about a mocked HTTP request that can be used to
 * simulate the behavior of a mocked DE instance.
 *
 * @see DigitalExchangesMocker
 */
public class DigitalExchangeMockedRequest {

    private HttpMethod method;
    private Map<String, String> urlParams;
    private HttpEntity entity;

    public HttpMethod getMethod() {
        return method;
    }

    public DigitalExchangeMockedRequest setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    public DigitalExchangeMockedRequest setUrlParams(Map<String, String> urlParams) {
        this.urlParams = urlParams;
        return this;
    }

    public RestListRequest getRestListRequest() {
        RestListRequest restListRequest = new RestListRequest();
        restListRequest.setFilters(getFilters());
        return restListRequest;
    }

    private Filter[] getFilters() {
        Map<Integer, Filter> filters = new TreeMap<>();
        if (urlParams != null) {
            urlParams.entrySet().forEach(entry -> {
                String paramName = entry.getKey();
                if (paramName.startsWith("filters[")) {
                    int endIndex = paramName.indexOf("]");
                    int index = Integer.parseInt(paramName.substring("filters[".length(), endIndex));
                    Filter filter = filters.get(index);
                    if (filter == null) {
                        filter = new Filter();
                        filters.put(index, filter);
                    }
                    String filterField = paramName.substring(endIndex + 2, paramName.length());
                    String filterValue = entry.getValue();
                    switch (filterField) {
                        case "attribute":
                            filter.setAttribute(filterValue);
                            break;
                        case "operator":
                            filter.setOperator(filterValue);
                            break;
                        case "value":
                            filter.setValue(filterValue);
                            break;
                    }
                }
            });
        }
        if (filters.isEmpty()) {
            return null;
        }
        return filters.values().toArray(new Filter[filters.size()]);
    }

    public HttpEntity getEntity() {
        return entity;
    }

    public DigitalExchangeMockedRequest setEntity(HttpEntity entity) {
        this.entity = entity;
        return this;
    }
}
