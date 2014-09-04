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

import com.agiletec.apsadmin.util.ApsRequestParamsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Form;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author E.Santoboni
 */
public class Submit extends org.apache.struts2.components.Submit {
	
	public Submit(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
    }
	
	@Override
	public void evaluateExtraParams() {
		super.evaluateExtraParams();
		if (align == null) {
			align = "right";
		}
		String submitType = BUTTONTYPE_INPUT;
		if (type != null && (BUTTONTYPE_BUTTON.equalsIgnoreCase(type) || (supportsImageType() && BUTTONTYPE_IMAGE.equalsIgnoreCase(type)))) {
			submitType = type;
		}
		this.addParameter("type", submitType);
		if (!BUTTONTYPE_INPUT.equals(submitType) && (label == null)) {
			this.addParameter("label", getParameters().get("nameValue"));
		}
		if (action != null || method != null) {
			String name;
			if (action != null) {
				ActionMapping mapping = new ActionMapping();
				mapping.setName(findString(action));
				if (method != null) {
					mapping.setMethod(findString(method));
				}
				mapping.setExtension("");
				name = ApsRequestParamsUtil.ENTANDO_ACTION_PREFIX + actionMapper.getUriFromActionMapping(mapping);
			} else {
				name = "method:" + findString(method);
			}
			addParameter("name", name);
		}
		addParameter("align", findString(align));
	}
	
	@Override
	protected void populateComponentHtmlId(Form form) {
        String _tmp_id = "";
        if (id != null) {
            // this check is needed for backwards compatibility with 2.1.x
        	_tmp_id = super.findStringIfAltSyntax(id);
        }
		if (!StringUtils.isEmpty(_tmp_id)) {
			this.addParameter("id", _tmp_id);
		}
    }
	
	static final String BUTTONTYPE_INPUT = "input";
    static final String BUTTONTYPE_BUTTON = "button";
    static final String BUTTONTYPE_IMAGE = "image";
	
}
