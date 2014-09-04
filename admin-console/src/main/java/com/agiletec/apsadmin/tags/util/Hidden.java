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
 * This class substitutes the Struts hidden tag, by changing the behaviour of the ID generation method. 
 * @author M.Minnai
 */
public class Hidden extends UIBean {
	
	public Hidden(ValueStack stack, HttpServletRequest request,	HttpServletResponse response) {
		super(stack, request, response);
	}
	
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
	
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}
	
	public static final String TEMPLATE = "hidden";
	
}