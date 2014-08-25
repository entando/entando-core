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
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;

/**
 * @author M.Casari
 */
public class TestRequestValidator extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testService() throws ApsSystemException {
    	RequestContext reqCtx = this.getRequestContext();
		((MockHttpServletRequest) reqCtx.getRequest()).setServletPath("/it/homepage.wp");
		int status = this._requestValidator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.CONTINUE, status);
		Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		assertNotNull(page);
		assertNotNull(lang);
		assertEquals("it", lang.getCode());
		assertEquals("homepage", page.getCode());
	}
	
	public void testServiceFailure_1() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		((MockHttpServletRequest) reqCtx.getRequest()).setServletPath("/it/notexists.wp");//Page does not exist
		int status = _requestValidator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.REDIRECT, status);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertEquals("/Entando/it/notfound.page?redirectflag=1", redirectUrl);
	}
	
	public void testServiceFailure_2() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		((MockHttpServletRequest) reqCtx.getRequest()).setServletPath("/wrongpath.wp");//wrong path
		int status = _requestValidator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.REDIRECT, status);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertEquals("/Entando/it/errorpage.page?redirectflag=1", redirectUrl);
	}
	
	public void testServiceFailure_3() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		((MockHttpServletRequest) reqCtx.getRequest()).setServletPath("/cc/homepage.wp");//lang does not exist
		int status = _requestValidator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(ControllerManager.REDIRECT, status);
		String redirectUrl = (String) reqCtx.getExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL);
		assertEquals("/Entando/it/errorpage.page?redirectflag=1", redirectUrl);
	}
	
	private void init() throws Exception {
        try {
        	this._requestValidator = (ControlServiceInterface) this.getApplicationContext().getBean("RequestValidatorControlService");
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
	
	private ControlServiceInterface _requestValidator;
    	
}
