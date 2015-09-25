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
