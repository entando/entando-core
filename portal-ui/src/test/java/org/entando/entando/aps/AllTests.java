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

package org.entando.entando.aps;

import org.entando.entando.aps.system.services.controller.TestControllerManager;
import org.entando.entando.aps.system.services.controller.control.TestAuthenticator;
import org.entando.entando.aps.system.services.controller.control.TestErrorManager;
import org.entando.entando.aps.system.services.controller.control.TestExecutor;
import org.entando.entando.aps.system.services.controller.control.TestRequestAuthorizator;
import org.entando.entando.aps.system.services.controller.control.TestRequestValidator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.entando.entando.aps.system.services.controller.executor.TestWidgetExecutorService;

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
		suite.addTestSuite(TestExecutor.class);
		suite.addTestSuite(TestRequestValidator.class);
		suite.addTestSuite(TestControllerManager.class);
		//
		//suite.addTestSuite(TestApplicationContext.class);
		//
		//suite.addTestSuite(TestWidgetExecutorService.class);
		
		return suite;
	}
	
}
