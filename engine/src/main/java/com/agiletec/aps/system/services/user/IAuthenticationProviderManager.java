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

import com.agiletec.aps.system.exception.ApsSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interfaccia base dell'oggetto Authentication Provider.
 * L'Authentication Provider è l'oggetto delegato alla restituzione di un'utenza 
 * (comprensiva delle sue autorizzazioni) in occasione di una richiesta di autenticazione utente.
 * @author E.Santoboni
 */
public interface IAuthenticationProviderManager extends AuthenticationManager, UserDetailsService {
    
    /**
     * Restituisce un'utente (comprensivo delle autorizzazioni) in base ad username. 
     * @param username La Username dell'utente da restituire.
     * @return L'utente cercato o null se non vi è nessun utente corrispondente ai parametri immessi.
     * @throws ApsSystemException In caso di errore.
     */
    public UserDetails getUser(String username) throws ApsSystemException;
    
    /**
     * Restituisce un'utente (comprensivo delle autorizzazioni) in base ad username e password. 
     * @param username La Username dell'utente da restituire.
     * @param password La password dell'utente da restituire.
     * @return L'utente cercato o null se non vi è nessun utente corrispondente ai parametri immessi.
     * @throws ApsSystemException In caso di errore.
     */
    public UserDetails getUser(String username, String password) throws ApsSystemException;
    
}