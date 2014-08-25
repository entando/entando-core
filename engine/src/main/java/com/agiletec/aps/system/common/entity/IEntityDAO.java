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
package com.agiletec.aps.system.common.entity;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * Base interface for the DAO classes that handle the ApsEntity objects.
 * @author E.Santoboni
 */
public interface IEntityDAO {
	
	/**
	 * Search for a content given the ID.
	 * @param id The entity id.
	 * @return The entity found.
	 */
	public ApsEntityRecord loadEntityRecord(String id);
	
	public void addEntity(IApsEntity entity);
	
	public void updateEntity(IApsEntity entity);
	
	public void deleteEntity(String entityId);
	
	/**
	 * Reload the search informations of an entity.
	 * @param id The entity ID.
	 * @param entity The entity whose search informations must be reloaded
	 */
	public void reloadEntitySearchRecords(String id, IApsEntity entity);
	
	/**
	 * Return the complete list of entity IDs.
	 * @return The list of all entity IDs.
	 * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntitySearcherDAO} searchId(EntitySearchFilter[]) method 
	 */
	public List<String> getAllEntityId();
	
}
