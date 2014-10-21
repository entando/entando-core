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
package org.entando.entando.plugins.jacms.aps.system.services.page;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import java.util.Properties;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sovrascrittura del servizio di gestione delle pagine {@link PageManager}.
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
			Widget[] widgets = targetPage.getWidgets();
			if (null != widgets) {
				for (int i = 0; i < widgets.length; i++) {
					Widget widget = widgets[i];
					ApsProperties config = (null != widget) ? widget.getConfig() : null;
					if (null == config || config.isEmpty()) {
						continue;
					}
					String extracted = config.getProperty("contentId");
					if (null != extracted && contentId.equals(extracted)) {
						pages.add(targetPage);
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
								pages.add(targetPage);
								break;
							}
						}
					}
				}
			}
			IPage[] children = targetPage.getChildren();
			if (null != children) {
				for (int i = 0; i < children.length; i++) {
					IPage page = children[i];
					this.searchContentUtilizers(page, pages, contentId);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading referenced pages", t);
            throw new ApsSystemException("Error loading referenced pages with content " + contentId, t);
		}
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	private IPageManager _pageManager;
	
}