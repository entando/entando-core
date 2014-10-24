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
		boolean checkGroups = this.getGroup().getName().equals(other.getGroup().getName());
		boolean isNotNullBothRoles = (null != this.getRole() && null != other.getRole());
		boolean isNullBothRoles = (null == this.getRole() && null == other.getRole());
		boolean isNullAnyRoles = (null == this.getRole() || null == other.getRole());
		boolean checkRoles = (isNotNullBothRoles) ? 
				this.getRole().getName().equals(other.getRole().getName()) : 
				(isNullBothRoles || !isNullAnyRoles);
		return (checkGroups && (isNullBothRoles || checkRoles));
	}
	
	private Group _group;
	private Role _role;
	
}