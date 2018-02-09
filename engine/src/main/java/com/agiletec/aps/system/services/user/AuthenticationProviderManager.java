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
package com.agiletec.aps.system.services.user;

import java.util.Calendar;
import java.util.List;

import com.agiletec.aps.system.services.role.Permission;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;

/**
 * Implementazione concreta dell'oggetto Authentication Provider di default del sistema.
 * L'Authentication Provider è l'oggetto delegato alla restituzione di un'utenza 
 * (comprensiva delle sue autorizzazioni) in occasione di una richiesta di autenticazione utente; 
 * questo oggetto non ha visibilità ai singoli sistemi (concreti) delegati alla gestione 
 * delle autorizzazioni.
 * @author E.Santoboni
 */
public class AuthenticationProviderManager extends AbstractService 
		implements IAuthenticationProviderManager {

	private static final Logger _logger = LoggerFactory.getLogger(AuthenticationProviderManager.class);
	
	@Override
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName() );
    }
    
	@Override
    public UserDetails getUser(String username) throws ApsSystemException {
        return this.extractUser(username, null);
    }
    
	@Override
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
            if (null == user || user.isDisabled()) {
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

            if (this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER)){
                this.registerToken(user);
            }


        } catch (Throwable t) {
            throw new ApsSystemException("Error detected during the authentication of the user " + username, t);
        }
        return user;
    }

    private void registerToken(final UserDetails user) {

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            final String accessToken = oauthIssuerImpl.accessToken();
            final String refreshToken = oauthIssuerImpl.refreshToken();
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            final OAuth2Token oAuth2Token = new OAuth2Token();
            oAuth2Token.setAccessToken(accessToken);
            oAuth2Token.setRefreshToken(refreshToken);
            oAuth2Token.setClientId("LOCAL_USER");
            oAuth2Token.setLocalUser(user.getUsername());
            Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, 3600);
            oAuth2Token.setExpiresIn(calendar.getTime());
            oAuth2Token.setGrantType(GrantType.IMPLICIT.toString());
            tokenManager.addApiOAuth2Token(oAuth2Token,true);
        } catch (OAuthSystemException e) {
            _logger.error("OAuthSystemException {} ", e.getMessage());
            _logger.debug("OAuthSystemException {} ", e);

        } catch (ApsSystemException e) {
            _logger.error("ApsSystemException {} ", e.getMessage());
            _logger.debug("ApsSystemException {} ", e);
        }


    }

    protected void addUserAuthorizations(UserDetails user) throws ApsSystemException {
        if (null == user) {
            return;
        }
		List<Authorization> auths = this.getAuthorizationManager().getUserAuthorizations(user.getUsername());
		if (null == auths) {
			return;
		}
		for (int i = 0; i < auths.size(); i++) {
			Authorization authorization = auths.get(i);
			user.addAuthorization(authorization);
		}
    }

	
    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

    public IApiOAuth2TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(IApiOAuth2TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    private IUserManager _userManager;
	private IAuthorizationManager _authorizationManager;
    private IApiOAuth2TokenManager tokenManager;
    
}
