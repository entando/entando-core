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
package org.entando.entando.apsadmin.common;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

import com.opensymphony.xwork2.Action;
import java.util.Date;

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
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamSeachBean;

/**
 * @author E.Santoboni
 */
public class TestActivityStream extends ApsAdminBaseTestCase {
    
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._helper.cleanRecords();
	}
	
	public void testLogAddPage() throws Throwable {
		String pageCode = "activity_stream_test_test";
		try {
			this.addPage(pageCode);
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("admin", null, null, null, null, null);
			List<Integer> ids = this._actionLoggerManager.getActionRecords(searchBean);
			assertEquals(1, ids.size());
			ActionLogRecord record = this._actionLoggerManager.getActionRecord(ids.get(0));
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
			this._pageManager.deletePage(pageCode);
		}
	}
	
	private void addPage(String pageCode) throws Throwable {
		assertNull(this._pageManager.getPage(pageCode));
		try {
			IPage root = this._pageManager.getRoot();
			Map<String, String> params = new HashMap<String, String>();
			params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			params.put("parentPageCode", root.getCode());
			List<Lang> langs = this._langManager.getLangs();
			for (int i = 0; i < langs.size(); i++) {
				Lang lang = langs.get(i);
				params.put("lang" + lang.getCode(), "Page " + lang.getDescr());
			}
			params.put("model", "home");
			params.put("group", Group.FREE_GROUP_NAME);
			params.put("pageCode", pageCode);
			String result = this.executeSave(params, "admin");
			assertEquals(Action.SUCCESS, result);
			IPage addedPage = this._pageManager.getPage(pageCode);
			assertNotNull(addedPage);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private String executeSave(Map<String, String> params, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Page", "save");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	// ----------------------------------------------
	
	public void testSaveNewContent_1() throws Throwable {
		Content content = this._contentManager.loadContent("ART1", false);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.ADD);
		content.setId(null);
		String contentId = null;
		try {
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			contentId = content.getId();
			assertNotNull(this._contentManager.loadContent(contentId, false));
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			
			ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("admin", null, null, null, null, null);
			List<Integer> ids = this._actionLoggerManager.getActionRecords(searchBean);
			assertEquals(1, ids.size());
			
			UserDetails currentUser = (UserDetails) super.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			List<Integer> activityStream = this._actionLoggerManager.getActivityStream(currentUser);
			assertEquals(activityStream.size(), ids.size());
			assertEquals(activityStream.get(0), ids.get(0));
			
			ActionLogRecord record = this._actionLoggerManager.getActionRecord(ids.get(0));
			assertEquals("/do/jacms/Content", record.getNamespace());
			assertEquals("save", record.getActionName());
			ActivityStreamInfo asi = record.getActivityStreamInfo();
			assertNotNull(asi);
			assertEquals(1, asi.getActionType());
			assertEquals(Permission.CONTENT_EDITOR, asi.getLinkAuthPermission());
			assertEquals(content.getMainGroup(), asi.getLinkAuthGroup());
			assertEquals("edit", asi.getLinkActionName());
			assertEquals("/do/jacms/Content", asi.getLinkNamespace());
			//assertEquals(1, asi.getLinkParameters().size());
			Properties parameters = asi.getLinkParameters();
			assertEquals(1, parameters.size());
			assertEquals(contentId, parameters.getProperty("contentId"));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._contentManager.deleteContent(content);
			assertNull(this._contentManager.loadContent(contentId, false));
		}
	}
	
	public void testSaveNewContent_2() throws Throwable {
		Content content = this._contentManager.loadContent("EVN41", false);//"coach" group
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.ADD);
		content.setId(null);
		String contentId = null;
		try {
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			contentId = content.getId();
			assertNotNull(this._contentManager.loadContent(contentId, false));
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			
			ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("admin", null, null, null, null, null);
			List<Integer> ids = this._actionLoggerManager.getActionRecords(searchBean);
			assertEquals(1, ids.size());
			
			UserDetails editorCustomers = super.getUser("editorCustomers");
			List<Integer> activityStreamCustomerUser = this._actionLoggerManager.getActivityStream(editorCustomers);
			assertEquals(0, activityStreamCustomerUser.size());
			
			UserDetails editorCoach = super.getUser("editorCoach");
			List<Integer> activityStreamCoachUser = this._actionLoggerManager.getActivityStream(editorCoach);
			assertEquals(1, activityStreamCoachUser.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._contentManager.deleteContent(content);
			assertNull(this._contentManager.loadContent(contentId, false));
		}
	}
	
	
	public void testActivityStreamSearchBean() throws Throwable {
		Content content = this._contentManager.loadContent("EVN41", false);//"coach" group
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.ADD);
		content.setId(null);
		String contentId = null;
		Date dateBeforeSave = new Date();
		Thread.sleep(1000);
		try {
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			contentId = content.getId();
			assertNotNull(this._contentManager.loadContent(contentId, false));
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			Date firstDate = new Date();
			ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("admin", null, null, null, null, null);
			List<Integer> ids = this._actionLoggerManager.getActionRecords(searchBean);
			assertEquals(1, ids.size());
			
			ActivityStreamSeachBean activityStreamSeachBean = new ActivityStreamSeachBean();
			activityStreamSeachBean.setEndCreation(firstDate);
			List<Integer> activityStreamEndDate = this._actionLoggerManager.getActivityStream(activityStreamSeachBean);
			assertEquals(1, activityStreamEndDate.size());
			
			activityStreamSeachBean = new ActivityStreamSeachBean();
			activityStreamSeachBean.setEndUpdate(dateBeforeSave);
			List<Integer> activityStreamDateBeforeSave = this._actionLoggerManager.getActivityStream(activityStreamSeachBean);
			assertEquals(0, activityStreamDateBeforeSave.size());
			
			Thread.sleep(1000);
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			contentId = content.getId();
			assertNotNull(this._contentManager.loadContent(contentId, false));
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			
			activityStreamSeachBean = new ActivityStreamSeachBean();
			activityStreamSeachBean.setStartUpdate(dateBeforeSave);
			activityStreamSeachBean.setEndUpdate(firstDate);
			List<Integer> activityStreamBetweenSave = this._actionLoggerManager.getActivityStream(activityStreamSeachBean);
			assertEquals(1, activityStreamBetweenSave.size());
			
			activityStreamSeachBean = new ActivityStreamSeachBean();
			activityStreamSeachBean.setStartUpdate(dateBeforeSave);
			activityStreamSeachBean.setEndUpdate(new Date());
			List<Integer> activityStreamBetweenSave2 = this._actionLoggerManager.getActivityStream(activityStreamSeachBean);
			assertEquals(2, activityStreamBetweenSave2.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._contentManager.deleteContent(content);
			assertNull(this._contentManager.loadContent(contentId, false));
		}
	}
	
	public void testLastUpdate() throws Throwable {
		Content content = this._contentManager.loadContent("EVN41", false);//"coach" group
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
			contentId = content.getId();
			assertNotNull(this._contentManager.loadContent(contentId, false));
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("admin", null, null, null, null, null);
			List<Integer> ids = this._actionLoggerManager.getActionRecords(searchBean);
			assertEquals(1, ids.size());
			
			Thread.sleep(1000);
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker, content);
			this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
			this.setUserOnSession("admin");
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			contentId = content.getId();
			assertNotNull(this._contentManager.loadContent(contentId, false));
			super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
			
			List<Integer> actionRecords = this._actionLoggerManager.getActionRecords(null);
			
			assertNotNull(actionRecords);
			assertEquals(2, actionRecords.size());
			ActionLogRecord actionRecord = this._actionLoggerManager.getActionRecord(actionRecords.get(1));
			UserDetails adminUser = this.getUser("admin", "admin");
			Date lastUpdateDate = this._actionLoggerManager.lastUpdateDate(adminUser);
			assertEquals(actionRecord.getUpdateDate(), lastUpdateDate);

		} catch (Throwable t) {
			throw t;
		} finally {
			this._contentManager.deleteContent(content);
			assertNull(this._contentManager.loadContent(contentId, false));
		}
	}
	
	protected void initContentAction(String namespace, String name, String contentOnSessionMarker) throws Exception {
		this.initAction(namespace, name);
		this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
	}
	
	private void init() {
		this._actionLoggerManager = (IActionLogManager) this.getService(SystemConstants.ACTION_LOGGER_MANAGER);
		this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		this._langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		this._helper = new ActionLoggerTestHelper(this.getApplicationContext());
	}
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanRecords();
		super.tearDown();
	}
	
	private IActionLogManager _actionLoggerManager;
	private IPageManager _pageManager = null;
	private ILangManager _langManager = null;
	private IContentManager _contentManager = null;
	private ActionLoggerTestHelper _helper;
	
}
