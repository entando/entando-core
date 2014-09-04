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
package com.agiletec.plugins.jacms.apsadmin.content;

/**
 * Interfaccia base per le classi Action delegate alla esecuzione delle operazioni 
 * di amministrazione dei contenuti.
 * @author E.Santoboni
 */
public interface IContentAdminAction {
	
	/**
	 * Effettua la richiesta di ricaricamento degli indici a servizio 
	 * del motore di ricerca interno.
	 * @return Il codice del risultato dell'azione.
	 */
	public String reloadContentsIndex();
	
	/**
	 * Effettua la richeista di ricaricamento delle referenze dei contenuti.
	 * Le referenze sono rappresentate sia dalle repliche dei valori degli Attributi 
	 * di Contenuto dichiarati ricercabili in apposita tabella (elementi a servizio degli erogatori di contenuti in lista) e 
	 * che delle referenze tra Contenuti e Pagine, Risorse, Gruppi e Contenuti stessi in apposita tabella (elementi a 
	 * servizio di controlli di autorizzazione e validazione).
	 * @return Il codice del risultato dell'azione.
	 */
	public String reloadContentsReference();
	
}