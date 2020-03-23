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

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @version 1.0
 * @author E.Mezzano
 */
public class TestLocaleStringFinderAction extends ApsAdminBaseTestCase {
	
	public void testList() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		LocaleStringFinderAction finderAction = (LocaleStringFinderAction) this.getAction();
		
		List<String> localeStrings = finderAction.getLocaleStrings();
		assertEquals(10, localeStrings.size());
		
		Map<String, ApsProperties> labels = finderAction.getLabels();
		assertEquals(10, labels.size());
		
		String firstLocaleString = (String) localeStrings.get(0);
		assertTrue(labels.containsKey(firstLocaleString));
		
		List<Lang> systemLangs = finderAction.getSystemLangs();
		assertEquals(2, systemLangs.size());
	}
	
	public void testSearch() throws Throwable {
		String result = this.executeSearch("admin", "all", "");
		assertEquals(Action.SUCCESS, result);
		List<String> localeStrings = ((LocaleStringFinderAction) this.getAction()).getLocaleStrings();
		assertEquals(10, localeStrings.size());
		
		result = this.executeSearch("admin", "all", "XXXXXXX");
		assertEquals(Action.SUCCESS, result);
		localeStrings = ((LocaleStringFinderAction) this.getAction()).getLocaleStrings();
		assertEquals(0, localeStrings.size());
		
		result = this.executeSearch("admin", "labelkey", "PAGE_");
		assertEquals(Action.SUCCESS, result);
		localeStrings = ((LocaleStringFinderAction) this.getAction()).getLocaleStrings();
		assertEquals(2, localeStrings.size());
		
		result = this.executeSearch("admin", "it", "page mod");
		assertEquals(Action.SUCCESS, result);
		localeStrings = ((LocaleStringFinderAction) this.getAction()).getLocaleStrings();
		assertEquals(0, localeStrings.size());
		
		result = this.executeSearch("admin", "en", "page temp");
		assertEquals(Action.SUCCESS, result);
		localeStrings = ((LocaleStringFinderAction) this.getAction()).getLocaleStrings();
		assertEquals(1, localeStrings.size());
	}
	
	private String executeList(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "list");
		String result = this.executeAction();
		return result;
	}
	
	private String executeSearch(String username, String searchOption, String text) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/LocaleString", "list");
		this.addParameter("text", text);
		this.addParameter("searchOption", searchOption);
		String result = this.executeAction();
		return result;
	}
	
}