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
package com.agiletec.aps.system.common.entity.cache;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public interface IEntityManagerCacheWrapper {

	public static final String ENTITY_MANAGER_CACHE_NAME_PREFIX = "Entando_";
	public static final String ENTITY_STATUS_CACHE_NAME = "EntityManager_status";

	public void initCache(String managerName) throws ApsSystemException;

	public Integer getEntityTypeStatus(String typeCode);

	public void updateEntityTypeStatus(String typeCode, Integer status);

}
