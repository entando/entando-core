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

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.*;

public class PagedMetadata<T> {

    private int page;
    private int pageSize;
    private int lastPage;
    private int totalItems;
    private String sort;
    private String direction;
    private Filter[] filters = new Filter[0];
    private Map<String, String> additionalParams = new HashMap<>();

    @JsonIgnore
    private int actualSize;

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
            this.actualSize = totalItems;
        } else {
            this.actualSize = req.getPageSize();
        }
        this.page = req.getPage();
        this.pageSize = req.getPageSize();
        Double pages = Math.ceil(new Double(totalItems) / new Double(this.actualSize));
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

    public PagedMetadata(RestListRequest req, List<T> body, int totalItems) {
        this(req, totalItems);
        this.setBody(body);
        if (null != req.getFilters()) {
            if (Arrays.stream(filters).anyMatch(filter -> filter.getAttribute() != null && filter.getAttribute().contains("."))) {
                this.applySubFilter(req, body);
            }
        }
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

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }

    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(Map<String, String> additionalParams) {
        this.additionalParams = additionalParams;
    }

    public void addAdditionalParams(String key, String value) {
        this.additionalParams.put(key, value);
    }

    public void imposeLimits() {
        // if the start of the requested paginated list is greater than the size of the entire list,
        // the returned payload is an empty array
        if (((this.getPage() - 1) * this.getActualSize()) > this.getBody().size()) {
            this.setBody(new ArrayList<>());
        } else {
            int start = ((this.getPage() - 1) * this.getActualSize());
            // if the end of the requested paginated list is greater than the size of the entire list,
            // the end is set on the size
            int end = (this.getPage() * this.getActualSize()) <= this.getBody().size() ? (this.getPage() * this.getActualSize())
                    : this.getBody().size();
            this.setBody(IntStream.range(start, end)
                    .mapToObj(this.getBody()::get)
                    .collect(Collectors.toList()));
        }
    }

    public void applySubFilter(RestListRequest req, List<T> list) {
        if (null != req.getFilters() && req.getFilters().length > 0) {
            List<Filter> dottedfilters = Arrays.stream(req.getFilters()).filter(filter -> (filter.getAttribute() != null
                    && filter.getAttribute().contains("."))).collect(Collectors.toList());
            this.setBody(list.stream().filter(elem -> isValidObj(dottedfilters, elem)).collect(Collectors.toList()));
        }
        if (0 == req.getPageSize()) {
            // no pagination
            this.actualSize = this.getBody().size();
        } else {
            this.actualSize = req.getPageSize();
        }
        this.totalItems = this.getBody().size();
        Double pages = Math.ceil(new Double(totalItems) / new Double(this.actualSize));
        this.lastPage = pages.intValue();
    }

    private boolean isValidObj(List<Filter> filters, Object obj) {
        return filters.stream().allMatch(filter -> isValidObj(filter, obj));
    }

    private boolean isValidObj(Filter filter, Object obj) {
        String fieldToScan = filter.getAttribute();
        Object value = this.getFieldValue(obj, fieldToScan);
        return value != null && value instanceof String && ((String) value).contains(filter.getValue());
    }

    private Object getFieldValue(Object bean, String fieldName) {
        Class< ?> componentClass = bean.getClass();
        Object value = bean;
        Map<String, Field> fields = new HashMap<>();
        fields = this.getAllFields(fields, componentClass);
        String[] nestedFields = StringUtils.split(fieldName, ".");
        try {
            for (String nested : nestedFields) {
                Field field = fields.get(nested);
                if (field == null) {
                    return null;
                }
                field.setAccessible(true);
                value = field.get(value);
                if (value != null) {
                    componentClass = value.getClass();
                    fields.clear();
                    fields = this.getAllFields(fields, componentClass);
                } else {
                    return null;
                }
            }
        } catch (IllegalAccessException iae) {
            throw new IllegalStateException(iae);
        }
        return value;
    }

    private Map<String, Field> getAllFields(Map<String, Field> fields, Class<?> type) {
        fields.putAll(Arrays.asList(type.getDeclaredFields()).stream()
                .collect(Collectors.toMap(field -> field.getName(), field -> field)));
        if (type.getSuperclass() != null) {
            return getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedMetadata<?> that = (PagedMetadata<?>) o;
        return page == that.page &&
               pageSize == that.pageSize &&
               lastPage == that.lastPage &&
               totalItems == that.totalItems &&
               actualSize == that.actualSize &&
               Objects.equals(sort, that.sort) &&
               Objects.equals(direction, that.direction) &&
               Arrays.equals(filters, that.filters) &&
               Objects.equals(additionalParams, that.additionalParams) &&
               Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(page, pageSize, lastPage, totalItems, sort, direction, additionalParams, actualSize, body);
        result = 31 * result + Arrays.hashCode(filters);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("page", page)
                .append("pageSize", pageSize)
                .append("lastPage", lastPage)
                .append("totalItems", totalItems)
                .append("sort", sort)
                .append("direction", direction)
                .append("filters", filters)
                .append("additionalParams", additionalParams)
                .append("actualSize", actualSize)
                .append("body", body)
                .toString();
    }
}
