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
package org.entando.entando.apsadmin.user;

import com.agiletec.aps.system.services.authorization.Authorization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class UserAuthsFormBean implements Serializable {
	
	protected UserAuthsFormBean(String username, List<Authorization> authorizations) {
		this.setUsername(username);
		this.setAuthorizations(authorizations);
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public boolean addAuthorization(Authorization authorization) {
		if (null == this.getAuthorizations()) {
			this.setAuthorizations(new ArrayList<Authorization>());
		}
		if (!this.getAuthorizations().contains(authorization)) {
			this.getAuthorizations().add(authorization);
			return true;
		} else {
			return false;
		}
	}
	
	public void removeAuthorization(Authorization authorization) {
		if (null == this.getAuthorizations()) return;
		this.getAuthorizations().remove(authorization);
	}
	
	public boolean removeAuthorization(int index) {
		if (null == this.getAuthorizations() || this.getAuthorizations().size() <= index) return false;
		Authorization authorization = this.getAuthorizations().remove(index);
		return (null != authorization);
	}
	
	public List<Authorization> getAuthorizations() {
		return _authorizations;
	}
	public void setAuthorizations(List<Authorization> authorizations) {
		this._authorizations = authorizations;
	}
	
	private String _username;
	private List<Authorization> _authorizations;
	
}
