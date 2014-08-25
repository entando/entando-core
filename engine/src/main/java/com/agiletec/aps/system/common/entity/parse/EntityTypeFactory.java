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
package com.agiletec.aps.system.common.entity.parse;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

/**
 * This class, which serves the ApsEntity managers, is used to obtain the Entity Types.
 * This class is utilized by default in the Spring bean configuration that defines the base
 * {@link ApsEntityManager} Entity Manager
 * @author E.Santoboni
 */
public class EntityTypeFactory implements IEntityTypeFactory {

	private static final Logger _logger = LoggerFactory.getLogger(EntityTypeFactory.class);
	
	/**
	 * Return the Map of the prototypes of the Entity Types (indexed by their code) that the
	 * entity service is going to handle.
	 * The structure of the Entity Types is obtained from a configuration XML.
	 * @param entityClass The class of the entity. 
	 * @param configItemName The configuration item where the Entity Types are defined.
	 * @param entityTypeDom The DOM class that parses the configuration XML.
	 * @param entityDom The DOM class that parses the XML representing the single (implemented) entity.
	 * @return The map of the Entity Types Prototypes, indexed by code. 
	 * @deprecated Since Entando 2.4.1, use getEntityTypes(Class, String, IEntityTypeDOM, String, IApsEntityDOM)
	 * @throws ApsSystemException If errors occurs during the parsing process of the XML.
	 */
	@Override
	public Map<String, IApsEntity> getEntityTypes(Class entityClass, String configItemName, 
			IEntityTypeDOM entityTypeDom, IApsEntityDOM entityDom) throws ApsSystemException {
		return this.getEntityTypes(entityClass, configItemName, entityTypeDom, null, entityDom);
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
	public Map<String, IApsEntity> getEntityTypes(Class entityClass, String configItemName, 
			IEntityTypeDOM entityTypeDom, String entityManagerName, IApsEntityDOM entityDom) throws ApsSystemException {
		Map<String, IApsEntity> entityTypes = null;
		try {
			String xml = this.getConfigManager().getConfigItem(configItemName);
			_logger.debug("{} : {}", configItemName , xml);
			entityTypeDom.initEntityTypeDOM(xml, entityClass, entityDom, entityManagerName);
			entityTypes = entityTypeDom.getEntityTypes();
		} catch (Throwable t) {
			_logger.error("Error in the entities initialization process. configItemName:{}", configItemName, t);
			//ApsSystemUtils.logThrowable(t, this, "getEntityTypes");
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
			_logger.error("Error detected while updating the Entity Types. configItemName: {}", configItemName, t);
			//ApsSystemUtils.logThrowable(t, this, "updateEntityTypes");
			throw new ApsSystemException("Error detected while updating the Entity Types", t);
		}
	}
	
	protected ConfigInterface getConfigManager() {
		return this._configManager;
	}
	
	/**
	 * Set up the manager of the system configuration.
	 * This method is silently invoked when parsing the XML that defines the service in the Spring
	 * configuration file. It cannot be invoked directly.
	 * @param configManager The manager of the system configuration.
	 */
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private ConfigInterface _configManager;
	
}
