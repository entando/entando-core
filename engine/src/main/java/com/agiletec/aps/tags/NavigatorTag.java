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
package com.agiletec.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.page.widget.INavigatorParser;
import com.agiletec.aps.system.services.page.widget.NavigatorTarget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Iterating tag that generates a list containing a set of pages
 * @author M.Diana - E.Santoboni
 */
public class NavigatorTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(NavigatorTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			this._reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			INavigatorParser navigatorParser = (INavigatorParser) ApsWebApplicationUtils.getBean(SystemConstants.NAVIGATOR_PARSER, this.pageContext); 
			IPage currPage = (IPage) this._reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (this._spec == null) {
				Widget currShowlet =  (Widget) _reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
				ApsProperties showletConfig = currShowlet.getConfig();
				String spec = (null != showletConfig) ? showletConfig.getProperty(CONFIG_PARAM_SPEC) : null;
				this._targets = navigatorParser.parseSpec(spec, currPage, currentUser);
			} else {
				this._targets = navigatorParser.parseSpec(this._spec, currPage, currentUser);
			}
			if (this._targets == null || this._targets.isEmpty()) {
				return SKIP_BODY;
			}
			this._index = 0;
			NavigatorTarget currentTarget = this.getCurrentTarget();
			this.pageContext.setAttribute(this.getVar(), currentTarget);
			return EVAL_BODY_INCLUDE;
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization ", t);
		}
	}
	
	@Override
	public int doAfterBody() throws JspException {
		this._index++;
		if (this._index >= _targets.size()) {
			return SKIP_BODY;
		} else {
			NavigatorTarget currentTarget = this.getCurrentTarget();
			this.pageContext.setAttribute(this.getVar(), currentTarget);
			return EVAL_BODY_AGAIN;
		}
	}
	
	private NavigatorTarget getCurrentTarget() {
		NavigatorTarget item = this._targets.get(this._index);
		item.setRequestContext(this._reqCtx);
		return item;
	}
	
	@Override
	public int doEndTag() throws JspException {
		this.pageContext.removeAttribute(this.getVar());
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		this._spec = null;
		this._targets = null;
		this._reqCtx = null;
		this._var = null;
	}
	
	/**
	 * Return the attribute that specifies the subset of pages.
	 * @return The specification of the subset.
	 */
	public String getSpec() {
		return _spec;
	}
	
	/**
	 * Set the attribute that specifies the subset of pages.
	 * @param spec The specification of the subset.
	 */
	public void setSpec(String spec) {
		this._spec = spec;
	}
	
	/**
	 * Return the name of the page context variable where the data of target
	 * being iterated are made available
	 * @return The name of the variable containing the current element
	 */
	public String getVar() {
		return _var;
	}
	
	/**
	 * Set the name of the page context variable where the data of target
	 * being iterated are made available
	 * @param var The name of the variable containing the current element
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	public static final String CONFIG_PARAM_SPEC = "navSpec";
	
	private String _spec;
	private List<NavigatorTarget> _targets;
	private int _index;
	private String _var;
	private RequestContext _reqCtx;
	
}