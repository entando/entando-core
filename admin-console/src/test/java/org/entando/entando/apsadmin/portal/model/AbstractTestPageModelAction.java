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
package org.entando.entando.apsadmin.portal.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

/**
 * @author E.Santoboni
 */
public abstract class AbstractTestPageModelAction extends ApsAdminBaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    protected PageModel createMockPageModel(String code) {
        PageModel model = new PageModel();
        model.setCode(code);
        model.setDescription("Description of model " + code);
        Frame frame0 = new Frame();
        frame0.setPos(0);
        frame0.setDescription("Frame 0");
        frame0.setMainFrame(true);
        Frame frame1 = new Frame();
        frame1.setPos(1);
        frame1.setDescription("Frame 1");
        Widget defWidg1 = new Widget();
        defWidg1.setType(this._widgetTypeManager.getWidgetType("formAction"));
        ApsProperties props1 = new ApsProperties();
        props1.setProperty("actionPath", "/do/login");
        defWidg1.setConfig(props1);
        frame1.setDefaultWidget(defWidg1);
        Frame frame2 = new Frame();
        frame2.setPos(1);
        frame2.setDescription("Frame 2");
        Widget defWidg2 = new Widget();
        defWidg2.setType(this._widgetTypeManager.getWidgetType("login_form"));
        frame2.setDefaultWidget(defWidg2);
        Frame[] configuration = {frame0, frame1, frame2};
        model.setConfiguration(configuration);
        model.setTemplate("<strong>Freemarker template content</strong>");
        return model;
    }

    private void init() throws Exception {
        try {
            this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    protected IWidgetTypeManager _widgetTypeManager;
    protected IPageModelManager _pageModelManager = null;

}
