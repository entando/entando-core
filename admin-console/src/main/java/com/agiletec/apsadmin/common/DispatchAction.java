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
package com.agiletec.apsadmin.common;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * Action specifica per la gestione delle operazioni di login.
 *
 * @author E.Santoboni
 */
public class DispatchAction extends BaseAction {

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
            _logger.error("error in LoginAction ", t);
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


    /**
     * Esegue l'operazione di richiesta login utente.
     *
     * @return Il codice del risultato dell'azione.
     */
    public String doLogin() {
        return SUCCESS;
    }

    /**
     * Esegue l'operazione di richiesta logout utente.
     *
     * @return Il codice del risultato dell'azione.
     */
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
