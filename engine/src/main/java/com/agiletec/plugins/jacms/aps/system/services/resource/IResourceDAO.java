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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;


/**
 * Interfaccia base per i Data Access Object 
 * degli oggetti risorsa (AbstractResource).
 * @author E.Santoboni - W.Ambu
 */
public interface IResourceDAO {
	
	/**
	 * Carica una risorsa nel db. 
	 * @param resource La risorsa da caricare nel db.
	 */
	public void addResource(ResourceInterface resource);
	
	/**
	 * Aggiorna una risorsa nel database.
	 * @param resource La risorsa da aggiornare nel db.
	 */
	public void updateResource(ResourceInterface resource);
	
	/**
	 * Cancella una risorsa dal db.
	 * @param id L'identificativo della risorsa da cancellare.
	 */
	public void deleteResource(String id);
	
	/**
	 * Carica una lista di identificativi di risorse 
	 * in base al tipo, ad una parola chiave e dalla categoria della risorsa. 
	 * @param type Tipo di risorsa da cercare.
	 * @param text Testo immesso per il raffronto con la descrizione della risorsa. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per parola chiave. 
	 * @param categoryCode Il codice della categoria delle risorse. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per categoria.
	 * @param groupCodes I codici dei gruppi utenti consentiti tramite il quale 
	 * filtrare le risorse. Nel caso che la collezione di codici sia nulla o vuota, 
	 * non verrà eseguito la selezione per gruppi.
	 * @return La lista di identificativi di risorse.
	 */
	public List<String> searchResourcesId(String type, String text, String categoryCode, Collection<String> groupCodes);
	
	/**
	 * Carica una lista di identificativi di risorse 
	 * in base al tipo, ad una parola chiave e dalla categoria della risorsa. 
	 * @param type Tipo di risorsa da cercare.
	 * @param text Testo immesso per il raffronto con la descrizione della risorsa. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per parola chiave. 
	 * @param filename Testo immesso per il raffronto con il nome del file della risorsa. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per nome file. 
	 * @param categoryCode Il codice della categoria delle risorse. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per categoria.
	 * @param groupCodes I codici dei gruppi utenti consentiti tramite il quale 
	 * filtrare le risorse. Nel caso che la collezione di codici sia nulla o vuota, 
	 * non verrà eseguito la selezione per gruppi.
	 * @return La lista di identificativi di risorse.
	 */
	public List<String> searchResourcesId(String type, String text, String filename, String categoryCode, Collection<String> groupCodes);
	
    public List<String> searchResourcesId(FieldSearchFilter[] filters, String categoryCode, Collection<String> groupCodes);
    
	/**
	 * Carica un record di risorse in funzione dell'idRisorsa. Questo record è 
	 * necessario per l'estrazione della risorse in oggetto tipo AbstractResource 
	 * da parte del ResourceManager. 
	 * @param id L'identificativo della risorsa.
	 * @return Il record della risorsa.
	 */
	public ResourceRecordVO loadResourceVo(String id);
	
}
