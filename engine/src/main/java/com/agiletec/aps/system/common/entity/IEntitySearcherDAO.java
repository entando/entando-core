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
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Basic interface for the DAO classes that perform searches on entities.
 * @author E.Santoboni
 */
public interface IEntitySearcherDAO {
	
	public List<ApsEntityRecord> searchRecords(EntitySearchFilter[] filters);
	
	public List<String> searchId(EntitySearchFilter[] filters);
	
	public List<String> searchId(String typeCode, EntitySearchFilter[] filters);
	
	/**
	 * @deprecated As of jAPS 2.0 version 2.0.9, replaced by the constant  {@link IEntityManager}.
	 */
	public static final String ID_FILTER_KEY = IEntityManager.ENTITY_ID_FILTER_KEY;
	
	/**
	 * @deprecated As of jAPS 2.0 version 2.0.9, replaced by contant on {@link IEntityManager}.
	 */
	public static final String TYPE_CODE_FILTER_KEY = IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY;
	
}
