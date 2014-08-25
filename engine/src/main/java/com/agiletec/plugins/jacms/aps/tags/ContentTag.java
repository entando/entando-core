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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentViewerHelper;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;

/**
 * Displays the content given its ID.
 */
public class ContentTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ContentTag.class);
	
	public ContentTag() {
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IContentViewerHelper helper = (IContentViewerHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_VIEWER_HELPER, this.pageContext);
			String contentId = (this.getContentId() != null && this.getContentId().trim().length() > 0) ? this.getContentId() : null;
			ContentRenderizationInfo renderInfo = helper.getRenderizationInfo(contentId, this.getModelId(), this.isPublishExtraTitle(), reqCtx);
			String renderedContent = (null != renderInfo) ? renderInfo.getRenderedContent() : "";
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), renderedContent);
			} else {
				this.pageContext.getOut().print(renderedContent);
			}
			if (null != renderInfo && null != this.getAttributeValuesByRoleVar()) {
				this.pageContext.setAttribute(this.getAttributeValuesByRoleVar(), renderInfo.getAttributeValues());
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error detected while initialising the tag", t);
		}
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		this.setContentId(null);
		this.setModelId(null);
		this.setPublishExtraTitle(false);
		this.setVar(null);
		this.setAttributeValuesByRoleVar(null);
	}

	/**
	 * Return the content ID The "expression language" is accepted.
	 * @return
	 */
	public String getContentId() {
		return _contentId;
	}
	/**
	 * ID of the content to display. The "expression language" is accepted.
	 * @param id The content ID
	 */
	public void setContentId(String id) {
		this._contentId = id;
	}
	
	/**
	 * Get the Id of the model to use to display the content.
	 * @return The model ID
	 */
	public String getModelId() {
		return _modelId;
	}
	
	/**
	 * Set the Id of the model to use to display the content.
	 * @param id The model ID
	 */
	public void setModelId(String id) {
		this._modelId = id;
	}

	/**
	 * Return true if the extra titles will be insert into Request Context.
	 * @return The property.
	 */
	public boolean isPublishExtraTitle() {
		return _publishExtraTitle;
	}
	
	/**
	 * Specify if the extra titles will be insert into Request Context.
	 * @param publishExtraTitle The property to set.
	 */
	public void setPublishExtraTitle(boolean publishExtraTitle) {
		this._publishExtraTitle = publishExtraTitle;
	}
	
	/**
	 * Inserts the rendered content in a variable of the page context with the name provided
	 * @return The name of the variable
	 */
	public String getVar() {
		return _var;
	}
	
	/**
	 * Inserts the rendered content in a variable of the page context with the name provided
	 * @param var The name of the variable
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	/**
	 * Inserts the map of the attribute values indexed by the attribute role, 
	 * in a variable of the page context with the name provided.
	 * @return The name of the variable.
	 */
	public String getAttributeValuesByRoleVar() {
		return _attributeValuesByRoleVar;
	}
	
	/**
	 * Inserts the map of the attribute values indexed by the attribute role, 
	 * in a variable of the page context with the name provided.
	 * @param attributeValuesVar The name of the variable.
	 */
	public void setAttributeValuesByRoleVar(String attributeValuesByRoleVar) {
		this._attributeValuesByRoleVar = attributeValuesByRoleVar;
	}
	
	private String _contentId;
	private String _modelId;
	private boolean _publishExtraTitle;
	private String _var;
	private String _attributeValuesByRoleVar;
	
}