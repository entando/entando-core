/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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