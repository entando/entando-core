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
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Print the style of the back-office. Can be normal|advanced.
 * @deprecated Unnecessary for UX Backoffice of entando 4.0.0
 * @author E.Santoboni
 */
public class BackendGuiClientTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(BackendGuiClientTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		try {
			String BACKEND_GUI_CLIENT_NORMAL = "normal";
			String client = BACKEND_GUI_CLIENT_NORMAL;
			if (null != this.getVar() && this.getVar().trim().length() > 0) {
				this.pageContext.getRequest().setAttribute(this.getVar(), client);
			} else {
				pageContext.getOut().print(client);
			}
		} catch (Throwable t) {
			_logger.error("Error on ClientTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Error on ClientTag", t);
		}
		return super.doEndTag();
	}
	
	/**
	 * Set the name used to reference the value of the gui client code pushed into the Value Stack.
	 * @return The name of the variable
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	/**
	 * Get the name used to reference the value of the gui client code pushed into the Value Stack.
	 * @return The name of the variable
	 */
	public String getVar() {
		return _var;
	}
	
	private String _var;
	
}