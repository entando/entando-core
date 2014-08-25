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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListBean;

/**
 * Il bean detentore dei parametri di ricerca di liste di contenuti.
 * @author E.Santoboni
 */
public interface IContentListTagBean extends IContentListBean {
	
	/**
	 * Setta il codice dei tipi di contenuto da cercare.
	 * @param contentType Il codice dei tipi di contenuto da cercare.
	 */
	public void setContentType(String contentType);

	/**
	 * Restituisce la categoria dei contenuto da cercare.
	 * @return La categoria dei contenuto da cercare.
	 * @deprecated use getCategories
	 */
	public String getCategory();
	
	/**
	 * Setta la categoria dei contenuto da cercare.
	 * @param category La categoria dei contenuto da cercare.
	 * @deprecated use addCategory
	 */
	public void setCategory(String category);
	
	public void addCategory(String category);
	
	/**
	 * Aggiunge un filtro in coda alla lista di filtri definita nel bean.
	 * @param filter Il filtro da aggiungere.
	 */
	public void addFilter(EntitySearchFilter filter);
	
	/**
	 * Aggiunge una opzione filtro utente in coda alla lista di filtri definita nel bean.
	 * @param filter L'opzione filtro utente da aggiungere.
	 */
	public void addUserFilterOption(UserFilterOptionBean filter);
	
	/**
	 * Restituisce la lista di opzioni filtro utente definita.
	 * @return La lista di opzioni filtro utente definita nel bean.
	 */
	public List<UserFilterOptionBean> getUserFilterOptions();
	
}