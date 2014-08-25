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
package com.agiletec.aps.system.services.category;

import java.util.List;

import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * Interfaccia di base per le classi DAO di gestione Categorie.
 * @author E.Santoboni
 */
public interface ICategoryDAO {
	
	/**
	 * Carica la lista delle categorie inserite nel sistema.
	 * @param langManager Il manager delle lingue.
	 * @return La lista delle categorie inserite nel sistema.
	 */
	public List<Category> loadCategories(ILangManager langManager);
	
	/**
     * Cancella la categoria corrispondente al codice immesso.
     * @param code Il codice relativo alla categoria da cancellare.
     */
    public void deleteCategory(String code);
    
    /**
     * Inserisce una nuova Categoria.
     * @param category La nuova Categoria da inserire.
     */
    public void addCategory(Category category);
    
    /**
     * Aggiorna una categoria sul db.
     * @param category La categoria da aggiornare.
     */
    public void updateCategory(Category category);
	
}
