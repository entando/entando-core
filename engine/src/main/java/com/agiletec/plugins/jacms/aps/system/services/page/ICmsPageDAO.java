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
package com.agiletec.plugins.jacms.aps.system.services.page;

import java.util.List;

/**
 * Estensione della interfaccia per i Data Access Object degli oggetti pagina (Page).
 * @author E.Santoboni
 */
public interface ICmsPageDAO {
	
    /**
	 * Restituisce la lista di pagine che referenziano il contenuto specificato.
	 * @param contentId L'identificativo di contenuto tramite il quale ricavare le pagine referenziate.
	 * @return La lista di pagine (codici) che referenziano il contenuto specificato.
	 */
	public List<String> getContentUtilizers(String contentId);
    
}