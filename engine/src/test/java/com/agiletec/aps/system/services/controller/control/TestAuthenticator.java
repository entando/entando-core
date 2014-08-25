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
package com.agiletec.aps.system.services.controller.control;

import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author M.Diana
 */
public class TestAuthenticator extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testService_1() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals(SystemConstants.GUEST_USER_NAME, currentUser.getUsername());
	}
	
	public void testService_2() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setParameter("username", "admin");
		request.setParameter("password", "admin");
		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals("admin", currentUser.getUsername());
	}
	
	public void testServiceFailure() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setParameter("user", "notauthorized");
		request.setParameter("password", "notauthorized");
		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals(SystemConstants.GUEST_USER_NAME, currentUser.getUsername());
	}
	
	private void init() throws Exception {
        try {
        	this._authenticator = (ControlServiceInterface) this.getApplicationContext().getBean("AuthenticatorControlService");
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
	
	private ControlServiceInterface _authenticator;
    
}
