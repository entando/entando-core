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
package com.agiletec.aps.system.services.lang;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @version 1.0
 * @author M.Diana - W.Ambu - S.Didaci
 */
public class TestLangManager extends BaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetLang() throws ApsSystemException {
		Lang lang = _langManager.getLang("en");
		String langCode = lang.getCode();
		String langDescr = lang.getDescr();
		assertEquals(langCode, "en");
		assertEquals(langDescr, "English");
	}
	
	public void testGetDefaultLang() throws ApsSystemException {
		Lang lang = this._langManager.getDefaultLang();
		String langCode = lang.getCode();
		String langDescr = lang.getDescr();
		assertEquals(langCode, "it");
		assertEquals(langDescr, "Italiano");
	}
	
	public void testGetLangs() throws ApsSystemException {
		List<Lang> langs = this._langManager.getLangs();
		assertEquals(2, langs.size());
		for (int i=0; i<langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
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
		List<Lang> assignableLangs = _langManager.getAssignableLangs();
		assertTrue(!assignableLangs.isEmpty());
		Lang firstLang = (Lang) assignableLangs.get(0);
		assertEquals("om", firstLang.getCode());
		assertEquals("(Afan) Oromo",firstLang.getDescr());
		
		Lang lastLang = (Lang) assignableLangs.get(assignableLangs.size()-1);
		assertEquals("zu", lastLang.getCode());
		assertEquals("Zulu", lastLang.getDescr());
	}
	
	public void testAddUpdateRemoveLang() throws Throwable {
		int systemLangs = _langManager.getLangs().size();
		try {
			this._langManager.addLang("ro");
			Lang addedLang = _langManager.getLang("ro");
			assertEquals("ro", addedLang.getCode());
			assertEquals("Romanian", addedLang.getDescr());
			assertEquals(systemLangs+1, _langManager.getLangs().size());
			
			this._langManager.updateLang("ro", "New Descr Romanian Lang");
			addedLang = _langManager.getLang("ro");
			assertEquals("ro", addedLang.getCode());
			assertEquals("New Descr Romanian Lang", addedLang.getDescr());
			assertEquals(systemLangs+1, _langManager.getLangs().size());
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this._langManager.removeLang("ro");
			assertNull(_langManager.getLang("ro"));
			assertEquals(systemLangs, _langManager.getLangs().size());
		}
	}
	
	private void init() throws Exception {
    	try {
    		_langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private ILangManager _langManager = null;
    
}
