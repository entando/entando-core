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
package org.entando.entando.web.common.model;

import java.util.ArrayList;
import java.util.List;

public class RestResponse<T, M> {

    private T payload;
    private M metaData;
    private List<RestError> errors;

    protected RestResponse() {
        errors = new ArrayList<>();
    }

    public RestResponse(T payload, M metadata) {
        this(payload, metadata, new ArrayList<>());
    }

    public RestResponse(T payload, M metadata, List<RestError> errors) {
        this.payload = payload;
        this.metaData = metadata;

        if (errors != null) {
            this.errors = errors;
        } else {
            this.errors = new ArrayList<>();
        }
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public M getMetaData() {
        return metaData;
    }

    public void setMetaData(M metaData) {
        this.metaData = metaData;
    }

    public List<RestError> getErrors() {
        return errors;
    }

    public void setErrors(List<RestError> errors) {
        this.errors = errors;
    }

    public void addErrors(List<RestError> errors) {
        this.errors.addAll(errors);
    }
    
    public void addError(RestError error) {
        this.errors.add(error);
    }
}
