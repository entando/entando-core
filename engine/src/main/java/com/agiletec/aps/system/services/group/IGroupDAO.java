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
package com.agiletec.aps.system.services.group;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * Interfaccia base per i Data Access Object degli oggetti Group.
 *
 * @author E.Santoboni
 */
public interface IGroupDAO {

	/**
	 * Carica la mappa dei gruppi presenti nel sistema indicizzandola in base al
	 * nome del gruppo.
	 *
	 * @return La mappa dei gruppi presenti nel sistema.
	 */
	public Map<String, Group> loadGroups();

	/**
	 * Aggiunge un gruppo nel db.
	 *
	 * @param group Il gruppo da aggiungere.
	 */
	public void addGroup(Group group);

	/**
	 * Aggiorna un gruppo nel db.
	 *
	 * @param group Il gruppo da aggiornare.
	 */
	public void updateGroup(Group group);

	/**
	 * Rimuove un gruppo dal db.
	 *
	 * @param group Il gruppo da rimuovere.
	 */
	public void deleteGroup(Group group);

	/**
	 * Rimuove un gruppo dal sistema.
	 *
	 * @param groupName Il nome del gruppo da rimuovere.
	 */
	public void deleteGroup(String groupName);

    public List<String> searchGroups(FieldSearchFilter[] filters);

    public int countGroups(FieldSearchFilter[] filters);

}
