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
package com.agiletec.aps.system.services.user;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @version 1.0
 * @author M.Casari
 */
public class TestUserManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetUsers() throws Throwable {
		List<UserDetails> users = this._userManager.getUsers();
		assertTrue(users.size()>=8);
    }
    
    //TODO FARE TEST PER OPERAZIONI SPECIALI SU UTENTE (VERIFICA DATE ACCESSI E CAMBIO PASSWORD)
    
    private void init() throws Exception {
    	try {
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
    }
    
    protected MockUser createUserForTest(String username) {
    	MockUser user = new MockUser();
		user.setUsername(username);
        user.setPassword("temp");
        return user;
	}
	
	private IUserManager _userManager = null;
	
}
