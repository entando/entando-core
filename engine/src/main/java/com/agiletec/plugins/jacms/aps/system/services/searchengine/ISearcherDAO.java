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

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i Data Access Object dediti 
 * alle operazioni di ricerca ad uso del motore di ricerca interno.
 * @author W.Ambu
 */
public interface ISearcherDAO {
	
	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException In caso di errore
	 */
	public void init(File dir) throws ApsSystemException;
	
	/**
     * Ricerca una lista di identificativi di contenuto in base 
     * al codice della lingua corrente ed alla parola immessa.
     * @param langCode Il codice della lingua corrente.
     * @param word La parola in base al quale fare la ricerca. Nel caso 
     * venissero inserite stringhe di ricerca del tipo "Venice Amsterdam" 
     * viene considerato come se fosse "Venice OR Amsterdam".
     * @param allowedGroups I gruppi autorizzati alla visualizzazione. Nel caso 
     * che la collezione sia nulla o vuota, la ricerca sarà effettuata su contenuti 
     * referenziati con il gruppo "Ad accesso libero". Nel caso che nella collezione 
     * sia presente il gruppo degli "Amministratori", la ricerca produrrà un'insieme 
     * di identificativi di contenuto non filtrati per gruppo.
     * @return La lista di identificativi contenuto.
     * @throws ApsSystemException
     */
    public List<String> searchContentsId(String langCode, 
    		String word, Collection<String> allowedGroups) throws ApsSystemException;
	
    public void close();
	
}
