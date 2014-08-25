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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe factory degli elementi ad uso del SearchEngine.
 * @author E.Santoboni
 */
public interface ISearchEngineDAOFactory {
	
	/**
	 * Inizializzazione della classe factory.
	 * @throws Exception In caso di errore.
	 */
	public void init() throws Exception;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di indicizzazione.
	 * @param newIndex Discrimina la costruzione.
	 * @return La classe dao delegata alle operazioni di indicizzazione.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public IIndexerDAO getIndexer(boolean newIndex) throws ApsSystemException;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di ricerca.
	 * @return La classe dao delegata alle operazioni di ricerca.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public ISearcherDAO getSearcher() throws ApsSystemException;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di indicizzazione.
	 * @param newIndex Discrimina la costruzione.
	 * @param subDir La sottocartella (figlia della root a servizio del sistema) 
	 * utilizzata per le operazioni di indicizzazione dei documenti.
	 * @return La classe dao delegata alle operazioni di indicizzazione.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public IIndexerDAO getIndexer(boolean newIndex, String subDir) throws ApsSystemException;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di ricerca.
	 * @param subDir La sottocartella (figlia della root a servizio del sistema) 
	 * utilizzata per le operazioni di ricerca dei documenti.
	 * @return La classe dao delegata alle operazioni di ricerca.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public ISearcherDAO getSearcher(String subDir) throws ApsSystemException;
	
	/**
	 * Aggiorna la sottocartella (figlia della root a servizio del sistema) 
	 * utilizzata per le operazioni di indicizzazione e ricerca dei documenti.
	 * @param newSubDirectory La nuova subdirectory.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public void updateSubDir(String newSubDirectory) throws ApsSystemException;
	
	/**
	 * Cancella la sottocartella (figlia della root a servizio del sistema) 
	 * utilizzata per le operazioni di indicizzazione e ricerca dei documenti.
	 * @param subDirectory La sottocartella 
	 */
	public void deleteSubDirectory(String subDirectory);
	
}