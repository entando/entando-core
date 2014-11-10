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
package com.agiletec.apsadmin.user;

import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public abstract class AbstractAuthorityAction extends BaseAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(AbstractAuthorityAction.class);
	
	public UserDetails getUser(String username) {
		UserDetails user = null;
		try {
			user = this.getUserManager().getUser(username);
		} catch (Throwable t) {
			_logger.error("Error extracting user '{}'", username, t);
		}
		return user;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	private IUserManager _userManager;
	
}
