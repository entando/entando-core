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
package org.entando.entando.web.common.validator;

import com.agiletec.aps.system.SystemConstants;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author E.Santoboni
 */
public abstract class AbstractPaginationValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String ERRCODE_PAGE_INVALID = "110";
    public static final String ERRCODE_NO_ITEM_ON_PAGE = "111";
    public static final String ERRCODE_PAGE_SIZE_INVALID = "112";
    public static final String ERRCODE_SORTING_INVALID = "100";
    public static final String ERRCODE_FILTERING_ATTR_INVALID = "101";
    public static final String ERRCODE_DIRECTION_INVALID = "102";
    public static final String ERRCODE_FILTERING_OP_INVALID = "103";

    private static final String[] directions = {"ASC", "DESC"};

    public void validateRestListRequest(RestListRequest listRequest, Class<?> type) {
        this.checkDefaultSortField(listRequest);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(listRequest, "listRequest");
        if (listRequest.getPage() < 1) {
            bindingResult.reject(ERRCODE_PAGE_INVALID, new Object[]{}, "pagination.page.invalid");
            throw new ValidationGenericException(bindingResult);
        }
        if (listRequest.getPageSize() < 0) {
            bindingResult.reject(ERRCODE_PAGE_SIZE_INVALID, new Object[]{}, "pagination.page.size.invalid");
        } else if (listRequest.getPage() > 1 && listRequest.getPageSize() == 0) {
            bindingResult.reject(ERRCODE_NO_ITEM_ON_PAGE, new String[]{String.valueOf(listRequest.getPage())}, "pagination.item.empty");
            throw new ResourceNotFoundException(bindingResult);
        }
        if (null == type) {
            return;
        }
        if (!isValidField(listRequest.getSort(), type)) {
            bindingResult.reject(ERRCODE_SORTING_INVALID, new Object[]{listRequest.getSort()}, "sorting.sort.invalid");
        }
        if (listRequest.getFilters() != null) {
            List<String> attributes = Arrays.asList(listRequest.getFilters()).stream().filter(filter -> filter.getEntityAttr() == null)
                    .map(filter -> filter.getAttributeName()).filter(attr -> !isValidField(attr, type)).collect(Collectors.toList());
            if (attributes.size() > 0) {
                bindingResult.reject(ERRCODE_FILTERING_ATTR_INVALID, new Object[]{attributes.get(0)}, "filtering.filter.attr.name.invalid");
            }

            if (Arrays.stream(FilterOperator.values())
                    .map(FilterOperator::getValue)
                    .noneMatch(op -> Arrays.stream(listRequest.getFilters())
                    .map(Filter::getOperator)
                    .anyMatch(op::equals))) {
                bindingResult.reject(ERRCODE_FILTERING_OP_INVALID, new Object[]{}, "filtering.filter.operation.invalid");
            }

            Arrays.stream(listRequest.getFilters())
                    .filter(f -> getDateFilterKeys().contains(f.getAttribute()))
                    .forEach(f -> {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
                            sdf.parse(f.getValue());
                        } catch (ParseException ex) {
                            bindingResult.reject(ERRCODE_FILTERING_OP_INVALID, new Object[]{f.getValue(), f.getAttribute()}, "filtering.filter.date.format.invalid");
                        }
                    });
        }
        if (!Arrays.asList(directions).contains(listRequest.getDirection())) {
            bindingResult.reject(ERRCODE_DIRECTION_INVALID, new Object[]{}, "sorting.direction.invalid");
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

    }

    protected List<String> getDateFilterKeys() {
        return new ArrayList<>();
    }

    public void validateRestListResult(RestListRequest listRequest, PagedMetadata<?> result) {
        if (listRequest.getPage() > 1 && (null == result.getBody() || result.getBody().isEmpty())) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(listRequest, "listRequest");
            bindingResult.reject(ERRCODE_NO_ITEM_ON_PAGE, new String[]{String.valueOf(listRequest.getPage())}, "pagination.item.empty");
            throw new ResourceNotFoundException(bindingResult);
        }
    }

    public boolean isValidField(String fieldName, Class<?> type) {
        Map<String, Field> fields = new HashMap<>();
        fields = getAllFields(fields, type);
        if (fieldName.contains(".")) {
            String fieldClass = fieldName.substring(0, fieldName.indexOf("."));
            if (fields.keySet().contains(fieldClass)) {
                String subFields = fieldName.substring(fieldName.indexOf(".") + 1);
                Class subType = null;
                try {
                    subType = fields.get(fieldClass).getType();
                    return isValidField(subFields, subType);
                } catch (Exception e) {
                    logger.error("Failed to validate field ", e);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return fields.keySet().contains(fieldName);
        }
    }

    private Map<String, Field> getAllFields(Map<String, Field> fields, Class<?> type) {
        fields.putAll(Arrays.asList(type.getDeclaredFields()).stream()
                .collect(Collectors.toMap(field -> field.getName(), field -> field)));
        if (type.getSuperclass() != null) {
            return getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    private void checkDefaultSortField(RestListRequest listRequest) {
        if (listRequest.getSort().equals(RestListRequest.SORT_VALUE_DEFAULT)) {
            listRequest.setSort(this.getDefaultSortProperty());
        }
    }

    protected String getDefaultSortProperty() {
        return RestListRequest.SORT_VALUE_DEFAULT;
    }

}
