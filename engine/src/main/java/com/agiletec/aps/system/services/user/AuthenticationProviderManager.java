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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import java.util.List;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Implementazione concreta dell'oggetto Authentication Provider di default del
 * sistema. L'Authentication Provider è l'oggetto delegato alla restituzione di
 * un'utenza (comprensiva delle sue autorizzazioni) in occasione di una
 * richiesta di autenticazione utente; questo oggetto non ha visibilità ai
 * singoli sistemi (concreti) delegati alla gestione delle autorizzazioni.
 *
 * @author E.Santoboni
 */
public class AuthenticationProviderManager extends AbstractService
        implements IAuthenticationProviderManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderManager.class);

    private IUserManager userManager;
    private IAuthorizationManager authorizationManager;
    private IApiOAuth2TokenManager tokenManager;

    @Override
    public void init() throws Exception {
        logger.debug("{} ready", this.getClass().getName());
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
        return this.extractUser(username, password, true);
    }

    protected UserDetails extractUser(String username, String password, boolean addToken) throws ApsSystemException {
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
                    logger.info("USER ACCOUNT '{}' EXPIRED", user.getUsername());
                    return user;
                }
            }
            this.getUserManager().updateLastAccess(user);
            if (!user.isCredentialsNotExpired()) {
                logger.info("USER '{}' credentials EXPIRED", user.getUsername());
                return user;
            }
            this.addUserAuthorizations(user);
            if (addToken) {
                OAuth2AccessToken token = this.getTokenManager().createAccessTokenForLocalUser(username);
                user.setAccessToken(token.getValue());
                user.setRefreshToken(token.getRefreshToken().getValue());
            }
        } catch (Exception e) {
            logger.error("Error detected during the authentication of the user '{}'", username, e);
            throw new ApsSystemException("Error detected during the authentication of the user " + username, e);
        }
        return user;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            if (null == authentication
                    || null == authentication.getPrincipal()
                    || null == authentication.getCredentials()) {
                throw new UsernameNotFoundException("Invalid principal and/or credentials");
            }
            UserDetails user = this.extractUser(authentication.getPrincipal().toString(),
                    authentication.getCredentials().toString(), false);
            if (null != user) {
                Authentication newAuth
                        = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                                authentication.getCredentials(), user.getAuthorizations());
                return newAuth;
            } else {
                throw new UsernameNotFoundException("Invalid username/password");
            }
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error detected during user authentication", e);
            throw new AuthenticationServiceException("Error detected during user authentication", e);
        }
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDetails localUser = this.extractUser(username, null, false);
            if (null == localUser) {
                throw new UsernameNotFoundException("User " + username + " not found");
            }
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username,
                    "", !localUser.isDisabled(), localUser.isAccountNotExpired(),
                    localUser.isCredentialsNotExpired(), true, localUser.getAuthorizations());
            return user;
        } catch (ApsSystemException ex) {
            logger.error("Error extracting user {}", username, ex);
            throw new UsernameNotFoundException("Error extracting user " + username, ex);
        }
    }

    protected IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    protected IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    protected IApiOAuth2TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(IApiOAuth2TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

}
