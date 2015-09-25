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

import javax.servlet.jsp.JspException;

/**
 * Rewriting of the Original IncludeTag from Struts 2 (version 2.0.11.1), needed for an extension 
 * of the attribute "value", to allow to use of the Expression Language.
 * 
 * @author E.Santoboni
 */
public class IncludeTag extends org.apache.struts2.views.jsp.IncludeTag {
	
	@Override
	public int doStartTag() throws JspException {
		this.getActualValue(this.value);
		return super.doStartTag();
	}
	
	private void getActualValue(String value) {
		if (value.startsWith("%{") && value.endsWith("}")) {
			value = value.substring(2, value.length() - 1);
			this.value = (String) getStack().findValue(value, String.class);
		}
	}
	
}
