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
package com.agiletec.plugins.jacms.apsadmin.tags;

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
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error detected during tag initialisation", t);
		}
		return EVAL_PAGE;
	}
	
}
