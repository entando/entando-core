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
package org.entando.entando.aps.system.services.dataobjectmapper.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.dataobjectmapper.DataObjectPageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class DataObjectMapperCacheWrapper extends AbstractCacheWrapper implements IDataObjectMapperCacheWrapper {

	private static final Logger logger = LoggerFactory.getLogger(DataObjectMapperCacheWrapper.class);

	@Override
	public void initCache(IPageManager pageManager) throws ApsSystemException {
		try {
			DataObjectPageMapper dataObjectPageMapper = new DataObjectPageMapper();
			IPage root = pageManager.getOnlineRoot();
			this.searchPublishedDataObjects(dataObjectPageMapper, root, pageManager);
			this.getCache().put(OBJECT_MAPPER_CACHE_KEY, dataObjectPageMapper);
		} catch (Throwable t) {
			logger.error("Error loading data object mapper", t);
			throw new ApsSystemException("Error loading data object mapper", t);
		}
	}

	private void searchPublishedDataObjects(DataObjectPageMapper dataObjectPageMapper, IPage page, IPageManager pageManager) {
		PageModel pageModel = page.getModel();
		if (pageModel != null) {
			int mainFrame = pageModel.getMainFrame();
			Widget[] widgets = page.getWidgets();
			Widget widget = null;
			if (null != widgets && mainFrame != -1) {
				widget = widgets[mainFrame];
			}
			ApsProperties config = (null != widget) ? widget.getConfig() : null;
			String dataId = (null != config) ? config.getProperty("dataId") : null;
			if (null != dataId) {
				dataObjectPageMapper.add(dataId, page.getCode());
			}
			String[] childCodes = page.getChildrenCodes();
			for (String childCode : childCodes) {
				IPage child = pageManager.getOnlinePage(childCode);
				if (null != child) {
					this.searchPublishedDataObjects(dataObjectPageMapper, child, pageManager);
				}
			}
		}
	}

	@Override
	public String getPageCode(String dataId) {
		DataObjectPageMapper dataObjectPageMapper = this.get(OBJECT_MAPPER_CACHE_KEY, DataObjectPageMapper.class);
		if (null != dataObjectPageMapper) {
			return dataObjectPageMapper.getPageCode(dataId);
		}
		return null;
	}

	@Override
	protected String getCacheName() {
		return IDataObjectMapperCacheWrapper.OBJECT_MAPPER_CACHE_NAME;
	}

}
