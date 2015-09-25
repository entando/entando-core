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
package org.entando.entando.aps;

import org.entando.entando.aps.system.services.controller.TestControllerManager;
import org.entando.entando.aps.system.services.controller.control.TestAuthenticator;
import org.entando.entando.aps.system.services.controller.control.TestErrorManager;
import org.entando.entando.aps.system.services.controller.control.TestRequestAuthorizator;
import org.entando.entando.aps.system.services.controller.control.TestRequestValidator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author E.Santoboni
 */
public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for portal-ui");
		//
		suite.addTestSuite(TestAuthenticator.class);
		suite.addTestSuite(TestRequestAuthorizator.class);
		suite.addTestSuite(TestErrorManager.class);
		suite.addTestSuite(TestRequestValidator.class);
		suite.addTestSuite(TestControllerManager.class);
		//
		//suite.addTestSuite(TestApplicationContext.class);
		//
		//suite.addTestSuite(TestWidgetExecutorService.class);
		
		return suite;
	}
	
}
