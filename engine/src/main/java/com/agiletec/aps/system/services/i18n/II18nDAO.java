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
package com.agiletec.aps.system.services.i18n;

import java.util.Map;

import com.agiletec.aps.util.ApsProperties;

/**
 * Interfaccia base per Data Access Object degli oggetti label (I18n).
 * @author E.Santoboni
 */
public interface II18nDAO {

	/**
	 * Carica la mappa che contiene tutti i gruppi delle label in tutte le lingue.
	 * @return La mappa contenente tutte le label.
	 */
	public Map<String, ApsProperties> loadLabelGroups();

	/**
	 * Aggiunge un gruppo di label.
	 * @param key La chiave del gruppo da aggiungere.
	 * @param labels La mappa delle labels indicizzate per codice lingua.
	 */
	public void addLabelGroup(String key, ApsProperties labels);

	/**
	 * Aggiorna un gruppo di label.
	 * @param key La chiave del gruppo da aggiornare.
	 * @param labels La mappa delle labels indicizzate per codice lingua.
	 */
	public void updateLabelGroup(String key, ApsProperties labels);

	/**
	 * Cancella un gruppo di label.
	 * @param key La chiave del gruppo da cancellare.
	 */
	public void deleteLabelGroup(String key);

}