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
package com.agiletec.plugins.jacms.aps.system.services.content.helper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Il bean detentore dei parametri di ricerca di liste di contenuti.
 * @author E.Santoboni
 */
public interface IContentListBean {
	
	/**
	 * Restituisce il nome identificativo della lista.
	 * @return Returns Il nome identificativo della lista.
	 */
	public String getListName();
	
	/**
	 * Restituisce il codice dei tipi di contenuto da cercare.
	 * @return Il codice dei tipi di contenuto da cercare.
	 */
	public String getContentType();
	
	/**
	 * Restituisce le categorie dei contenuti da cercare.
	 * @return La categorie dei contenuti da cercare.
	 */
	public String[] getCategories();
	
	/**
	 * Restituisce la lista di filtri definita nel bean.
	 * @return La lista di filtri definita nel bean.
	 */
	public EntitySearchFilter[] getFilters();
	
	/**
	 * Indica se nel recupero della lista deve essere utilizzata la cache di sistema.
	 * @return True se deve essere utilizzata la chache di sistema, false in caso contrario.
	 */
	public boolean isCacheable();
	
}