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
package com.agiletec.apsadmin.system.dispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.HostConfig;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * Extension of the InitOperations class used by Struts2 main filter.
 * The extension lets to place the base configuration for Struts2 (the definition of the configuration files) 
 * outside the filter configuration but always within the Deployment Descriptor (web.xml).
 * The name of the configuration parameter (init-param of web.xml) is defined in the system constants 
 * in the interface {@link ApsAdminSystemConstants}.
 * @author E.Santoboni
 */
public class InitOperations extends org.apache.struts2.dispatcher.ng.InitOperations {
	
	@Override
	public Dispatcher initDispatcher(HostConfig filterConfig) {
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator<String> e = filterConfig.getInitParameterNames(); e.hasNext();) {
			String name = (String) e.next();
			String value = filterConfig.getInitParameter(name);
			params.put(name, value);
		}
		String struts2Config = filterConfig.getServletContext().getInitParameter(ApsAdminSystemConstants.STRUTS2_CONFIG_INIT_PARAM_NAME);
		if (null != struts2Config) {
			params.put("config", struts2Config);
		}
		Dispatcher dispatcher = new Dispatcher(filterConfig.getServletContext(), params);
		dispatcher.init();
		return dispatcher;
	}
	
}