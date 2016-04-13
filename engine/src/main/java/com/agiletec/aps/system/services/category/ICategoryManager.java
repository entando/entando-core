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
package com.agiletec.aps.system.services.category;

import java.util.List;

import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i Manager delegati alla gestione delle categorie.
 * @author E.Santoboni
 */
public interface ICategoryManager extends ITreeNodeManager {
	
	/**
     * Aggiunge una categoria al db.
     * @param category La categoria da aggiungere.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
    public void addCategory(Category category) throws ApsSystemException;
    
    /**
     * Cancella una categoria.
     * @param code Il codice della categoria da eliminare.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public void deleteCategory(String code) throws ApsSystemException;
    
    /**
     * Aggiorna una categoria nel db.
     * @param category La categoria da modificare.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
    public void updateCategory(Category category) throws ApsSystemException;
	
    /**
     * Restituisce la radice dell'albero delle categorie
     * @return la categoria radice
     */
	@Override
    public Category getRoot();
	
    /**
	 * Restituisce la categoria corrispondente al codice immesso.
	 * @param categoryCode Il codice della categoria da restituire.
	 * @return La categoria richiesta.
	 */
	public Category getCategory(String categoryCode);
	
	/**
	 * Restituisce una lista piatta delle categorie disponibili, 
	 * ordinate secondo la gerarchia dell'albero delle categorie.
	 * La categoria root non viene inclusa nella lista.
	 * @return La lista piatta delle categorie disponibili.
	 */
	public List<Category> getCategoriesList();
	
	public List<Category> searchCategories(String categoryCodeToken) throws ApsSystemException;
    
}
