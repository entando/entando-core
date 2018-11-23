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
import java.util.Collections;
import java.util.List;

public class ErrorRestResponse extends RestResponse<List, List> {

    public ErrorRestResponse() {
        super(new ArrayList<>(), new ArrayList<>());
    }

    public ErrorRestResponse(List<RestError> errors) {
        super(new ArrayList<>(), new ArrayList<>(), errors);
    }

    public ErrorRestResponse(RestError error) {
        this(Collections.singletonList(error));
    }
}
