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
package com.agiletec.apsadmin.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.agiletec.apsadmin.tags.util.ApsActionParamComponent;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * This "ApsActionParamTag" sub-tag provides the parameters to build the name of the action.
 * Parameters are retrieved in the same order they are inserted, assuming the form:<br />
 * &#60;ACTION_NAME&#62;?&#60;PARAM_NAME_1&#62;=&#60;PARAM_VALUE_1&#62;;&#60;PARAM_NAME_2&#62;=&#60;PARAM_VALUE_2&#62;;....;&#60;PARAM_NAME_N&#62;=&#60;PARAM_VALUE_N&#62;
 * <br />
 * @author E.Santoboni
 */
@SuppressWarnings("serial")
public class ApsActionParamSubTag extends ComponentTagSupport {
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse resp) {
		ApsActionParamTag parent = (ApsActionParamTag) findAncestorWithClass(this, ApsActionParamTag.class);
		return parent.getComponent();
	}

	protected void populateParams() {
		super.populateParams();
		ApsActionParamComponent component = (ApsActionParamComponent) this.getComponent();
		component.setParam(this.getName(), this.getValue());
	}
	
	/**
	 * Get the name of the parameter to add to the action name.
	 * @return the name of the parameter
	 */
	protected String getName() {
		return _name;
	}
	/**
	 * Set the name of the parameter to add to the action name.
	 * @param name the name of the parameter.
	 */
	public void setName(String name) {
		this._name = name;
	}
	
	/**
	 * Get the value of the parameter added.
	 * @return The value of the parameter added.
	 */
	public String getValue() {
		return _value;
	}
	
	/**
	 * Set the value of the parameter added
	 * @param value The value of the parameter added.
	 */
	public void setValue(String value) {
		this._value = value;
	}
	
	private String _name;
	private String _value;
	
}
