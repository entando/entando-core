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
package org.entando.entando.apsadmin.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.authorization.IApsAuthority;

/**
 * @author E.Santoboni
 */
public class UserAuthsFormBean implements Serializable {
	
	public UserAuthsFormBean(String username, List<IApsAuthority> roles, List<IApsAuthority> groups) {
		this.setUsername(username);
		if (null != roles) {
			this.getRoles().addAll(roles);
		}
		if (null != groups) {
			this.getGroups().addAll(groups);
		}
	}
	
	public List<IApsAuthority> getGroups() {
		return _groups;
	}
	public void setGroups(List<IApsAuthority> groups) {
		this._groups = groups;
	}
	public void addGroup(IApsAuthority group) {
		if (!this._groups.contains(group)) {
			this._groups.add(group);
		}
	}
	public void removeGroup(IApsAuthority group) {
		this._groups.remove(group);
	}
	
	public List<IApsAuthority> getRoles() {
		return _roles;
	}
	public void setRoles(List<IApsAuthority> roles) {
		this._roles = roles;
	}
	public void addRole(IApsAuthority role) {
		if (!this._roles.contains(role)) {
			this._roles.add(role);
		}
	}
	public void removeRole(IApsAuthority role) {
		this._roles.remove(role);
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	private String _username;
	
	private List<IApsAuthority> _roles = new ArrayList<IApsAuthority>();
	private List<IApsAuthority> _groups = new ArrayList<IApsAuthority>();
	
}
