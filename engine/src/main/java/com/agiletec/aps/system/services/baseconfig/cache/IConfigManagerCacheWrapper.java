/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.baseconfig.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.IConfigItemDAO;

/**
 * @author E.Santoboni
 */
public interface IConfigManagerCacheWrapper {

	public static final String CONFIG_MANAGER_CACHE_NAME = "Entando_ConfigManager";
	public static final String CONFIG_ITEM_CACHE_NAME_PREFIX = "ConfigManager_item_";
	public static final String CONFIG_ITEMS_CODES_CACHE_NAME = "ConfigManager_items";

	public static final String CONFIG_PARAM_CACHE_NAME_PREFIX = "ConfigManager_param_";
	public static final String CONFIG_PARAMS_CODES_CACHE_NAME = "ConfigManager_params";

	public void initCache(IConfigItemDAO configItemDAO, String version) throws ApsSystemException;

	public String getConfigItem(String name);

	public String getParam(String name);
	
}
