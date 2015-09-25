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
package org.entando.entando.apsadmin.system.services.shortcut;

/**
 * Interface for the Data Access Object for the configuration of user shortcut.
 * @author E.Santoboni
 */
public interface IUserShortcutDAO {
	
	/**
	 * Load the configuration by user.
	 * The returned string will be a xml 
	 * (if the specificated user has a configuration) or null;
	 * @param username The username.
	 * @return The config of the given username.
	 */
	public String loadUserConfig(String username);
	
	/**
	 * Save a user configuration.
	 * @param username The username.
	 * @param config The xml string to save
	 */
	public void saveUserConfig(String username, String config);
	
	/**
	 * Delete a user configuration
	 * @param username The username of the config to delete
	 */
	public void deleteUserConfig(String username);
	
}