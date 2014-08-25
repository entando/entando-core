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

import java.io.Serializable;
import java.util.List;

import com.agiletec.aps.system.services.authorization.IApsAuthority;

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
		return (this.getAuthorities().length == 0);
	}
	
	@Override
	public void addAutority(IApsAuthority auth) {
		int len = this.getAuthorities().length;
		IApsAuthority[] newAuths = new IApsAuthority[len + 1];
		for (int i=0; i < len; i++){
			newAuths[i] = this.getAuthorities()[i];
		}
		newAuths[len] = auth;
		this._authorities = newAuths;
	}

	@Override
	public void addAutorities(List<IApsAuthority> auths) {
		for (int i=0; i<auths.size(); i++) {
			IApsAuthority auth = auths.get(i);
			this.addAutority(auth);
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

	public void setAuthorities(IApsAuthority[] authorities) {
		this._authorities = authorities;
	}

	@Override
	public IApsAuthority[] getAuthorities() {
		return _authorities;
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

	private IApsAuthority[] _authorities = new IApsAuthority[0];

}