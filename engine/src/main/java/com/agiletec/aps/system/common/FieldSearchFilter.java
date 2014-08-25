/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
	
	protected FieldSearchFilter() {}
	
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
	
	protected void setKey(String key) {
		if (null == key || key.trim().length() == 0) {
			throw new RuntimeException("Error: Key required");
		}
		this._key = key;
	}
	
	/**
	 * Return the key of the filter object.
	 * @return The key of the filter object.
	 */
	public String getKey() {
		return _key;
	}
	
	/**
	 * Return the value of the key of the entity to look for.
	 * @return The value to look for.
	 */
	public Object getValue() {
		return _value;
	}
	protected void setValue(Object value) {
		this._value = value;
	}
	
	/**
	 * Return the starting value of the interval. It can be an object of type
	 * "String", "Date", "BigDecimal", "Boolean" o null.
	 * @return The starting value of the interval.
	 */
	public Object getStart() {
		return _start;
	}
	protected void setStart(Object start) {
		this._start = start;
	}
	
	/**
	 * Return the ending date of the interval. It can be an object of type
	 * "String", "Date", "BigDecimal", "Boolean" o null.
	 * @return L'end dell'intervallo;
	 */
	public Object getEnd() {
		return _end;
	}
	protected void setEnd(Object end) {
		this._end = end;
	}
	
	/**
	 * Return the order to use to sort results found when the filter is applied.
	 * @return The order used to sort results found applying the filter.
	 */
	public Order getOrder() {
		return this._order;
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
		this._order = Enum.valueOf(Order.class, order);
	}
	
	/**
	 * Set up the order to use to sort results found when the filter is applied.
	 * @param order The order used to sort results found applying the filter.
	 */
	public void setOrder(Order order) {
		this._order = order;
	}
	
	/**
	 * Check whether the string must be performed using the "LIKE" clause or not.
	 * This option can be used to filter by a specific value in this attributes containing text strings.
	 * @return True if the search process can have the "LIKE" clause enabled, false otherwise. 
	 */
	public boolean isLikeOption() {
		return _likeOption;
	}
	protected void setLikeOption(boolean likeOption) {
		if (likeOption && ((null == this.getValue() || !(this.getValue() instanceof String)) 
					&& (null == this.getAllowedValues() || this.getAllowedValues().isEmpty() || !(this.getAllowedValues().get(0) instanceof String)))) {
			throw new RuntimeException("Error: The 'like' clause cannot be applied on a null value or on a not string type");
		}
		this._likeOption = likeOption;
		if (likeOption) {
			this.setLikeOptionType(LikeOptionType.COMPLETE);
		}
	}
	
	public LikeOptionType getLikeOptionType() {
		return _likeOptionType;
	}
	protected void setLikeOptionType(LikeOptionType likeOptionType) {
		if (!this.isLikeOption()) {
			throw new RuntimeException("Error: The 'like type' clause cannot be applied on a false value of 'like option'");
		}
		this._likeOptionType = likeOptionType;
	}
	
	public boolean isNullOption() {
		return _nullOption;
	}
	
	public void setNullOption(boolean nullOption) {
		if (nullOption && (null != this.getValue() || null != this.getAllowedValues() || null != this.getStart() || null != this.getEnd())) {
			throw new RuntimeException("Error: the NULL cluase may be used only in conjunction with null metadata fields");
		}
		this._nullOption = nullOption;
	}
	
	public List<T> getAllowedValues() {
		return _allowedValues;
	}
	protected void setAllowedValues(List<T> allowedValues) {
		this._allowedValues = allowedValues;
	}
	
	public Integer getStartDateDelay() {
		return _startDateDelay;
	}
	public void setStartDateDelay(Integer startDateDelay) {
		this._startDateDelay = startDateDelay;
	}

	public Integer getEndDateDelay() {
		return _endDateDelay;
	}
	public void setEndDateDelay(Integer endDateDelay) {
		this._endDateDelay = endDateDelay;
	}

	public Integer getValueDateDelay() {
		return _valueDateDelay;
	}
	public void setValueDateDelay(Integer valueDateDelay) {
		this._valueDateDelay = valueDateDelay;
	}
	
	private String _key;
	
	private Object _value;
	
	private Order _order;
	private Object _start;
	private Object _end;
	
	private boolean _likeOption;
	private LikeOptionType _likeOptionType;
	private boolean _nullOption;
	
	private List<T> _allowedValues;
	
	private Integer _startDateDelay;
	private Integer _endDateDelay;
	private Integer _valueDateDelay;
	
	public static final String ASC_ORDER = Order.ASC.toString();
	public static final String DESC_ORDER = Order.DESC.toString();
	
	public enum Order {ASC, DESC}
	
	public enum LikeOptionType {COMPLETE, RIGHT, LEFT}
	
}