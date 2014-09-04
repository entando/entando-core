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
package com.agiletec.apsadmin.admin.localestring;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Mezzano
 */
public class TestLocaleStringAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNew() throws Throwable {
		String result = this.executeNew("admin");
		assertEquals(Action.SUCCESS, result);
		LocaleStringAction localeStringAction = (LocaleStringAction) this.getAction();
		assertEquals(2, localeStringAction.getLangs().size());
		assertEquals(0, localeStringAction.getLabels().size());
	}
	
	public void testEdit() throws Throwable {
		assertEquals(Action.SUCCESS, this.executeEdit("admin", "PAGE"));
		LocaleStringAction localeStringAction = (LocaleStringAction) this.getAction();
		assertEquals(2, localeStringAction.getLangs().size());
		assertEquals(2, localeStringAction.getLabels().size());
	}
	
	public void testFailureSaveNew_1() throws Throwable {
		// Chiave label duplicata
		String duplicatedKey = "PAGE";
		String result = this.executeSaveNew("admin", duplicatedKey, "newKeyIt", "newKeyEn");
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertNotNull(fieldErrors.get("key"));
		
		String newKey = "NEW_KEY";
		assertFalse(this._i18nManager.getLabelGroups().containsKey(newKey));
		try {
			// Label Italiano non valorizzata
			result = this.executeSaveNew("admin", newKey, "", "enValue");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertNotNull(fieldErrors.get("it"));
			
			// Label Italiano e Inglese non valorizzate
			result = this.executeSaveNew("admin", newKey, "", "");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertNotNull(fieldErrors.get("it"));
			assertNull(fieldErrors.get("en"));
		} catch (Throwable t) {
			this._i18nManager.deleteLabelGroup(newKey);
			throw t;
		}
	}
	
	public void testFailureSaveNew_2() throws Throwable {
		//key length exceed max
		String longKey = "veryLongCategoryCode_veryLongCategoryCode_veryLongCategoryCode";
		assertFalse(this._i18nManager.getLabelGroups().containsKey(longKey));
		try {
			String result = this.executeSaveNew("admin", longKey, "itValue", "enValue");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertNotNull(fieldErrors.get("key"));
		} catch (Throwable t) {
			this._i18nManager.deleteLabelGroup(longKey);
			throw t;
		}
	}
	
	public void testSaveNew() throws Throwable {
		String key = "NEW_KEY";
		assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		try {
			assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
			String result = this.executeSaveNew("admin", key, "newKeyIt", "newKeyEn");
			assertEquals(Action.SUCCESS, result);
			assertTrue(this._i18nManager.getLabelGroups().containsKey(key));
			String labelItaliano = this._i18nManager.getLabel(key, "it");
			assertEquals("newKeyIt", labelItaliano);
			String labelInglese = this._i18nManager.getLabel(key, "en");
			assertEquals("newKeyEn", labelInglese);
		} catch(Throwable t) {
			throw t;
		} finally {
			this._i18nManager.deleteLabelGroup(key);
			assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		}
	}
	
	public void testFailureSaveEdit() throws Throwable {
		String updatedKey = "PAGE";
		
		// Label Inglese non valorizzata
		String result = this.executeSaveEdit("admin", updatedKey, "", "updatedKeyEn");
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertNotNull(fieldErrors.get("it"));
		assertNull(fieldErrors.get("en"));
		
		// Label Italiano e Inglese non valorizzate
		result = this.executeSaveEdit("admin", updatedKey, "", "");
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertNotNull(fieldErrors.get("it"));
		assertNull(fieldErrors.get("en"));
	}
	
	public void testSaveEdit() throws Throwable {
		String key = "NEW_KEY";
		assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		try {
			ApsProperties labels = this.prepareLabelProperties("itLabel", "enLabel");
			this._i18nManager.addLabelGroup(key, labels);
			
			String result = this.executeSaveEdit("admin", key, "updatedKeyIt", "updatedKeyEn");
			assertEquals(Action.SUCCESS, result);
			assertTrue(this._i18nManager.getLabelGroups().containsKey(key));
			assertEquals(this._i18nManager.getLabel(key, "it"), "updatedKeyIt");
			assertEquals(this._i18nManager.getLabel(key, "en"), "updatedKeyEn");
		} catch(Throwable t) {
			throw t;
		} finally {
			this._i18nManager.deleteLabelGroup(key);
			assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		}
	}
	
	public void testSaveDelete() throws Throwable {
		String key = "NEW_KEY";
		try {
			ApsProperties labels = this.prepareLabelProperties("itLabel", "enLabel");
			this._i18nManager.addLabelGroup(key, labels);
			
			String result = this.executeDelete("admin", key);
			assertEquals(Action.SUCCESS, result);
			assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		} catch(Throwable t) {
			throw t;
		} finally {
			this._i18nManager.deleteLabelGroup(key);
		}
	}
	
	private ApsProperties prepareLabelProperties(String itLabel, String enLabel) {
		ApsProperties labels = new ApsProperties();
		labels.setProperty("it", itLabel);
		labels.setProperty("en", enLabel);
		return labels;
	}
	
	private String executeNew(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "new");
		return this.executeAction();
	}
	
	private String executeEdit(String username, String key) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "edit");
		this.addParameter("key", key);
		return this.executeAction();
	}
	
	private String executeSaveNew(String username, String key, String it, String en) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		this.addParameter("key", key);
		this.addParameter("it", it);
		this.addParameter("en", en);
		return this.executeAction();
	}
	
	private String executeSaveEdit(String username, String key, String it, String en) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
		this.addParameter("key", key);
		this.addParameter("it", it);
		this.addParameter("en", en);
		return this.executeAction();
	}
	
	private String executeDelete(String username, String key) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "delete");
		this.addParameter("key", key);
		return this.executeAction();
	}
	
	private void init() throws Exception {
		try {
			this._i18nManager = (II18nManager) this.getService(SystemConstants.I18N_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	private II18nManager _i18nManager;
	
}