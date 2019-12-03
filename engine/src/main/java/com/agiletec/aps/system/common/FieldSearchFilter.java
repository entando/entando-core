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
package com.agiletec.aps.system.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class implements a filter to search among database field.
 * @author E.Santoboni
 */
public class FieldSearchFilter<T> implements Serializable {
    
    public static final String ASC_ORDER = Order.ASC.toString();
    public static final String DESC_ORDER = Order.DESC.toString();

    public enum Order {
        ASC,
        DESC
    }

    public enum LikeOptionType {
        COMPLETE,
        RIGHT,
        LEFT
    }

    private String key;

    private Object value;

    private Order order;
    private Object start;
    private Object end;

    private boolean likeOption;
    private LikeOptionType likeOptionType;
    private boolean nullOption;
    private boolean notOption;

    private List<T> allowedValues;

    private Integer startDateDelay;
    private Integer endDateDelay;
    private Integer valueDateDelay;

    private Integer limit;
    private Integer offset;

    protected FieldSearchFilter() {}

    /**
     * Filter constructor for paginated results
     * @param limit
     * @param offset
     */
    public FieldSearchFilter(Integer limit, Integer offset) {
        this.setLimit(limit);
        this.setOffset(offset);
    }
    
    /**
     * Filter constructor.
     * This constructor is used when checking the presence of a value contained
     * either in the metadata.
     * @param key The key of the filtering element; it may be the ID of metadata.
     */
    public FieldSearchFilter(String key) {
        this.setKey(key);
    }
    
    /**
     * Filter constructor.
     * This constructor must be used to filter the attribute values or entity metadata
     * looking for a specific value.
     * @param key The key of the filtering element; it may be the ID of metadata.
     * @param value The value to look for.
     * @param useLikeOption When true the database search will be performed using the "LIKE" clause.
     * This option can be used to filter by the value of a string metadata. It can be
     * used only with string and with not null values.
     */
    public FieldSearchFilter(String key, Object value, boolean useLikeOption) {
        this(key);
        if (null != value && value instanceof Collection && ((Collection) value).size() > 0) {
            List<T> allowedValues = new ArrayList<T>();
            allowedValues.addAll((Collection) value);
            this.setAllowedValues(allowedValues);
            if (allowedValues.get(0) instanceof String) {
                this.setLikeOption(useLikeOption);          
            }
        } else {
            this.setValue(value);
            if (value instanceof String) {          
                this.setLikeOption(useLikeOption);
            }
        }
    }
    
    public FieldSearchFilter(String key, Object value, boolean useLikeOption, LikeOptionType likeOptionType) {
        this(key, value, useLikeOption);
        if (this.isLikeOption()) {
            this.setLikeOptionType(likeOptionType);
        }
    }
    
    /**
     * Filter constructor.
     * This constructor is used when filtering by a range of values.
     * @param key The key of the filtering element; it may be the ID of metadata.
     * @param start The starting value of the interval. It can be an object of type
     * "String", "Date", "BigDecimal", "Boolean" o null.
     * @param end The ending value of the interval. It can be an object of type 
     * "String", "Date", "BigDecimal", "Boolean" o null.
     */
    public FieldSearchFilter(String key, Object start, Object end) {
        this(key);
        if (start != null && end != null && !start.getClass().equals(end.getClass())) {
            throw new RuntimeException("Error: 'start' and 'end' types have to be equals");
        }
        this.setStart(start);
        this.setEnd(end);
    }
    
    /**
     * Filter constructor.
     * This constructor is used when filtering by a collection of allowed values.
     * @param key key The key of the filtering element; it may be the ID of metadata.
     * @param allowedValues The allowed values to look for. If null, the filter checks 
     * if the metadata has been valued.
     * @param useLikeOption When true the database search will be performed using the "LIKE" clause.
     * This option can be used to filter by the value of a string metadata. It can be
     * used only with string and with allowed values not null.
     */
    public FieldSearchFilter(String key, List<T> allowedValues, boolean useLikeOption) {
        this(key);
        this.setAllowedValues(allowedValues);
        if (null != allowedValues && allowedValues.size() > 0 && (allowedValues.get(0) instanceof String)) {
            this.setLikeOption(useLikeOption);          
        }
    }
    
    public FieldSearchFilter(String key, List<T> allowedValues, boolean useLikeOption, LikeOptionType likeOptionType) {
        this(key, allowedValues, useLikeOption);
        if (this.isLikeOption()) {
            this.setLikeOptionType(likeOptionType);
        }
    }
    
    public void setKey(String key) {
        if (null == key || key.trim().length() == 0) {
            throw new RuntimeException("Error: Key required");
        }
        this.key = key;
    }
    
    /**
     * Return the key of the filter object.
     * @return The key of the filter object.
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Return the value of the key of the entity to look for.
     * @return The value to look for.
     */
    public Object getValue() {
        return value;
    }
    protected void setValue(Object value) {
        this.value = value;
    }
    
