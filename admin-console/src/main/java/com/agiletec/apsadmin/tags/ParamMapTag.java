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
package com.agiletec.apsadmin.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.agiletec.apsadmin.tags.util.ParamMap;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Tag used to parameterize other tags with a map of parameters.
 * @author E.Santoboni
 */
public class ParamMapTag extends ComponentTagSupport {

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new ParamMap(stack);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		ParamMap param = (ParamMap) this.component;
		param.setMap(this.getMap());
	}

	protected String getMap() {
		return _map;
	}
	public void setMap(String map) {
		this._map = map;
	}

	private String _map;

}