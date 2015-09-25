/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.baseconfig;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author M.Casari
 */
public class TestBaseConfigService extends BaseTestCase {
	
    public void testGetParam() throws ApsSystemException {
    	ConfigInterface baseConfigManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		String param = baseConfigManager.getParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE);
		assertEquals(param, "notfound");
	}
    
}