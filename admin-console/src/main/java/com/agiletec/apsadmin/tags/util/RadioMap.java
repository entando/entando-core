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
package com.agiletec.apsadmin.tags.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Form;
import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * This class implements the radio tag which inherits the basic functionality of the s:radio of Struts. But it does
 * not currently accept a list of values since it's conceived to be the base of other complex CMS attributes such as
 * threeStateAttribute and boleanAttribute. As for the 'hidden' attribute the ID is not auto-generated. 
 * @author M. Minnai
 */
public class RadioMap extends UIBean {

	public RadioMap(ValueStack stack, HttpServletRequest request, HttpServletResponse response, String checked, String var) {
		super(stack, request, response);
		if (null != var) {
			String value = (String) request.getAttribute(var).toString();
			this.setVar(value);
		}
		if (null != checked) this.setChecked(checked);
	}

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	@Override
	protected void evaluateExtraParams() {
		Object rtValue = null;
		// evaluate the value parameter and check if evaluates the value of the 'checkedVar' attribute
		if (null == this.getChecked()) {
			rtValue = findValue(this.value);
			if (null != rtValue && rtValue.toString().equals(this.getVar())) {
				addParameter("checked", "true");
			}
		}
		// evaluate the 'checked' attribute and then check if it is true.  
		else {
			rtValue = findValue(this.getChecked());
			if (null != this.getChecked() && null == rtValue) {
				rtValue = this.getChecked();
			}			
			if (rtValue.toString().equals("true")) {
				addParameter("checked", "true");				
			}
		}
	}

	/**
	 * If the ID is not provided, don't generate it!
	 */
	@Override
	protected void populateComponentHtmlId(Form form) {
		String tryId = null;
		if (this.getId() != null) {
			// this check is needed for backwards compatibility with 2.1.x
			tryId = this.findStringIfAltSyntax(this.getId());
		}
		this.addParameter("id", tryId);
		this.addParameter("escapedId", this.escape(tryId));
	}


	public void setChecked(String checked) {
		this._checked = checked;
	}

	public String getChecked() {
		return _checked;
	}


	public void setVar(String var) {
		this._var = var;
	}

	public String getVar() {
		return _var;
	}

	private String _checked = null;
	private String _var = null;
	
	/**
	 * The name of the default template for the TextFieldTag
	 */
	public static final String TEMPLATE = "radiomap";
}
