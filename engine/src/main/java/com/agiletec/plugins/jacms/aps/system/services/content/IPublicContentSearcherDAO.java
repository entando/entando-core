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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Interfaccia base per i Data Access Object 
 * riservato al caricamento/ricerca lista di contenuti pubblici.
 * @author E.Santoboni
 */
public interface IPublicContentSearcherDAO extends IEntitySearcherDAO {
	
	/**
	 * Carica una lista di identificativi di contenuti publici in base ai parametri immessi.
	 * @param categories Le categorie dei contenuti da cercare.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista. 
	 * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno 
	 * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente 
	 * il codice del gruppo degli amministratori, non sarà applicato alcun il filtro sul gruppo.
	 * @return La lista degli id dei contenuti cercati.
	 */
	public List<String> loadPublicContentsId(String[] categories, EntitySearchFilter[] filters, 
			Collection<String> userGroupCodes);
	
	public List<String> loadPublicContentsId(String[] categories, 
			boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes);
	
	/**
	 * Carica una lista di identificativi di contenuti publici in base ai parametri immessi.
	 * @param contentType Il codice dei tipi di contenuto da cercare.
	 * @param categories Le categorie dei contenuti da cercare.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista. 
	 * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno 
	 * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente 
	 * il codice del gruppo degli amministratori, non sarà applicato alcun il filtro sul gruppo.
	 * @return La lista degli id dei contenuti cercati.
	 */
	public List<String> loadPublicContentsId(String contentType, String[] categories, EntitySearchFilter[] filters, 
			Collection<String> userGroupCodes);
	
	public List<String> loadPublicContentsId(String contentType, 
			String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes);
	
}
