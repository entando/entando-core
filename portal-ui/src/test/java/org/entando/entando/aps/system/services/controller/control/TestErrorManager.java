/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
import org.springframework.mock.web.MockHttpServletResponse;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;

/**
 * @author M.Casari
 */
public class TestErrorManager extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testService() throws ApsSystemException {
		RequestContext reqCtx = new RequestContext();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(RequestContext.PAR_REDIRECT_FLAG, "");
		reqCtx.setRequest(request);
		MockHttpServletResponse response = new MockHttpServletResponse();
		reqCtx.setResponse(response);
		int status = _errorManager.service(reqCtx, ControllerManager.ERROR);
		assertEquals(status, ControllerManager.REDIRECT);
	}
	
	private void init() throws Exception {
        try {
        	this._errorManager = (ControlServiceInterface) this.getApplicationContext().getBean("ErrorManagerControlService");
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
	
    private ControlServiceInterface _errorManager = null;
    
}
