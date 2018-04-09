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

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PagedMetadata<T> {

    private int page;
    private int pageSize;
    private int lastPage;
    private int totalItems;
    private String sort;
    private String direction;
    private Filter[] filters = new Filter[0];

    @JsonIgnore
    private List<T> body;

    public PagedMetadata() {
    }

    public PagedMetadata(RestListRequest req, SearcherDaoPaginatedResult<?> result) {
        this(req, result.getCount());
    }

    public PagedMetadata(RestListRequest req, Integer totalItems) {
        if (0 == req.getPageSize()) {
            // no pagination
            req.setPageSize(totalItems);
        }
        this.page = req.getPage();
        this.pageSize = req.getPageSize();
        Double pages = Math.ceil(new Double(totalItems) / new Double(req.getPageSize()));
        this.lastPage = pages.intValue();
        this.totalItems = totalItems;
        this.setSort(req.getSort());
        this.setDirection(req.getDirection());
        if (null != req.getFilters()) {
            this.setFilters(req.getFilters());
        }
    }

    public PagedMetadata(int page, int size, int last, int totalItems) {
        this.page = page;
        this.pageSize = size;
        this.lastPage = last;
        this.totalItems = totalItems;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int last) {
        this.lastPage = last;
    }

    public List<T> getBody() {
        return body;
    }

    public void setBody(List<T> body) {
        this.body = body;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Filter[] getFilters() {
        return filters;
    }

    public void setFilters(Filter[] filters) {
        this.filters = filters;
    }

}
