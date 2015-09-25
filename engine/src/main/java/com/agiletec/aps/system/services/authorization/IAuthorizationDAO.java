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
package com.agiletec.aps.system.services.authorization;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public interface IAuthorizationDAO {
	
	public void addUserAuthorization(String username, Authorization authorization);
	
	public void addUserAuthorizations(String username, List<Authorization> authorizations);
	
	public void updateUserAuthorizations(String username, List<Authorization> authorizations);
	
	public void deleteUserAuthorization(String username, String groupname, String rolename);
	
	public List<Authorization> getUserAuthorizations(String username, Map<String, Group> groups, Map<String, Role> roles);
	
	public void deleteUserAuthorizations(String username);
	
	public List<String> getUsersByAuthorities(List<String> groupNames, List<String> roleNames);
}