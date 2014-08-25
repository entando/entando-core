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
package com.agiletec.aps.system.services.group;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi gestori dei gruppi.
 * @author E.Santoboni
 */
public interface IGroupManager {
	
	/**
	 * Aggiunge un gruppo nel sistema.
	 * @param group Il gruppo da aggiungere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	public void addGroup(Group group) throws ApsSystemException;
	
	/**
	 * Rimuove un gruppo dal sistema.
	 * @param group Il gruppo da rimuovere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	public void removeGroup(Group group) throws ApsSystemException;
	
	/**
	 * Aggiorna un gruppo di sistema.
	 * @param group Il gruppo da aggiornare.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	public void updateGroup(Group group) throws ApsSystemException;
	
	/**
	 * Restituisce la lista ordinata dei gruppi presenti nel sistema.
	 * @return La lista dei gruppi presenti nel sistema.
	 */
	public List<Group> getGroups();
	
	/**
	 * Restituisce la mappa dei gruppi presenti nel sistema. 
	 * La mappa è indicizzata in base al nome del gruppo.
	 * @return La mappa dei gruppi presenti nel sistema.
	 */
	public Map<String, Group> getGroupsMap();
	
	/**
	 * Restituisce un gruppo in base al nome.
	 * @param groupName Il nome del gruppo.
	 * @return Il gruppo cercato.
	 */
	public Group getGroup(String groupName);
	
}
