/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.apsadmin.common;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.services.shortcut.IShortcutManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestShortcutConfigAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testInit() throws Throwable {
    	this.initAction("/do", "main");
    	this.setUserOnSession("admin");
    	String result = super.executeAction();
		assertEquals(Action.SUCCESS, result);
		MyShortcutConfigAction action = (MyShortcutConfigAction) this.getAction();
		String[] config = action.getUserConfig();
		for (int i = 0; i < config.length; i++) {
			if (i==0 || i==2 || i>7) {
				assertNull(config[i]);
			} else {
				assertNotNull(config[i]);
			}
		}
	}
	
	public void testValidateConfigPosition() throws Throwable {
		String result = this.executeConfigPosition(4, 3, null);
		assertEquals("apslogin", result);
		
		result = this.executeConfigPosition(null, null, "admin");
		assertEquals(Action.SUCCESS, result);
		
		result = this.executeConfigPosition(4, 7, "admin");
		assertEquals(Action.SUCCESS, result);
		MyShortcutConfigAction action = (MyShortcutConfigAction) this.getAction();
		assertEquals(new Integer(4), action.getPosition());
		assertEquals(new Integer(7), action.getPositionTarget());
	}
	
	public void testValidateJoinMyShortcut_1() throws Throwable {
		try {
			String result = this.executeJoinMyShortcut(null, 2, "admin", ApsAdminSystemConstants.ADD);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("shortcutCode").size());
			
			result = this.executeJoinMyShortcut("invalidCode", 2, "admin", ApsAdminSystemConstants.ADD);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("shortcutCode").size());
			
			result = this.executeJoinMyShortcut("core.component.user.list", 20, "admin", ApsAdminSystemConstants.ADD);//invalid position
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("position").size());
			
			result = this.executeJoinMyShortcut("core.component.user.list", 2, "admin", null);//invalid operation
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("strutsAction").size());
			
			result = this.executeJoinMyShortcut("core.component.user.list", 2, "admin", 9);//invalid operation
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("strutsAction").size());
		} catch (Exception e) {
			UserDetails user = this.getUser("admin");
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
			throw e;
		}
	}
	
	public void testValidateJoinMyShortcut_2() throws Throwable {
		try {
			String result = this.executeJoinMyShortcut("core.component.user.list", 2, "editorCoach", ApsAdminSystemConstants.ADD);//shortcut not allowed
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("shortcutCode").size());
		} catch (Exception e) {
			this._shortcutManager.deleteUserConfig("editorCoach");
			throw e;
		}
	}
	
	public void testJoinMyShortcut_1() throws Throwable {
		UserDetails user = this.getUser("admin");
		try {
			String result = this.executeJoinMyShortcut("core.component.user.list", 3, "admin", ApsAdminSystemConstants.ADD);
			assertEquals(Action.SUCCESS, result);
			String[] newConfig = this._shortcutManager.getUserConfig(user);
			for (int i = 0; i < newConfig.length; i++) {
				if (i==3) {
					assertEquals("core.component.user.list", newConfig[i]);
				} else {
					assertEquals(ADMIN_CONFIG[i], newConfig[i]);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
		}
	}
	
	public void testJoinMyShortcut_2() throws Throwable {
		UserDetails user = this.getUser("admin");
		try {
			String result = this.executeJoinMyShortcut("core.tools.setting", 9, "admin", ApsAdminSystemConstants.ADD);
			assertEquals(Action.SUCCESS, result);
			String[] newConfig = this._shortcutManager.getUserConfig(user);
			for (int i = 0; i < newConfig.length; i++) {
				if (i==9) {
					assertEquals("core.tools.setting", newConfig[i]);
				} else {
					assertEquals(ADMIN_CONFIG[i], newConfig[i]);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
		}
	}
	
	public void testValidateRemoveMyShortcut() throws Throwable {
		try {
			String result = this.executeEmptyPosition(20, "admin");//invalid position
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("position").size());
		} catch (Exception e) {
			UserDetails user = this.getUser("admin");
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
			throw e;
		}
	}
	
	public void testRemoveMyShortcut() throws Throwable {
		UserDetails user = this.getUser("admin");
		try {
			String result = this.executeEmptyPosition(3, "admin");
			assertEquals(Action.SUCCESS, result);
			String[] newConfig = this._shortcutManager.getUserConfig(user);
			for (int i = 0; i < newConfig.length; i++) {
				if (i==3) {
					assertNull(newConfig[i]);
				} else {
					assertEquals(ADMIN_CONFIG[i], newConfig[i]);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
		}
	}
	
	public void testValidateSwapMyShortcut() throws Throwable {
		try {
			String result = this.executeSwapMyShortcut(null, 2, "admin", ApsAdminSystemConstants.EDIT);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("positionTarget").size());
			
			result = this.executeSwapMyShortcut(13, 2, "admin", ApsAdminSystemConstants.EDIT);//invalid positionTarget
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("positionTarget").size());
			
			result = this.executeSwapMyShortcut(2, 20, "admin", ApsAdminSystemConstants.EDIT);//invalid positionDest
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("positionDest").size());
			
			result = this.executeSwapMyShortcut(3, 2, "admin", null);//invalid operation
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("strutsAction").size());
			
			result = this.executeSwapMyShortcut(3, 2, "admin", 9);//invalid operation
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("strutsAction").size());
		} catch (Exception e) {
			UserDetails user = this.getUser("admin");
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
			throw e;
		}
	}
	
	public void testSwapMyShortcut() throws Throwable {
		UserDetails user = this.getUser("admin");
		try {
			String[] oldConfig = this._shortcutManager.getUserConfig(user);
			String result = this.executeSwapMyShortcut(2, 9, "admin", ApsAdminSystemConstants.EDIT);
			assertEquals(Action.SUCCESS, result);
			String[] newConfig = this._shortcutManager.getUserConfig(user);
			assertEquals(newConfig[9], oldConfig[2]);
			assertEquals(newConfig[2], oldConfig[9]);
			
			result = this.executeSwapMyShortcut(8, 9, "admin", ApsAdminSystemConstants.EDIT);
			assertEquals(Action.SUCCESS, result);
			String[] newConfig2 = this._shortcutManager.getUserConfig(user);
			assertEquals(newConfig2[9], newConfig[8]);
			assertEquals(newConfig2[8], newConfig[9]);
		} catch (Exception e) {
			throw e;
		} finally {
			this._shortcutManager.saveUserConfig(user, ADMIN_CONFIG);
		}
	}
	
	private String executeConfigPosition(Integer pos, Integer positionTarget, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/MyShortcut", "configPosition");
		this.addParameter("position", pos);
		this.addParameter("positionTarget", positionTarget);
    	return super.executeAction();
	}
	
	private String executeJoinMyShortcut(String shortcutCode, Integer pos, String username, Integer strutsAction) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/MyShortcut", "joinMyShortcut");
		this.addParameter("shortcutCode", shortcutCode);
		this.addParameter("position", pos);
		this.addParameter("strutsAction", strutsAction);
    	return super.executeAction();
	}
	
	private String executeEmptyPosition(Integer pos, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/MyShortcut", "removeMyShortcut");
    	this.addParameter("position", pos);
    	this.addParameter("strutsAction", ApsAdminSystemConstants.DELETE);
    	return super.executeAction();
	}
	
	private String executeSwapMyShortcut(Integer positionTarget, Integer positionDest, String username, Integer strutsAction) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/MyShortcut", "swapMyShortcut");
		this.addParameter("positionTarget", positionTarget);
		this.addParameter("positionDest", positionDest);
		this.addParameter("strutsAction", strutsAction);
    	return super.executeAction();
	}
	
	private void init() throws Exception {
    	try {
    		this._shortcutManager = (IShortcutManager) this.getService(ApsAdminSystemConstants.SHORTCUT_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
    private IShortcutManager _shortcutManager = null;
    
    private final String[] ADMIN_CONFIG = {null, "core.component.user.list", null, "jacms.content.list", "core.portal.pageTree", 
    		"core.portal.widgetType", "core.tools.setting", "core.tools.entities", null, null};
    
}
