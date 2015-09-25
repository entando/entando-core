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
package com.agiletec.apsadmin.system.entity.type;

/**
 * @author E.Santoboni
 */
public interface IEntityTypeConfigAction {
	
	public String addEntityType();
	
	public String editEntityType();
	
	public String addAttribute();
	
	public String editAttribute();
	
	public String moveAttribute();
	
	public String removeAttribute();
	
	public String saveEntityType();
	
	public static final String ENTITY_TYPE_OPERATION_ID_SESSION_PARAM = "operationId_sessionParam";
	public static final String ENTITY_TYPE_MANAGER_SESSION_PARAM = "entityTypeManager_sessionParam";
	public static final String ENTITY_TYPE_ON_EDIT_SESSION_PARAM = "entityTypeOnEdit_sessionParam";
	
}