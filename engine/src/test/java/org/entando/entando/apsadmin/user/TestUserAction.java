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
package org.entando.entando.apsadmin.user;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Santoboni, E.Mezzano
 */
public class TestUserAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNew() throws Throwable {
		String result = this.executeNew("developersConf");
		assertEquals("apslogin", result);
		
		result = this.executeNew("admin");
		assertEquals(Action.SUCCESS, result);
		UserAction action = (UserAction) this.getAction();
		assertNull(action.getUsername());
	}
	
	public void testFailureEdit() throws Throwable {
		// Utente non abilitato
		String result = this.executeNew("developersConf");
		assertEquals("apslogin", result);
		
		// Modifica utente admin
		result = this.executeEdit("admin", "admin");
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testEdit() throws Throwable {
		String result = this.executeEdit("admin", "mainEditor");
		assertEquals(Action.SUCCESS, result);
		UserAction action = (UserAction) this.getAction();
		assertNotNull(action.getUsername());
	}
	
	public void testAddNew_1() throws Throwable {
		String username = "usernameForTest";
		String password = "password";
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/User", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			this.addParameter("username", username);
			this.addParameter("password", password);
			this.addParameter("passwordConfirm", password);
			this.addParameter("active", "true");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			UserDetails extracted = this._userManager.getUser(username, password);
			assertNotNull(extracted);
			assertFalse(extracted.isDisabled());
		} catch(Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testAddNew_2() throws Throwable {
		String username = "user.name_for_test"; // the dot '.' is accepted in the username as well as the underscore '_'
		String password = "password";
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/User", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			this.addParameter("username", username);
			this.addParameter("password", password);
			this.addParameter("passwordConfirm", password);
			this.addParameter("active", "true");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			UserDetails extracted = this._userManager.getUser(username, password);
			assertNotNull(extracted);
			assertFalse(extracted.isDisabled());
		} catch(Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
			assertNull(this._userManager.getUser(username));
		}
	}
	
	public void testAddEditDelete() throws Throwable {
		String username = "username";
		String password = "password";
		String newPassword = "pluto1234";
		try {
			this.addUser(username, password);
			this.setUserOnSession("admin");
			this.initAction("/do/User", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("username", username);
			this.addParameter("password", newPassword);
			this.addParameter("passwordConfirm", newPassword);
			this.addParameter("active", "true");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			UserDetails extracted = this._userManager.getUser(username, newPassword);
			assertNotNull(extracted);
			assertFalse(extracted.isDisabled());
			
			this.initAction("/do/User", "delete");
			this.addParameter("username", username);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			extracted = this._userManager.getUser(username);
			assertNull(extracted);
		} catch(Throwable t) {
			this._userManager.removeUser(username);
			throw t;
		}
	}
	
	public void testFailureDisableAdminUser() throws Throwable {
		String username = SystemConstants.ADMIN_USER_NAME;
		UserDetails adminUser = this._userManager.getUser(username);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/User", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("username", username);
			this.addParameter("active", "false");
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			List<String> errors = fieldErrors.get("active");
			assertEquals(1, errors.size());
		} catch(Throwable t) {
			this._userManager.updateUser(adminUser);
			throw t;
		}
	}
	
	public void testSaveNewFailure() throws Throwable {
		UserDetails oldUser = this._userManager.getUser("admin");
		try {
			// permessi non disponibili
			String result = this.executeSaveNew("developersConf", "username", "password", "password");
			assertEquals("apslogin", result);
			
			// username già presente
			result = this.executeSaveNew("admin", "admin", "password", "password");
			this.verifyErrors(result, 1, "username", 1);
			
			// username non valorizzato
			result = this.executeSaveNew("admin", "", "password", "password");
			this.verifyErrors(result, 1, "username", 1);
			
			// username troppo lunga
			result = this.executeSaveNew("admin", "usernameDecisamenteTroppoLunga", "password", "password");
			this.verifyErrors(result, 1, "username", 1);
			
			// username con caratteri non consentiti
			result = this.executeSaveNew("admin", "user name", "password", "password");
			this.verifyErrors(result, 1, "username", 1);
			
			// username con caratteri non consentiti - 2
			result = this.executeSaveNew("admin", "user\name", "password", "password");
			this.verifyErrors(result, 1, "username", 1);
						
			// password troppo corta
			result = this.executeSaveNew("admin", "test", "p", "p");
			this.verifyErrors(result, 1, "password", 1);
			
			// password troppo lunga
			result = this.executeSaveNew("admin", "test", "passwordDecisamenteTroppoLunga", "passwordDecisamenteTroppoLunga");
			this.verifyErrors(result, 1, "password", 1);
			
			// password con caratteri non consentiti
			result = this.executeSaveNew("admin", "test", "&345bnr67fg$", "&345bnr67fg$");
			this.verifyErrors(result, 1, "password", 1);
		} catch (RuntimeException e) {
			this._userManager.updateUser(oldUser);
			throw e;
		}
	}
	
	private String executeSaveNew(String currentUser, String username, String password, String passwordConfirm) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		this.addParameter("username", username);
		this.addParameter("password", password);
		this.addParameter("passwordConfirm", passwordConfirm);
		return this.executeAction();
	}
	
	public void testSaveEditFailure() throws Throwable {
		this.setUserOnSession("admin");
		String usernameForTest = "testUserName";
		this.addUser(usernameForTest, "password");
		try {
			// password non valorizzata
			String result = this.executeSaveEdit("admin", usernameForTest, "", "password");
			this.verifyErrors(result, 1, "password", 1);
			
			// passwordConfirm non valorizzata
			result = this.executeSaveEdit("admin", usernameForTest, "password", "");
			this.verifyErrors(result, 1, "password", 1);
			
			// password e passwordConfirm non identiche
			result = this.executeSaveEdit("admin", usernameForTest, "password", "passwordConfirm");
			this.verifyErrors(result, 1, "password", 1);
			
			// password troppo lunga
			result = this.executeSaveEdit("admin", usernameForTest, "passwordDecisamenteTroppoLunga", "passwordDecisamenteTroppoLunga");
			this.verifyErrors(result, 1, "password", 1);
			
			// password con caratteri speciali
			result = this.executeSaveEdit("admin", usernameForTest, "pass&word", "pass&word");
			this.verifyErrors(result, 1, "password", 1);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(usernameForTest);
		}
	}
	
	private void verifyErrors(String result, int extectedFieldErrors, String fieldWithErrors, int expectedErrorsOnField) {
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(extectedFieldErrors, fieldErrors.size());
		List<String> errors = fieldErrors.get(fieldWithErrors);
		assertEquals(expectedErrorsOnField, errors.size());
	}
	
	public void testTrash() throws Throwable {
		String username = "username";
		String password = "password";
		try {
			this.addUser(username, password);
			
			String result = this.executeTrash("admin", username);
			assertEquals(Action.SUCCESS, result);
			assertNotNull(this._userManager.getUser(username));
		} catch(Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testFailureTrash() throws Throwable {
		String result = this.executeTrash("developersConf", "editor");
		assertEquals("apslogin", result);
		
		result = this.executeTrash("admin", "admin");
		assertEquals("userList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testDelete() throws Throwable {
		String username = "user.name_to_delete";
		String password = "password";
		try {
			this.addUser(username, password);
			String result = this.executeDelete("admin", username);
			assertEquals(Action.SUCCESS, result);
			assertNull(this._userManager.getUser(username));
		} catch(Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	public void testFailureDelete() throws Throwable {
		String result = this.executeDelete("developersConf", "editor");
		assertEquals("apslogin", result);
		
		result = this.executeDelete("admin", "admin");
		assertEquals("userList", result);
		Collection<String> actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testResetUser() throws Throwable {
		String username = "username";
		String password = "password";
		String datePattern = "ddMMyyyy";
		String today = DateConverter.getFormattedDate(new Date(), datePattern);
		try {
			this.addUser(username, password);
			User extracted = (User) this._userManager.getUser(username, password);
			assertEquals(today, DateConverter.getFormattedDate(extracted.getCreationDate(), datePattern));
			assertNull(extracted.getLastAccess());
			assertNull(extracted.getLastPasswordChange());
			
			this.setUserOnSession("admin");
			this.initAction("/do/User", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("username", username);
			this.addParameter("reset", "true");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			extracted = (User) this._userManager.getUser(username, password);
			assertNotNull(extracted);
			assertEquals(today, DateConverter.getFormattedDate(extracted.getCreationDate(), datePattern));
			assertEquals(today, DateConverter.getFormattedDate(extracted.getLastAccess(), datePattern));
			assertEquals(today, DateConverter.getFormattedDate(extracted.getLastPasswordChange(), datePattern));
		} catch(Throwable t) {
			throw t;
		} finally {
			this._userManager.removeUser(username);
		}
	}
	
	private String executeNew(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "new");
		return this.executeAction();
	}
	
	private String executeEdit(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "edit");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private String executeTrash(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "trash");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private String executeDelete(String currentUser, String username) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "delete");
		this.addParameter("username", username);
		return this.executeAction();
	}
	
	private String executeSaveEdit(String currentUser, String username, String password, String passwordConfirm) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/User", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
		this.addParameter("username", username);
		this.addParameter("password", password);
		this.addParameter("passwordConfirm", passwordConfirm);
		return this.executeAction();
	}
	
	private void addUser(String username, String password) throws ApsSystemException {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		this._userManager.addUser(user);
	}
	
	private void init() {
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
	}

	private IUserManager _userManager;
	
}