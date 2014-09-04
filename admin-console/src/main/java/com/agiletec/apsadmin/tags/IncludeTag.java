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
