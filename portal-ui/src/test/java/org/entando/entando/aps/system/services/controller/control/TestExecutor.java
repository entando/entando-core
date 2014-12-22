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

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;

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
