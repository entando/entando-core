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
package org.entando.entando.plugins.jacms.apsadmin.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentViewerHelper;
import com.agiletec.plugins.jacms.aps.tags.ContentTag;

/**
 * This returns the content ready for the preview functionality
 * This tags comes from the ContentTag class used in the front-end to render a content.
 * @author E.Santoboni
 */
public class ContentPreviewTag extends ContentTag {
	
	private static final Logger _logger = LoggerFactory.getLogger(ContentPreviewTag.class);
			
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IContentViewerHelper helper = (IContentViewerHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_PREVIEW_VIEWER_HELPER, this.pageContext);
			String renderedContent = helper.getRenderedContent(null, null, reqCtx);
			this.pageContext.getOut().print(renderedContent);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected during tag initialisation", t);
		}
		return EVAL_PAGE;
	}
	
}
