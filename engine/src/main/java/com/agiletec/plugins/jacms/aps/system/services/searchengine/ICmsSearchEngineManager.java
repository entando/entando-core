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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import org.entando.entando.aps.system.services.searchengine.IEntitySearchEngineManager;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi detentori delle operazioni di indicizzazione 
 * di oggetti ricercabili tramite motore di ricerca.
 * @author W.Ambu - E.Santoboni
 */
public interface ICmsSearchEngineManager extends IEntitySearchEngineManager {
	
	/**
     * Ricarica in blocco le indicizzazioni dei contenuti 
	 * necessari per le diverse ricerche sui contenuti.
	 * @return Il thread lanciato in esecuzione.
	 * @throws ApsSystemException in caso di errore in inizializzazione processo.
     */
    public Thread startReloadContentsReferences() throws ApsSystemException;
    
    /**
     * Return the service status id.
     * @return The service status id.
     * @deprecated From jAPS 2.0 version 2.0.9. Use getStatus() method
     */
    public int getState();
    
    /**
     * Return the service status id.
     * @return The service status id.
     */
    public int getStatus();
    
    /**
     * Restituisce le informazioni sull'ultimo ricaricamento della configurazione.
     * @return Le informazioni sull'ultimo ricaricamento della configurazione.
     */
    public LastReloadInfo getLastReloadInfo();
	
    public static final int STATUS_READY = 0;
	public static final int STATUS_RELOADING_INDEXES_IN_PROGRESS = 1;
	public static final int STATUS_NEED_TO_RELOAD_INDEXES = 2;
	
    /**
	 * @deprecated From jAPS 2.0 version 2.0.9. Use STATUS_READY
	 */
    public static final int ID_STATE_READY = STATUS_READY;
    
    /**
	 * @deprecated From jAPS 2.0 version 2.0.9. Use STATUS_RELOADING_INDEXES_IN_PROGRESS
	 */
    public static final int ID_RELOAD_INDEX_IN_PROGRESS = STATUS_RELOADING_INDEXES_IN_PROGRESS;
    
}