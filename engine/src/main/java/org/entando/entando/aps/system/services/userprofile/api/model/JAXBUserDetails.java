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
package org.entando.entando.aps.system.services.userprofile.api.model;

import com.agiletec.aps.system.services.authorization.Authorization;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "user")
@XmlType(propOrder = {"username", "authorizations", "profile"})
public class JAXBUserDetails {
    
    public JAXBUserDetails() {}
    
    public JAXBUserDetails(UserDetails userDetails) {
        this.setUsername(userDetails.getUsername());
		this.setAuthorizations(userDetails.getAuthorizations());
		/*
        IApsAuthority[] autorities = userDetails.getAuthorities();
        if (null != autorities) {
            for (int i = 0; i < autorities.length; i++) {
                IApsAuthority authority = autorities[i];
                if (authority instanceof Role) {
                    this.addRole((Role) authority);
                } else if (authority instanceof Group) {
                    this.addGroup((Group) authority);
                }
            }
        }
		*/
        Object profile = userDetails.getProfile();
        if (null != profile && profile instanceof IUserProfile) {
            this.setProfile(new JAXBUserProfile((IUserProfile) profile, null));
        }
    }
    
    @XmlElement(name = "username", required = true)
    public String getUsername() {
        return _username;
    }
    public void setUsername(String username) {
        this._username = username;
    }

	/*
	protected void addGroup(Group group) {
		if (null == group) {
			return;
		}
		if (null == this.getGroups()) {
			this.setGroups(new ArrayList<Group>());
		}
		this.getGroups().add(group);
	}
	@XmlElement(name = "group", required = true)
	@XmlElementWrapper(name = "groups", required = false)
	public List<Group> getGroups() {
		return _groups;
	}
	protected void setGroups(List<Group> groups) {
		this._groups = groups;
	}
	
	protected void addRole(Role role) {
		if (null == role) {
			return;
		}
		if (null == this.getRoles()) {
			this.setRoles(new ArrayList<Role>());
		}
		this.getRoles().add(role);
	}
	@XmlElement(name = "role", required = true)
	@XmlElementWrapper(name = "roles", required = false)
	public List<Role> getRoles() {
		return _roles;
	}
	protected void setRoles(List<Role> roles) {
		this._roles = roles;
	}
	*/
	
	@XmlElement(name = "authorization", required = true)
	@XmlElementWrapper(name = "authorizations", required = false)
	public List<Authorization> getAuthorizations() {
		return _authorizations;
	}
	public void setAuthorizations(List<Authorization> authorizations) {
		this._authorizations = authorizations;
	}
	
    @XmlElement(name = "profile", required = false)
    public JAXBUserProfile getProfile() {
        return _profile;
    }
    public void setProfile(JAXBUserProfile profile) {
        this._profile = profile;
    }
    
    private String _username;
	private List<Authorization> _authorizations;
    //private List<Group> _groups;
    //private List<Role> _roles;
    private JAXBUserProfile _profile;
    
}
