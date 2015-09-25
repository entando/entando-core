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
package org.entando.entando.aps.system.services.userprofile;

import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.User;

public class MockUser extends User {
	
	public void addAuthorization(Group group, Role role) {
		if (null == group) return;
		Authorization auth = new Authorization(group, role);
		super.addAuthorization(auth);
	}
	
	/*
	public Set<Group> getGroups() {
		return _groups;
	}
	
	public void setGroups(Set<Group> groups) {
		Iterator<Group> iter = groups.iterator();
		while (iter.hasNext()) {
			Group group = (Group) iter.next();
			this.addAutority(group);
		}
		this._groups = groups;
	}
	
	public void addGroup(Group group) {
		this.addAutority(group);
		this._groups.add(group);
	}
	
	public Set<Role> getRoles() {
		return _roles;
	}
	
	public void setRoles(Set<Role> roles) {
		Iterator<Role> iter = roles.iterator();
		while (iter.hasNext()) {
			Role role = (Role) iter.next();
			this.addAutority(role);
		}
		this._roles = roles;
	}
	
	public void addRole(Role role) {
		this.addAutority(role);
		this._roles.add(role);
	}
	
	private Set<Group> _groups = new HashSet<Group>();
    private Set<Role> _roles = new HashSet<Role>();
	*/
}