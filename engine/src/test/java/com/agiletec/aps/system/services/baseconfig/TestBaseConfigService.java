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