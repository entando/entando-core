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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.plugin.PluginSubMenuContainer;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * This helper tag generates the menu of the plugins installed in the system.
 * Basically this looks for {@link PluginSubMenuContainer} objects type to create
 * the left menu containing all the plugins sub menus.
 * @author E.Santoboni
 */
public class PluginsSubMenuTag extends StrutsBodyTagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		WebApplicationContext wac = ApsWebApplicationUtils.getWebApplicationContext(request);
		List<PluginSubMenuContainer> containters = new ArrayList<PluginSubMenuContainer>();
		ValueStack stack = this.getStack();
		try {
			String[] beanNames =  wac.getBeanNamesForType(PluginSubMenuContainer.class);
			for (int i=0; i<beanNames.length; i++) {
				PluginSubMenuContainer container = (PluginSubMenuContainer) wac.getBean(beanNames[i]);
				containters.add(container);
			}
			if (containters.size()>0) {
				stack.getContext().put(this.getObjectName(), containters);
				stack.setValue("#attr['" + this.getObjectName() + "']", containters, false);
				return EVAL_BODY_INCLUDE;
			}
		} catch (Throwable t) {
			throw new JspException("Error creating the plugins menu list", t);
		}
		return super.doStartTag();
	}

	protected String getObjectName() {
		return _objectName;
	}
	public void setObjectName(String objectName) {
		this._objectName = objectName;
	}

	private String _objectName;

}
