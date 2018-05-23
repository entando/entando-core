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
package org.entando.entando.apsadmin.portal.guifragment;

import java.util.HashMap;
import java.util.Map;

import org.entando.entando.aps.system.services.guifragment.GuiFragment;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;

import com.opensymphony.xwork2.Action;

public class TestGuiFragmentAction extends ApsAdminBaseTestCase {

    public void testList() throws Throwable {
        String result = this.executeList("admin");
        assertEquals(Action.SUCCESS, result);
        GuiFragmentFinderAction action = (GuiFragmentFinderAction) this.getAction();
        assertEquals(1, action.getGuiFragmentsCodes().size());
    }

    public void testDetail() throws Throwable {
        Map<String, String> params = new HashMap<>();
        params.put("code", "login_form");
        String result = this.executeDetail("admin", params);
        assertEquals(Action.SUCCESS, result);
        GuiFragmentAction action = (GuiFragmentAction) this.getAction();
        assertEquals(1, action.getReferences().keySet().size());
    }

    protected GuiFragment createMockFragment(String code, String gui, String widgetTypeCode) {
        GuiFragment mFragment = new GuiFragment();
        mFragment.setCode(code);
        mFragment.setGui(gui);
        mFragment.setWidgetTypeCode(widgetTypeCode);
        return mFragment;
    }

    private String executeList(String currentUser) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction(NS, "list");
        return this.executeAction();
    }

    private String executeDetail(String currentUser, Map<String, String> params) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction(NS, "detail");
        this.addParameters(params);
        return this.executeAction();
    }

    private static final String NS = "/do/Portal/GuiFragment";

}
