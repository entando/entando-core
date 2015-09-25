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
package com.agiletec.apsadmin.admin;

import java.util.Map;

/**
 * This interface declares all the base actions available for the system administration.
 * @author E.Santoboni
 */
public interface IBaseAdminAction {
	
	/**
	 * Get the system parameters in order to edit them.
	 * @return the result code.
	 */
	public String configSystemParams();
	
	/**
	 * Update the system params.
	 * @return the result code.
	 */
	public String updateSystemParams();
	
	public Map<String, String> getSystemParams();
	
	/**
	 * Reload the system configuration.
	 * @return the result code.
	 */
	public String reloadConfig();
	
	/**
	 * Reload the references of all the existing entities.
	 * @return the result code.
	 */
	public String reloadEntitiesReferences();
	
}
