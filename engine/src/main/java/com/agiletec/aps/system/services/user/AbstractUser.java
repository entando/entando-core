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

import com.agiletec.aps.system.services.authorization.Authorization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresentazione di un'utente astratto.
 *
 * @author M.Diana - E.Santoboni
 */
public abstract class AbstractUser implements UserDetails, Serializable {

    /**
     * Restituisce la username dell'utente.
     *
     * @return Stringa username.
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Setta lo username dell'utente.
     *
     * @param username Stringa identificatrice della username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return Stringa password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Setta la password dell'utente.
     *
     * @param password Stringa identificatrice della password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return this.getUsername();
    }

    /**
     * Verifica se l'utente è un'utente "guest" senza permessi specifici.
     *
     * @return true se l'utente è "guest", false in caso contrario.
     */
    public boolean isGuest() {
        return (this.getAuthorizations().isEmpty());
    }

    @Override
    public void addAuthorization(Authorization auth) {
        if (null == auth) return;
        if (!this.getAuthorizations().contains(auth)) {
            this.getAuthorizations().add(auth);
        }
    }

    @Override
    public void addAuthorizations(List<Authorization> auths) {
        for (Authorization auth : auths) {
            this.addAuthorization(auth);
        }
    }

    @Override
    public boolean isAccountNotExpired() {
        return true;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isCredentialsNotExpired() {
        return true;
    }

    @Override
    public List<Authorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
    }

    @Override
    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    @Override
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    private String username;
    private String password;

    private Object profile;

    private String accessToken;
    private String refreshToken;

    private List<Authorization> authorizations = new ArrayList<>();

}