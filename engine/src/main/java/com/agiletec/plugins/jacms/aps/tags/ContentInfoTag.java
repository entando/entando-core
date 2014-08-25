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
package com.agiletec.plugins.jacms.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentViewerHelper;

/**
 * Return an information of a specified content.
 * The content can will be extracted by id from showlet parameters or from request parameter.
 * The tag extract any specific parameter (by "param" attribute) 
 * or entire {@link ContentAuthorizationInfo} object (setting "var" attribute and anything on "param" attribute).
 * Admitted values for "param" attribute are:<br/>
 * "contentId" returns the code of content id,
 * "mainGroup" returns the code main (owner) group,
 * "authToEdit" returns true if the current user can edit the content (else false).
 * @author E.Santoboni
 */
public class ContentInfoTag extends OutSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ContentInfoTag.class);
	
	public ContentInfoTag() {
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IContentViewerHelper helper = (IContentViewerHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_VIEWER_HELPER, this.pageContext);
			PublicContentAuthorizationInfo authInfo = helper.getAuthorizationInfo(this.getContentId(), reqCtx);
			if (null == authInfo) {
				return super.doStartTag();
			}
			if (null == this.getParam() && null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), authInfo);
			} else if (null != this.getParam()) {
				Object value = null;
				if ("contentId".equals(this.getParam())) {
					value = authInfo.getContentId();
				} else if ("modelId".equals(this.getParam())) {
					value = this.extractModelId(authInfo, reqCtx);
				} else if ("mainGroup".equals(this.getParam())) {
					value = authInfo.getMainGroup();
				} else if ("authToEdit".equals(this.getParam())) {
					IContentAuthorizationHelper contentAuthHelper = (IContentAuthorizationHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_AUTHORIZATION_HELPER, this.pageContext);
					HttpSession session = this.pageContext.getSession();
					UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
					boolean isAuth = contentAuthHelper.isAuthToEdit(currentUser, authInfo);
					value = Boolean.valueOf(isAuth);
				}
				if (null != value) {
					String var = this.getVar();
					if (null == var || "".equals(var)) {
						this.pageContext.getOut().print(value);
					} else {
						this.pageContext.setAttribute(this.getVar(), value);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error detected while initializing the tag", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error detected while initializing the tag", t);
		}
		return super.doStartTag();
	}
	
	private Object extractModelId(PublicContentAuthorizationInfo authInfo, RequestContext reqCtx) {
		Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
        ApsProperties showletConfig = widget.getConfig();
		String modelId = (String) showletConfig.get("modelId");
		if (null == modelId) {
			modelId = reqCtx.getRequest().getParameter("modelId");
		}
		if (null == modelId) {
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.pageContext);
			modelId = contentManager.getDefaultModel(authInfo.getContentId());
		}
		return modelId;
	}
	
	@Override
	public void release() {
		this.setContentId(null);
		this.setParam(null);
		this.setVar(null);
	}

	/**
	 * Return the content ID.
	 * @return The content id.
	 */
	public String getContentId() {
		return _contentId;
	}
	
	/**
	 * ID of the content to display.
	 * @param id The content id.
	 */
	public void setContentId(String id) {
		this._contentId = id;
	}
	
	public String getParam() {
		return _param;
	}
	public void setParam(String param) {
		this._param = param;
	}
	
	/**
	 * Inserts the required parameter (or the entire authorization info object) 
	 * in a variable of the page context with the name provided.
	 * @return The name of the variable.
	 */
	public String getVar() {
		return _var;
	}
	
	/**
	 * Inserts the required parameter (or the entire authorization info object) 
	 * in a variable of the page context with the name provided.
	 * @param var The name of the variable.
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	private String _contentId;
	private String _param;
	private String _var;
	
}