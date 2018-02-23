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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class RestListRequest {

    public static final Integer PAGE_SIZE_DEFAULT = 25;

    private String sort;
    private String direction;

    private Integer pageNum = 0;
    private Integer pageSize = PAGE_SIZE_DEFAULT;

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
    public List<FieldSearchFilter> getFieldSearchFilters() {
        List<FieldSearchFilter> fieldSearchFilters = new ArrayList<>();

        if (null != filter && filter.length > 0) {
            Arrays.stream(filter).forEach(i -> fieldSearchFilters.add(i.getFieldSearchFilter()));
        }

        FieldSearchFilter pageFilter = this.getPaginationFilter();
        if (null != pageFilter) {
            fieldSearchFilters.add(pageFilter);
        }

        FieldSearchFilter sortFilter = this.getSortFilter();
        if (null != sortFilter) {
            fieldSearchFilters.add(sortFilter);
        }

        return fieldSearchFilters;
    }

    @SuppressWarnings("rawtypes")
    private FieldSearchFilter getPaginationFilter() {
        if (null != this.getPageSize() && this.getPageSize() > 0) {
            FieldSearchFilter pageFilter = new FieldSearchFilter(this.getPageSize(), this.getOffset());
            return pageFilter;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private FieldSearchFilter getSortFilter() {
        if (StringUtils.isNotBlank(StringEscapeUtils.escapeSql(this.getSort()))) {
            FieldSearchFilter sort = new FieldSearchFilter(this.getSort());
            if (StringUtils.isNotBlank(this.getDirection())) {
                sort.setOrder(FieldSearchFilter.Order.valueOf(StringEscapeUtils.escapeSql(this.getDirection())));
            }
            return sort;
        }
        return null;
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
