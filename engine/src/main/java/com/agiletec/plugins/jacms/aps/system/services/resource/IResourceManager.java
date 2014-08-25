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
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Interfaccia base per i servizi gestori tipi di risorse (immagini, audio, video, etc..).
 * @author W.Ambu - E.Santoboni
 */
public interface IResourceManager {
	
	/**
     * Crea una nuova istanza di un tipo di risorsa del tipo richiesto. Il nuovo
     * tipo di risorsa è istanziato mediante clonazione del prototipo corrispondente.
     * @param typeCode Il codice del tipo di risorsa richiesto, come definito in configurazione.
     * @return Il tipo di risorsa istanziato (vuoto).
     */
    public ResourceInterface createResourceType(String typeCode);
    
    /**
     * Restituisce la lista delle chiavi dei tipi risorsa presenti nel sistema.
     * @return La lista delle chiavi dei tipi risorsa esistenti.
     */
    public List<String> getResourceTypeCodes();
    
    /**
     * Salva una risorsa nel db con incluse nel filesystem, indipendentemente dal tipo.
     * @param bean L'oggetto detentore dei dati della risorsa da inserire.
     * @return la risorsa aggiunta.
     * @throws ApsSystemException in caso di errore.
     */
    public ResourceInterface addResource(ResourceDataBean bean) throws ApsSystemException;
    
    /**
     * Salva una risorsa nel db, indipendentemente dal tipo.
     * @param resource La risorsa da salvare.
     * @throws ApsSystemException in caso di errore.
     */
    public void addResource(ResourceInterface resource) throws ApsSystemException;
    
    /**
	 * Aggiorna una risorsa nel db.
	 * @param resource La risorsa da modificare.
	 * @throws ApsSystemException in caso di errore.
	 */
    public void updateResource(ResourceInterface resource) throws ApsSystemException;
	
    /**
	 * Aggiorna una risorsa nel db.
	 * @param bean L'oggetto detentore dei dati della risorsa da modificare.
	 * @throws ApsSystemException in caso di errore.
	 */
    public void updateResource(ResourceDataBean bean) throws ApsSystemException;
	
	/**
     * Carica una lista di identificativi di risorse 
	 * in base al tipo, ad una parola chiave e dalla categoria della risorsa. 
	 * @param type Tipo di risorsa da cercare.
	 * @param text Testo immesso per il raffronto con la descrizione della risorsa. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per parola chiave. 
	 * @param categoryCode Il codice della categoria delle risorse. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per categoria.
	 * @param groupCodes I codici dei gruppi utenti consentiti tramite il quale 
	 * filtrare le risorse.
     * @return La lista di identificativi di risorse.
     * @throws ApsSystemException In caso di errore.
     */
	public List<String> searchResourcesId(String type, String text, 
    		String categoryCode, Collection<String> groupCodes) throws ApsSystemException;
    
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
	 * filtrare le risorse.
     * @return La lista di identificativi di risorse.
     * @throws ApsSystemException In caso di errore.
     */
	public List<String> searchResourcesId(String type, String text, 
			String filename, String categoryCode, Collection<String> groupCodes) throws ApsSystemException;
    
    public List<String> searchResourcesId(FieldSearchFilter[] filters, 
			String categoryCode, Collection<String> groupCodes) throws ApsSystemException;
    
    /**
     * Restituisce la risorsa con l'id specificato.
     * @param id L'identificativo della risorsa da caricare.
     * @return La risorsa cercata. null se non vi è nessuna risorsa con l'identificativo immesso.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public ResourceInterface loadResource(String id) throws ApsSystemException;
    
    /**
     * Cancella una risorsa dal db ed i file di ogni istanza dal filesystem.
     * @param resource La risorsa da cancellare.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public void deleteResource(ResourceInterface resource) throws ApsSystemException;
    
    /**
     * Reload the master file name to all Resources.
     * This method is used to improve the porting from jAPS 2.0.x to version 2.2.x
     * @throws ApsSystemException In case of error
     */
    public void refreshMasterFileNames() throws ApsSystemException;
    
    /**
     * Refresh all the instance (not the "main" instance) of resources of the given type 
     * @param resourceTypeCode The type of the resources to refresh
     * @throws ApsSystemException In case of error.
     */
    public void refreshResourcesInstances(String resourceTypeCode) throws ApsSystemException;
    
    /**
     * Return the service status id.
     * @return The service status id.
     */
    public int getStatus();
	
	public static final String RESOURCE_ID_FILTER_KEY = "resid";
	
	public static final String RESOURCE_TYPE_FILTER_KEY = "restype";
	
	public static final String RESOURCE_DESCR_FILTER_KEY = "descr";
	
	public static final String RESOURCE_MAIN_GROUP_FILTER_KEY = "maingroup";
	
	public static final String RESOURCE_FILENAME_FILTER_KEY = "masterfilename";
	
	public static final String RESOURCE_CREATION_DATE_FILTER_KEY = "creationdate";
	
	public static final String RESOURCE_MODIFY_DATE_FILTER_KEY = "lastmodified";
	
    public static final int STATUS_READY = 0;
	public static final int STATUS_RELOADING_RESOURCE_MAIN_FILENAME_IN_PROGRESS = 1;
	public static final int STATUS_RELOADING_RESOURCE_INSTANCES_IN_PROGRESS = 2;
    
}