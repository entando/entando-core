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
package com.agiletec.apsadmin.system.services;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.services.shortcut.IShortcutManager;
import com.agiletec.apsadmin.system.services.shortcut.model.Shortcut;

/**
 * @author E.Santoboni
 */
public class TestShortcutManager extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testGetAllowedShortcuts() throws Throwable {
    	assertNotNull(this._shortcutManager);
    	String expectedShortcut = "core.tools.setting";
    	UserDetails adminUser = super.getUser("admin");
    	List<Shortcut> shortcuts = this._shortcutManager.getAllowedShortcuts(adminUser);
    	assertTrue(this.containsShortcut(shortcuts, expectedShortcut));
    	
    	UserDetails editorCoach = super.getUser("editorCoach");
    	shortcuts = this._shortcutManager.getAllowedShortcuts(editorCoach);
    	assertFalse(this.containsShortcut(shortcuts, expectedShortcut));
    }
    
    private boolean containsShortcut(List<Shortcut> shortcuts, String expectedShortcut) {
		for (int i = 0; i < shortcuts.size(); i++) {
			Shortcut shortcut = shortcuts.get(i);
			if (shortcut.getId().equals(expectedShortcut)) {
				return true;
			}
		}
		return false;
	}
    
    public void testSaveShortcutConfig_1() throws Throwable {
    	String username = "admin";
    	String[] configToSave = {null, "jacms.content.new", null, null, "jacms.content.list", "core.portal.widgetType", null, "core.tools.setting", null, null};
    	String[] expected = {null, null, null, null, "jacms.content.list", "core.portal.widgetType", null, "core.tools.setting", null, null};
    	this.addDeleteShortcutConfig(username, configToSave, expected, true);
    }
    
    public void testSaveShortcutConfig_2() throws Throwable {
    	String username = "admin";
    	String[] configToSave = {null, "jacms.content.new", null, null, "jacms.content.list", "core.portal.widgetType", null, "core.tools.setting", "shortcut.invalid", null};
    	String[] expected = {null, null, null, null, "jacms.content.list", "core.portal.widgetType", null, "core.tools.setting", null, null};
    	this.addDeleteShortcutConfig(username, configToSave, expected, true);
    }
    
    public void testSaveShortcutConfig_3() throws Throwable {
    	String username = "editorCoach";
    	String[] configToSave = {null, "jacms.content.new", null, null, "jacms.content.list"};
    	String[] expected = {null, null, null, null, "jacms.content.list", null, null, null, null, null};
    	this.addDeleteShortcutConfig(username, configToSave, expected, false);
    }
    
    public void testSaveShortcutConfig_4() throws Throwable {
    	String username = "editorCoach";
    	String[] configToSave = {null, "jacms.content.new", null, null, "core.tools.setting", 
    			"jacms.content.list", "shortcut.invalid", null, null, null, "jacms.content.new"};
    	String[] expected = {null, null, null, null, null, "jacms.content.list", null, null, null, null};
    	this.addDeleteShortcutConfig(username, configToSave, expected, false);
    }
    
    private void addDeleteShortcutConfig(String username, String[] configToSave, String[] expected, boolean restoreOnFinish) throws Throwable {
    	UserDetails user = super.getUser(username);
    	String[] toRestore = (restoreOnFinish) ? this._shortcutManager.getUserConfig(user) : null;
    	try {
    		String[] savedConfig = this._shortcutManager.saveUserConfig(user, configToSave);
    		for (int i = 0; i < savedConfig.length; i++) {
				assertEquals(expected[i], savedConfig[i]);
			}
    	} catch (Throwable t) {
			throw t;
		} finally {
			if (restoreOnFinish) {
				this._shortcutManager.saveUserConfig(user, toRestore);
			} else {
				this._shortcutManager.deleteUserConfig(username);
			}
			String[] restored = this._shortcutManager.getUserConfig(user);
			for (int i = 0; i < restored.length; i++) {
				if (restoreOnFinish) {
					assertEquals(toRestore[i], restored[i]);
				} else {
					assertNull(restored[i]);
				}
			}
		}
    }
    
	private void init() throws Exception {
    	try {
    		this._shortcutManager = (IShortcutManager) this.getService(ApsAdminSystemConstants.SHORTCUT_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
    private IShortcutManager _shortcutManager = null;
    
}