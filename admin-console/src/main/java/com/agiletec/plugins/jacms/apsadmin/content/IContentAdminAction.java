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