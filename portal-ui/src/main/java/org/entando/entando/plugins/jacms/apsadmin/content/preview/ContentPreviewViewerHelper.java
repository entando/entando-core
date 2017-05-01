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
package org.entando.entando.plugins.jacms.apsadmin.content.preview;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentViewerHelper;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

/**
 * Classe helper per la showlet di erogazione contenuti per la funzione preview da redazione contenuti.
 * La classe deriva direttamente dalla classe helper {@link ContentViewerHelper} utilizzata nel front-end di portale per le funzioni di renderizzazione contenuti.
 * @author E.Santoboni
 */
public class ContentPreviewViewerHelper extends ContentViewerHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentPreviewViewerHelper.class);
	
	@Override
	public String getRenderedContent(String contentId, String modelId, RequestContext reqCtx) throws ApsSystemException {
		String renderedContent = "";
		HttpServletRequest request = reqCtx.getRequest();
		try {
			String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
			if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
				contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
			}
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			String langCode = currentLang.getCode();
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			Content contentOnSession = (Content) request.getSession()
					.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
			contentId = contentOnSession.getId() == null ? contentOnSession.getTypeCode()+"123" : contentOnSession.getId();
			ApsProperties widgetConfig = widget.getConfig();
			modelId = this.extractModelId(contentId, modelId, widgetConfig);
			if (null != contentId && null != modelId) {
				long longModelId = Long.parseLong(modelId);
				this.setStylesheet(longModelId, reqCtx);
				ContentRenderizationInfo renderizationInfo = this.getContentDispenser().getRenderizationInfo(contentId, longModelId, langCode, reqCtx);
	            if (null == renderizationInfo) {
	            	_logger.warn("Null Renderization informations: content={}", contentId);
	            } else {
					this.getContentDispenser().resolveLinks(renderizationInfo, reqCtx);
					renderedContent = renderizationInfo.getRenderedContent();
				}
			} else {
				_logger.warn("Parametri visualizzazione contenuto incompleti: contenuto={} modello={}", contentId, modelId);
			}
		} catch (Throwable t) {
			_logger.error("error loading rendered content for preview", t);
			throw new ApsSystemException("error loading rendered content for preview", t);
		}
		return renderedContent;
	}
	
}
