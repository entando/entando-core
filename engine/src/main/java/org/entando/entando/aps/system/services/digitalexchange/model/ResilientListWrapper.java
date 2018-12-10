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
package org.entando.entando.aps.system.services.digitalexchange.model;

import java.util.ArrayList;
import java.util.List;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.SimpleRestResponse;

/**
 * Wrapper for handling a sequence of responses with partial failure.
 */
public class ResilientListWrapper<T> {

    private final List<T> list;
    private final List<RestError> errors;

    public ResilientListWrapper() {
        list = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public void addValueFromResponse(SimpleRestResponse<T> response) {
        if (response.getErrors() != null && !response.getErrors().isEmpty()) {
            errors.addAll(response.getErrors());
        } else {
            list.add(response.getPayload());
        }
    }

    public List<T> getList() {
        return list;
    }

    public List<RestError> getErrors() {
        return errors;
    }
}
