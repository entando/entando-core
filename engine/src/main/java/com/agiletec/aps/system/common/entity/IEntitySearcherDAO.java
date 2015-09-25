/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
