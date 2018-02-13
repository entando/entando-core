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
 * Interfaccia base per i Data Access Object degli oggetti ruolo (Role).
 *
 * @author E.Santoboni
 */
public interface IRoleDAO {

	/**
	 * Carica da db una mappa completa di tutti i ruoli. Nella mappa, la chiave
	 * è costituita dal nome del ruolo. Nei ruoli sono caricati tutti i permessi
	 * assegnati al ruolo.
	 *
	 * @return La mappa completa di tutti i ruoli.
	 */
	public Map<String, Role> loadRoles();

	/**
	 * Aggiunge un ruolo ad db.
	 *
	 * @param role Oggetto di tipo Role relativo al ruolo da aggiungere.
	 */
	public void addRole(Role role);

	/**
	 * Elimima un ruolo dal db.
	 *
	 * @param role Il ruolo (oggetto Role) da eliminare dal db.
	 */
	public void deleteRole(Role role);

	/**
	 * Aggiorna un ruolo nel db.
	 *
	 * @param role Il ruolo (oggetto Role) da aggiornare nel db.
	 */
	public void updateRole(Role role);

}
