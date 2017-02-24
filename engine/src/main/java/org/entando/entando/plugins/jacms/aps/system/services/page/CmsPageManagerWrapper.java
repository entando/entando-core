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
package org.entando.entando.plugins.jacms.aps.system.services.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;

/**
 * @author E.Santoboni
 */
public class CmsPageManagerWrapper implements ContentUtilizer {
	
	private static final Logger _logger =  LoggerFactory.getLogger(CmsPageManagerWrapper.class);
	
	@Override
	public String getName() {
		return "CmsPageManagerWrapper";
	}
	
	@Override
    public List getContentUtilizers(String contentId) throws ApsSystemException {
		List<IPage> pages = new ArrayList<IPage>();
		try {
			IPage root = this.getPageManager().getRoot();
			this.searchContentUtilizers(root, pages, contentId);
		} catch (Throwable t) {
			_logger.error("Error loading referenced pages", t);
            throw new ApsSystemException("Error loading referenced pages with content " + contentId, t);
		}
    	return pages;
	}
	
	public void searchContentUtilizers(IPage targetPage, List<IPage> pages, String contentId) throws ApsSystemException {
		if (null == contentId) return;
		try {
			boolean found = this.findContentUtilizers(targetPage.getOnlineWidgets(), contentId) 
					|| this.findContentUtilizers(targetPage.getDraftWidgets(), contentId);
			if (found) {
				pages.add(targetPage);
			}
			IPage[] children = targetPage.getAllChildren();
			if (null != children) {
				for (IPage page : children) {
					this.searchContentUtilizers(page, pages, contentId);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading referenced pages", t);
            throw new ApsSystemException("Error loading referenced pages with content " + contentId, t);
		}
	}
	
	public boolean findContentUtilizers(Widget[] widgets, String contentId) throws ApsSystemException {
		boolean found = false;
		if (null != widgets) {
			for (Widget widget : widgets) {
				ApsProperties config = (null != widget) ? widget.getConfig() : null;
				if (null == config || config.isEmpty()) {
					continue;
				}
				String extracted = config.getProperty("contentId");
				if (null != extracted && contentId.equals(extracted)) {
					found = true;
				} else {
					String contents = config.getProperty("contents");
					List<Properties> properties = (null != contents) ? RowContentListHelper.fromParameterToContents(contents) : null;
					if (null == properties || properties.isEmpty()) {
						continue;
					}
					for (int j = 0; j < properties.size(); j++) {
						Properties widgProp = properties.get(j);
						String extracted2 = widgProp.getProperty("contentId");
						if (null != extracted2 && contentId.equals(extracted2)) {
							found = true;
							break;
						}
					}
				}
			}
		}
		return found;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	private IPageManager _pageManager;
	
}