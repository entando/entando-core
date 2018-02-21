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
package org.entando.entando.web.common.model;

import java.util.Arrays;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class RestListRequest {

    private String sort;
    private String direction;

    private Integer pageNum = 0;
    private Integer pageSize = 5;

    private Filter[] filter;

    public Filter[] getFilter() {
        return filter;
    }

    public void setFilter(Filter[] filter) {
        this.filter = filter;
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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void addFilter(Filter filter) {
        this.filter = ArrayUtils.add(this.filter, filter);
    }

    @SuppressWarnings("rawtypes")
    public FieldSearchFilter[] getFieldSearchFilters() {
        FieldSearchFilter[] filters = new FieldSearchFilter[0];
        if (null != filter && filter.length > 0) {
            for (Filter filter : filter) {
                filters = ArrayUtils.add(filters, filter.getFieldSearchFilter());
            }
        }
        filters = addSortFilter(filters);
        filters = addPaginationFilter(filters);
        return filters;
    }

    @SuppressWarnings("rawtypes")
    private FieldSearchFilter[] addPaginationFilter(FieldSearchFilter[] filters) {
        if (null != this.getPageSize()) {
            FieldSearchFilter pageFilter = new FieldSearchFilter(this.getPageSize(), this.getOffset());
            filters = ArrayUtils.add(filters, pageFilter);
        }
        return filters;
    }

    @SuppressWarnings("rawtypes")
    private FieldSearchFilter[] addSortFilter(FieldSearchFilter[] filters) {
        if (StringUtils.isNotBlank(StringEscapeUtils.escapeSql(this.getSort()))) {
            FieldSearchFilter sort = new FieldSearchFilter(this.getSort());
            if (StringUtils.isNotBlank(this.getDirection())) {
                sort.setOrder(FieldSearchFilter.Order.valueOf(StringEscapeUtils.escapeSql(this.getDirection())));
            }
            filters = ArrayUtils.add(filters, sort);
        }
        return filters;
    }

    private Integer getOffset() {
        int page = this.getPageNum();
        if (null == this.getPageNum() || this.getPageNum() == 0) {
            return 0;
        }
        return this.getPageSize() * page;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + Arrays.hashCode(filter);
        result = prime * result + ((pageNum == null) ? 0 : pageNum.hashCode());
        result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RestListRequest other = (RestListRequest) obj;
        if (direction == null) {
            if (other.direction != null)
                return false;
        } else if (!direction.equals(other.direction))
            return false;
        if (!Arrays.equals(filter, other.filter))
            return false;
        if (pageNum == null) {
            if (other.pageNum != null)
                return false;
        } else if (!pageNum.equals(other.pageNum))
            return false;
        if (pageSize == null) {
            if (other.pageSize != null)
                return false;
        } else if (!pageSize.equals(other.pageSize))
            return false;
        if (sort == null) {
            if (other.sort != null)
                return false;
        } else if (!sort.equals(other.sort))
            return false;
        return true;
    }

}
