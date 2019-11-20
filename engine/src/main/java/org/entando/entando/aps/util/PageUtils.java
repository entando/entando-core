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
package org.entando.entando.aps.util;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.List;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class PageUtils {

    private static final Logger logger = LoggerFactory.getLogger(PageUtils.class);

    /**
     * Check whether the page can publish free content, related to the draft
     * configuration of the page.
     *
     * @param page The page to check.
     * @param viewerWidgetCode The code of the viewer widget (optional)
     * @return True if the page can publish free content, false else.
     */
    public static boolean isDraftFreeViewerPage(IPage page, String viewerWidgetCode) {
        if (page.isOnlineInstance()) {
            logger.warn("this check expects a draft instance of the page");
            return false;
        }
        boolean found = false;
        PageMetadata metadata = page.getMetadata();
        Widget[] widgets = page.getWidgets();
        if (metadata != null) {
            found = isFreeViewerPage(metadata.getModel(), widgets, viewerWidgetCode);
        }
        return found;
    }

    /**
     * Check whether the page can publish free content, related to the online
     * configuration of the page.
     *
     * @param page The page to check.
     * @param viewerWidgetCode The code of the viewer widget (optional)
     * @return True if the page can publish free content, false else.
     */
    public static boolean isOnlineFreeViewerPage(IPage page, String viewerWidgetCode) {
        if (!page.isOnlineInstance()) {
            logger.warn("this check expects an online instance of the page");
            return false;
        }
        boolean found = false;
        PageMetadata metadata = page.getMetadata();
        Widget[] widgets = page.getWidgets();
        if (metadata != null) {
            found = isFreeViewerPage(metadata.getModel(), widgets, viewerWidgetCode);
        }
        return found;
    }

    /**
     * Check whether the page can publish free content, related to the model and
     * the widgets of the page.
     *
     * @param model The model of the page to check.
     * @param widgets The widgets of the page to check.
     * @param viewerWidgetCode The code of the viewer widget (optional)
     * @return True if the page can publish free content, false else.
     */
    public static boolean isFreeViewerPage(PageModel model, Widget[] widgets, String viewerWidgetCode) {
        try {
            if (model != null && widgets != null) {
                int mainFrame = model.getMainFrame();
                if (mainFrame < 0) {
                    return false;
                }
                Widget viewer = widgets[mainFrame];
                if (null == viewer) {
                    return false;
                }
                boolean isRightCode = null == viewerWidgetCode || viewer.getType().getCode().equals(viewerWidgetCode);
                String actionName = viewer.getType().getAction();
                boolean isRightAction = (null != actionName && actionName.toLowerCase().indexOf("viewer") >= 0);
                List<WidgetTypeParameter> typeParameters = viewer.getType().getTypeParameters();
                if ((isRightCode || isRightAction) && (null != typeParameters && !typeParameters.isEmpty()) && (null == viewer.getConfig()
                        || viewer.getConfig().isEmpty())) {
                    return true;
                }
            }
        } catch (Throwable t) {
            logger.error("Error while checking page for widget '{}'", viewerWidgetCode, t);
        }
        return false;
    }
}
