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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.util.CheckFormatUtil;

/**
 * A user filter option of the list viewer showlet
 * @author E.Santoboni
 */
public class UserFilterOptionBean {

	private static final Logger _logger = LoggerFactory.getLogger(UserFilterOptionBean.class);
	
	public UserFilterOptionBean(Properties properties, IApsEntity prototype) throws Throwable {
		this.setKey(properties.getProperty(PARAM_KEY));
		if (null == this.getKey()) {
			throw new ApsSystemException("Null option key");
		}
		String isAttributeFilter = properties.getProperty(PARAM_IS_ATTRIBUTE_FILTER);
		this.setAttributeFilter(null != isAttributeFilter && isAttributeFilter.equalsIgnoreCase("true"));
		if (this.isAttributeFilter()) {
			this.setAttribute((AttributeInterface) prototype.getAttribute(this.getKey()));
			if (null == this.getAttribute()) {
				throw new ApsSystemException("Null attribute by key '" + this.getKey() + "'");
			}
		} else if (this.getKey().equals(KEY_CATEGORY)) {
			String catCode = properties.getProperty(PARAM_CATEGORY_CODE);
			if (null != catCode && catCode.trim().length() > 0) {
				this.setUserFilterCategoryCode(catCode);
			}
		} else if (!this.getKey().equals(KEY_FULLTEXT) && !this.getKey().equals(KEY_CATEGORY)) {
			throw new ApsSystemException("Invalid metadata key '" + this.getKey() + "'");
		}
	}

