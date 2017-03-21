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
package com.agiletec.apsadmin.system.dispatcher;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.HostConfig;

/**
 * Extension of the InitOperations class used by Struts2 main filter.
 * The extension lets to place the base configuration for Struts2 (the definition of the configuration files) 
 * outside the filter configuration but always within the Deployment Descriptor (web.xml).
 * The name of the configuration parameter (init-param of web.xml) is defined in the system constants 
 * in the interface {@link ApsAdminSystemConstants}.
 * @author E.Santoboni
 */
public class InitOperations extends org.apache.struts2.dispatcher.InitOperations {
	
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