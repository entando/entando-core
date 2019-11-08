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
package org.entando.entando.aps.system.services.dataobjectsearchengine;

import java.io.File;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 *
 * @author W.Ambu
 */
public interface IIndexerDAO {

	/**
	 * Inizializzazione dell'indicizzatore.
	 *
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException In caso di errori.
	 */
	public void init(File dir) throws ApsSystemException;

	/**
	 * Aggiunge un dataobject nel db del motore di ricerca.
	 *
	 * @param entity Il dataobject da aggiungere.
	 * @throws ApsSystemException In caso di errori.
	 */
	public void add(IApsEntity entity) throws ApsSystemException;

	/**
	 * Cancella un documento indicizzato.
	 *
	 * @param name Il nome del campo Field da utilizzare per recupero del
	 * documento.
	 * @param value La chiave mediante il quale è stato indicizzato il
	 * documento.
	 * @throws ApsSystemException In caso di errori.
	 */
	public void delete(String name, String value) throws ApsSystemException;

	public void close();

	public void setLangManager(ILangManager langManager);

	public void setCategoryManager(ICategoryManager categoryManager);

	public static final String FIELD_PREFIX = "entity:";
	public static final String DATAOBJECT_ID_FIELD_NAME = FIELD_PREFIX + "id";
	public static final String DATAOBJECT_TYPE_FIELD_NAME = FIELD_PREFIX + "type";
	public static final String DATAOBJECT_GROUP_FIELD_NAME = FIELD_PREFIX + "group";
	public static final String DATAOBJECT_CATEGORY_FIELD_NAME = FIELD_PREFIX + "category";
	public static final String DATAOBJECT_CATEGORY_SEPARATOR = "/";

}
