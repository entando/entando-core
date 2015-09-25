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
	 * @return La classe dao delegata alle operazioni di indicizzazione.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public IIndexerDAO getIndexer() throws ApsSystemException;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di ricerca.
	 * @return La classe dao delegata alle operazioni di ricerca.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public ISearcherDAO getSearcher() throws ApsSystemException;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di indicizzazione.
	 * @param subDir La sottocartella (figlia della root a servizio del sistema) 
	 * utilizzata per le operazioni di indicizzazione dei documenti.
	 * @return La classe dao delegata alle operazioni di indicizzazione.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public IIndexerDAO getIndexer(String subDir) throws ApsSystemException;
	
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