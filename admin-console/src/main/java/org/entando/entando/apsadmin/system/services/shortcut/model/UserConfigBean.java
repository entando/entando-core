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
package org.entando.entando.apsadmin.system.services.shortcut.model;

import java.io.Serializable;

/**
 * The shortcut user config class.
 * @author E.Santoboni
 */
public class UserConfigBean implements Serializable {
	
	public UserConfigBean(String username, String[] config) {
		this.setUsername(username);
		this.setConfig(config);
	}
	
	public String getUsername() {
		return _username;
	}
	protected void setUsername(String username) {
		this._username = username;
	}
	
	public String[] getConfig() {
		return _config;
	}
	protected void setConfig(String[] config) {
		this._config = config;
	}
	
	private String _username;
	private String[] _config;
	
}