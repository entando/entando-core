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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base dell'oggetto Authentication Provider.
 * L'Authentication Provider è l'oggetto delegato alla restituzione di un'utenza 
 * (comprensiva delle sue autorizzazioni) in occasione di una richiesta di autenticazione utente.
 * @author E.Santoboni
 */
public interface IAuthenticationProviderManager {
    
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