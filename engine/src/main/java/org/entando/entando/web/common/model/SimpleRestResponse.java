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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a response that needs only a payload.
 * 
 * NOTE: Using Map<Object, Object> is necessary for Swagger.
 */
public class SimpleRestResponse<T> extends RestResponse<T, Map<Object, Object>> {

    protected SimpleRestResponse() {
        super();
    }
    
    public SimpleRestResponse(T payload) {
        super(payload, new HashMap<>());
    }

    public SimpleRestResponse(T payload, List<RestError> errors) {
        super(payload, new HashMap<>(), errors);
    }
}
