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
package com.agiletec.plugins.jacms.aps.system.services.content.helper;

import java.util.List;

import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public interface IContentListHelper {
	
	/**
	 * Restituisce la lista di identificativi di contenuto in base ai parametri di ricerca.
	 * @param bean Il contenitore delle informazioni base sulla interrogazione da eseguire.
	 * @return La lista di identificativi di contenuto in base ai parametri di ricerca.
	 * @throws Throwable In caso di errore.
	 */
	public List<String> getContentsId(IContentListBean bean, UserDetails user) throws Throwable;
	
	public EntitySearchFilter[] getFilters(String contentType, String filtersShowletParam, String langCode);
	
	/**
	 * @deprecated From Entando 2.0 version 2.4.1. Use getFilter(String contentType, IEntityFilterBean, String) method
	 */
	public EntitySearchFilter getFilter(String contentType, IContentListFilterBean bean, String langCode);
	
	public EntitySearchFilter getFilter(String contentType, IEntityFilterBean bean, String langCode);

	public String getFilterParam(EntitySearchFilter[] filters);
	
	public static final String CATEGORIES_SEPARATOR = ",";
	
}