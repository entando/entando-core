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
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientListWrapper;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

/**
 * Provides the logic for combining a set of SimpleRestResponse retrieved from
 * DE instances.
 */
public class SimpleDigitalExchangeCall<T> extends DigitalExchangeCall<SimpleRestResponse<T>, ResilientListWrapper<T>> {

    public SimpleDigitalExchangeCall(HttpMethod method,
            ParameterizedTypeReference<SimpleRestResponse<T>> parameterizedTypeReference, String... urlSegments) {
        super(method, parameterizedTypeReference, urlSegments);
    }

    @Override
    protected SimpleRestResponse<T> getEmptyRestResponse() {
        return new SimpleRestResponse<>(null);
    }

    @Override
    protected ResilientListWrapper<T> combineResults(List<SimpleRestResponse<T>> results) {
        ResilientListWrapper<T> wrapper = new ResilientListWrapper<>();
        results.forEach(wrapper::addValueFromResponse);
        return wrapper;
    }
}