	public UserFilterOptionBean(Properties properties, IApsEntity prototype,
			Integer currentFrame, Lang currentLang, String dateFormat, HttpServletRequest request) throws Throwable {
		this(properties, prototype);
		this.setCurrentLang(currentLang);
		this.setCurrentFrame(currentFrame);
		this.setDateFormat(dateFormat);
		this.extractFormParameters(request);
	}

	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}

	public boolean isAttributeFilter() {
		return _attributeFilter;
	}
	public void setAttributeFilter(boolean attributeFilter) {
		this._attributeFilter = attributeFilter;
	}

	public void setAttribute(AttributeInterface attribute) {
		this._attribute = attribute;
	}
	public AttributeInterface getAttribute() {
		return _attribute;
	}

	public Integer getCurrentFrame() {
		return _currentFrame;
	}
	protected void setCurrentFrame(Integer currentFrame) {
		this._currentFrame = currentFrame;
	}

	protected Lang getCurrentLang() {
		return _currentLang;
	}
	protected void setCurrentLang(Lang currentLang) {
		this._currentLang = currentLang;
	}
	
	protected String getDateFormat() {
		return _dateFormat;
	}
	protected void setDateFormat(String dateFormat) {
		this._dateFormat = dateFormat;
	}
	
	protected void extractFormParameters(HttpServletRequest request) throws Throwable {
		String[] formFieldNames = null;
		try {
			String frameIdSuffix = (null != this.getCurrentFrame()) ? "_frame" + this.getCurrentFrame().toString() : "";
			if (!this.isAttributeFilter()) {
				formFieldNames = new String[1];
				String fieldName = null;
				if (this.getKey().equals(KEY_FULLTEXT)) {
					fieldName = TYPE_METADATA + "_fulltext" + frameIdSuffix;
				} else if (this.getKey().equals(KEY_CATEGORY)) {
					fieldName = TYPE_METADATA + "_category" + frameIdSuffix;
				}
				formFieldNames[0] = fieldName;
				String value = request.getParameter(fieldName);
				this.addFormValue(fieldName, value, formFieldNames.length);
			} else {
				AttributeInterface attribute = this.getAttribute();
				if (attribute instanceof ITextAttribute) {
					String[] fieldsSuffix = {"_textFieldName"};
					formFieldNames = this.extractAttributeParams(fieldsSuffix, frameIdSuffix, request);
				} else if (attribute instanceof DateAttribute) {
					String[] fieldsSuffix = {"_dateStartFieldName", "_dateEndFieldName"};
					formFieldNames = this.extractAttributeParams(fieldsSuffix, frameIdSuffix, request);
					this.checkRange(formFieldNames);
				} else if (attribute instanceof BooleanAttribute) {
					String[] fieldsSuffix = {"_booleanFieldName", "_booleanFieldName_ignore", "_booleanFieldName_control"};
					formFieldNames = this.extractAttributeParams(fieldsSuffix, frameIdSuffix, request);
				} else if (attribute instanceof NumberAttribute) {
					String[] fieldsSuffix = {"_numberStartFieldName", "_numberEndFieldName"};
					formFieldNames = this.extractAttributeParams(fieldsSuffix, frameIdSuffix, request);
					this.checkRange(formFieldNames);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting form parameters", t);
			//ApsSystemUtils.logThrowable(t, this, "extractFormParameters");
			throw new ApsSystemException("Error extracting form parameters", t);
		}
		this.setFormFieldNames(formFieldNames);
	}
	
	private void checkRange(String[] formFieldNames) {
		if (!this.isAttributeFilter() || null != this.getFormFieldErrors() ||
				null == this.getFormFieldValues() || this.getFormFieldValues().size() < 2) return;
		boolean check = false;
		if (this.getAttribute() instanceof DateAttribute) {
			Date start = DateConverter.parseDate(this.getFormFieldValues().get(formFieldNames[0]), this.getDateFormat());
			Date end = DateConverter.parseDate(this.getFormFieldValues().get(formFieldNames[1]), this.getDateFormat());
			check = (!start.equals(end) && start.after(end));
		} else if (this.getAttribute() instanceof NumberAttribute) {
			Integer start = Integer.parseInt(this.getFormFieldValues().get(formFieldNames[0]));
			Integer end = Integer.parseInt(this.getFormFieldValues().get(formFieldNames[1]));
			check = (!start.equals(end) && start.intValue() > end.intValue());
		}
		if (check) {
			this.setFormFieldErrors(new HashMap<String, AttributeFormFieldError>(2));
			AttributeFormFieldError error = new AttributeFormFieldError(this.getAttribute().getName(), formFieldNames[1], AttributeFormFieldError.INVALID_RANGE_KEY, null);
			this.getFormFieldErrors().put(formFieldNames[1], error);
		}
	}
	
	protected String[] extractAttributeParams(String[] fieldsSuffix, String frameIdSuffix, HttpServletRequest request) {
		String[] formFieldNames = new String[fieldsSuffix.length];
		for (int i = 0; i < fieldsSuffix.length; i++) {
			String fieldSuffix = fieldsSuffix[i];
			String fieldName = this.getAttribute().getName() + fieldSuffix + frameIdSuffix;
			formFieldNames[i] = fieldName;
			String value = request.getParameter(fieldName);
			this.addFormValue(fieldName, value, fieldsSuffix.length);
			String attributeType = this.getAttribute().getType();
			if (attributeType.equals("Date") || attributeType.equals("Number")) {
				boolean isDateAttribute = attributeType.equals("Date");
				String rangeField = (i==0) ? AttributeFormFieldError.FIELD_TYPE_RANGE_START : AttributeFormFieldError.FIELD_TYPE_RANGE_END;
				this.checkNoTextAttributeFormValue(isDateAttribute, value, fieldName, rangeField);
			}
		}
		return formFieldNames;
	}
	
	private void checkNoTextAttributeFormValue(boolean isDateAttribute, String value, String fieldName, String rangeField) {
		if (value == null || value.trim().length() == 0) {
			return;
		}
		boolean check = (isDateAttribute) ? 
				CheckFormatUtil.isValidDate(value.trim(), this.getDateFormat()) : 
				CheckFormatUtil.isValidNumber(value.trim());
		if (!check) {
			if (null == this.getFormFieldErrors()) {
				this.setFormFieldErrors(new HashMap<String, AttributeFormFieldError>(2));
			}
			AttributeFormFieldError error = new AttributeFormFieldError(this.getAttribute().getName(), fieldName, AttributeFormFieldError.INVALID_FORMAT_KEY, rangeField);
			this.getFormFieldErrors().put(fieldName, error);
		}
	}

	private void addFormValue(String key, String value, Integer formFields) {
		if (null != value && value.trim().length() > 0) {
			if (null == this.getFormFieldValues()) {
				this.setFormFieldValues(new HashMap<String, String>(formFields));
			}
			this.getFormFieldValues().put(key, value.trim());
		}
	}

	public EntitySearchFilter getEntityFilter() throws ApsSystemException {
		EntitySearchFilter filter = null;
		try {
			if (!this.isAttributeFilter() || null == this.getFormFieldValues()) {
				return null;
			}
			AttributeInterface attribute = this.getAttribute();
			if (attribute instanceof ITextAttribute) {
				String text = this.getFormFieldValues().get(this.getFormFieldNames()[0]);
				filter = new EntitySearchFilter(attribute.getName(), true, text, true);
				if (attribute.isMultilingual()) {
					filter.setLangCode(this.getCurrentLang().getCode());
				}
			} else if (attribute instanceof DateAttribute) {
				String start = this.getFormFieldValues().get(this.getFormFieldNames()[0]);
				String end = this.getFormFieldValues().get(this.getFormFieldNames()[1]);
				Date startDate = DateConverter.parseDate(start, this.getDateFormat());
				Date endDate = DateConverter.parseDate(end, this.getDateFormat());
				filter = new EntitySearchFilter(attribute.getName(), true, startDate, endDate);
			} else if (attribute instanceof BooleanAttribute) {
				String value = this.getFormFieldValues().get(this.getFormFieldNames()[0]);
				String ignore = this.getFormFieldValues().get(this.getFormFieldNames()[1]);
				if (null != ignore) {
					return null;
				} else if (null == value
						|| value.equals("both")) {//special option for three state Attribute
					filter = new EntitySearchFilter(attribute.getName(), true);
					filter.setNullOption(true);
				} else {
					filter = new EntitySearchFilter(attribute.getName(), true, value, false);
				}
			} else if (attribute instanceof NumberAttribute) {
				String start = this.getFormFieldValues().get(this.getFormFieldNames()[0]);
				String end = this.getFormFieldValues().get(this.getFormFieldNames()[1]);
				BigDecimal startNumber = null;
				try {
					Integer startNumberInt = Integer.parseInt(start);
					startNumber = new BigDecimal(startNumberInt);
				} catch (Throwable t) {}
				BigDecimal endNumber = null;
				try {
					Integer endNumberInt = Integer.parseInt(end);
					endNumber = new BigDecimal(endNumberInt);
				} catch (Throwable t) {}
				filter = new EntitySearchFilter(attribute.getName(), true, startNumber, endNumber);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting entity search filters", t);
			//ApsSystemUtils.logThrowable(t, this, "getFilter");
			throw new ApsSystemException("Error extracting entity search filters", t);
		}
		return filter;
	}
	
	public String getFormFieldValue(String formName) {
		String value = null;
		if (null != this.getFormFieldValues()) {
			value = this.getFormFieldValues().get(formName);
		}
		if (null != value) {
			return value;
		}
		return "";
	}

	public String[] getFormFieldNames() {
		return _formFieldNames;
	}
	public void setFormFieldNames(String[] formFieldNames) {
		this._formFieldNames = formFieldNames;
	}

	public Map<String, String> getFormFieldValues() {
		return _formFieldValues;
	}
	public void setFormFieldValues(Map<String, String> formFieldValues) {
		this._formFieldValues = formFieldValues;
	}

	public Map<String, AttributeFormFieldError> getFormFieldErrors() {
		return _formFieldErrors;
	}
	public void setFormFieldErrors(Map<String, AttributeFormFieldError> formFieldErrors) {
		this._formFieldErrors = formFieldErrors;
	}

	public String getUserFilterCategoryCode() {
		return _userFilterCategoryCode;
	}
	public void setUserFilterCategoryCode(String userFilterCategoryCode) {
		this._userFilterCategoryCode = userFilterCategoryCode;
	}

	private String _key; //'fulltext' || 'category' || a name of attribute
	private boolean _attributeFilter;
	private AttributeInterface _attribute;

	private Integer _currentFrame;
	private Lang _currentLang;
	
	private String _dateFormat;
	
	private String[] _formFieldNames;
	private Map<String, String> _formFieldValues;
	private Map<String, AttributeFormFieldError> _formFieldErrors;

	public static final String PARAM_KEY = "key";
	public static final String PARAM_IS_ATTRIBUTE_FILTER = "attributeFilter";

	public static final String TYPE_METADATA = "metadata";
	public static final String TYPE_ATTRIBUTE = "attribute";

	public static final String KEY_FULLTEXT = "fulltext";
	public static final String KEY_CATEGORY = "category";
	public static final String PARAM_CATEGORY_CODE = "categoryCode";

	private String _userFilterCategoryCode;

	public class AttributeFormFieldError {

		public AttributeFormFieldError(String attributeName, String fieldName, String errorKey, String rangeFieldType) {
			this._attributeName = attributeName;
			this._fieldName = fieldName;
			this._errorKey = errorKey;
			this._rangeFieldType = rangeFieldType;
		}

		public String getAttributeName() {
			return _attributeName;
		}
		public String getFieldName() {
			return _fieldName;
		}
		public String getErrorKey() {
			return _errorKey;
		}
		public String getRangeFieldType() {
			return _rangeFieldType;
		}
		
		private String _attributeName;
		private String _fieldName;
		private String _errorKey;
		private String _rangeFieldType;

		public static final String INVALID_FORMAT_KEY = "jacms_LIST_VIEWER_INVALID_FORMAT";
		public static final String INVALID_RANGE_KEY = "jacms_LIST_VIEWER_INVALID_RANGE";

		public static final String FIELD_TYPE_RANGE_START = "START";
		public static final String FIELD_TYPE_RANGE_END = "END";
		
	}
	
}