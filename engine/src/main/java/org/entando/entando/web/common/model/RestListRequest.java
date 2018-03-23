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
import java.util.Arrays;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class RestListRequest {

    public static final Integer PAGE_SIZE_DEFAULT = 100;
    public static final String SORT_VALUE_DEFAULT = "code";
    public static final String DIRECTION_VALUE_DEFAULT = FieldSearchFilter.ASC_ORDER;

    private String sort = SORT_VALUE_DEFAULT;

    private String direction = DIRECTION_VALUE_DEFAULT;

    private Integer page = 1;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
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
    public List<FieldSearchFilter> buildFieldSearchFilters() {
        List<FieldSearchFilter> fieldSearchFilters = new ArrayList<>();

        if (null != filter && filter.length > 0) {
            Arrays.stream(filter).forEach(i -> fieldSearchFilters.add(i.getFieldSearchFilter()));
        }

        FieldSearchFilter pageFilter = this.buildPaginationFilter();
        if (null != pageFilter) {
            fieldSearchFilters.add(pageFilter);
        }

        FieldSearchFilter sortFilter = this.buildSortFilter();
        if (null != sortFilter) {
            fieldSearchFilters.add(sortFilter);
        }

        return fieldSearchFilters;
    }

    @SuppressWarnings("rawtypes")
    private FieldSearchFilter buildPaginationFilter() {
        if (null != this.getPageSize() && this.getPageSize() > 0) {
            FieldSearchFilter pageFilter = new FieldSearchFilter(this.getPageSize(), this.getOffset());
            return pageFilter;
        }
        return null;
    }

    public List getSublist(List master) {
        if (null == master) {
            return null;
        }
        FieldSearchFilter pagFilter = this.buildPaginationFilter();
        int limit = (null != pagFilter) ? pagFilter.getLimit() : PAGE_SIZE_DEFAULT;
        if (null == pagFilter) {
            this.setPageSize(PAGE_SIZE_DEFAULT);
        }
        int offset = (null != pagFilter) ? pagFilter.getOffset() : this.getOffset();
        int size = master.size();
        int offsetToApply = (offset >= size) ? size : offset;
        int limitToApply = ((offsetToApply + limit) > size) ? size : (offsetToApply + limit);
        return master.subList(offsetToApply, limitToApply);
    }

    @SuppressWarnings("rawtypes")
    private FieldSearchFilter buildSortFilter() {
        if (StringUtils.isNotBlank(StringEscapeUtils.escapeSql(this.getSort()))) {
            FieldSearchFilter sort = new FieldSearchFilter(this.getSort());

            if (StringUtils.isNotBlank(this.getDirection())) {
                if (!this.getDirection().equalsIgnoreCase(FieldSearchFilter.ASC_ORDER) && !this.getDirection().equalsIgnoreCase(FieldSearchFilter.DESC_ORDER)) {
                    this.setDirection(DIRECTION_VALUE_DEFAULT);
                }
                sort.setOrder(FieldSearchFilter.Order.valueOf(StringEscapeUtils.escapeSql(this.getDirection())));
            }
            return sort;
        }
        return null;
    }

    private Integer getOffset() {
        int page = this.getPage() - 1;
        if (null == this.getPage() || this.getPage() == 0) {
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
        result = prime * result + ((page == null) ? 0 : page.hashCode());
        result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RestListRequest other = (RestListRequest) obj;
        if (direction == null) {
            if (other.direction != null) {
                return false;
            }
        } else if (!direction.equals(other.direction)) {
            return false;
        }
        if (!Arrays.equals(filter, other.filter)) {
            return false;
        }
        if (page == null) {
            if (other.page != null) {
                return false;
            }
        } else if (!page.equals(other.page)) {
            return false;
        }
        if (pageSize == null) {
            if (other.pageSize != null) {
                return false;
            }
        } else if (!pageSize.equals(other.pageSize)) {
            return false;
        }
        if (sort == null) {
            if (other.sort != null) {
                return false;
            }
        } else if (!sort.equals(other.sort)) {
            return false;
        }
        return true;
    }

}
