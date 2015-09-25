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
package com.agiletec.plugins.jacms.apsadmin.content.model;

import java.util.List;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;

/**
 * Interfaccia base per le classi action delegate 
 * alle operazioni di erogazione e ricerca modelli di contenuti.
 * @author E.Santoboni
 */
public interface IContentModelFinderAction {
	
	/**
	 * Restituisce la lista di modelli di contenuto.
	 * @return La lista di modelli di contenuto.
	 */
	public List<ContentModel> getContentModels();
	
}
