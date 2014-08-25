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

/**
 * Interfaccia base per i Data Access Object dei permessi (Permission).
 * @author M.Diana
 */
public interface IPermissionDAO {

	/**
	 * Carica la mappa (indicizzata in base al nome) dei permessi di autorizzazione presenti nel db.
	 * @return La mappa dei permessi di autorizzazione.
	 */
	public Map<String, Permission> loadPermissions();

	/**
	 * Aggiunge un permesso di autorizzazione al db.
	 * @param permission Oggetto Permission rappresentante il permesso da aggiungere.
	 */
	public void addPermission(Permission permission);

	/**
	 * Aggiorna un permesso nel db.
	 * @param permission Oggetto Permission rappresentante il permesso da aggionare.
	 */
	public void updatePermission(Permission permission);

	/**
	 * Cancella un permesso di autorizzazione dal db.
	 * @param permission Oggetto Permission rappresentante il permesso da eliminare.
	 */
	public void deletePermission(Permission permission);

	/**
	 * Cancella un permesso di autorizzazione dal db.
	 * @param permissionName Oggetto Permission rappresentante il permesso da eliminare.
	 */
	public void deletePermission(String permissionName);

}
