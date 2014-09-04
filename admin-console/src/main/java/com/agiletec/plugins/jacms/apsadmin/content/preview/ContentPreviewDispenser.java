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
package com.agiletec.plugins.jacms.apsadmin.content.preview;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

/**
 * Fornisce i contenuti formattati per la funzione preview da redazione contenuti.
 * La classe deriva direttamente dalla classe utilizzata nel front-end {@link BaseContentDispenser} di portale per le funzioni di renderizzazione contenuti.
 * @author E.Santoboni
 */
public class ContentPreviewDispenser extends BaseContentDispenser {

	private static final Logger _logger = LoggerFactory.getLogger(ContentPreviewDispenser.class);
	
	@Override
	public String getRenderedContent(String contentId, long modelId, String langCode, RequestContext reqCtx) {
		HttpServletRequest request = reqCtx.getRequest();
		String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
		if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
			contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
		}
		Content contentOnSession = (Content) request.getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
		String renderedContent = this.getRenderedContent(contentOnSession, modelId, langCode, reqCtx);
		return renderedContent;
	}
	
	protected String getRenderedContent(Content content, long modelId, String langCode, RequestContext reqCtx) {
		String renderedContent = null;
		boolean ok = false;
		try {
			renderedContent = this.getContentRender().render(content, modelId, langCode, reqCtx);
			ok = true;
		} catch (Throwable t) {
			_logger.error("Error in getRenderedContent",  t);
		}
		if (!ok) {
			_logger.warn("Preview problem for {}", content.getId());
			return "";
		}
		renderedContent = this.getLinkResolverManager().resolveLinks(renderedContent, reqCtx);
		return renderedContent;
	}
	
}
