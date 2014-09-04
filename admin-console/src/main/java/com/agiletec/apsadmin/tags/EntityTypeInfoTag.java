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
package com.agiletec.apsadmin.tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns a entity type (or one of its property) through the code and the entity service name.
 * You can choose whether to return the entire object (leaving the attribute "property" empty) or a single property.
 * The names of the available property of "Entity Type": "typeCode", "typeDescr", 
 * "attributeMap" (map of attributes indexed by the name), "attributeList" (list of attributes).
 * @author E.Santoboni
 */
public class EntityTypeInfoTag extends AbstractObjectInfoTag {

	private static final Logger _logger = LoggerFactory.getLogger(EntityTypeInfoTag.class);
	
	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		String managerNameValue = (String) super.findValue(this.getEntityManagerName(), String.class);
		try {
			IEntityManager entityManager = (IEntityManager) ApsWebApplicationUtils.getBean(managerNameValue, this.pageContext);
			if (null != entityManager) {
				return entityManager.getEntityPrototype(keyValue);
			}
		} catch (Throwable t) {
			String message = "Error extracting entity prototype : key '" + keyValue + "' - entity manager '" + managerNameValue + "'";
			_logger.error("Error extracting entity prototype : key '{}' - entity manager '{}'", keyValue, managerNameValue, t);
			//ApsSystemUtils.logThrowable(t, this, "getMasterObject", message);
			throw new ApsSystemException(message, t);
		}
		_logger.debug("Null entity manager : service name '{}'", managerNameValue);
		return null;
	}
	
	/**
	 * Set the code of the entity type to return
	 * @param typeCode The code of the entity type to return
	 */
	public void setTypeCode(String typeCode) {
		super.setKey(typeCode);
	}
	
	protected String getEntityManagerName() {
		return _entityManagerName;
	}
	public void setEntityManagerName(String entityManagerName) {
		this._entityManagerName = entityManagerName;
	}
	
	private String _entityManagerName;
	
}