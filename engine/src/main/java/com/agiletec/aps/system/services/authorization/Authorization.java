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
package com.agiletec.aps.system.services.authorization;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "authorization")
public class Authorization implements Serializable {
	
	protected Authorization() {}
	
	public Authorization(Group group, Role role) {
		this._group = group;
		this._role = role;
	}
	
	@XmlElement(name = "group", required = false)
	public Group getGroup() {
		return _group;
	}
	protected void setGroup(Group group) {
		this._group = group;
	}
	
	@XmlElement(name = "role", required = false)
	public Role getRole() {
		return _role;
	}
	protected void setRole(Role role) {
		this._role = role;
	}
	
	@Override
	public boolean equals(Object obj) {
		Authorization other = (Authorization) obj;
		boolean isGroupsEquals = (null == this.getGroup() && null == other.getGroup()) || 
				(null != this.getGroup() && null != other.getGroup() && other.getGroup().equals(this.getGroup()));
		boolean isRolesEquals = (null == this.getRole() && null == other.getRole()) || 
				(null != this.getRole() && null != other.getRole() && other.getRole().equals(this.getRole()));
		return (isRolesEquals && isGroupsEquals);
	}
	
	private Group _group;
	private Role _role;
	
}