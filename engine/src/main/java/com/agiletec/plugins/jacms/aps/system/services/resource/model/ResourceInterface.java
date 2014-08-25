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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;

/**
 * Interfaccia per gli oggetti risorsa.
 * @author W.Ambu - E.Santoboni
 */
public interface ResourceInterface {
	
	/**
     * Specifica se la risorsa è composta da più istanze.
     * @return true se la risorsa è composta da più istanze, 
     * false se è composta da una sola istanza. 
     */
    public boolean isMultiInstance();
    
    /**
	 * Restituisce l'identificativo della risorsa.
     * @return L'identificativo della risorsa.
     */
    public String getId();
    
    /**
     * Setta l'identificativo della risorsa.
     * @param id L'identificativo della risorsa.
     */
    public void setId(String id);
    
    /**
     * Restituisce il codice del tipo della risorsa.
     * @return Il codice del tipo della risorsa.
     */
    public String getType();
    
    /**
     * Setta il codice del tipo della risorsa.
     * @param typeCode Il codice del tipo della risorsa.
     */
    public void setType(String typeCode);
    
    /**
     * Restituisce la descrizione della risorsa.
     * @return La descrizione della risorsa.
     */
    public String getDescr();
    
    /**
     * Setta la descrizione della risorsa.
     * @param descr La descrizione della risorsa.
     */
    public void setDescr(String descr);
    
    /**
     * Restituisce la stringa identificante 
     * il gruppo principale di cui la risorsa è membro.
     * @return Il gruppo principale di cui la risorsa è membro.
     */
    public String getMainGroup();
    
    /**
	 * Setta la stringa identificante 
     * il gruppo principale di cui il contenuto è membro.
	 * @param mainGroup Il gruppo principale di cui il contenuto è membro.
	 */
    public void setMainGroup(String mainGroup);
    
    /**
     * Return the name of the file master.
     * @return The name of the master file.
     */
    public String getMasterFileName();
    
    /**
     * Set the name of the file master.
     * @param mainFileName The name of the master file.
     */
	public void setMasterFileName(String mainFileName);
	
	public Date getCreationDate();
	
	public void setCreationDate(Date creationDate);
	
	public Date getLastModified();
	
	public void setLastModified(Date lastModified);
	
	@Deprecated
	public File getFile();
    
    public InputStream getResourceStream();
	
	public InputStream getResourceStream(ResourceInstance instance);
    
    public InputStream getResourceStream(int size, String langCode);
    
    /**
     * Restituisce la cartella (a partire dalla cartella delle risorse) 
     * dove è posizionata la risorsa.
     * @return La cartella dove è posizionata la risorsa. 
     */
    public String getFolder();
    
    /**
     * Setta la cartella (a partire dalla cartella delle risorse) 
     * dove è posizionata la risorsa.
     * @param folder La cartella dove è posizionata la risorsa. 
     */
    public void setFolder(String folder);
    
    /**
     * Restituisce il path assoluto su disco del folder contenitore 
     * dei file delle istanze relative alla risorsa specificata. 
     * Questo path è necessario al salvataggio o alla rimozione
     * dei file associati ad ogni istanza della risorse.
     * @return Il path assoluto su disco completo.
	 * @deprecated since Entando 3.2.1. Use IStorageManager
     */
    public String getDiskFolder();
    
	/**
	 * Aggiunge una categoria alla lista delle categorie della risorsa.
	 * @param category La categoria da aggiungere.
	 */
	public void addCategory(Category category);
	
	/**
	 * Restituisce la lista di categorie associate alla risorsa.
	 * @return La lista di categorie associate alla risorsa.
	 */
	public List<Category> getCategories();
	
	/**
	 * Setta la lista di categorie associate alla risorsa.
	 * @param categories La lista di categorie associate alla risorsa.
	 */
	public void setCategories(List<Category> categories);
	
    /**
     * Crea un oggetto risorsa prototipo da un clone del corrente.
	 * @return L'oggetto risorsa prototipo.
     */
	public ResourceInterface getResourcePrototype();
	
    /**
     * Aggiunge in'istanza alla risorsa.
     * @param instance L'istanza da aggiungere alla risorsa.
     */
    public void addInstance(ResourceInstance instance);
    
    /**
	 * Restituisce l'array di estensioni di file consentiti 
	 * per il particolare ripo di risorsa.
	 * Se l'array è null o vuoto sono consentiti tutti i tipi di file.
	 * @return L'array di estensioni di file consentiti.
	 */
	public String[] getAllowedFileTypes();
	
	/**
	 * Restituisce l'xml completo della risorsa.
	 * @return L'xml completo della risorsa.
	 */
	public String getXML();
	
	/**
	 * Ricava ed salva tutte le istanze associate ad una risorsa, 
	 * valorizzando quest'ultima con i dati delle istanze ricavate.
	 * @param bean L'oggetto detentore dei dati della risorsa da inserire.
	 * @throws ApsSystemException In caso di eccezioni.
	 */
	public void saveResourceInstances(ResourceDataBean bean) throws ApsSystemException;
	
	/**
	 * Cancella tutte le istanze associate alla risorsa.
	 * @throws ApsSystemException In caso di eccezioni.
	 */	
	public void deleteResourceInstances() throws ApsSystemException;
	
	public void reloadResourceInstances() throws ApsSystemException;
	
	public boolean exists(String masterFormFileName) throws ApsSystemException;
    
}