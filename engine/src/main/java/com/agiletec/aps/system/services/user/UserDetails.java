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

import com.agiletec.aps.system.services.authorization.IApsAuthority;

/**
 * Abstract description of a generic user
 * @author E.Santoboni
 */
public interface UserDetails {
	
	/**
	 * Return 'true' if the current user is a jAPS user, that is, exists within jAPS local table
	 * @return
	 * @deprecated use isEntandoUser
	 */
	public boolean isJapsUser();
	
	public boolean isEntandoUser();
	
	/**
	 * Get the authorities of the current user
	 * @return
	 */
	public IApsAuthority[] getAuthorities();
	
	/**
	 * Add an authority to the current user
	 * @param auth
	 */
	public void addAutority(IApsAuthority auth);
    
	/**
	 * Add a list of authorities to the current user
	 * @param auths
	 */
    public void addAutorities(List<IApsAuthority> auths);
    
    /**
     * Return the plain password (that is, NOT decrypted) of the current user
     * @return the user password
     */
    public java.lang.String getPassword();
	
    /**
     * Return the username or, in other words, the ID of the current user
     * @return
     */
	public java.lang.String getUsername();
	
	/**
	 * Return the expiration status of the current user
	 * @return 'true' if the user is not expired, false otherwise
	 */
	public boolean isAccountNotExpired();
	
	/**
	 * Return the credential status of the current user
	 * @return 'true' when the credentials are not expired, false otherwise
	 */
	public boolean isCredentialsNotExpired();
	
	/**
	 * Return the ability of the current user to access the system
	 * @return 'true' if the current user has been disabled
	 */
	public boolean isDisabled();
	
	/**
	 * Return the profile associated to the current user, if any
	 * @return
	 */
	public Object getProfile();
	
}
