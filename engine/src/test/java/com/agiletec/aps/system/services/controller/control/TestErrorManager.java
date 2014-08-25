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
import org.springframework.mock.web.MockHttpServletResponse;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;

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
