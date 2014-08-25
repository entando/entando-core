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
package com.agiletec.aps.system.services.authorization.authorizator;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

/**
 * Interfaccia base per gli oggetti DAO delle autorizzazioni.
 * L'interfaccia fornisce i metodi base per l'implementazioni dei dao gestori 
 * di tipi autorizzazioni. Nelle implementazioni concrete viene utilizzata per le implementazioni 
 * dei dao di Gruppi (oggetti {@link Group}) e Ruoli (oggetti {@link Role}).
 * @author E.Santoboni
 */
public interface IApsAuthorityDAO {
	
	/**
	 * Setta una autorizzazione di un utente.
	 * @param username L'utente al quale associare la autorizzazione.
	 * @param auth La autorizzazione da associare all'utente.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void setUserAuthorization(String username, IApsAuthority auth) throws ApsSystemException;
	
	/**
	 * Rimuove una autorizzazione di un utente.
	 * @param username L'utente al quale rimuovere la autorizzazione.
	 * @param auths La autorizzazione da rimuovere all'utente speecificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void removeUserAuthorization(String username, IApsAuthority auth) throws ApsSystemException;
	
	/**
	 * Setta le autorizzazioni di un utente.
	 * Il metodo effettua un'aggiunta dele autorizzazioni specificate a quelle già associate all'utente, ma 
	 * elimina preliminarmente le autorizzazioni dell'utente per poi settare le autorizzazioni specificate.
	 * @param username L'utente al quale associare le autorizzazioni.
	 * @param auths Le autorizzazioni da associare.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void setUserAuthorizations(String username, List<IApsAuthority> auths) throws ApsSystemException;
	
	/**
	 * Restituisce le autorizzazioni (nomi) assegnate all'utente specificato.
	 * @param username Lo username dell'utente.
	 * @return La lista di autorizzazioni assegnate all'utente specificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<String> getAuthorizationNamesForUser(String username) throws ApsSystemException;
	
	/**
	 * Restituisce la lista di utenti (username) cui è stata assegnata l'autorizzazione specificata.
	 * @param auth L'autorizzazione.
	 * @return La lista di utenti (username) cui è stata assegnata l'autorizzazione specificata.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<String> getUserAuthorizated(IApsAuthority auth) throws ApsSystemException;
	
}
