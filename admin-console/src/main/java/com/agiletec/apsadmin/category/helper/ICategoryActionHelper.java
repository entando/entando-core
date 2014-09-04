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
