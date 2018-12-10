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

import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Contains all the information necessary for querying a specific Digital
 * Exchange endpoint (HTTP method, response type, path) and provides the logic
 * necessary for combining multiple responses provided by the same endpoint of
 * different Digital Exchange instances.
 *
 * @param <R> the type of each DE response
 * @param <C> the type of the combined response
 */
public abstract class DigitalExchangeCall<R extends RestResponse, C> {

    private final HttpMethod method;
    private final ParameterizedTypeReference<R> parameterizedTypeReference;
    private final String[] urlSegments;
    private HttpEntity entity;

    /**
     * @param method e.g. GET, POST, ...
     * @param parameterizedTypeReference The ParameterizedTypeReference class is
     * used to tell Spring how to convert the HTTP response into a Java object.
     * It is important to specify all the nested generic types, otherwise
     * unknown types will be converted into instances of Map causing
     * ClassCastException at runtime.
     * @param urlSegments path segments necessary for building the last part of
     * the endpoint URL
     */
    public DigitalExchangeCall(HttpMethod method,
            ParameterizedTypeReference<R> parameterizedTypeReference,
            String... urlSegments) {

        this.method = method;
        this.parameterizedTypeReference = parameterizedTypeReference;

        this.urlSegments = new String[urlSegments.length + 1];
        this.urlSegments[0] = "api";
        System.arraycopy(urlSegments, 0, this.urlSegments, 1, urlSegments.length);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpEntity getEntity() {
        return entity;
    }

    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    public ParameterizedTypeReference<R> getParameterizedTypeReference() {
        return parameterizedTypeReference;
    }

    protected boolean isResponseParsable(R response) {
        return response != null;
    }

    protected String getURL(DigitalExchange digitalExchange) {
        return UriComponentsBuilder
                .fromHttpUrl(digitalExchange.getUrl())
                .pathSegment(urlSegments)
                .toUriString();
    }

    protected abstract R getEmptyRestResponse();

    protected abstract C combineResults(List<R> results);
}
