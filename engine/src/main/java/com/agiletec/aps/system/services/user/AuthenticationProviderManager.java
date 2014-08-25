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
package com.agiletec.aps.system.services.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;

/**
 * Implementazione concreta dell'oggetto Authentication Provider di default del sistema.
 * L'Authentication Provider è l'oggetto delegato alla restituzione di un'utenza 
 * (comprensiva delle sue autorizzazioni) in occasione di una richiesta di autenticazione utente; 
 * questo oggetto non ha visibilità ai singoli sistemi (concreti) delegati alla gestione 
 * delle autorizzazioni, ma possiede una referenza alla lista (astratta) di "Authorizators" 
 * e al Gestore Utenti (per il recupero dell'utenza base).
 * @author E.Santoboni
 */
public class AuthenticationProviderManager extends AbstractService 
		implements IAuthenticationProviderManager {

	private static final Logger _logger = LoggerFactory.getLogger(AuthenticationProviderManager.class);
	
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName() );
    }
    
    public UserDetails getUser(String username) throws ApsSystemException {
        return this.extractUser(username, null);
    }
    
    public UserDetails getUser(String username, String password) throws ApsSystemException {
        return this.extractUser(username, password);
    }
    
    protected UserDetails extractUser(String username, String password) throws ApsSystemException {
        UserDetails user = null;
        try {
            if (null == password) {
                user = this.getUserManager().getUser(username);
            } else {
                user = this.getUserManager().getUser(username, password);
            }
            if (null == user || (null != user && user.isDisabled())) {
                return null;
            }
            if (!user.getUsername().equals(SystemConstants.ADMIN_USER_NAME)) {
                if (!user.isAccountNotExpired()) {
                    _logger.info("USER ACCOUNT '{}' EXPIRED", user.getUsername());
                    return user;
                }
            }
            this.getUserManager().updateLastAccess(user);
            if (!user.isCredentialsNotExpired()) {
                _logger.info("USER '{}' credentials EXPIRED", user.getUsername());
                return user;
            }
            this.addUserAuthorizations(user);
        } catch (Throwable t) {
            throw new ApsSystemException("Error detected during the authentication of the user " + username, t);
        }
        return user;
    }
    
    protected void addUserAuthorizations(UserDetails user) throws ApsSystemException {
        if (null == user) {
            return;
        }
        //setta autorizzazioni "interne"
        for (int i = 0; i < this.getAuthorizators().size(); i++) {
            IApsAuthorityManager authorizator = this.getAuthorizators().get(i);
            List<IApsAuthority> auths = authorizator.getAuthorizationsByUser(user);
            user.addAutorities(auths);
            /*
            Nel caso che le autorizzazioni vengano settate all'esterno dell'applicazione, 
            bisogna prevedere un elemento di aggancio alle autorizzazioni remote.
            L'oggetto non può essere in questo caso generalizzabile, in quanto presuppone l'interazione diretta con il sistema esterno e 
            con la logica di assegnazione delle autorizzazioni che man mano vengono stabilite caso per caso nel sistema centrale.
             */
        }
    }

    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }

    protected List<IApsAuthorityManager> getAuthorizators() {
        return _authorizators;
    }
    public void setAuthorizators(List<IApsAuthorityManager> authorizators) {
        this._authorizators = authorizators;
    }
    
    private List<IApsAuthorityManager> _authorizators;
    private IUserManager _userManager;
    
}
