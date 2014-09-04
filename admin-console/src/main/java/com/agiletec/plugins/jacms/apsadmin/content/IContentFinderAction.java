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
