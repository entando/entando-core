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
package com.agiletec.aps.system.services.authorization.authorizator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Classe astratta base per le classi manager gestori delle autorizzazioni.
 * @author E.Santoboni
 */
public abstract class AbstractApsAutorityManager extends AbstractService implements IApsAuthorityManager {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractApsAutorityManager.class);
	
	@Override
	public List<UserDetails> getUsersByAuthority(IApsAuthority authority) throws ApsSystemException {
		if (!this.checkAuthority(authority)) {
			return null;
		}
		List<UserDetails> users = new ArrayList<UserDetails>();
		try {
			List<String> usernames = this.getUsernamesByAuthority(authority);
			if (null != usernames && !usernames.isEmpty()) {
				for (int i=0; i<usernames.size(); i++) {
					String username = usernames.get(i);
					UserDetails user = this.getUserManager().getUser(username);
					if (null != user) {
						users.add(user);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error retrieving the list of authorized users", t);
			//ApsSystemUtils.logThrowable(t, this, "getUsersByAuthority");
			throw new ApsSystemException("Error retrieving the list of authorized users", t);
		}
		return users;
	}
	
	@Override
	public List<String> getUsernamesByAuthority(IApsAuthority authority) throws ApsSystemException {
		if (!this.checkAuthority(authority)) {
			return null;
		}
		List<String> usernames = null;
		try {
			usernames = this.getAuthorizatorDAO().getUserAuthorizated(authority);
		} catch (Throwable t) {
			_logger.error("Error retrieving the list of authorized users", t);
			//ApsSystemUtils.logThrowable(t, this, "getUsernamesByAuthority");
			throw new ApsSystemException("Error retrieving the list of authorized users", t);
		}
		return usernames;
	}
	
	@Override
	public void setUserAuthorization(String username, IApsAuthority authority) throws ApsSystemException {
		if (!this.checkAuthority(authority)) return;
		try {
			this.getAuthorizatorDAO().setUserAuthorization(username, authority);
		} catch (Throwable t) {
			_logger.error("Error while setting the user authorization for {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "setUserAuthorization");
			throw new ApsSystemException("Error while setting the user authorization", t);
		}
	}
	
	@Override
	public void removeUserAuthorization(String username, IApsAuthority authority) throws ApsSystemException {
		if (!this.checkAuthority(authority)) return;
		try {
			this.getAuthorizatorDAO().removeUserAuthorization(username, authority);
		} catch (Throwable t) {
			_logger.error("Error while deleting the user authorization for {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "removeUserAuthorization");
			throw new ApsSystemException("Error while deleting the user authorization", t);
		}
	}
	
	@Override
	public void setUserAuthorizations(String username, List<IApsAuthority> authorities) throws ApsSystemException {
		for (int i = 0; i < authorities.size(); i++) {
			IApsAuthority authorityToVerify = authorities.get(i);
			if (!this.checkAuthority(authorityToVerify)) {
				_logger.warn("Attempt to set invalid authority to user {}", username);
				return;
			}
		}
		try {
			this.getAuthorizatorDAO().setUserAuthorizations(username, authorities);
		} catch (Throwable t) {
			_logger.error("Error detected while granting user authorizations for {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "setUserAuthorizations");
			throw new ApsSystemException("Error detected while granting user authorizations", t);
		}
	}
	
	@Override
	public List<IApsAuthority> getAuthorizationsByUser(UserDetails user) throws ApsSystemException {
		return this.getAuthorizationsByUser(user.getUsername());
	}
	
	@Override
	public List<IApsAuthority> getAuthorizationsByUser(String username) throws ApsSystemException {
		List<IApsAuthority> auths = new ArrayList<IApsAuthority>();
		try {
			List<String> authsName = this.getAuthorizatorDAO().getAuthorizationNamesForUser(username);
			for (int i=0; i<authsName.size(); i++) {
				String authName = authsName.get(i);
				IApsAuthority auth = this.getAuthority(authName);
				if (null != auth) {
					auths.add(auth);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while retrieving the authorizations of the user {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "getAuthorizationsByUser");
			throw new ApsSystemException("Error while retrieving the authorizations of the user", t);
		}
		return auths;
	}
	
	/**
	 * Verify the validity of the Authority.
	 * @param authority The authority to verify.
	 * @return True if the authority is valid, else false.
	 */
	protected boolean checkAuthority(IApsAuthority authority) {
		if (null == authority) {
			_logger.warn("Invalid authority detected");
//					"Required Users by null authority";
			return false;
		}
		IApsAuthority authForCheck = this.getAuthority(authority.getAuthority());
		if (null == authForCheck) {
			_logger.warn("The authority with code {} does not exist", authority.getAuthority());
//					"Required Users by not existing authority : code " + authority.getAuthority());
			return false;
		}
		if (!authForCheck.getClass().equals(authority.getClass())) {
			_logger.warn("Mismatching authority classes detected; code {} - Class {} is different by {}", authority.getAuthority(), authority.getClass(), authForCheck.getClass());
//					"Required Users by invalid authority: code " + authority.getAuthority() + " - Class " + authority.getClass());
			return false;
		}
		return true;
	}
	
	/**
	 * Return the Data Access Object for the specific Authority.
	 * @return The required Data Access Object.
	 */
	protected abstract IApsAuthorityDAO getAuthorizatorDAO();
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	private IUserManager _userManager;
	
}