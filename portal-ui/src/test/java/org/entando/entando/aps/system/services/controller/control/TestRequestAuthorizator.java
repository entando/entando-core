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
package org.entando.entando.aps.system.services.controller.control;

import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;

/**
 * @author M.Diana
 */
public class TestRequestAuthorizator extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testService_1() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		this.setUserOnSession(SystemConstants.GUEST_USER_NAME);
		IPage root = this._pageManager.getRoot();
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, root);
		int status = _authorizator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertNull(redirectUrl);
	}
	
	public void testService_2() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		this.setUserOnSession("admin");
		IPage root = this._pageManager.getRoot();
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, root);
		int status = this._authorizator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertNull(redirectUrl);
	}
	
	public void testServiceFailure_1() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		((MockHttpServletRequest) reqCtx.getRequest()).setRequestURI("/Entando/it/customers_page.page");
		this.setUserOnSession(SystemConstants.GUEST_USER_NAME);
		IPage requiredPage = this._pageManager.getOnlinePage("customers_page");
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, requiredPage);
		int status = _authorizator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.REDIRECT);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertTrue(redirectUrl.contains("/Entando/it/login.page?"));
		assertTrue(redirectUrl.contains("redirectflag=1"));
		assertTrue(redirectUrl.contains("returnUrl="));
		assertTrue(redirectUrl.contains("customers_page.page"));
	}
	
	public void testServiceFailure_2() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		reqCtx.getRequest().getSession().removeAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		IPage root = this._pageManager.getRoot();
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, root);
		int status = _authorizator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.SYS_ERROR);
	}
	
	private void init() throws Exception {
        try {
        	this._authorizator = (ControlServiceInterface) this.getApplicationContext().getBean("RequestAuthorizatorControlService");
        	this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
	
	private ControlServiceInterface _authorizator;
	
	private IPageManager _pageManager;
    
}