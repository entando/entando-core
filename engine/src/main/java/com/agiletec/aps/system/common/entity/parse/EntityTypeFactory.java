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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.util.List;

/**
 * This class, which serves the ApsEntity managers, is used to obtain the Entity Types.
 * This class is utilized by default in the Spring bean configuration that defines the base
 * {@link ApsEntityManager} Entity Manager
 * @author E.Santoboni
 */
public class EntityTypeFactory implements IEntityTypeFactory {

	private static final Logger logger = LoggerFactory.getLogger(EntityTypeFactory.class);
	
	private ConfigInterface configManager;
	
	@Override
	public List<SmallEntityType> extractSmallEntityTypes(String configItemName, IEntityTypeDOM entityTypeDom) throws ApsSystemException {
		String xml = this.getConfigManager().getConfigItem(configItemName);
		return entityTypeDom.extractSmallEntityTypes(xml);
	}
	
	@Override
	public IApsEntity extractEntityType(String typeCode, Class entityClass, String configItemName, 
			IEntityTypeDOM entityTypeDom, String entityManagerName, IApsEntityDOM entityDom) throws ApsSystemException {
		String xml = this.getConfigManager().getConfigItem(configItemName);
		logger.debug("{} : {}", configItemName , xml);
		return entityTypeDom.extractEntityType(typeCode, xml, entityClass, entityDom, entityManagerName);
	}
	
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
	@Override
	public Map<String, IApsEntity> extractEntityTypes(Class entityClass, String configItemName, 
			IEntityTypeDOM entityTypeDom, String entityManagerName, IApsEntityDOM entityDom) throws ApsSystemException {
		Map<String, IApsEntity> entityTypes = null;
		try {
			String xml = this.getConfigManager().getConfigItem(configItemName);
			logger.debug("{} : {}", configItemName , xml);
			entityTypes = entityTypeDom.extractEntityTypes(xml, entityClass, entityDom, entityManagerName);
		} catch (Throwable t) {
			logger.error("Error in the entities initialization process. configItemName:{}", configItemName, t);
			throw new ApsSystemException("Error in the entities initialization process", t);
		}
		return entityTypes;
	}
	
	@Override
	public void updateEntityTypes(Map<String, IApsEntity> entityTypes, String configItemName, IEntityTypeDOM entityTypeDom) throws ApsSystemException {
		try {
			String xml = entityTypeDom.getXml(entityTypes);
			this.getConfigManager().updateConfigItem(configItemName, xml);
		} catch (Throwable t) {
			logger.error("Error detected while updating the Entity Types. configItemName: {}", configItemName, t);
			throw new ApsSystemException("Error detected while updating the Entity Types", t);
		}
	}
	
	protected ConfigInterface getConfigManager() {
		return this.configManager;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this.configManager = configManager;
	}
	
}
