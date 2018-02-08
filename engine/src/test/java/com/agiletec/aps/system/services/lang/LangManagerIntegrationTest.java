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
package com.agiletec.aps.system.services.lang;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @version 1.0
 * @author M.Diana - W.Ambu - S.Didaci
 */
public class LangManagerIntegrationTest extends BaseTestCase {

	private ILangManager langManager = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetLang() throws ApsSystemException {
		Lang lang = this.langManager.getLang("en");
		String langCode = lang.getCode();
		String langDescr = lang.getDescr();
		assertEquals(langCode, "en");
		assertEquals(langDescr, "English");
	}

	public void testGetDefaultLang() throws ApsSystemException {
		Lang lang = this.langManager.getDefaultLang();
		String langCode = lang.getCode();
		String langDescr = lang.getDescr();
		assertEquals(langCode, "it");
		assertEquals(langDescr, "Italiano");
	}

	public void testGetLangs() throws ApsSystemException {
		List<Lang> langs = this.langManager.getLangs();
		assertEquals(2, langs.size());
		for (Lang lang : langs) {
			String code = lang.getCode();
			if (code.equals("it")) {
				assertEquals("Italiano", lang.getDescr());
			} else if (code.equals("en")) {
				assertEquals("English", lang.getDescr());
			} else {
				fail();
			}
		}
	}

	public void testGetAssignableLangs() throws Throwable {
		List<Lang> assignableLangs = this.langManager.getAssignableLangs();
		assertTrue(!assignableLangs.isEmpty());
		Lang firstLang = (Lang) assignableLangs.get(0);
		assertEquals("om", firstLang.getCode());
		assertEquals("(Afan) Oromo", firstLang.getDescr());

		Lang lastLang = (Lang) assignableLangs.get(assignableLangs.size() - 1);
		assertEquals("zu", lastLang.getCode());
		assertEquals("Zulu", lastLang.getDescr());
	}

	public void testAddUpdateRemoveLang() throws Throwable {
		int systemLangs = this.langManager.getLangs().size();
		try {
			this.langManager.addLang("ro");
			Lang addedLang = this.langManager.getLang("ro");
			assertEquals("ro", addedLang.getCode());
			assertEquals("Romanian", addedLang.getDescr());
			assertEquals(systemLangs + 1, this.langManager.getLangs().size());

			this.langManager.updateLang("ro", "New Descr Romanian Lang");
			addedLang = this.langManager.getLang("ro");
			assertEquals("ro", addedLang.getCode());
			assertEquals("New Descr Romanian Lang", addedLang.getDescr());
			assertEquals(systemLangs + 1, this.langManager.getLangs().size());

		} catch (Throwable t) {
			throw t;
		} finally {
			this.langManager.removeLang("ro");
			assertNull(this.langManager.getLang("ro"));
			assertEquals(systemLangs, this.langManager.getLangs().size());
		}
	}

	private void init() throws Exception {
		try {
			this.langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

}
