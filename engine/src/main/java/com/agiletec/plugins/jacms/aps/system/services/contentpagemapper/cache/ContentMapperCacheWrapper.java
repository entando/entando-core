/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.ContentPageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ContentMapperCacheWrapper extends AbstractCacheWrapper implements IContentMapperCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentMapperCacheWrapper.class);

	@Override
	public void initCache(IPageManager pageManager) throws ApsSystemException {
		try {
			ContentPageMapper contentPageMapper = new ContentPageMapper();
			IPage root = pageManager.getOnlineRoot();
			this.searchPublishedDataObjects(contentPageMapper, root, pageManager);
			this.getCache().put(CONTENT_MAPPER_CACHE_KEY, contentPageMapper);
		} catch (Throwable t) {
			_logger.error("Error loading data object mapper", t);
			throw new ApsSystemException("Error loading data object mapper", t);
		}
	}

	private void searchPublishedDataObjects(ContentPageMapper contentPageMapper, IPage page, IPageManager pageManager) {
		PageModel pageModel = page.getModel();
		if (pageModel != null) {
			int mainFrame = pageModel.getMainFrame();
			Widget[] widgets = page.getWidgets();
			Widget widget = null;
			if (null != widgets && mainFrame != -1) {
				widget = widgets[mainFrame];
			}
			ApsProperties config = (null != widget) ? widget.getConfig() : null;
			String contentId = (null != config) ? config.getProperty("contentId") : null;
			if (null != contentId) {
				contentPageMapper.add(contentId, page.getCode());
			}
			String[] childCodes = page.getChildrenCodes();
			for (String childCode : childCodes) {
				IPage child = pageManager.getOnlinePage(childCode);
				if (null != child) {
					this.searchPublishedDataObjects(contentPageMapper, child, pageManager);
				}
			}
		}
	}
	
	@Override
	public String getPageCode(String dataId) {
		ContentPageMapper contentPageMapper = this.get(CONTENT_MAPPER_CACHE_KEY, ContentPageMapper.class);
		if (null != contentPageMapper) {
			return contentPageMapper.getPageCode(dataId);
		}
		return null;
	}
	
	@Override
	protected String getCacheName() {
		return IContentMapperCacheWrapper.CONTENT_MAPPER_CACHE_NAME;
	}
	
}
