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

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;

/**
 * @author M.Diana
 */
public class TestExecutor extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testService() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		int status = this._executor.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.OUTPUT);
	}
	
	private void init() throws Exception {
        try {
        	this._executor = (ControlServiceInterface) this.getApplicationContext().getBean("ExecutorControlService");
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
	
	private ControlServiceInterface _executor;
    
}
