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

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Classe action delegata alla gestione delle operazioni di associazione 
 * delle autorizzazioni agli utenti del sistema.
 * @author E.Mezzano - E.Santoboni
 */
public class AuthorityToUsersAction extends UserProfileFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AuthorityToUsersAction.class);
	
	public String addUser() {
		IApsAuthority auth = this.getApsAuthority();
		try {
			if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsernameToSet())) {
				this.addActionError(this.getText("error.user.cannotModifyAdminUser"));
				return INPUT;
			}
			UserDetails user = this.getUser();
			if (user != null && !this.hasUserAuthority()) {
				IApsAuthorityManager authorizatorManager = this.getAuthorizatorManager();
				authorizatorManager.setUserAuthorization(this.getUsernameToSet(), auth);
			}
		} catch (Throwable t) {
			_logger.error("error in addUser", t);
			//ApsSystemUtils.logThrowable(t, this, "addUser");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeUser() {
		IApsAuthority auth = this.getApsAuthority();
		try {
			if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsernameToSet())) {
				this.addActionError(this.getText("error.user.cannotModifyAdminUser"));
				return INPUT;
			}
			UserDetails user = this.getUser();
			if (user != null) {
				IApsAuthorityManager authorizatorManager = this.getAuthorizatorManager();
				authorizatorManager.removeUserAuthorization(this.getUsernameToSet(), auth);
			}
		} catch (Throwable t) {
			_logger.error("error in removeUser", t);
			//ApsSystemUtils.logThrowable(t, this, "removeUser");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public IApsAuthority getApsAuthority() {
		String authName = this.getAuthName();
		IApsAuthority authority = this.getAuthorizatorManager().getAuthority(authName);
		return authority;
	}
	
	/**
	 * Restituisce la lista degli utenti associati all'authority corrente.
	 * @return La lista degli utenti associati all'authority corrente.
	 */
	public List<UserDetails> getAuthorizedUsers() {
		IApsAuthority auth = this.getApsAuthority();
		try {
			return this.getAuthorizatorManager().getUsersByAuthority(auth);
		} catch (Throwable t) {
			_logger.error("Error in getUserAuthorizated", t);
			//ApsSystemUtils.logThrowable(t, this, "getUserAuthorizated");
			throw new RuntimeException("Error searching authorized users", t);
		}
	}
	
	/**
	 * Recupera l'utente richiesto. Se non esiste restituisce null.
	 * @return L'utente richiesto.
	 * @throws ApsSystemException In caso di errore.
	 */
	protected UserDetails getUser() throws ApsSystemException {
		String username = this.getUsernameToSet();
		UserDetails user = null;
		if (username != null && username.trim().length()>=0) {
			user = this.getUserManager().getUser(username);
		}
		return user;
	}
	
	//TODO TROVARE IL MODO DI ELIMINARE QUESTO METODO
	protected boolean hasUserAuthority() throws ApsSystemException {
		String username = this.getUsernameToSet();
		List<UserDetails> users = this.getAuthorizatorManager().getUsersByAuthority(this.getApsAuthority());
		Iterator<UserDetails> usersIter = users.iterator();
		while (usersIter.hasNext()) {
			UserDetails currentUser = usersIter.next();
			if (currentUser.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public String getAuthName() {
		return _authName;
	}
	public void setAuthName(String authName) {
		this._authName = authName;
	}
	
	public String getUsernameToSet() {
		return _usernameToSet;
	}
	public void setUsernameToSet(String usernameToSet) {
		this._usernameToSet = usernameToSet;
	}
	
	protected IApsAuthorityManager getAuthorizatorManager() {
		return _authorizatorManager;
	}
	public void setAuthorizatorManager(IApsAuthorityManager authorizatorManager) {
		this._authorizatorManager = authorizatorManager;
	}
	
	private String _authName;
	private String _usernameToSet;
	private IApsAuthorityManager _authorizatorManager;
	
}