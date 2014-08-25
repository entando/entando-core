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