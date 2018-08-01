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

import java.util.Date;
import java.util.List;

import org.entando.entando.aps.system.services.actionlog.ActionLoggerTestHelper;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamSeachBean;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestActivityStreamAction extends ApsAdminBaseTestCase {

    private IActionLogManager actionLoggerManager;
    private IContentManager contentManager = null;
    private ActionLoggerTestHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this.helper.cleanRecords();
    }

    public void testActivityStreamSearchBean() throws Throwable {
        Content content = this.contentManager.loadContent("EVN41", false);//"coach" group
        String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.ADD);
        content.setId(null);
        String contentId = null;
        Thread.sleep(1000);
        try {
            this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
            this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
            this.setUserOnSession("admin");
            String result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
            synchronized (this) {
                this.wait(1000);
            }
            contentId = content.getId();
            assertNotNull(this.contentManager.loadContent(contentId, false));
            super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
            Date firstDate = new Date();
            ActionLogRecordSearchBean searchBean = this.helper.createSearchBean("admin", null, null, null, null, null);
            List<Integer> ids = this.actionLoggerManager.getActionRecords(searchBean);
            assertEquals(1, ids.size());
            ActionLogRecord firstRecord = this.actionLoggerManager.getActionRecord(ids.get(0));

            ActivityStreamSeachBean activityStreamSeachBean = new ActivityStreamSeachBean();
            activityStreamSeachBean.setEndUpdate(firstDate);
            List<Integer> activityStreamEndDate = this.actionLoggerManager.getActivityStream(activityStreamSeachBean);
            assertEquals(1, activityStreamEndDate.size());

            this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
            this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
            this.setUserOnSession("admin");
            result = this.executeAction();
            synchronized (this) {
                this.wait(1000);
            }
            assertEquals(Action.SUCCESS, result);
            contentId = content.getId();
            assertNotNull(this.contentManager.loadContent(contentId, false));
            super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);

            activityStreamSeachBean = new ActivityStreamSeachBean();
            activityStreamSeachBean.setEndUpdate(new Date());
            List<Integer> activityStreamBetweenSave2 = this.actionLoggerManager.getActivityStream(activityStreamSeachBean);
            assertEquals(2, activityStreamBetweenSave2.size());

            String firstDateString = DateConverter.getFormattedDate(firstDate, ApsAdminSystemConstants.CALENDAR_TIMESTAMP_PATTERN);
            this.initActivityStreamAction("/do/ActivityStream", "update", firstDateString);
            this.setUserOnSession("admin");
            result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
            ActivityStreamAction activityStreamAction = (ActivityStreamAction) this.getAction();
            List<Integer> update = activityStreamAction.getActionRecordIds();
            assertEquals(1, update.size());
            ActionLogRecord updateRecord = this.actionLoggerManager.getActionRecord(update.get(0));

            String actionRecordDate = DateConverter.getFormattedDate(updateRecord.getActionDate(), ApsAdminSystemConstants.CALENDAR_TIMESTAMP_PATTERN);
            this.initActivityStreamAction("/do/ActivityStream", "viewMore", actionRecordDate);
            this.setUserOnSession("admin");
            result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
            activityStreamAction = (ActivityStreamAction) this.getAction();
            List<Integer> viewMore = activityStreamAction.getActionRecordIds();
            assertEquals(1, viewMore.size());
            assertEquals(firstRecord.getId(), viewMore.get(0).intValue());

        } catch (Throwable t) {
            throw t;
        } finally {
            this.contentManager.deleteContent(content);
            assertNull(this.contentManager.loadContent(contentId, false));
        }
    }

    public void testCallAction() throws Throwable {
        this.initActivityStreamAction("/do/ActivityStream", "update", "2012-12-12 12:12:12|121");
        this.setUserOnSession("admin");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);
    }

    protected void initContentAction(String namespace, String name, String contentOnSessionMarker) throws Exception {
        this.initAction(namespace, name);
        this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
    }

    protected void initActivityStreamAction(String namespace, String name, String timestamp) throws Exception {
        this.initAction(namespace, name);
        this.addParameter("timestamp", timestamp);
    }

    private void init() {
        this.actionLoggerManager = (IActionLogManager) this.getService(SystemConstants.ACTION_LOGGER_MANAGER);
        this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
        this.helper = new ActionLoggerTestHelper(this.getApplicationContext());
    }

    @Override
    protected void tearDown() throws Exception {
        this.helper.cleanRecords();
        super.tearDown();
    }

}
