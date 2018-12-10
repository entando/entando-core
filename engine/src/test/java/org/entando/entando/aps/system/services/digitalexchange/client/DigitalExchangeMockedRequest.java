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

    public HttpEntity getEntity() {
        return entity;
    }

    public DigitalExchangeMockedRequest setEntity(HttpEntity entity) {
        this.entity = entity;
        return this;
    }
}
