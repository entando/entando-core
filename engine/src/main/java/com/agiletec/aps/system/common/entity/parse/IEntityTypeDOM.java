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
package com.agiletec.aps.system.common.entity.parse;

import java.util.Map;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;

/**
 * Interface of the JDOM classes that parse the XML code defining the Entity Types.
 * @author E.Santoboni
 */
public interface IEntityTypeDOM {
	
	public List<SmallEntityType> extractSmallEntityTypes(String xml) throws ApsSystemException;
	
	/**
	 * Return the Map of the prototypes of the Entity Types (indexed by their code) that the
	 * entity service is going to handle.
	 * The structure of the Entity Types is obtained from a configuration XML.
	 * @param xml The configuration XML of the Entity Types available.
	 * @param entityClass The class of the Entity Type.
	 * @param entityDom The DOM class that creates the XML of the entity instances. 
	 * @param entityManagerName The entity manager name
	 * @return The map of the Entity Types Prototypes, indexed by code. 
	 * @throws ApsSystemException If errors are detected while parsing the configuration XML.
	 */
	public Map<String, IApsEntity> extractEntityTypes(String xml, Class entityClass, 
			IApsEntityDOM entityDom, String entityManagerName) throws ApsSystemException;
	
	public IApsEntity extractEntityType(String typeCode, String xml, Class entityClass, 
			IApsEntityDOM entityDom, String entityManagerName) throws ApsSystemException;
	
	/**
	 * Prepare the map with the Attribute Types.
	 * The map is indexed by the code of the Attribute type.
	 * The Attributes are utilized (as elementary "bricks") to build the structure
	 * of the Entity Types.
	 * @param attributeTypes The map containing the Attribute Types indexed by the type code. 
	 */
	public void setAttributeTypes(Map<String, AttributeInterface> attributeTypes);
	
	public Map<String, AttributeInterface> getAttributeTypes();
	
	public String getXml(Map<String, IApsEntity> entityTypes) throws ApsSystemException;
	
	public String getXml(IApsEntity entityType) throws ApsSystemException;
	
	public IApsEntity extractEntityType(String entityTypeXml, Class entityClass, 
			IApsEntityDOM entityDom, String entityManagerName) throws ApsSystemException;
	
}
