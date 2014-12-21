/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.apsadmin.resource.helper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Interfaccia Base per le classi helper della gestione risorse.
 * @author E.Santoboni
 */
public interface IResourceActionHelper {
	
	/**
	 * Return the map (indexed by the Key of the manager) of the list of the object that reference the given resource.
	 * @param resourceId The id of the resource.
	 * @param request The request
	 * @return The References of the given resource.
	 * @throws ApsSystemException In case of exception
	 */
	public Map<String, List> getReferencingObjects(String resourceId, HttpServletRequest request) throws ApsSystemException;
	
	/**
	 * Return the map (indexed by the Key of the manager) of the list of the object that reference the given resource.
	 * @param resource The resource.
	 * @param request The request
	 * @return The References of the given resource.
	 * @throws ApsSystemException In case of exception
	 */
	public Map<String, List> getReferencingObjects(ResourceInterface resource, HttpServletRequest request) throws ApsSystemException;
	
}