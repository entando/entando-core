/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.user.model;

import org.entando.entando.web.common.model.RestListRequest;

/**
 *
 * @author paddeo
 */
public class UserListRequest extends RestListRequest {

    public static final String USERS_SORT_VALUE_DEFAULT = "username";

    private String sort = USERS_SORT_VALUE_DEFAULT;

    @Override
    public String getSort() {
        return sort;
    }

    @Override
    public void setSort(String sort) {
        this.sort = sort;
    }

}
