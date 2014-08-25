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
package com.agiletec.aps.system.services.controller;

import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author M.Diana - W.Ambu
 */
public class TestControllerManager extends BaseTestCase {
	
	public void testService_1() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		ControllerManager controller = (ControllerManager) this.getService(SystemConstants.CONTROLLER_MANAGER);
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setServletPath("/it/homepage.page");
		int status = controller.service(reqCtx);
		assertEquals(ControllerManager.OUTPUT, status);
		
		request.setParameter("username", "admin");
		request.setParameter("password", "admin");
		status = controller.service(reqCtx);
		assertEquals(ControllerManager.OUTPUT, status);
	}
	
	public void testService_2() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		ControllerManager controller = (ControllerManager) this.getService(SystemConstants.CONTROLLER_MANAGER);
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setServletPath("/it/customers_page.page");
		int status = controller.service(reqCtx);
		assertEquals(ControllerManager.REDIRECT, status);
		
		request.setParameter("username", "admin");
		request.setParameter("password", "admin");
		request.setServletPath("/it/customers_page.page");
		status = controller.service(reqCtx);
		assertEquals(ControllerManager.OUTPUT, status);
	}
	
	public void testService_3() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		ControllerManager controller = (ControllerManager) this.getService(SystemConstants.CONTROLLER_MANAGER);
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setServletPath("/it/administrators_page.page");
		request.setRequestURI("/Entando/it/customers_page.page");
		int status = controller.service(reqCtx);
		assertEquals(ControllerManager.REDIRECT, status);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertTrue(redirectUrl.contains("/Entando/it/login.page?"));
		assertTrue(redirectUrl.contains("redirectflag=1"));
		assertTrue(redirectUrl.contains("returnUrl="));
		assertTrue(redirectUrl.contains("customers_page.page"));
		
		request.setParameter(RequestContext.PAR_REDIRECT_FLAG, "1");
		status = controller.service(reqCtx);
		assertEquals(ControllerManager.REDIRECT, status);
		redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertEquals("/Entando/it/errorpage.page", redirectUrl);
	}
	
}