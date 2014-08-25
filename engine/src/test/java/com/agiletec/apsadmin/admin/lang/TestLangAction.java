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
package com.agiletec.apsadmin.admin.lang;

import java.util.Collection;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @version 1.0
 * @author E.Mezzano
 */
public class TestLangAction extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testAddNewLang() throws Throwable {
		String langCode = "fr";
		try {
			//utente non autorizzato
			String result = this.executeAddLang("mainEditor", langCode);
			assertEquals("userNotAllowed", result);
			
			result = this.executeAddLang("admin", langCode);
			assertEquals(Action.SUCCESS, result);
			Lang language = this._langManager.getLang(langCode);
			assertEquals(language.getDescr(), "French");
		} catch (Throwable t) {
			throw t;
		} finally {
			this._langManager.removeLang(langCode);
		}
	}
	
	public void testFailureAddNewLang() throws Throwable {
		String langCode = "fr";
		try {
			String result = this.executeAddLang("admin", langCode);
			assertEquals(Action.SUCCESS, result);
			Lang language = this._langManager.getLang(langCode);
			assertEquals(language.getDescr(), "French");
			result = this.executeAddLang("admin", langCode);
			assertEquals(Action.INPUT, result);
			result = this.executeAddLang("admin", "en");
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._langManager.removeLang(langCode);
		}
	}
	
	public void testRemoveLang() throws Throwable {
		String langCode = "fr";
		try {
			this._langManager.addLang(langCode);
			String result = this.executeRemoveLang("admin", langCode);
			assertEquals(Action.SUCCESS, result);
			assertNull(this._langManager.getLang(langCode));
		} catch (Throwable t) {
			this._langManager.removeLang(langCode);
			throw t;
		}
	}
	
	public void testFailureRemoveDefaultLang() throws Throwable {
		int initLangs = this._langManager.getLangs().size();
		Lang defaultLang = this._langManager.getDefaultLang();
		
		String result = this.executeRemoveLang("admin", defaultLang.getCode());
		assertEquals(Action.INPUT, result);
		assertEquals(initLangs, this._langManager.getLangs().size());
		ActionSupport action = this.getAction();
		Collection<String> errors = action.getActionErrors();
		assertEquals(1, errors.size());
	}
	
	private String executeAddLang(String username, String langCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Lang", "add");
		this.addParameter("langCode", langCode);
		String result = this.executeAction();
		return result;
	}
	
	private String executeRemoveLang(String username, String langCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Lang", "remove");
		this.addParameter("langCode", langCode);
		String result = this.executeAction();
		return result;
	}
	
	private void init() throws Exception {
		try {
			this._langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	private ILangManager _langManager;
	
}