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
import java.util.List;
import java.util.ArrayList;

/**
 * Rappresentazione di un'utente astratto.
 * @author M.Diana - E.Santoboni
 */
public abstract class AbstractUser implements UserDetails, Serializable {

	/**
	 * Restituisce la username dell'utente.
	 * @return Stringa username.
	 */
	@Override
	public String getUsername() {
		return this._username;
	}

	/**
	 * Setta lo username dell'utente.
	 * @param username Stringa identificatrice della username.
	 */
	public void setUsername(String username) {
		this._username = username;
	}

	/**
	 * Restituisce la password dell'utente.
	 * @return Stringa password.
	 */
	@Override
	public String getPassword() {
		return _password;
	}

	/**
	 * Setta la password dell'utente.
	 * @param password Stringa identificatrice della password.
	 */
	public void setPassword(String password) {
		this._password = password;
	}

	@Override
	public String toString() {
		return this.getUsername();
	}

	/**
	 * Verifica se l'utente è un'utente "guest" senza permessi specifici.
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
		for (int i=0; i<auths.size(); i++) {
			Authorization auth = auths.get(i);
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
		return _authorizations;
	}
	
	public void setAuthorizations(List<Authorization> authorizations) {
		this._authorizations = authorizations;
	}
	
	@Override
	public Object getProfile() {
		return _profile;
	}
	
	public void setProfile(Object profile) {
		this._profile = profile;
	}

	private String _username;
	private String _password;

	private Object _profile;
	
	private List<Authorization> _authorizations = new ArrayList<Authorization>();
	
}