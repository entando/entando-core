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

import java.util.Collection;
import java.util.Iterator;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;

/**
 * @author E.Santoboni
 */
public class TestPageAction extends ApsAdminBaseTestCase {

    private IPageManager pageManager = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testSavePage_1() throws Throwable {
        String pageCode = "customer_subpage_2";
        IPage page = this.pageManager.getDraftPage(pageCode);
        assertNotNull(page);
        try {
            this.prepareTestSavePage(page, page.getGroup(), "management");
            String result = this.executeAction();
            assertEquals(Action.INPUT, result);
            ActionSupport action = this.getAction();
            assertEquals(2, action.getFieldErrors().size());
            assertEquals(1, action.getFieldErrors().get("langit").size());
            assertEquals(1, action.getFieldErrors().get("extraGroups").size());
        } catch (Throwable t) {
            this.pageManager.updatePage(page);
            throw t;
        }
    }

    public void testSavePage_2() throws Throwable {
        String pageCode = "customer_subpage_2";
        IPage page = this.pageManager.getDraftPage(pageCode);
        assertNotNull(page);
        try {
            this.prepareTestSavePage(page, "management", "");
            String result = this.executeAction();
            assertEquals(Action.INPUT, result);
            ActionSupport action = this.getAction();
            assertEquals(3, action.getFieldErrors().size());
            assertEquals(1, action.getFieldErrors().get("group").size());
            assertEquals(1, action.getFieldErrors().get("langit").size());
            assertEquals(1, action.getFieldErrors().get("extraGroups").size());
        } catch (Throwable t) {
            this.pageManager.updatePage(page);
            throw t;
        }
    }

    private void prepareTestSavePage(IPage page, String mainGroup, String extendedGroup) throws Throwable {
        PageMetadata metadata = page.getMetadata();
        this.setUserOnSession("admin");
        this.initAction("/do/Page", "save");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
        this.addParameter("langit", "");
        this.addParameter("langen", metadata.getTitle("en"));
        this.addParameter("model", metadata.getModel().getCode());
        this.addParameter("group", mainGroup);
        this.addParameter("pageCode", page.getCode());
        Collection<String> extraGroups = metadata.getExtraGroups();
        if (null != extraGroups) {
            Iterator<String> extraGroupIter = extraGroups.iterator();
            while (extraGroupIter.hasNext()) {
                String extraGroup = (String) extraGroupIter.next();
                this.addParameter("extraGroups", extraGroup);
            }
        }
        if (StringUtils.isEmpty(extendedGroup)) {
            this.addParameter("extraGroups", extendedGroup);
        }
    }

    private void init() throws Exception {
        try {
            this.pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
