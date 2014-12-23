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
package com.agiletec.aps.system.common.entity.parse;

import java.util.Map;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interface of the the entities class factory.
 * This interface is used by the factory classes that serve the various ApsEntities managers. 
 * @author E.Santoboni
 */
public interface IEntityTypeFactory {
	
	/**
	 * Return the Map of the prototypes of the Entity Types (indexed by their code) that the
	 * entity service is going to handle.
	 * The structure of the Entity Types is obtained from a configuration XML.
	 * @param entityClass The class of the entity. 
	 * @param configItemName The configuration item where the Entity Types are defined.
	 * @param entityTypeDom The DOM class that parses the configuration XML.
	 * @param entityDom The DOM class that parses the XML representing the single (implemented) entity.
	 * @return The map of the Entity Types Prototypes, indexed by code. 
	 * @throws ApsSystemException If errors occurs during the parsing process of the XML. 
	 * @deprecated Since Entando 2.4.1, use getEntityTypes(Class, String, IEntityTypeDOM, String, IApsEntityDOM)
	 */
	public Map<String, IApsEntity> getEntityTypes(Class entityClass, String configItemName, 
			IEntityTypeDOM entityTypeDom, IApsEntityDOM entityDom) throws ApsSystemException;
	
	/**
	 * Return the Map of the prototypes of the Entity Types (indexed by their code) that the
	 * entity service is going to handle.
	 * The structure of the Entity Types is obtained from a configuration XML.
	 * @param entityClass The class of the entity. 
	 * @param configItemName The configuration item where the Entity Types are defined.
	 * @param entityTypeDom The DOM class that parses the configuration XML.
	 * @param entityDom The DOM class that parses the XML representing the single (implemented) entity.
	 * @param entityManagerName The entity manager name
	 * @return The map of the Entity Types Prototypes, indexed by code. 
	 * @throws ApsSystemException If errors occurs during the parsing process of the XML. 
	 */
	public Map<String, IApsEntity> getEntityTypes(Class entityClass, String configItemName, 
			IEntityTypeDOM entityTypeDom, String entityManagerName, IApsEntityDOM entityDom) throws ApsSystemException;
	
	/**
	 * Update the Entity Type prototypes in the configuration.
	 * @param entityTypes The map of the Entity Type Prototypes.
	 * @param configItemName The configuration item where the Entity Types to update are defined.
	 * @param entityTypeDom The DOM class that parses the configuration XML.
	 * @throws ApsSystemException
	 */
	public void updateEntityTypes(Map<String, IApsEntity> entityTypes, String configItemName, IEntityTypeDOM entityTypeDom) throws ApsSystemException;
	
}
