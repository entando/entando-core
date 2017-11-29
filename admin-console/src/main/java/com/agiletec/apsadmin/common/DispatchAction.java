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
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

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

                this.addOAuth2Token(user.getUsername());

                _logger.info("User - {} logged", user.getUsername());
            } else {
                this.addActionError(this.getText("error.user.login.userNotAbilitated"));
            }
        }
    }


    private OAuth2Token createOAuth2Token(final OAuthIssuer oauthIssuerImpl, final String localUser){
        final int expires = 3600;
        final OAuth2Token oAuth2Token = new OAuth2Token();
        try {
            oAuth2Token.setAccessToken(oauthIssuerImpl.accessToken());
            oAuth2Token.setRefreshToken(oauthIssuerImpl.refreshToken());
        } catch (OAuthSystemException e) {
            _logger.error("OAuthSystemException - {}",e);
        }
        oAuth2Token.setClientId("LOCAL_USER");
        oAuth2Token.setLocalUser(localUser);
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, expires);
        oAuth2Token.setExpiresIn(calendar.getTime());
        oAuth2Token.setGrantType(GrantType.AUTHORIZATION_CODE.toString());
        return oAuth2Token;
    }

    private void addOAuth2Token(final String localUser) {
        try {
            OAuth2Token oAuth2Token;
            final String accessToken = tokenManager.getAccessTokenFromLocalUser(localUser);
            if (accessToken == null){
                oAuth2Token = createOAuth2Token(new OAuthIssuerImpl(new MD5Generator()),localUser);
                tokenManager.addApiOAuth2Token(oAuth2Token, true);
            }
            else {

                oAuth2Token = tokenManager.getApiOAuth2Token(accessToken);

                if (oAuth2Token.getExpiresIn().getTime() > System.currentTimeMillis()){
                    tokenManager.updateToken(accessToken,3600);
                }
            }



        } catch (ApsSystemException e) {
            _logger.error("Exception during creation oauth2 token {}" ,e);
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

    public IApiOAuth2TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(IApiOAuth2TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    private String _username;

    private String _password;

    private IAuthenticationProviderManager _authenticationProvider;

    private IApiOAuth2TokenManager tokenManager;

}
