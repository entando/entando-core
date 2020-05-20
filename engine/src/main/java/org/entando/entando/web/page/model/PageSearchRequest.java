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
package org.entando.entando.web.page.model;

import org.entando.entando.web.common.model.RestListRequest;

// TODO can we change its name in something like CodeEntitySearchRequest in order to generify?
/**
 *
 * @author paddeo
 */
public class PageSearchRequest extends RestListRequest {

    private String pageCodeToken;


    public PageSearchRequest() {
        super();
    }

    public PageSearchRequest(String pageCodeToken) {
        super();
        this.pageCodeToken = pageCodeToken;
    }

    public String getPageCodeToken() {
        return pageCodeToken;
    }

    public void setPageCodeToken(String pageCodeToken) {
        this.pageCodeToken = pageCodeToken;
    }

    @Override
    public String toString() {
        return "RestListRequest{" + "sort=" + this.getSort() + ", direction=" + this.getDirection() + ", page=" + this.getPage() + ", pageSize=" + this.getPageSize() + ", pageCodeToken=" + pageCodeToken + '}';
    }
}
