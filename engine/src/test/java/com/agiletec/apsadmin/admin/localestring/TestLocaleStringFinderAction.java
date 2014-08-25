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
		assertEquals(9, localeStrings.size());
		
		Map<String, ApsProperties> labels = finderAction.getLabels();
		assertEquals(9, labels.size());
		
		String firstLocaleString = (String) localeStrings.get(0);
		assertTrue(labels.containsKey(firstLocaleString));
		
		List<Lang> systemLangs = finderAction.getSystemLangs();
		assertEquals(2, systemLangs.size());
	}
	
	public void testSearch() throws Throwable {
		String result = this.executeSearch("admin", "all", "");
		assertEquals(Action.SUCCESS, result);
		List<String> localeStrings = ((LocaleStringFinderAction) this.getAction()).getLocaleStrings();
		assertEquals(9, localeStrings.size());
		
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
		
		result = this.executeSearch("admin", "en", "page mod");
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