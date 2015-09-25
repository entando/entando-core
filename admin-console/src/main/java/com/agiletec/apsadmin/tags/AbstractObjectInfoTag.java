/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.tags;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;

import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * Abstract class for tags that return concrete objects by key, or one her property value.
 * @author E.Santoboni
 */
public abstract class AbstractObjectInfoTag extends StrutsBodyTagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractObjectInfoTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			String keyValue = (String) super.findValue(this.getKey(), String.class);
			Object masterObject = this.getMasterObject(keyValue);
			if (null == masterObject ) {
				_logger.debug(this.getNullMasterObjectLogMessage(keyValue));
				return super.doStartTag();
			}
			String propertyValue = (null != this.getProperty()) ? (String) super.findValue(this.getProperty(), String.class) : null;
			Object requiredObject = (null != propertyValue) ? this.getPropertyValue(masterObject, propertyValue) : masterObject;
			if (null == requiredObject) {
				_logger.debug(this.getNullObjectLogMessage(keyValue, propertyValue));
				return super.doStartTag();
			}
			if (null != this.getVar()) {
				ValueStack stack = this.getStack();
				stack.getContext().put(this.getVar(), requiredObject);
	            stack.setValue("#attr['" + this.getVar() + "']", requiredObject, false);
			} else {
				this.pageContext.getOut().print(requiredObject);
			}
		} catch (Throwable t) {
			_logger.error("Error on doStartTag", t);
			throw new JspException("Error on doStartTag", t);
		}
        return super.doStartTag();
    }
	
	protected String getNullMasterObjectLogMessage(String keyValue) {
		return "Null master object : key '" + keyValue + "'";
	}
	
	protected String getNullObjectLogMessage(String keyValue, String propertyValue) {
		return "Null required object property : key '" + keyValue + "' - property '" + propertyValue + "'";
	}
	
	protected Object getPropertyValue(Object masterObject, String propertyValue) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(masterObject.getClass());
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < descriptors.length; i++) {
				PropertyDescriptor descriptor = descriptors[i];
				if (!descriptor.getName().equals(propertyValue)) {
					continue;
				}
				Method method = descriptor.getReadMethod();
				Object[] args = null;
				return method.invoke(masterObject, args);
			}
				_logger.debug("Invalid required object property : Master Object '{}' - property '{}'", masterObject.getClass().getName(), propertyValue);
		} catch (Throwable t) {
			_logger.error("Error extracting property value : Master Object {} - property: '{}'", masterObject.getClass().getName(), propertyValue, t);
		}
		return null;
	}
    
	/**
	 * Return the required master object.
	 * @param keyValue The unique key of the master object
	 * @return The required master object.
	 * @throws Throwable In case of error.
	 */
	protected abstract Object getMasterObject(String keyValue) throws Throwable;
	
	protected String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	/**
	 * Get the name of the property to return.
	 * @return The required property.
	 */
	protected String getProperty() {
		return _property;
	}
	
	/**
	 * Set the name of the property to return. It can be one of those indicated in the declaration of the tag.
	 * @param property The required property. 
	 */
	public void setProperty(String property) {
		this._property = property;
	}
	
	/**
	 * Return the name used to reference the required object (or one of its property) pushed into the Value Stack.
	 * @return The name used to reference the required object.
	 */
	protected String getVar() {
		return _var;
	}
	
	/**
	 * Set the name used to reference the required object (or one of its property) pushed into the Value Stack.
	 * @param var The name of the variable
	 */
	public void setVar(String var) {
		this._var = var;
	}

	private String _key;
	private String _property;
	private String _var;

}