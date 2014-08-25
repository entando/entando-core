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
package com.agiletec.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.tags.util.IParameterParentTag;

/**
 * This tag can be used to parameterise other tags.<br/>
 * The parameter value can be added through the 'value' attribute or the body tag.
 * The parameters are added using the {@link IParameterParentTag#addParameter(String, String) addParamter} method.
 * When you declare the param tag, the value can be defined in either a value attribute or as text between the start and the ending of the tag.
 * @author E.Santoboni
 */
@SuppressWarnings("serial")
public class ParameterTag extends BodyTagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ParameterTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		try {
			String value = this.extractValue();
			if (null == value) {
				return super.doEndTag();
			}
			IParameterParentTag parentTag = (IParameterParentTag) findAncestorWithClass(this, IParameterParentTag.class);
			parentTag.addParameter(this.getName(), value);
		} catch (Throwable t) {
			_logger.error("Error closing tag", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Error closing tag ", t);
		}
		return super.doEndTag();
	}
	
	private String extractValue() {
		if (null != this.getValue()) {
			return this.getValue();
		} else {
			BodyContent body = this.getBodyContent();
			if (null != body) {
				return body.getString();
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void release() {
		this.setName(null);
		this.setValue(null);
	}
	
	/**
	 * Return the name of the parameter
	 * @return The name of the parameter
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Set the name of the parameter
	 * @param name The name of the parameter
	 */
	public void setName(String name) {
		this._name = name;
	}
	
	/**
	 * Return the value of the parameter.
	 * @return The value of the parameter.
	 */
	public String getValue() {
		return _value;
	}
	
	/**
	 * Set the value of the parameter.
	 * @param value The value of the parameter.
	 */
	public void setValue(String value) {
		this._value = value;
	}
	
	private String _name;
	private String _value;
	
}