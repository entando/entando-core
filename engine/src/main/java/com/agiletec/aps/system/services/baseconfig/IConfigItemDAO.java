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
package com.agiletec.aps.system.services.baseconfig;

import java.util.Map;

/**
 * Interfaccia base per i Data Access Object delegate 
 * alla gestione delle le voci di configurazione. 
 * @author W.Ambu - E.Santoboni
 */
public interface IConfigItemDAO {
	
	/**
	 * Carica e restituisce un Map con tutte le voci di
	 * configurazione di una versione di configurazione.
	 * @param version La versione di configurazione.
	 * @return Il Map con le voci di configurazione
	 */
	public Map<String, String> loadVersionItems(String version);
	
	/**
	 * Carica e restituisce una voce di configurazione
	 * di una versione di configurazione. Questo metodo NON deve essere utilizzato
	 * normalmente, ma solo nelle fasi di inizializzazione del sistema, quando
	 * il SysContext non è ancora disponibile.
	 * @param version La versione di configurazione
	 * @param itemName Il nome della voce di configurazione.
	 * @return La voce di configurazione richiesta
	 */
	public String loadVersionItem(String version, String itemName);
	
	/**
	 * Aggiorna un'item di configurazione nel db.
	 * @param itemName Il nome dell'item da aggiornare.
	 * @param config La nuova configurazione.
	 * @param version La versione da aggiornare.
	 */
	public void updateConfigItem(String itemName, String config, String version);
	
}
