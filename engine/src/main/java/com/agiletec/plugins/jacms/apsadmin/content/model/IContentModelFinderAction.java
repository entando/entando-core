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
