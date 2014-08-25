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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.plugin.HookPointElementContainer;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Defines a hookpoint, a point of inclusion of objects -that is, jsp fragments - in
 * well defined points in the JSP of the administration interface.
 * @author E.Santoboni
 */
public class HookPointTag extends StrutsBodyTagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		ValueStack stack = this.getStack();
		try {
			List<HookPointElementContainer> containers = extractElements(request);
			if (containers.size()>0) {
				stack.getContext().put(this.getObjectName(), containers);
				stack.setValue("#attr['" + this.getObjectName() + "']", containers, false);
				return EVAL_BODY_INCLUDE;
			}
		} catch (Throwable t) {
			throw new JspException("Error detected ", t);
		}
		return super.doStartTag();
	}

	private List<HookPointElementContainer> extractElements(HttpServletRequest request) {
		WebApplicationContext wac = ApsWebApplicationUtils.getWebApplicationContext(request);
		String[] beanNames =  wac.getBeanNamesForType(HookPointElementContainer.class);
		List<HookPointElementContainer> containers = new ArrayList<HookPointElementContainer>();
		for (int i=0; i<beanNames.length; i++) {
			HookPointElementContainer container = (HookPointElementContainer) wac.getBean(beanNames[i]);
			if (null != container && null != container.getHookPointKey() && container.getHookPointKey().equals(this.getKey())) {
				containers.add(container);
			}
		}
		BeanComparator comparator = new BeanComparator("priority");
		Collections.sort(containers, comparator);
		return containers;
	}
	
	/**
	 * Get the jack to include
	 * @return The bean handling the object to include.
	 */
	protected String getObjectName() {
		return _objectName;
	}
	/**
	 * Set the jack to include.
	 * @param objectName The bean handling the object to include.
	 */
	public void setObjectName(String objectName) {
		this._objectName = objectName;
	}

	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}

	private String _objectName;
	private String _key;

}
