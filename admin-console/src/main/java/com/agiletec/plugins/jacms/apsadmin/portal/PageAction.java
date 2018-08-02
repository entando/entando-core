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
package com.agiletec.plugins.jacms.apsadmin.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;

/**
 * @author E.Santoboni
 */
public class PageAction extends com.agiletec.apsadmin.portal.PageAction {

    private static final Logger logger = LoggerFactory.getLogger(PageAction.class);

    private boolean viewerPage;
    private String viewerWidgetCode;

    private IContentManager contentManager;

    /**
     * Check if a page che publish a single content.
     *
     * @param page The page to check
     * @return True if the page can publish a free content, else false.
     */
    public boolean isFreeViewerPage(IPage page) {
        return CmsPageUtil.isDraftFreeViewerPage(page, this.getViewerWidgetCode());
    }

    public String setViewerPageAPI() {
        IPage page = null;
        try {
            page = this.getPage(this.getPageCode());
            int mainFrame = page.getMetadata().getModel().getMainFrame();
            if (mainFrame > -1) {
                IWidgetTypeManager widgetTypeManager = (IWidgetTypeManager) ApsWebApplicationUtils.getBean(SystemConstants.WIDGET_TYPE_MANAGER, this.getRequest());
                Widget viewer = new Widget();
                viewer.setConfig(new ApsProperties());
                WidgetType type = widgetTypeManager.getWidgetType(this.getViewerWidgetCode());
                if (null == type) {
                    logger.warn("No widget found for on-the-fly publishing config for page {}", page.getCode());
                    return SUCCESS;
                }
                viewer.setType(type);
                Widget[] widgets = page.getWidgets();
                widgets[mainFrame] = viewer;
            }
            this.getPageManager().updatePage(page);
        } catch (Throwable t) {
            logger.error("Error setting on-the-fly publishing config to page {}", page.getCode(), t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public Collection<Content> getPublishedContents(String pageCode) {
        return this.getOnlinePublishedContents(pageCode);
    }

    public Collection<Content> getOnlinePublishedContents(String pageCode) {
        return CmsPageUtil.getPublishedContents(pageCode, false, ApsWebApplicationUtils.getWebApplicationContext(this.getRequest()));
    }

    public List<ContentRecordVO> getReferencingContents(String pageCode) {
        List<ContentRecordVO> referencingContents = null;
        try {
            List<String> referencingContentsId = this.getReferencingContentsId(pageCode);
            if (null != referencingContentsId) {
                referencingContents = new ArrayList<>();
                for (int i = 0; i < referencingContentsId.size(); i++) {
                    ContentRecordVO contentVo = this.getContentManager().loadContentVO(referencingContentsId.get(i));
                    if (null != contentVo) {
                        referencingContents.add(contentVo);
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Error getting referencing contents by page '{}'", pageCode, t);
            String msg = "Error getting referencing contents by page '" + pageCode + "'";
            throw new RuntimeException(msg, t);
        }
        return referencingContents;
    }

    public List<String> getReferencingContentsId(String pageCode) {
        List<String> referencingContentsId = null;
        try {
            referencingContentsId = ((PageUtilizer) this.getContentManager()).getPageUtilizers(pageCode);
        } catch (Throwable t) {
            logger.error("Error getting referencing contents by page '{}'", pageCode, t);
            String msg = "Error getting referencing contents by page '" + pageCode + "'";
            throw new RuntimeException(msg, t);
        }
        return referencingContentsId;
    }

    public boolean isViewerPage() {
        return viewerPage;
    }

    public void setViewerPage(boolean viewerPage) {
        this.viewerPage = viewerPage;
    }

    @Deprecated
    protected String getViewerShowletCode() {
        return this.getViewerWidgetCode();
    }

    @Deprecated
    public void setViewerShowletCode(String viewerShowletCode) {
        this.setViewerWidgetCode(viewerShowletCode);
    }

    protected String getViewerWidgetCode() {
        return viewerWidgetCode;
    }

    public void setViewerWidgetCode(String viewerWidgetCode) {
        this.viewerWidgetCode = viewerWidgetCode;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

}
