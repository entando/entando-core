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
