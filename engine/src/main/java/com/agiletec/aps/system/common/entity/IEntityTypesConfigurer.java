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
package com.agiletec.aps.system.common.entity;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public interface IEntityTypesConfigurer {
	
	/**
	 * Add a new entity prototype on the catalog.
	 * @param entityType The entity type to add.
	 * @throws ApsSystemException In case of error.
	 */
	public void addEntityPrototype(IApsEntity entityType) throws ApsSystemException;
	
	/**
	 * Update an entity prototype on the catalog.
	 * @param entityType The entity type to update
	 * @throws ApsSystemException In case of error.
	 */
	public void updateEntityPrototype(IApsEntity entityType) throws ApsSystemException;
	
	/**
	 * Remove an entity type from the catalog.
	 * @param entityTypeCode The code of the entity type to remove.
	 * @throws ApsSystemException In case of error.
	 */
	public void removeEntityPrototype(String entityTypeCode) throws ApsSystemException;
	
}
