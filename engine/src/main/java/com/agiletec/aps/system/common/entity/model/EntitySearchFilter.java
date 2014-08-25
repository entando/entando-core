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
package com.agiletec.aps.system.common.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

/**
 * This class implements a filter to search among entities.
 * @author E.Santoboni
 */
public class EntitySearchFilter<T> extends FieldSearchFilter implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(EntitySearchFilter.class);
	
	protected EntitySearchFilter() {}
	
	/**
	 * Filter constructor.
	 * This constructor is used when checking the presence of a value contained
	 * either in the attribute field or in the entity metadata.
	 * @param key The key of the filtering element; it may be a content attribute of type {@link AttributeInterface}
	 * or the ID of metadata).
	 * @param isAttributeFilter Decide whether the filter is applied to an Entity Attribute (true) or
	 * to a metadata (false).
	 */
	public EntitySearchFilter(String key, boolean isAttributeFilter) {
		super(key);
		this.setAttributeFilter(isAttributeFilter);
	}
	
	/**
	 * Filter constructor.
	 * This constructor must be used to filter the attribute values or entity metadata
	 * looking for a specific value.
	 * @param key The key of the filtering element; it may be a content attribute of type {@link AttributeInterface}
	 * or the ID of metadata).
	 * @param isAttributeFilter Decide whether the filter is applied to an Entity Attribute (true) or
	 * to a metadata (false).
	 * @param value The value to look for. If null, the filter checks if the attribute (or metadata)
	 * has been valued.
	 * @param useLikeOption When true the database search will be performed using the "LIKE" clause.
	 * This option can be used to filter by the value of a string attribute (or metadata). It can be
	 * used only with string and with not null values.
	 */
	public EntitySearchFilter(String key, boolean isAttributeFilter, Object value, boolean useLikeOption) {
		this(key, isAttributeFilter);
		if (null != value && value instanceof Collection && ((Collection) value).size() > 0) {
			List<Object> allowedValues = new ArrayList<Object>();
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
	
	public EntitySearchFilter(String key, boolean isAttributeFilter, Object value, boolean useLikeOption, LikeOptionType likeOptionType) {
		this(key, isAttributeFilter, value, useLikeOption);
		if (this.isLikeOption()) {
			this.setLikeOptionType(likeOptionType);
		}
	}
	
	/**
	 * Filter constructor.
	 * This constructor is used when filtering by a range of values; this can applied to both
	 * Entity Attributes and metadata).
	 * @param key The key of the filtering element; it may be a content attribute of type {@link AttributeInterface}
	 * or the ID of metadata).
	 * @param isAttributeFilter Decide whether the filter is applied to an Entity Attribute (true) or
	 * to a metadata (false).
	 * @param start The starting value of the interval. It can be an object of type
	 * "String", "Date", "BigDecimal", "Boolean" o null.
	 * @param end The ending value of the interval. It can be an object of type 
	 * "String", "Date", "BigDecimal", "Boolean" o null.
	 */
	public EntitySearchFilter(String key, boolean isAttributeFilter, Object start, Object end) {
		this(key, isAttributeFilter);
		if (start != null && end != null && !start.getClass().equals(end.getClass())) {
			throw new RuntimeException("Error: 'start' and 'end' types have to be equals");
		}
		this.setStart(start);
		this.setEnd(end);
	}
	
	/**
	 * Filter constructor.
	 * This constructor is used when filtering by a collection of allowed values; this can applied to both
	 * Entity Attributes and metadata).
	 * @param key The key of the filtering element; it may be a content attribute of type {@link AttributeInterface}
	 * or the ID of metadata).
	 * @param isAttributeFilter Decide whether the filter is applied to an Entity Attribute (true) or
	 * to a metadata (false).
	 * @param allowedValues The allowed values to look for. If null, the filter checks 
	 * if the attribute (or metadata) has been valued.
	 * @param useLikeOption When true the database search will be performed using the "LIKE" clause.
	 * This option can be used to filter by the value of a string attribute (or metadata). It can be
	 * used only with string and with allowed values not null.
	 */
	public EntitySearchFilter(String key, boolean isAttributeFilter, List<T> allowedValues, boolean useLikeOption) {
		this(key, isAttributeFilter);
		this.setAllowedValues(allowedValues);
		this.setLikeOption(useLikeOption);
	}
	
	public EntitySearchFilter(String key, boolean isAttributeFilter, List<T> allowedValues, boolean useLikeOption, LikeOptionType likeOptionType) {
		this(key, isAttributeFilter, allowedValues, useLikeOption);
		if (this.isLikeOption()) {
			this.setLikeOptionType(likeOptionType);
		}
	}
	
	public static EntitySearchFilter createRoleFilter(String roleName) {
		EntitySearchFilter filter = new EntitySearchFilter();
		filter.setAttributeFilter(true);
		filter.setRoleName(roleName);
		return filter;
	}
	
	public static <T> EntitySearchFilter createRoleFilter(String roleName, Object value, boolean useLikeOption) {
		EntitySearchFilter filter = new EntitySearchFilter();
		filter.setAttributeFilter(true);
		filter.setRoleName(roleName);
		if (null != value && value instanceof Collection && ((Collection) value).size() > 0) {
			List<T> allowedValues = new ArrayList<T>();
			allowedValues.addAll((Collection) value);
			filter.setAllowedValues(allowedValues);
			if (allowedValues.get(0) instanceof String) {
				filter.setLikeOption(useLikeOption);			
			}
		} else {
			filter.setValue(value);
			if (value instanceof String) {			
				filter.setLikeOption(useLikeOption);
			}
		}
		return filter;
	}
	
	public static <T> EntitySearchFilter createRoleFilter(String roleName, Object value, boolean useLikeOption, LikeOptionType likeOptionType) {
		EntitySearchFilter filter = EntitySearchFilter.createRoleFilter(roleName, value, useLikeOption);
		if (filter.isLikeOption()) {
			filter.setLikeOptionType(likeOptionType);
		}
		return filter;
	}
	
	public static EntitySearchFilter createRoleFilter(String roleName, Object start, Object end) {
		EntitySearchFilter filter = new EntitySearchFilter();
		filter.setAttributeFilter(true);
		filter.setRoleName(roleName);
		if (start != null && end != null && !start.getClass().equals(end.getClass())) {
			throw new RuntimeException("Error: 'start' and 'end' types have to be equals");
		}
		filter.setStart(start);
		filter.setEnd(end);
		return filter;
	}
	
	public static <T> EntitySearchFilter createRoleFilter(String roleName, List<T> allowedValues, boolean useLikeOption) {
		EntitySearchFilter filter = new EntitySearchFilter();
		filter.setAttributeFilter(true);
		filter.setRoleName(roleName);
		filter.setAllowedValues(allowedValues);
		filter.setLikeOption(useLikeOption);
		return filter;
	}
	
	public static <T> EntitySearchFilter createRoleFilter(String roleName, List<T> allowedValues, boolean useLikeOption, LikeOptionType likeOptionType) {
		EntitySearchFilter filter = EntitySearchFilter.createRoleFilter(roleName, allowedValues, useLikeOption);
		if (filter.isLikeOption()) {
			filter.setLikeOptionType(likeOptionType);
		}
		return filter;
	}
	
	/**
	 * This method shows if the filter must be applied on a Entity Attribute or
	 * a metadata.
	 * @return true if the filter is to be applied to an attribute entity or a 
	 * to a metadata of the an entity.
	 */
	public boolean isAttributeFilter() {
		return _attributeFilter;
	}
	protected void setAttributeFilter(boolean attributeFilter) {
		this._attributeFilter = attributeFilter;
	}
	
	public String getRoleName() {
		return _roleName;
	}
	protected void setRoleName(String roleName) {
		this._roleName = roleName;
	}
	
	public String getLangCode() {
		return this._langCode;
	}
	
	public void setLangCode(String langCode) {
		if (null == langCode) {
			return;
		}
		if (!this.isAttributeFilter()) {
			throw new RuntimeException("Error: The language can be only specified on attribute filters");
		}
		if ((null != this.getValue() && !(this.getValue() instanceof String)) 
				|| (null != this.getStart() &&  !(this.getStart() instanceof String)) 
				|| (null != this.getEnd() &&  !(this.getEnd() instanceof String)) 
				|| (this.getAllowedValues() != null && !this.getAllowedValues().isEmpty() && !(this.getAllowedValues().get(0) instanceof String))) {
			throw new RuntimeException("Error: The language can be only specified on a null value of type string");
		}
		this._langCode = langCode;
	}
	
	@Override
	public void setNullOption(boolean nullOption) {
		if (null == this.getKey() && null != this.getRoleName()) {
			throw new RuntimeException("Error: the NULL cluase may be used only in conjunction with not null key");
		}
		super.setNullOption(nullOption);
	}
	
	@Override
	public String toString() {
		StringBuffer param = new StringBuffer();
		if (null != this.getKey()) {
			param.append(KEY_PARAM).append("=").append(this.getKey()).append(SEPARATOR);
		}
		this.appendParamValue(param, this.getRoleName(), ROLE_PARAM);
		param.append(FILTER_TYPE_PARAM).append("=").append(Boolean.toString(this.isAttributeFilter()));
		this.appendParamValue(param, this.getValue(), VALUE_PARAM);
		if (this.getValue() instanceof String) {
			this.appendParamValue(param, this.isLikeOption(), LIKE_OPTION_PARAM);
		}
		if (null != this.getAllowedValues() && !this.getAllowedValues().isEmpty()) {
			this.appendParamValue(param, this.getAllowedValues(), ALLOWED_VALUES_PARAM);
		}
		this.appendParamValue(param, this.getLangCode(), LANG_PARAM);
		this.appendParamValue(param, this.getStart(), START_PARAM);
		this.appendParamValue(param, this.getEnd(), END_PARAM);
		this.appendParamValue(param, this.getOrder(), ORDER_PARAM);
		if (null != this.getValueDateDelay()){
			this.appendParamValue(param, this.getValueDateDelay(), VALUE_DATE_DELAY_PARAM);
		}
		if (null != this.getStartDateDelay()){
			this.appendParamValue(param, this.getStartDateDelay(), START_DATE_DELAY_PARAM);
		}
		if (null != this.getEndDateDelay()){
			this.appendParamValue(param, this.getEndDateDelay(), END_DATE_DELAY_PARAM);
		}
		if (this.isNullOption()) {
			this.appendParamValue(param, this.isNullOption(), NULL_VALUE_PARAM);
		}
		return param.toString();
	}
	
	private void appendParamValue(StringBuffer param, Object value, String paramValue) {
		if (null != value) {
			param.append(SEPARATOR).append(paramValue).append("=");
			if (value instanceof List) {
				List<Object> values = (List<Object>) value;
				for (int i = 0; i < values.size() ; i++) {
					if (i>0) {
						param.append(ALLOWED_VALUES_SEPARATOR);
					}
					param.append(values.get(i));
				}
			} else {
				param.append(this.getToStringValue(value));
			}
		}
	}
	
	private String getToStringValue(Object value) {
		if (value instanceof String) {
			return value.toString();
		} else if (value instanceof Date) {
			return DateConverter.getFormattedDate((Date)value, DATE_PATTERN);
		} else if (value instanceof BigDecimal) {
			value = ((BigDecimal)value).toString();
		} else if (value instanceof Boolean) {
			value = ((Boolean)value).toString();
		}
		return value.toString();
	}
	
	@Deprecated
	public static EntitySearchFilter getInstance(IApsEntity prototype, String toStringFilter) {
		return getInstance(prototype, getProperties(toStringFilter));
	}
	
	@Deprecated
	public static Properties getProperties(String toStringFilter) {
		Properties props = new Properties();
		String[] params = toStringFilter.split(SEPARATOR);
		for (int i=0; i<params.length; i++) {
			String[] elements = params[i].split("=");
			if (elements.length != 2) {
				continue;
			}
			props.setProperty(elements[0], elements[1]);
		}
		return props;
	}
	
	public static EntitySearchFilter getInstance(IApsEntity prototype, Properties props) {
		EntitySearchFilter filter = null;
		try {
			String key = props.getProperty(KEY_PARAM);
			String roleName = props.getProperty(ROLE_PARAM);
			if (null == key && null == roleName) {
				return null;
			}
			boolean isAttributeFilter = Boolean.parseBoolean(props.getProperty(FILTER_TYPE_PARAM));
			filter = new EntitySearchFilter();
			boolean isDateAttribute = false;
            if (!isAttributeFilter) {
				filter.setKey(key);
				String dataType = props.getProperty(DATA_TYPE_PARAM);
				if (null == dataType) {
					dataType = DATA_TYPE_STRING;
				}
				setValues(filter, props, dataType);
			} else {
				AttributeInterface attr = null;
				if (null != key) {
					attr = (AttributeInterface) prototype.getAttribute(key);
					filter.setKey(key);
				} else {
					attr = (AttributeInterface) prototype.getAttributeByRole(roleName);
					filter.setRoleName(roleName);
				}
				filter.setAttributeFilter(true);
				if (null != attr && null != prototype) {
					String dataType = null;
					if (attr instanceof DateAttribute) {
						dataType = DATA_TYPE_DATE;
                        isDateAttribute = true;
					} else if (attr instanceof ITextAttribute || attr instanceof BooleanAttribute) {
						dataType = DATA_TYPE_STRING;
					} else if (attr instanceof NumberAttribute) {
						dataType = DATA_TYPE_NUMBER;
					}
					setValues(filter, props, dataType);
				} else throw new ApsSystemException("ERROR: Entity type '" + prototype.getTypeCode() 
						+ "' and attribute '" + key + "' not recognized");
			}
			if (isDateAttribute) {
				String valueDateDelay = props.getProperty(EntitySearchFilter.VALUE_DATE_DELAY_PARAM);
				filter.setValueDateDelay(null != valueDateDelay ? Integer.valueOf(valueDateDelay) : null);
				
				String endDateDelay = props.getProperty(EntitySearchFilter.END_DATE_DELAY_PARAM);
				filter.setEndDateDelay(null != endDateDelay ? Integer.valueOf(endDateDelay) : null);
				
				String startDateDelay = props.getProperty(EntitySearchFilter.START_DATE_DELAY_PARAM);
				filter.setStartDateDelay(null != startDateDelay ? Integer.valueOf(startDateDelay) : null);
			}
			String order = props.getProperty(EntitySearchFilter.ORDER_PARAM);
			filter.setOrder(order);
		} catch (Throwable t) {
			_logger.error("Error on creation of filter instance", t);
			//ApsSystemUtils.logThrowable(t, EntitySearchFilter.class, "Error on creation of filter instance");
			throw new RuntimeException("Error on creation of filter instance", t);
		}
		return filter;
	}
	
	private static void setValues(EntitySearchFilter filter, Properties props, String dataType) {
		if (null == dataType) {
			return;
		}
		String value = props.getProperty(VALUE_PARAM);
		String allowedValues = props.getProperty(ALLOWED_VALUES_PARAM);
		String start = props.getProperty(START_PARAM);
		String end = props.getProperty(END_PARAM);
		Object objectValue = getDataObject(value, dataType);
		List<Object> objectAllowedValues = buildAllowedValues(allowedValues, dataType);
		Object objectStart = getDataObject(start, dataType);
		Object objectEnd = getDataObject(end, dataType);
		String likeOptionString = props.getProperty(LIKE_OPTION_PARAM);
		boolean likeOption = (null != likeOptionString) ? Boolean.parseBoolean(likeOptionString) : false;
		String likeOptionTypeString = props.getProperty(LIKE_OPTION_TYPE_PARAM);
		LikeOptionType likeOptionType = LikeOptionType.COMPLETE;
		if (null != likeOptionTypeString) {
			try {
				likeOptionType = Enum.valueOf(LikeOptionType.class, likeOptionTypeString.trim().toUpperCase());
			} catch (Throwable t) {
				_logger.error("Error parsing 'like option type' parameter", t);
				//ApsSystemUtils.logThrowable(t, EntitySearchFilter.class, "setValues", "Error parsing 'like option type' parameter");
			}
		}
		if (objectValue != null) {
			filter.setValue(objectValue);
			filter.setLikeOption(likeOption);
			if (filter.isLikeOption()) {
				filter.setLikeOptionType(likeOptionType);
			}
		} else if (objectAllowedValues != null) {
			filter.setAllowedValues(objectAllowedValues);
			filter.setLikeOption(likeOption);
			if (filter.isLikeOption()) {
				filter.setLikeOptionType(likeOptionType);
			}
		} else if ((null != objectStart) || (null != objectEnd)) {
			filter.setStart(objectStart);
			filter.setEnd(objectEnd);
		} else {
			String nullValue = props.getProperty(NULL_VALUE_PARAM);
			boolean nullOption = (null != nullValue && nullValue.equalsIgnoreCase("true"));
			filter.setNullOption(nullOption);
		}
		String langCode = props.getProperty(LANG_PARAM);
		filter.setLangCode(langCode);
	}
	
	private static List<Object> buildAllowedValues(String allowedValues, String dataType) {
		if (null == allowedValues) {
			return null;
		}
		List<Object> values = null;
		String[] stringValues = allowedValues.split(ALLOWED_VALUES_SEPARATOR);
		if (null != stringValues && stringValues.length > 0) {
			values = new ArrayList<Object>();
			for (int i = 0; i < stringValues.length; i++) {
				String stringValue = stringValues[i];
				Object object = getDataObject(stringValue, dataType);
				if (null != object) {
					values.add(object);
				}
			}
		}
		if (null == values || values.isEmpty()) {
			return null;
		}
		return values;
	}
	
	private static Object getDataObject(String stringValue, String dataType) {
		if (null == stringValue) {
			return null;
		}
		Object object = null;
		if (dataType.equals(DATA_TYPE_DATE)) {
			object = buildDate(stringValue);
		} else if (dataType.equals(DATA_TYPE_STRING)) {
			object = stringValue;
		} else if (dataType.equals(DATA_TYPE_NUMBER)) {
			object = buildNumber(stringValue);
		}
		return object;
	}
	
	private static Date buildDate(String dateString) {
		String today = "today, oggi, odierna";
		Date data = null;
		try {
			if (today.indexOf(dateString) != -1) {
				data = new java.util.Date();
			} else {
				SimpleDateFormat dataF = new SimpleDateFormat(EntitySearchFilter.DATE_PATTERN);
				data = dataF.parse(dateString);
			}
		} catch (ParseException ex) {
			_logger.error("Invalid string - '{}'", dateString);
		}
		return data;
	}
	
	private static BigDecimal buildNumber(String numberString) {
		BigDecimal number = null;
		try {
			number = new BigDecimal(numberString);
		} catch (NumberFormatException e) {
			_logger.error("Invalid string - '{}'", numberString);
		}
		return number;
	}
	
	private boolean _attributeFilter;
	
	private String _langCode;
	private String _roleName;
	
	public static final String SEPARATOR = ";";
	public static final String KEY_PARAM = "key";
	public static final String ROLE_PARAM = "role";
	public static final String FILTER_TYPE_PARAM = "attributeFilter";
	public static final String VALUE_PARAM = "value";
	public static final String ALLOWED_VALUES_PARAM = "allowedValues";
	public static final String ALLOWED_VALUES_SEPARATOR = ",";
	public static final String LIKE_OPTION_PARAM = "likeOption";
	public static final String LIKE_OPTION_TYPE_PARAM = "likeOptionType";
	public static final String LANG_PARAM = "lang";
	public static final String START_PARAM = "start";
	public static final String END_PARAM = "end";
	public static final String ORDER_PARAM = "order";
	public static final String DATA_TYPE_PARAM = "dataType";
    public static final String NULL_VALUE_PARAM = "nullValue";
	
	public static final String DATA_TYPE_STRING = "string";
	public static final String DATA_TYPE_DATE = "date";
	public static final String DATA_TYPE_NUMBER = "number";
	
	public static final String DATE_PATTERN = "dd/MM/yyyy";
	
	public static final String START_DATE_DELAY_PARAM = "startDateDelay";
	public static final String END_DATE_DELAY_PARAM = "endDateDelay";
	public static final String VALUE_DATE_DELAY_PARAM = "valueDateDelay";
	
}
