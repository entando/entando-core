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

/**
 * PagedMetadata for handling responses with partial failure.
 */
public class ResilientPagedMetadata<T> extends PagedMetadata<T> {

    private List<RestError> errors;

    public ResilientPagedMetadata() {
        super();
    }
    
    public ResilientPagedMetadata(RestListRequest req, List<T> body, int totalItems) {
        super(req, body, totalItems);
    }

    public List<RestError> getErrors() {
        return errors;
    }

    public void setErrors(List<RestError> errors) {
        this.errors = errors;
    }

    public void addError(RestError restError) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(restError);
    }
}
