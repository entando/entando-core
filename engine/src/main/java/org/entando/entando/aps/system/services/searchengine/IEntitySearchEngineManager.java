/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.searchengine;

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi detentori delle operazioni di indicizzazione 
 * di oggetti ricercabili tramite motore di ricerca.
 * @author E.Santoboni
 */
public interface IEntitySearchEngineManager extends ISearchEngineManager {
	
	/**
     * Aggiorna le indicizzazioni relative ad una entità.
     * @param entity L'entità di cui aggiornare le indicizzazioni.
     * @throws ApsSystemException
     */
    public void updateIndexedEntity(IApsEntity entity) throws ApsSystemException;
    
    /**
     * Cancella una entità in base all'identificativo.
     * @param entityId L'identificativo dell'entità.
     * @throws ApsSystemException
     */
    public void deleteIndexedEntity(String entityId) throws ApsSystemException;
    
    /**
     * Aggiunge un documento relativo ad una entità 
     * nel db del motore di ricerca.
     * @param entity Il contenuto da cui estrarre il documento.
     * @throws ApsSystemException
     */
    public void addEntityToIndex(IApsEntity entity) throws ApsSystemException;
    
    /**
     * Ricerca una lista di identificativi di entità in base 
     * alla chiave lingua corrente ed alla parola immessa.
     * @param langCode Il codice della lingua corrente.
     * @param word La parola in base al quale fare la ricerca. Nel caso venissero 
     * inserite stringhe di ricerca del tipo "Venice Amsterdam" viene considerato come 
     * se fosse "Venice OR Amsterdam".
     * @param allowedGroups I gruppi autorizzati alla visualizzazione.
     * @throws ApsSystemException
     */
	public List<String> searchEntityId(String langCode, 
			String word, Collection<String> allowedGroups) throws ApsSystemException;
	
}