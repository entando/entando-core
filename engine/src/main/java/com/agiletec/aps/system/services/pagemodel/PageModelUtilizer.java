/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system.services.pagemodel;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Base interface for the services whose elements can directly reference the page models
 * @author E.Santoboni
 */
public interface PageModelUtilizer {
	
	/**
	 * Return the id of the utilizer service.
	 * @return The id of the utilizer
	 */
	public String getName();
	
	/**
	 * Return the list of the objects which reference the given page model.
	 * @param pageModelCode The code of the page
	 * @return The list of the object referencing the given page model
	 * @throws ApsSystemException In case of error
	 */
	public List getPageModelUtilizers(String pageModelCode) throws ApsSystemException;
	
}
