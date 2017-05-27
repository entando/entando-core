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
package com.agiletec.apsadmin.portal.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.ActionSupport;

public class PageActionReferencesHelper implements IPageActionReferencesHelper {

	private static final Logger _logger = LoggerFactory.getLogger(PageActionReferencesHelper.class);

	@Override
	public boolean checkContentsForSetOnline(IPage page, ActionSupport action) {
		try {
			for (Widget widget : page.getWidgets()) {
				if (null != widget) {
					ApsProperties config = widget.getConfig();
					String contentId = (null != config) ? config.getProperty("contentId") : null;
					this.checkContent(action, contentId);
				}
			}
		} catch (ApsSystemException e) {
			_logger.error("error checking draft page - content references");
			return false;
		}
		return true;
	}

	protected void checkContent(ActionSupport action, String contentId) throws ApsSystemException {
		if (StringUtils.isNotBlank(contentId)) {
			Content content = this.getContentManager().loadContent(contentId, true);
			if (null == content || !content.isOnLine()) {
				List<String> args = new ArrayList<>();
				args.add(null == content ? contentId : content.getDescription());
				action.addActionError(action.getText("error.page.set.online.content.ref.offline", args));
			}
		}
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	private IContentManager _contentManager;
}
