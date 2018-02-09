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

import java.util.List;

/**
 * Abstract description of a generic user
 *
 * @author E.Santoboni
 */
public interface UserDetails {

    /**
     * Return 'true' if the current user is an Entando user, that is, exists within jAPS local table
     *
     * @return 'true' if the current user is an Entando user
     * @deprecated use isEntandoUser()
     */
    public boolean isJapsUser();

    public boolean isEntandoUser();

    /**
     * Get the authorizations of the current user
     *
     * @return The user authorizations
     */
    public List<Authorization> getAuthorizations();

    /**
     * Add an authorization to the current user
     *
     * @param auth The authorization to add
     */
    public void addAuthorization(Authorization auth);

    /**
     * Add a list of authorizations to the current user
     *
     * @param auths The authorizations to set
     */
    public void addAuthorizations(List<Authorization> auths);

    /**
     * Return the plain password (that is, NOT decrypted) of the current user
     *
     * @return the user password
     */
    public String getPassword();

    /**
     * Return the username or, in other words, the ID of the current user
     *
     * @return the username
     */
    public String getUsername();

    /**
     * Return the expiration status of the current user
     *
     * @return 'true' if the user is not expired, false otherwise
     */
    public boolean isAccountNotExpired();

    /**
     * Return the credential status of the current user
     *
     * @return 'true' when the credentials are not expired, false otherwise
     */
    public boolean isCredentialsNotExpired();

    /**
     * Return the ability of the current user to access the system
     *
     * @return 'true' if the current user has been disabled
     */
    public boolean isDisabled();

    /**
     * Return the profile associated to the current user, if any
     *
     * @return The profile
     */
    public Object getProfile();

    void setAccessToken(final String accessToken);

    String getAccessToken();

    void setRefreshToken(final String refreshToken);

    String getRefreshToken();

}
