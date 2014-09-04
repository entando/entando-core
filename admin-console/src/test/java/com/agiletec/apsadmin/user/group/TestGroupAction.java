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
package com.agiletec.apsadmin.user.group;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;

public class TestGroupAction extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNew() throws Throwable {
		// Utente non autorizzato
		String result = this.executeNew("developersConf");
		assertEquals("apslogin", result);
		
		result = this.executeNew("admin");
		assertEquals(Action.SUCCESS, result);
		GroupAction groupAction = (GroupAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.ADD, groupAction.getStrutsAction());
	}
	
	public void testFailureEdit() throws Throwable {
		// Utente non autorizzato
		String result = this.executeEdit("developersConf", "customers");
		assertEquals("apslogin", result);
		
		// Gruppo inesistente
		result = this.executeEdit("admin", "gruppoInesistente");
		assertEquals("groupList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testEdit() throws Throwable {
		String groupName = "customers";
		String result = this.executeEdit("admin", groupName);
		assertEquals(Action.SUCCESS, result);
		GroupAction groupAction = (GroupAction) this.getAction();
		Group group = this._groupManager.getGroup(groupName);
		assertEquals(ApsAdminSystemConstants.EDIT, groupAction.getStrutsAction());
		assertEquals(group.getName(), groupAction.getName());
		assertEquals(group.getDescr(), groupAction.getDescription());
	}
	
	public void testSaveNew() throws Throwable {
		String groupName = "newGroup";
		try {
			this.executeNew("admin");
			
			String result = this.executeSaveNew("admin", groupName, "groupDescription");
			assertEquals(Action.SUCCESS, result);
			this.checkGroup(groupName, "groupDescription");
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteGroup(groupName);
		}
	}
	
	public void testSaveEdit() throws Throwable {
		String groupName = "newGroup";
		try {
			this.addGroup(groupName, "groupDescription");
			this.executeEdit("admin", groupName);
			
			String result = this.executeSaveEdit("admin", groupName, "modifiedDescription");
			assertEquals(Action.SUCCESS, result);
			this.checkGroup(groupName, "modifiedDescription");
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteGroup(groupName);
		}
	}
	
	public void testFailureSave() throws Throwable {
		this.executeNew("admin");
		// permessi non disponibili
		String result = this.executeSaveNew("developersConf", "groupName", "description");
		assertEquals("apslogin", result);
		
		// groupName già esistente
		result = this.executeSaveNew("admin", "customers", "description");
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> errors = fieldErrors.get("name");
		assertEquals(1, errors.size());
		
		// description non valorizzato
		result = this.executeSaveNew("admin", "groupName", "");
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		errors = fieldErrors.get("description");
		assertEquals(1, errors.size());
		
		// name e description non valorizzati
		result = this.executeSaveNew("admin", "", "");
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		errors = fieldErrors.get("name");
		assertEquals(1, errors.size());
		errors = fieldErrors.get("description");
		assertEquals(1, errors.size());
		
		// name troppo lungo
		result = this.executeSaveNew("admin", "groupNameDecisamenteTroppoLungo", "description");
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		errors = fieldErrors.get("name");
		assertEquals(1, errors.size());
	}
	
	public void testFailureTrash() throws Throwable {
		// permessi non disponibili
		String result = this.executeTrash("developersConf", "customers");
		assertEquals("apslogin", result);
		
		// Rimozione gruppo administrators non consentita
		result = this.executeTrash("admin", "administrators");
		assertEquals("groupList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
		
		// Rimozione gruppo inesistente
		result = this.executeTrash("admin", "gruppoInesistente");
		assertEquals("groupList", result);
		actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testTrash() throws Throwable {
		String result = this.executeTrash("admin", "management");
		assertEquals(Action.SUCCESS, result);
		assertNotNull(this._groupManager.getGroup("management"));
	}
	
	public void testFailureTrashReferencedGroup() throws Throwable {
		String result = this.executeTrash("admin", "customers");
		assertEquals("references", result);
		GroupAction groupAction = (GroupAction) this.getAction();
		Map<String, List<Object>> references = groupAction.getReferences();
		assertEquals(4, references.size());
		List<Object> pages = references.get("PageManagerUtilizers");
		assertEquals(3, pages.size());
		List<Object> users = references.get("UserManagerUtilizers");
		assertEquals(6, users.size());
	}
	
	public void testDelete() throws Throwable {
		String groupName = "newGroup";
		try {
			this.addGroup(groupName, "groupDescription");
			String result = this.executeDelete("admin", groupName);
			assertEquals(Action.SUCCESS, result);
			assertNull(this._groupManager.getGroup(groupName));
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteGroup(groupName);
		}
	}
	
	public void testFailureDelete() throws Throwable {
		// permessi non disponibili
		String result = this.executeDelete("developersConf", "customers");
		assertEquals("apslogin", result);
		
		// Rimozione gruppo administrators non consentita
		result = this.executeDelete("admin", "administrators");
		assertEquals("groupList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
		
		// Rimozione gruppo inesistente
		result = this.executeDelete("admin", "gruppoInesistente");
		assertEquals("groupList", result);
		actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
		
		result = this.executeDelete("admin", "customers");
		assertEquals("references", result);
	}
	
	private String executeNew(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "new");
		return this.executeAction();
	}
	
	private String executeEdit(String currentUser, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "edit");
		this.addParameter("name", groupName);
		return this.executeAction();
	}
	
	private String executeSaveNew(String currentUser, String groupName, String descr) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		this.addParameter("name", groupName);
		this.addParameter("description", descr);
		return this.executeAction();
	}
	
	private String executeSaveEdit(String currentUser, String groupName, String descr) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
		this.addParameter("name", groupName);
		this.addParameter("description", descr);
		return this.executeAction();
	}
	
	private String executeTrash(String currentUser, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "trash");
		this.addParameter("name", groupName);
		return this.executeAction();
	}
	
	private String executeDelete(String currentUser, String groupName) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/Group", "delete");
		this.addParameter("name", groupName);
		return this.executeAction();
	}
	
	private void checkGroup(String groupName, String descr) {
		Group group = this._groupManager.getGroup(groupName);
		assertEquals(group.getName(), groupName);
		assertEquals(group.getDescr(), descr);
	}
	
	private void addGroup(String name, String descr) throws ApsSystemException {
		Group group = new Group();
		group.setName(name);
		group.setDescr(descr);
		this._groupManager.addGroup(group);
	}
	
	private void deleteGroup(String groupName) throws ApsSystemException {
		Group group = this._groupManager.getGroup(groupName);
		if (group!=null) {
			this._groupManager.removeGroup(group);
		}
	}
	
	private void init() {
		this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
	}

	private IGroupManager _groupManager;
	
}