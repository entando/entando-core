/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