    /**
     * Return the starting value of the interval. It can be an object of type
     * "String", "Date", "BigDecimal", "Boolean" o null.
     * @return The starting value of the interval.
     */
    public Object getStart() {
        return start;
    }
    protected void setStart(Object start) {
        this.start = start;
    }
    
    /**
     * Return the ending date of the interval. It can be an object of type
     * "String", "Date", "BigDecimal", "Boolean" o null.
     * @return L'end dell'intervallo;
     */
    public Object getEnd() {
        return end;
    }
    protected void setEnd(Object end) {
        this.end = end;
    }
    
    /**
     * Return the order to use to sort results found when the filter is applied.
     * @return The order used to sort results found applying the filter.
     */
    public Order getOrder() {
        return this.order;
    }
    
    /**
     * Set up the order to use to sort results found when the filter is applied. It can assume the values
     * "ASC" or "DESC" and depends on the field specified in the filter.
     * @param order The order used to sort results found applying the filter.
     */
    public void setOrder(String order) {
        if (null == order) {
            return;
        } else if (!(order.equals(ASC_ORDER) || order.equals(DESC_ORDER))) {
            throw new RuntimeException("Error: the 'order' clause cannot be null and must be comparable using the constants ASC_ORDER or DESC_ORDER");
        }
        this.order = Enum.valueOf(Order.class, order);
    }
    
    /**
     * Set up the order to use to sort results found when the filter is applied.
     * @param order The order used to sort results found applying the filter.
     */
    public void setOrder(Order order) {
        this.order = order;
    }
    
    /**
     * Check whether the string must be performed using the "LIKE" clause or not.
     * This option can be used to filter by a specific value in this attributes containing text strings.
     * @return True if the search process can have the "LIKE" clause enabled, false otherwise. 
     */
    public boolean isLikeOption() {
        return likeOption;
    }
    protected void setLikeOption(boolean likeOption) {
        if (likeOption && ((null == this.getValue() || !(this.getValue() instanceof String)) 
                    && (null == this.getAllowedValues() || this.getAllowedValues().isEmpty() || !(this.getAllowedValues().get(0) instanceof String)))) {
            throw new RuntimeException("Error: The 'like' clause cannot be applied on a null value or on a not string type");
        }
        this.likeOption = likeOption;
        if (likeOption) {
            this.setLikeOptionType(LikeOptionType.COMPLETE);
        }
    }
    
    public LikeOptionType getLikeOptionType() {
        return likeOptionType;
    }
    protected void setLikeOptionType(LikeOptionType likeOptionType) {
        if (!this.isLikeOption()) {
            throw new RuntimeException("Error: The 'like type' clause cannot be applied on a false value of 'like option'");
        }
        this.likeOptionType = likeOptionType;
    }
    
    public boolean isNullOption() {
        return nullOption;
    }
    
    public void setNullOption(boolean nullOption) {
        if (nullOption && (null != this.getValue() || null != this.getAllowedValues() || null != this.getStart() || null != this.getEnd())) {
            throw new RuntimeException("Error: the NULL clause may be used only in conjunction with null metadata fields");
        }
        this.nullOption = nullOption;
    }

    public boolean isNotOption() {
        return notOption;
    }

    public void setNotOption(boolean notOption) {
        if (notOption && (null == this.getValue() || null != this.getAllowedValues() || null != this.getStart() || null != this.getEnd())) {
            throw new RuntimeException("Error: the NOT clause must have a value");
        }
        this.notOption = notOption;
    }
    
    public List<T> getAllowedValues() {
        return allowedValues;
    }
    protected void setAllowedValues(List<T> allowedValues) {
        this.allowedValues = allowedValues;
    }
    
    public Integer getStartDateDelay() {
        return startDateDelay;
    }
    public void setStartDateDelay(Integer startDateDelay) {
        this.startDateDelay = startDateDelay;
    }

    public Integer getEndDateDelay() {
        return endDateDelay;
    }
    public void setEndDateDelay(Integer endDateDelay) {
        this.endDateDelay = endDateDelay;
    }

    public Integer getValueDateDelay() {
        return valueDateDelay;
    }
    public void setValueDateDelay(Integer valueDateDelay) {
        this.valueDateDelay = valueDateDelay;
    }
    
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "FieldSearchFilter [key=" + key + ", value=" + value + ", order=" + order + ", start=" + start + ", end=" + end + ", likeOption=" + likeOption + ", likeOptionType=" + likeOptionType + ", nullOption=" +
               nullOption + ", allowedValues=" + allowedValues + ", startDateDelay=" + startDateDelay + ", endDateDelay=" + endDateDelay + ", valueDateDelay=" + valueDateDelay + ", limit=" + limit + ", offset=" +
               offset + ", notOption=" + notOption + "]";
    }

}
