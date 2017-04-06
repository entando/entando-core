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
package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.IContentBulkActionHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.Action;

public class TestContentGroupBulkAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testUserNotAllowed() throws Throwable {
		String[] contentIds = {"ART1", "RAH101", "EVN103"};
		String[] groupCodes = {"free"};
		int strutsAction = ApsAdminSystemConstants.ADD;
		String currentUser = "pageManagerCoach";
		
		String result = this.executeEntry(currentUser, strutsAction, contentIds);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
		
		result = this.executeJoinGroup(currentUser, strutsAction, contentIds, groupCodes, "coach");
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
		
		result = this.executeDisjoinGroup(currentUser, strutsAction, contentIds, groupCodes, "coach");
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
		
		result = this.executeCheckApply(currentUser, strutsAction, contentIds, groupCodes);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
		
		result = this.executeApply(currentUser, strutsAction, contentIds, groupCodes);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
		
		result = this.executeCheckResult(currentUser, null);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
		
		result = this.executeViewResult(currentUser, null);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
	}

	public void testEntryCheckApply() throws Throwable {
		String currentUser = "editorCustomers";
		String[] contentIds = new String[] {"ART1", "RAH101", "EVN103"};
		String[] groupCodes = new String[] {"administrators", "customers"};
		
		String result = this.executeEntry(currentUser, ApsAdminSystemConstants.ADD, contentIds);
		assertEquals(Action.SUCCESS, result);
		this.checkItems(contentIds, ((ContentGroupBulkAction) this.getAction()).getSelectedIds());
		
		result = this.executeEntry(currentUser, ApsAdminSystemConstants.DELETE, contentIds);
		assertEquals(Action.SUCCESS, result);
		this.checkItems(contentIds, ((ContentGroupBulkAction) this.getAction()).getSelectedIds());
		
		result = this.executeCheckApply(currentUser, ApsAdminSystemConstants.ADD, contentIds, groupCodes);
		assertEquals(Action.INPUT, result);
		
		result = this.executeCheckApply("mainEditor", ApsAdminSystemConstants.ADD, contentIds, groupCodes);
		assertEquals(Action.SUCCESS, result);
		this.checkItems(contentIds, ((ContentGroupBulkAction) this.getAction()).getSelectedIds());
		this.checkItems(groupCodes, ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames());
	}

	public void testJoinDisjoin() throws Throwable {
		String[] contentIds = new String[] {"ART1", "RAH101", "EVN103"};
		String[] groupCodes = new String[] {"administrators", "customers"};
		String username = "editorCustomers";
		
		this.executeJoinGroup(username, ApsAdminSystemConstants.ADD, contentIds, null, "coach");
		Collection<String> foundGroupCodes = ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames();
		this.checkItems(new String[] {"coach"}, foundGroupCodes);
		
		this.executeJoinGroup(username, ApsAdminSystemConstants.DELETE, contentIds, groupCodes, "customers");
		foundGroupCodes = ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames();
		this.checkItems(new String[] {"administrators", "customers"}, foundGroupCodes);
		
		this.executeJoinGroup(username, ApsAdminSystemConstants.DELETE, contentIds, groupCodes, "coach");
		foundGroupCodes = ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames();
		this.checkItems(new String[] {"administrators", "customers", "coach"}, foundGroupCodes);
		
		this.executeDisjoinGroup(username, ApsAdminSystemConstants.ADD, contentIds, null, "customers");
		foundGroupCodes = ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames();
		this.checkItems(new String[] {}, foundGroupCodes);
		
		this.executeDisjoinGroup(username, ApsAdminSystemConstants.DELETE, contentIds, groupCodes, "administrators");
		foundGroupCodes = ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames();
		this.checkItems(new String[] {"customers"}, foundGroupCodes);
		
		this.executeDisjoinGroup(username, ApsAdminSystemConstants.ADD, contentIds, groupCodes, "coach");
		foundGroupCodes = ((ContentGroupBulkAction) this.getAction()).getExtraGroupNames();
		this.checkItems(new String[] {"administrators", "customers"}, foundGroupCodes);
	}

	public void testApplyAddRemove() throws Throwable {
		String currentUser = "mainEditor";
		String[] groupCodes = new String[] {"administrators", "customers"};
		int size = 8;
		List<String> contentList = this.addContents("ART1", size);
		try {
			String[] contentIds = contentList.toArray(new String[0]);
			String result = this.executeApply(currentUser, ApsAdminSystemConstants.ADD, contentIds, groupCodes);
			assertEquals(Action.SUCCESS, result);
			ContentGroupBulkAction action = (ContentGroupBulkAction) this.getAction();
			this.checkItems(contentIds, action.getSelectedIds());
			this.checkItems(groupCodes, action.getExtraGroupNames());
			String commandId = action.getCommandId();
			assertNotNull(commandId);
			
			result = this.executeCheckResult(currentUser, commandId);
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeViewResult(currentUser, commandId);
			assertEquals(Action.SUCCESS, result);

			commandId = ((ContentBulkAction) this.getAction()).getCommandId();
			this.checkReport(commandId, size, size, size, 0, ApsCommandStatus.COMPLETED);

			this.checkContentGroups(contentIds, groupCodes, true, false);
			result = this.executeApply(currentUser, ApsAdminSystemConstants.DELETE, contentIds, groupCodes);
			this.checkContentGroups(contentIds, groupCodes, false, false);
			commandId = ((ContentGroupBulkAction) this.getAction()).getCommandId();
			
			this.checkReport(commandId, size, size, size, 0, ApsCommandStatus.COMPLETED);
		} finally {
			this.deleteContents(contentList);
		}
	}

	private void checkContentGroups(String[] contentIds, String[] groupCodes, boolean expectedWork, boolean expectedOnline) throws ApsSystemException {
		for (String contentId : contentIds) {
			Content current = this._contentManager.loadContent(contentId, false);
			Collection<String> contentGroups = current.getGroups();
			for (String group : groupCodes) {
				assertEquals(expectedWork, contentGroups.contains(group));
			}
			if (current.isOnLine()) {
				current = this._contentManager.loadContent(contentId, true);
				contentGroups = current.getGroups();
				for (String group : groupCodes) {
					assertEquals(expectedOnline, contentGroups.contains(group));
				}
			}
		}
	}

	private List<String> addContents(String masterContentId, int size) throws ApsSystemException {
		List<String> contentIds = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			Content current = this._contentManager.loadContent(masterContentId, false);
			current.setId(null);
			this._contentManager.addContent(current);
			contentIds.add(current.getId());
			if (i % 2 == 0) {
				this._contentManager.insertOnLineContent(current);
			}
		}
		return contentIds;
	}
	
	private void deleteContents(List<String> contentIds) throws ApsSystemException {
		for (String contentId : contentIds) {
			Content current = this._contentManager.loadContent(contentId, false);
			this._contentManager.deleteContent(current);
		}
	}
	
	private void checkItems(String[] expected, Collection<?> actual) {
		assertEquals(expected.length, actual.size());
		for (Object current : expected) {
			assertTrue(actual.contains(current));
		}
	}
	
	private String executeEntry(String currentUser, int strutsAction, String[] contentIds) throws Throwable {
		return this.executeGroupAction(currentUser, "entry", strutsAction, contentIds, null, null);
	}
	
	private String executeJoinGroup(String currentUser, int strutsAction, String[] contentIds, String[] groupCodes, String groupName) throws Throwable {
		return this.executeGroupAction(currentUser, "join", strutsAction, contentIds, groupCodes, groupName);
	}
	
	private String executeDisjoinGroup(String currentUser, int strutsAction, String[] contentIds, String[] groupCodes, String groupName) throws Throwable {
		return this.executeGroupAction(currentUser, "disjoin", strutsAction, contentIds, groupCodes, groupName);
	}
	
	private String executeCheckApply(String currentUser, int strutsAction, String[] contentIds, String[] groupCodes) throws Throwable {
		return this.executeGroupAction(currentUser, "checkApply", strutsAction, contentIds, groupCodes, null);
	}
	
	private String executeApply(String currentUser, int strutsAction, String[] contentIds, String[] groupCodes) throws Throwable {
		return this.executeGroupAction(currentUser, "apply", strutsAction, contentIds, groupCodes, null);
	}
	
	private String executeCheckResult(String currentUser, String commandId) throws Throwable {
		return this.executeCommandAction(currentUser, "checkResult", commandId);
	}
	
	private String executeViewResult(String currentUser, String commandId) throws Throwable {
		return this.executeCommandAction(currentUser, "viewResult", commandId);
	}
	
	private String executeCommandAction(String currentUser, String actionName, String commandId) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction(NAMESPACE, actionName);
		this.addParameter("commandId", commandId);
		return this.executeAction();
	}
	
	private String executeGroupAction(String currentUser, String actionName, int strutsAction, String[] contentIds, String[] groupCodes, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction(NAMESPACE, actionName);
		this.addParameter("strutsAction", strutsAction);
		this.addParameter("selectedIds", contentIds);
		this.addParameter("extraGroupNames", groupCodes);
		this.addParameter("groupName", groupName);
		return this.executeAction();
	}
	
	private void checkReport(String commandId, int total, int applyTotal, int applySuccesses, int applyErrors, ApsCommandStatus status) {
		BulkCommandReport<?> report = this._bulkCommandManager.getCommandReport(IContentBulkActionHelper.BULK_COMMAND_OWNER, commandId);
		assertEquals(total, report.getTotal());
		assertEquals(applyTotal, report.getApplyTotal());
		assertEquals(applySuccesses, report.getApplySuccesses());
		assertEquals(applyErrors, report.getApplyErrors());
		assertEquals(status, report.getStatus());
		if (total != applyTotal) {
			assertNull(report.getEndingTime());
		} else {
			assertNotNull(report.getEndingTime());
		}
	}
	
	private void init() {
		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		this._bulkCommandManager = (IBulkCommandManager) this.getApplicationContext().getBean(SystemConstants.BULK_COMMAND_MANAGER);
	}

	private IContentManager _contentManager;
	private IBulkCommandManager _bulkCommandManager;
	
	private static final String NAMESPACE = "/do/jacms/Content/Group";

}
