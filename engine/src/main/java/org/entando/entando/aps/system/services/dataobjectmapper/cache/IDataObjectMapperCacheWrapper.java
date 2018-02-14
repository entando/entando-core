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
package org.entando.entando.aps.system.services.dataobjectmapper.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPageManager;

/**
 * @author E.Santoboni
 */
public interface IDataObjectMapperCacheWrapper {

	public static final String OBJECT_MAPPER_CACHE_NAME = "Entando_DataObjectPageMapperManager";
	public static final String OBJECT_MAPPER_CACHE_KEY = "DataObjectMapper_mapper";

	public void initCache(IPageManager pageManager) throws ApsSystemException;

	public String getPageCode(String dataId);

}
