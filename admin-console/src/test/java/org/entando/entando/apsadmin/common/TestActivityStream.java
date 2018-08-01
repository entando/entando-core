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
package org.entando.entando.apsadmin.common;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;

import com.opensymphony.xwork2.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.entando.entando.aps.system.services.actionlog.ActionLoggerTestHelper;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;

/**
 * @author E.Santoboni
 */
public class TestActivityStream extends ApsAdminBaseTestCase {

    private IActionLogManager actionLoggerManager;
    private IPageManager pageManager = null;
    private ILangManager langManager = null;
    private ActionLoggerTestHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this.helper.cleanRecords();
    }

    public void testLogAddPage() throws Throwable {
        String pageCode = "act_stream_test";
        try {
            this.addPage(pageCode);
            synchronized (this) {
                this.wait(1000);
            }
            super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
            ActionLogRecordSearchBean searchBean = this.helper.createSearchBean("admin", null, null, null, null, null);
            List<Integer> ids = this.actionLoggerManager.getActionRecords(searchBean);
            assertEquals(1, ids.size());
            ActionLogRecord record = this.actionLoggerManager.getActionRecord(ids.get(0));
            assertEquals("/do/Page", record.getNamespace());
            assertEquals("save", record.getActionName());
            ActivityStreamInfo asi = record.getActivityStreamInfo();
            assertNotNull(asi);
            assertEquals(1, asi.getActionType());
            assertEquals("edit", asi.getLinkActionName());
            assertEquals("/do/Page", asi.getLinkNamespace());
            Properties parameters = asi.getLinkParameters();
            assertEquals(1, parameters.size());
            assertEquals(pageCode, parameters.getProperty("selectedNode"));
        } catch (Throwable t) {
            throw t;
        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    private void addPage(String pageCode) throws Throwable {
        assertNull(this.pageManager.getDraftPage(pageCode));
        try {
            IPage root = this.pageManager.getOnlineRoot();
            Map<String, String> params = new HashMap<String, String>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
            params.put("parentPageCode", root.getCode());
            List<Lang> langs = this.langManager.getLangs();
            for (int i = 0; i < langs.size(); i++) {
                Lang lang = langs.get(i);
                params.put("lang" + lang.getCode(), "Page " + lang.getDescr());
            }
            params.put("model", "home");
            params.put("group", Group.FREE_GROUP_NAME);
            params.put("pageCode", pageCode);
            String result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);
            IPage addedPage = this.pageManager.getDraftPage(pageCode);
            assertNotNull(addedPage);
        } catch (Throwable t) {
            throw t;
        }
    }

    private String executeSave(Map<String, String> params, String username) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", "save");
        this.addParameters(params);
        return this.executeAction();
    }

    private void init() {
        this.actionLoggerManager = (IActionLogManager) this.getService(SystemConstants.ACTION_LOGGER_MANAGER);
        this.pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        this.langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
        this.helper = new ActionLoggerTestHelper(this.getApplicationContext());
    }

    @Override
    protected void tearDown() throws Exception {
        this.helper.cleanRecords();
        super.tearDown();
    }

}
