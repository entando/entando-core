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
package com.agiletec.aps.system.services.page;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Base interface for the services whose elements can directly reference the pages in the portal
 * @author E.Santoboni
 */
public interface PageUtilizer {
	
	/**
	 * Return the id of the utilizer service.
	 * @return The id of the utilizer
	 */
	public String getName();
	
	/**
	 * Return the list of the objects which reference the given page.
	 * @param pageCode The code of the page
	 * @return The list of the object referencing the given page 
	 * @throws ApsSystemException In case of error
	 */
	public List getPageUtilizers(String pageCode) throws ApsSystemException;
	
}
