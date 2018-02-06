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
package org.entando.entando.aps.system.services.dataobjectsearchengine;

import org.entando.entando.aps.system.services.searchengine.IEntitySearchEngineManager;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi detentori delle operazioni di indicizzazione
 * di oggetti ricercabili tramite motore di ricerca.
 *
 * @author W.Ambu - E.Santoboni
 */
public interface IDataObjectSearchEngineManager extends IEntitySearchEngineManager {

	/**
	 * Ricarica in blocco le indicizzazioni dei DataObject necessari per le
	 * diverse ricerche sui DataObject.
	 *
	 * @return Il thread lanciato in esecuzione.
	 * @throws ApsSystemException in caso di errore in inizializzazione
	 * processo.
	 */
	public Thread startReloadDataObjectsReferences() throws ApsSystemException;

	public Thread startReloadDataObjectsReferences(String subDirectory) throws ApsSystemException;

	/**
	 * Return the service status id.
	 *
	 * @return The service status id.
	 */
	public int getStatus();

	/**
	 * Restituisce le informazioni sull'ultimo ricaricamento della
	 * configurazione.
	 *
	 * @return Le informazioni sull'ultimo ricaricamento della configurazione.
	 */
	public LastReloadInfo getLastReloadInfo();

	public static final int STATUS_READY = 0;
	public static final int STATUS_RELOADING_INDEXES_IN_PROGRESS = 1;
	public static final int STATUS_NEED_TO_RELOAD_INDEXES = 2;

}
