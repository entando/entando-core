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
package com.agiletec.aps.system.services.pagemodel;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @author M.Diana
 */
public class TestPageModelDOM extends BaseTestCase {

    public void testGetFrames() throws Throwable {
        IWidgetTypeManager widgetTypeManager
                = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
        PageModelDOM pageModelDOM = new PageModelDOM(FRAMES_XML, widgetTypeManager);
        Frame[] configuration = pageModelDOM.getConfiguration();
        assertTrue(configuration[0].getDescription().equals("Box sinistra alto"));
        assertEquals("login_form", configuration[1].getDefaultWidget().getType().getCode());
    }

    public final static String FRAMES_XML = "<frames>"
            + "<frame pos=\"0\"><descr>Box sinistra alto</descr></frame>"
            + "<frame pos=\"1\"><descr>Box sinistra basso</descr><defaultWidget code=\"login_form\" /></frame>"
            + "<frame pos=\"2\" main=\"true\"><descr>Box centrale 1</descr></frame>"
            + "<frame pos=\"3\"><descr>Box centrale 2</descr></frame>"
            + "<frame pos=\"4\"><descr>Box destra alto</descr></frame>"
            + "<frame pos=\"5\"><descr>Box destra basso</descr></frame>"
            + "</frames>";

}
