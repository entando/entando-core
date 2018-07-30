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
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
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

    public void testValidateSavePage() throws Throwable {
        String pageCode = "pagina_test";
        assertNull(this.pageManager.getDraftPage(pageCode));
        try {
            Map<String, String> params = new HashMap<>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
            params.put("parentPageCode", "customers_page");
            params.put("langit", "Pagina Test");
            params.put("model", "home");
            params.put("langen", "Test Page");
            params.put("group", Group.FREE_GROUP_NAME);
            params.put("pageCode", pageCode);
            String result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("group"));

            params.put("group", Group.ADMINS_GROUP_NAME);
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("group"));

            params.put("group", "coach");
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("group"));

            params.put("group", "customers");
            result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);

            assertNotNull(this.pageManager.getDraftPage(pageCode));
        } catch (Throwable t) {
            throw t;
        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    private String executeSave(Map<String, String> params, String username) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", "save");
        this.addParameters(params);
        String result = this.executeAction();
        return result;
    }

    private void init() throws Exception {
        try {
            this.pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
