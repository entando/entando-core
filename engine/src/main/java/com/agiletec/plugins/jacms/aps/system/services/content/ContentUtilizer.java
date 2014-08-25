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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for the services whose handled elements may have references to contents. 
 * @author E.Santoboni
 */
public interface ContentUtilizer {
	
	/**
	 * Return the ID of the utilizer service.
	 * @return the ID of the utilizer service. 
	 */
	public String getName();
	
	/**
	 * Return the list of the objects which reference the given content.
	 * @param contentId The code of the content to inspect.
	 * @return the list of the objects which reference the content. 
	 * @throws ApsSystemException in case of error.
	 */
	public List getContentUtilizers(String contentId) throws ApsSystemException;
	
}
