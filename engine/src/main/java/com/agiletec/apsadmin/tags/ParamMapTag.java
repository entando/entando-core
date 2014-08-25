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