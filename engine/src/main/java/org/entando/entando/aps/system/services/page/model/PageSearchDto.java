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
package org.entando.entando.aps.system.services.page.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.page.model.PageSearchRequest;

public class PageSearchDto extends PagedMetadata<PageDto> {

    private String pageCodeToken;

    @JsonIgnore
    private Filter[] filters;

    public String getPageCodeToken() {
        return pageCodeToken;
    }

    public void setPageCodeToken(String pageCodeToken) {
        this.pageCodeToken = pageCodeToken;
    }

    public PageSearchDto() {
    }

    public PageSearchDto(PageSearchRequest req, List<PageDto> body) {
        if (0 == req.getPageSize()) {
            // no pagination
            this.setActualSize(body.size());
        } else {
            this.setActualSize(req.getPageSize());
        }
        this.setPage(req.getPage());
        this.setPageSize(req.getPageSize());
        Double pages = Math.ceil(new Double(body.size()) / new Double(this.getActualSize()));
        this.setLastPage(pages.intValue());
        this.setTotalItems(body.size());
        this.setSort(req.getSort());
        this.setDirection(req.getDirection());
        this.setFilters(req.getFilters());
        this.setBody(body);
        this.setPageCodeToken(req.getPageCodeToken());
    }

}
