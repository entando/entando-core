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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.io.File;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 * @author W.Ambu
 */
public interface IIndexerDAO {
	
	/**
	 * Inizializzazione dell'indicizzatore.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @param taxoDir La cartella locale delle tassonomie
	 * @throws ApsSystemException In caso di errori.
	 */
	public void init(File dir, File taxoDir) throws ApsSystemException;
	
	/**
	 * Aggiunge un contenuto nel db del motore di ricerca.
     * @param entity Il contenuto da aggiungere.
	 * @throws ApsSystemException In caso di errori.
	 */
	public void add(IApsEntity entity) throws ApsSystemException;
	
	/**
     * Cancella un documento in base alla chiave mediante il quale è stato indicizzato.
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale 
     * è stato indicizzato il documento.
     * @throws ApsSystemException In caso di errori.
     */
    public void delete(String name, String value) throws ApsSystemException;
    
    public void close();
	
	public void setLangManager(ILangManager langManager);
    
    public static final String CONTENT_ID_FIELD_NAME = "id";
    public static final String CONTENT_GROUP_FIELD_NAME = "group";
    
}