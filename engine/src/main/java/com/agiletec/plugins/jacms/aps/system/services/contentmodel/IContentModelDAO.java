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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.Map;

/**
 * Interfaccia base per i Data Access Object 
 * degli oggetti modello contenuto (ContentModel).
 * @author M.Diana
 */
public interface IContentModelDAO {
	
	/**
	 * Carica e restituisce la mappa dei modelli di contenuto.
	 * @return La mappa dei modelli di contenuto.
	 */
	public Map<Long, ContentModel> loadContentModels();
	
	/**
	 * Aggiunge un modello di contenuto nel db.
	 * @param model Il modello di contenuto da aggiungere.
	 */
	public void addContentModel(ContentModel model);
	
	/**
	 * Rimuove un modello di contenuto dal db.
	 * @param model Il modello di contenuto da rimuovere.
	 */
	public void deleteContentModel(ContentModel model);
	
	/**
	 * Aggiorna un modello di contenuto nel db.
	 * @param model Il modello di contenuto da aggiornare.
	 */
	public void updateContentModel(ContentModel model);
	
}
