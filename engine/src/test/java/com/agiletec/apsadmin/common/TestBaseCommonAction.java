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
package com.agiletec.apsadmin.common;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestBaseCommonAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGoChangePasswordPage() throws Throwable {
    	this.initAction("/do/CurrentUser", "editProfile");
    	this.setUserOnSession("admin");
    	String result = super.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	public void testFailureUpdate() throws Throwable {
		String username = "editorCoach";
		String rightOldPassword = "editorCoach";
		this.setUserOnSession(username);
		UserDetails oldUser = this._userManager.getUser(username);
		try {

			// oldPassword non valorizzata
			String result = this.executeUpdate("", "password", "password");
			this.verifyErrors(result, 1, "oldPassword", 1);

			// oldPassword errata
			result = this.executeUpdate("wrongOldPassword", "password", "password");
			this.verifyErrors(result, 1, "oldPassword", 1);

			// Conferma errata
			result = this.executeUpdate(rightOldPassword, "password", "badConfirm");
			this.verifyErrors(result, 1, "password", 1);

			// password uguale alla vecchia
			result = this.executeUpdate(rightOldPassword, rightOldPassword, rightOldPassword);
			this.verifyErrors(result, 1, "password", 1);

			// password con caratteri non validi corta
			result = this.executeUpdate(rightOldPassword, "p123%456$hj", "p123%456$hj");
			this.verifyErrors(result, 1, "password", 1);

			// password troppo corta
			result = this.executeUpdate(rightOldPassword, "p", "p");
			this.verifyErrors(result, 1, "password", 1);

			// password troppo lunga
			result = this.executeUpdate(rightOldPassword, "passwordDecisamenteTroppoLunga", "passwordDecisamenteTroppoLunga");
			this.verifyErrors(result, 1, "password", 1);
		} catch (RuntimeException e) {
			this._userManager.updateUser(oldUser);
			throw e;
		}
	}

	private void verifyErrors(String result, int extectedFieldErrors, String fieldWithErrors, int expectedErrorsOnField) {
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(extectedFieldErrors, fieldErrors.size());
		List<String> errors = fieldErrors.get(fieldWithErrors);
		assertEquals(expectedErrorsOnField, errors.size());
	}

	public void testSuccessfulUpdate() throws Throwable {
		String username = "editorCoach";
		String rightOldPassword = "editorCoach";
		String newPassword = "newPassword";
		this.setUserOnSession(username);
		UserDetails oldUser = this._userManager.getUser(username);
		try {
			String result = this.executeUpdate(rightOldPassword, newPassword, newPassword);
			assertEquals(Action.SUCCESS, result);

			UserDetails updatedUser = this._userManager.getUser(username, newPassword);
			assertNotNull(updatedUser);
		} catch (RuntimeException e) {
			throw e;
		} finally {
			this._userManager.updateUser(oldUser);
		}
	}

	private String executeUpdate(String oldPassword, String password, String passwordConfirm) throws Throwable {
		this.initAction("/do/CurrentUser", "changePassword");
		this.addParameter("oldPassword", oldPassword);
		this.addParameter("password", password);
		this.addParameter("passwordConfirm", passwordConfirm);
		return this.executeAction();
	}

	private void init() {
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
	}

	private IUserManager _userManager;

}
