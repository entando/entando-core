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
package org.entando.entando.aps.system.services.dataobject.helper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Il bean detentore dei parametri di ricerca di liste di DataObject.
 *
 * @author E.Santoboni
 */
public interface IContentListBean {

	/**
	 * Restituisce il nome identificativo della lista.
	 *
	 * @return Returns Il nome identificativo della lista.
	 */
	public String getListName();

	/**
	 * Restituisce il codice dei tipi di DataObject da cercare.
	 *
	 * @return Il codice dei tipi di DataObject da cercare.
	 */
	public String getContentType();

	/**
	 * Restituisce le categorie dei DataObject da cercare.
	 *
	 * @return La categorie dei DataObject da cercare.
	 */
	public String[] getCategories();

	/**
	 * Restituisce la lista di filtri definita nel bean.
	 *
	 * @return La lista di filtri definita nel bean.
	 */
	public EntitySearchFilter[] getFilters();

	/**
	 * Indica se nel recupero della lista deve essere utilizzata la cache di
	 * sistema.
	 *
	 * @return True se deve essere utilizzata la cache di sistema, false in caso
	 * contrario.
	 */
	public boolean isCacheable();

}
