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
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interfaccia base per le classi manager gestori delle autorizzazioni.
 * L'interfaccia fornisce essenzialmente i metodi per la gestione delle associazioni 
 * tra autorizzazione ed utente.
 * Nelle implementazioni concrete viene utilizzata per le implementazioni 
 * dei Manager gestori degli oggetti Gruppi (classe {@link Group}) e Ruoli (classe {@link Role}).
 * @author E.Santoboni
 */
public interface IApsAuthorityManager {
	
	/**
	 * Restituisce la lista di utenti con l'autorizzazione specificata.
	 * @param authority L'autorizzazione.
	 * @return La lista di utenti autorizzati.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<UserDetails> getUsersByAuthority(IApsAuthority authority) throws ApsSystemException;
	
	public List<String> getUsernamesByAuthority(IApsAuthority authority) throws ApsSystemException;
	
	/**
	 * Setta le autorizzazioni di un utente.
	 * Il metodo effettua un'aggiunta dele autorizzazioni specificate a quelle già associate all'utente, ma 
	 * elimina preliminarmente le autorizzazioni dell'utente per poi settare le autorizzazioni specificate.
	 * @param username L'utente al quale associare le autorizzazioni.
	 * @param authorities Le autorizzazioni da associare.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void setUserAuthorizations(String username, List<IApsAuthority> authorities) throws ApsSystemException;
	
	/**
	 * Setta una autorizzazione di un utente.
	 * @param username L'utente al quale associare la autorizzazione.
	 * @param authority La autorizzazione da aggiungere.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void setUserAuthorization(String username, IApsAuthority authority) throws ApsSystemException;
	
	/**
	 * Rimuove una autorizzazione di un utente.
	 * @param username L'utente al quale rimuovere la autorizzazione.
	 * @param authority La autorizzazione da rimuovere all'utente speecificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void removeUserAuthorization(String username, IApsAuthority authority) throws ApsSystemException;
	
	/**
	 * Restituisce la lista delle autorizzazioni detenute dall'utente specificato.
	 * @param user L'utente cui ricavare le autorizzazioni.
	 * @return Le autorizzazioni dell'utente specificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<IApsAuthority> getAuthorizationsByUser(UserDetails user) throws ApsSystemException;
	
	public List<IApsAuthority> getAuthorizationsByUser(String username) throws ApsSystemException;
	
	/**
	 * Restituisce una autorizzazione in base al nome.
	 * @param authName Il nome dell'autorizzazione da restituire.
	 * @return L'autorizzazione cercata.
	 */
	public IApsAuthority getAuthority(String authName);
	
}
