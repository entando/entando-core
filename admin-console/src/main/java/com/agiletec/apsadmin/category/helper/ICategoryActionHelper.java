/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.category.helper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;

/**
 * Interface for helper classes providing support for categories management.
 * @author E.Santoboni
 */
public interface ICategoryActionHelper extends ITreeNodeBaseActionHelper {
	
	/** 
	 * Return the map of the objects referenced by the given category.
	 * The map is indexed by the name of the service which handles the type of the referenced elements;
	 * the value is the list of the elements fetched by the appropriate service.
	 * @param category The category by which you want to find any referenced element.
	 * @param request The request.
	 * @return The map of the objects referenced in the given category. 
	 * @throws ApsSystemException if error is detected
	 */
	public Map getReferencingObjects(Category category, HttpServletRequest request) throws ApsSystemException;
	
	/**
	 * Create a new category based on the parameters passed.
	 * @param code The code of the new category.
	 * @param parentCode The code of the category becoming the parent of the new one. 
	 * @param titles The titles of the new category.
	 * @return The new category.
	 * @throws ApsSystemException if error is detected.
	 */
	public Category buildNewCategory(String code, String parentCode, ApsProperties titles) throws ApsSystemException;
	
}
