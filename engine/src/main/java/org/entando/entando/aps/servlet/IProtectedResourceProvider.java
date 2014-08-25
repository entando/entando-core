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
package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.exception.ApsSystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provider bean of protected resources.
 * @author E.Santoboni
 */
public interface IProtectedResourceProvider {
	
	/**
	 * Provide a protected resource.
	 * @param request The servlet request.
	 * @param response 
	 * @return true if the resource was provided and the response was committed.
	 * @throws ApsSystemException In case of error
	 */
	public boolean provideProtectedResource(HttpServletRequest request, HttpServletResponse response) throws ApsSystemException;
	
}
