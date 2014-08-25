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

/**
 * Interfaccia base per Data Access Object degli oggetti User (User). 
 * @version 1.0
 * @author M.Diana - E.Santoboni
 */
public interface IUserDAO {
	
	public List<String> loadUsernames();
	
	public List<String> searchUsernames(String text);
	
	/**
	 * Carica e restituisce la lista completa di utenti.
	 * @return La lista completa di utenti.
	 */
	public List<UserDetails> loadUsers();
	
	/**
	 * Restituisce la lista di utenti ricavata dalla ricerca sulla username (o porzione di essa).
	 * @param text Il testo tramite il quale effettuare la ricerca sulla username.
	 * @return La lista di utenti ricavati.
	 */
	public List<UserDetails> searchUsers(String text);
	
	/**
	 * Carica un'utente corrispondente alla userName e password immessa. 
	 * null se non vi è nessun utente corrispondente.
	 * @param username Nome utente dell'utente cercato
	 * @param password password dell'utente cercato
	 * @return L'oggetto utente corrispondente ai parametri richiesti, oppure 
	 * null se non vi è nessun utente corrispondente.
	 */
	public UserDetails loadUser(String username, String password);
	
	/**
	 * Carica un'utente corrispondente alla userName immessa. null
	 * se non vi è nessun utente corrispondente.
	 * @param username Nome utente dell'utente cercato.
	 * @return L'oggetto utente corrispondente ai parametri richiesti, 
	 * oppure null se non vi è nessun utente corrispondente.
	 */
	public UserDetails loadUser(String username);
	
	/**
	 * Cancella l'utente.
	 * @param user L'oggetto di tipo User relativo all'utente da cancellare.
	 */
	public void deleteUser(UserDetails user);
	
	/**
	 * Cancella l'utente corrispondente alla userName immessa.
	 * @param username Il nome identificatore dell'utente.
	 */
	public void deleteUser(String username);
	
	/**
	 * Aggiunge un nuovo utente.
	 * @param user Oggetto di tipo User relativo all'utente da aggiungere.
	 */
	public void addUser(UserDetails user);
	
	/**
	 * Aggiorna un utente già presente con nuovi valori 
	 * (tranne la username che è fissa).
	 * @param user Oggetto di tipo User relativo all'utente da aggiornare.
	 */
	public void updateUser(UserDetails user);
	
	/**
	 * Carica gli utenti membri di un gruppo.
	 * @param groupName Il nome del grupo tramite il quale cercare gli utenti.
	 * @return La lista degli utenti (oggetti User) membri del gruppo specificato.
	 * @deprecated USE loadUsernamesForGroup and load single users from current UserManager.
	 */
	public List<UserDetails> loadUsersForGroup(String groupName);
	
	/**
	 * Carica la lista di usernames correlati con il gruppo specificato.
	 * @param groupName Il nome del gruppo tramite il quale cercare i nomi utenti.
	 * @return La lista di usernames correlati con il gruppo specificato.
	 */
	public List<String> loadUsernamesForGroup(String groupName);
	
	/**
	 * Effettua l'aggiornamento della password di un'utente.
	 * @param username La username dell'utente a cui aggiornare la password.
	 * @param password La nuova password.
	 */
	public void changePassword(String username, String password);
	
	public void updateLastAccess(String username);
	
}