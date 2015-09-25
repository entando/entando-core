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