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
		assertNull(this._i18nManager.getLabelGroup(longKey));
		try {
			String result = this.executeSaveNew("admin", longKey, "itValue", "enValue");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertNotNull(fieldErrors.get("key"));
            assertNull(this._i18nManager.getLabelGroup(longKey));
		} catch (Throwable t) {
			this._i18nManager.deleteLabelGroup(longKey);
			throw t;
		}
	}
	
	public void testFailureSaveNew_3() throws Throwable {
		//key with special characters
		String wrongKey = "test_&HF";
		assertNull(this._i18nManager.getLabelGroup(wrongKey));
		try {
			String result = this.executeSaveNew("admin", wrongKey, "itValue", "enValue");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertNotNull(fieldErrors.get("key"));
            assertNull(this._i18nManager.getLabelGroup(wrongKey));
		} catch (Throwable t) {
			this._i18nManager.deleteLabelGroup(wrongKey);
			throw t;
		}
	}
	
	public void testSaveNew() throws Throwable {
		String key = "NEW_KEY_12";
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
        String key = "NEW_KEY_X";
		assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		try {
			ApsProperties labels = this.prepareLabelProperties("itLabel", "enLabel");
			this._i18nManager.addLabelGroup(key, labels);
			
            // Label Inglese non valorizzata
            String result = this.executeSaveEdit("admin", key, "", "updatedKeyEn");
            assertEquals(Action.INPUT, result);
            Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertNotNull(fieldErrors.get("it"));
            assertNull(fieldErrors.get("en"));

            // Label Italiano e Inglese non valorizzate
            result = this.executeSaveEdit("admin", key, "", "");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertNotNull(fieldErrors.get("it"));
            assertNull(fieldErrors.get("en"));
		} catch(Throwable t) {
			throw t;
		} finally {
			this._i18nManager.deleteLabelGroup(key);
			assertFalse(this._i18nManager.getLabelGroups().containsKey(key));
		}
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
        return this.executeSave(username, key, it, en, ApsAdminSystemConstants.ADD);
	}
	
	private String executeSaveEdit(String username, String key, String it, String en) throws Throwable {
        return this.executeSave(username, key, it, en, ApsAdminSystemConstants.EDIT);
	}
	
	private String executeSave(String username, String key, String it, String en, int strutsAction) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "save");
		this.addParameter("strutsAction", String.valueOf(strutsAction));
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