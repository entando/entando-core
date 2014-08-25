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
import javax.servlet.jsp.JspException;

import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.common.IMyShortcutConfigAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.services.shortcut.IShortcutManager;
import com.agiletec.apsadmin.system.services.shortcut.model.UserConfigBean;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Returns the configured shortcuts (object {@link UserConfigBean}) of the current user.
 * @author E.Santoboni
 */
public class UserShortcutsConfigTag extends StrutsBodyTagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(UserShortcutsConfigTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		try {
			UserConfigBean config = (UserConfigBean) request.getSession().getAttribute(IMyShortcutConfigAction.SESSION_PARAM_MY_SHORTCUTS);
			UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null == config || !currentUser.getUsername().equals(config.getUsername())) {
				request.getSession().removeAttribute(IMyShortcutConfigAction.SESSION_PARAM_MY_SHORTCUTS);
				IShortcutManager shortcutManager = (IShortcutManager) ApsWebApplicationUtils.getBean(ApsAdminSystemConstants.SHORTCUT_MANAGER, this.pageContext);
				config = shortcutManager.getUserConfigBean(currentUser);
			}
			if (null != this.getVar()) {
				ValueStack stack = this.getStack();
				stack.getContext().put(this.getVar(), config);
	            stack.setValue("#attr['" + this.getVar() + "']", config, false);
			}
		} catch (Throwable t) {
			_logger.error("Error on doStartTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error on doStartTag", t);
		}
		return super.doEndTag();
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	private String _var;
	
}