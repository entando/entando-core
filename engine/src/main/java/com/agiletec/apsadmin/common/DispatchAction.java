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
package com.agiletec.apsadmin.common;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Action specifica per la gestione delle operazioni di login.
 * @author E.Santoboni
 */
public class DispatchAction extends BaseAction implements IDispatchAction {

	private static final Logger _logger = LoggerFactory.getLogger(DispatchAction.class);
	
	@Override
    public void validate() {
		super.validate();
		if (this.hasFieldErrors()) return;
    	_logger.debug("Authentication : user {} - password ******** ", this.getUsername());
    	UserDetails user = null;
		try {
			user = this.getAuthenticationProvider().getUser(this.getUsername(), this.getPassword());
		} catch (Throwable t) {
			_logger.error("error in LoginAction ",  t);
			throw new RuntimeException("Login error : username " + this.getUsername(), t);
		}
		if (null == user) {
        	_logger.debug("Login failed : username {} - password ******** ", this.getUsername());
        	this.addActionError(this.getText("error.user.login.loginFailed"));
        } else {
        	//UTENTE RICONOSCIUTO ED ATTIVO
        	if (!user.isAccountNotExpired()) {
        		this.addActionError(this.getText("error.user.login.accountExpired"));
        		this.getSession().removeAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        		return;
        	}
        	this.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
        	if (!user.isCredentialsNotExpired()) {
        		this.addActionError(this.getText("error.user.login.credentialsExpired"));
        		return;
        	}
        	if (this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER) 
        			|| this.getAuthorizationManager().isAuthOnPermission(user, Permission.BACKOFFICE)) {
    			_logger.info("User - {} logged", user.getUsername());
    		} else {
    			this.addActionError(this.getText("error.user.login.userNotAbilitated"));
    		}
        }
	}
    
	@Override
    public String doLogin() {
    	return SUCCESS;
    }
    
	@Override
    public String doLogout() {
    	this.getSession().invalidate();
    	return "homepage";
    }
    
    public String getUsername() {
    	return _username;
    }
    public void setUsername(String username) {
    	this._username = username;
    }
    
    public String getPassword() {
    	return _password;
    }
    public void setPassword(String password) {
    	this._password = password;
    }
	
	protected HttpSession getSession() {
		return this.getRequest().getSession();
	}
	
	protected IAuthenticationProviderManager getAuthenticationProvider() {
		return _authenticationProvider;
	}
	public void setAuthenticationProvider(IAuthenticationProviderManager authenticationProvider) {
		this._authenticationProvider = authenticationProvider;
	}
    
	private String _username;
    private String _password;
    
	private IAuthenticationProviderManager _authenticationProvider;
	
}
