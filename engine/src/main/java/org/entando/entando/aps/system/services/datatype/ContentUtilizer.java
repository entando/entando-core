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
package org.entando.entando.aps.system.services.datatype;

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
