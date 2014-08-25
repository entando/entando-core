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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi di gestione utenti.
 * @author M.Diana - E.Santoboni
 */
public interface IUserManager {
	
	public List<String> getUsernames() throws ApsSystemException;
	
	public List<String> searchUsernames(String text) throws ApsSystemException;
	
	/**
	 * Restituisce la lista completa degli utenti (in oggetti User).
	 * @return La lista completa degli utenti (in oggetti User).
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<UserDetails> getUsers() throws ApsSystemException;

	/**
	 * Restituisce la lista di utenti ricavata dalla ricerca sulla username (o porzione di essa).
	 * @param text Il testo tramite il quale effettuare la ricerca sulla username.
	 * @return La lista di utenti ricavati.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<UserDetails> searchUsers(String text) throws ApsSystemException;
	
	/**
	 * Elimina un utente.
	 * @param user L'utente da eliminare dal db.
	 * @throws ApsSystemException in caso di errore.
	 */
	public void removeUser(UserDetails user) throws ApsSystemException;

	/**
	 * Elimina un utente.
	 * @param username La username dell'utente da eliminare.
	 * @throws ApsSystemException in caso di errore.
	 */
	public void removeUser(String username) throws ApsSystemException;

	/**
	 * Aggiorna un utente.
	 * @param user L'utente da aggiornare.
	 * @throws ApsSystemException in caso di errore.
	 */
	public void updateUser(UserDetails user) throws ApsSystemException;

	/**
	 * Aggiorna la data (a quella odierna) di ultimo accesso dell'utente specificato.
	 * @param user L'utente a cui aggiornare la data di ultimo accesso.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void updateLastAccess(UserDetails user) throws ApsSystemException;

	/**
	 * Effettua l'operazione di cambio password.
	 * @param username Lo username al quale cambiare la password.
	 * @param password La nuova password.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void changePassword(String username, String password) throws ApsSystemException;

	/**
	 * Aggiunge un utente.
	 * @param user L'utente da aggiungere.
	 * @throws ApsSystemException in caso di errore.
	 */
	public void addUser(UserDetails user) throws ApsSystemException;

	/**
	 * Restituisce un utente. Se la userName non 
	 * corrisponde ad un utente restituisce null.
	 * @param username Lo username dell'utente da restituire.
	 * @return L'utente cercato, null se non vi è nessun utente 
	 * corrispondente alla username immessa.
	 * @throws ApsSystemException in caso di errore.
	 */
	public UserDetails getUser(String username) throws ApsSystemException;

	/**
	 * Restituisce un utente. Se userName e password non
	 * corrispondono ad un utente, restituisce null.
	 * @param username Lo username dell'utente da restituire.
	 * @param password La password dell'utente da restituire.
	 * @return L'utente cercato, null se non vi è nessun utente 
	 * corrispondente alla username e password immessa.
	 * @throws ApsSystemException in caso di errore.
	 */
	public UserDetails getUser(String username, String password) throws ApsSystemException;

	/**
	 * Restituisce l'utente di default di sistema.
	 * L'utente di default rappresenta un utente "ospite" senza nessuna autorizzazione 
	 * di accesso ad elementi non "liberi" e senza nessuna autorizzazione ad eseguire 
	 * qualunque azione sugli elementi del sistema. 
	 * @return L'utente di default di sistema.
	 */
	public UserDetails getGuestUser();

}
