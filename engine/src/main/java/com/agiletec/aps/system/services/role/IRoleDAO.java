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
package com.agiletec.aps.system.services.role;

import java.util.Map;

import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityDAO;

/**
 * Interfaccia base per i Data Access Object degli oggetti ruolo (Role).
 * @author E.Santoboni
 */
public interface IRoleDAO extends IApsAuthorityDAO {

	/**
	 * Carica da db una mappa completa di tutti i ruoli. Nella mappa, la chiave 
	 * è costituita dal nome del ruolo. Nei ruoli sono caricati tutti i permessi 
	 * assegnati al ruolo. 
	 * @return La mappa completa di tutti i ruoli.
	 */
	public Map<String, Role> loadRoles();

	/**
	 * Aggiunge un ruolo ad db.
	 * @param role Oggetto di tipo Role relativo al ruolo da aggiungere.
	 */
	public void addRole(Role role);

	/**
	 * Elimima un ruolo dal db.
	 * @param role Il ruolo (oggetto Role) da eliminare dal db.
	 */
	public void deleteRole(Role role);

	/**
	 * Aggiorna un ruolo nel db.
	 * @param role Il ruolo (oggetto Role) da aggiornare nel db.
	 */
	public void updateRole(Role role);

	/**
	 * Restituisce il numero di utenti che utilizzano il ruolo immesso.
	 * @param role Il ruolo di cui trovate il numero di utenti che lo utilizzano.
	 * @return Il numero di utenti che utilizzano quel ruolo.
	 */
	public int getRoleUses(Role role);

}
