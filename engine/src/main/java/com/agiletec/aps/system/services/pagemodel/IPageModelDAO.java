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
package com.agiletec.aps.system.services.pagemodel;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * Interfaccia base per Data Access Object deii modelli di pagina (PageModel)
 * @author E.Santoboni
 */
public interface IPageModelDAO {
	
	/**
	 * Carica e restituisce la mappa dei modelli di pagina.
	 * @return la mappa dei modelli.
	 */
	public Map<String, PageModel> loadModels();
	
	public void addModel(PageModel model);
	
	public void updateModel(PageModel model);
	
	public void deleteModel(String code);

    public int count(FieldSearchFilter[] filters);

    public List<String> search(FieldSearchFilter[] filters);
	
}
