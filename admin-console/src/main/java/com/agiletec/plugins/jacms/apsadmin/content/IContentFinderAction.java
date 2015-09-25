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

import java.util.List;

/**
 * Interfaccia base per le action delegate alla ricerca contenuti.
 * @author E.Santoboni
 */
public interface IContentFinderAction {
	
	/**
	 * Restituisce la lista identificativi di contenuti che deve essere erogata dall'interfaccia di 
	 * visualizzazione dei contenuti. La lista deve essere filtrata secondo i parametri di ricerca impostati.
	 * @return La lista di contenuti che deve essere erogata dall'interfaccia di 
	 * visualizzazione dei contenuti.
	 */
	public List<String> getContents();
	
	/**
	 * Esegue la publicazione di un singolo contenuto direttamente 
	 * dall'interfaccia di visualizzazione dei contenuti in lista.
	 * @return Il codice del risultato dell'azione.
	 */
	public String insertOnLine();
	
	/**
	 * Esegue la rimozione dall'area pubblica di un singolo contenuto direttamente 
	 * dall'interfaccia di visualizzazione dei contenuti in lista.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeOnLine();
	
	/**
	 * Esegue le operazioni di richiesta di cancellazione contenuto o gruppo contenuti.
	 * @return Il codice del risultato.
	 */
	public String trash();
	
	/**
	 * Esegue l'operazione di cancellazione contenuto o gruppo contenuti.
	 * @return Il codice del risultato.
	 */
	public String delete();
	
}
