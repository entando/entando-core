/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;

import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fornisce i contenuti formattati per la funzione preview da redazione contenuti.
 * La classe deriva direttamente dalla classe utilizzata nel front-end {@link BaseContentDispenser} di portale per le funzioni di renderizzazione contenuti.
 * @author E.Santoboni
 */
public class ContentPreviewDispenser extends BaseContentDispenser {

	private static final Logger _logger = LoggerFactory.getLogger(ContentPreviewDispenser.class);
	
	@Override
	public ContentRenderizationInfo getRenderizationInfo(String contentId, long modelId, String langCode, RequestContext reqCtx) {
		return super.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
	}
	
	@Override
	public ContentRenderizationInfo getBaseRenderizationInfo(PublicContentAuthorizationInfo authInfo, 
			String contentId, long modelId, String langCode, UserDetails currentUser, RequestContext reqCtx) {
		HttpServletRequest request = reqCtx.getRequest();
		String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
		if (null == contentOnSessionMarker || contentOnSessionMarker.trim().length() == 0) {
			contentOnSessionMarker = request.getParameter("contentOnSessionMarker");
		}
		ContentRenderizationInfo renderInfo = null;
		try {
			List<Group> userGroups = (null != currentUser) ? this.getAuthorizationManager().getUserGroups(currentUser) : new ArrayList<Group>();
			if (authInfo.isUserAllowed(userGroups)) {
				Content contentOnSession = (Content) request.getSession()
						.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
				String renderedContent = this.buildRenderedContent(contentOnSession, modelId, langCode, reqCtx);
				if (null != renderedContent && renderedContent.trim().length() > 0) {
					List<AttributeRole> roles = this.getContentManager().getAttributeRoles();
					renderInfo = new ContentRenderizationInfo(contentOnSession, renderedContent, modelId, langCode, roles);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while rendering content {}", contentId, t);
			return null;
		}
		return renderInfo;
	}
	
}
