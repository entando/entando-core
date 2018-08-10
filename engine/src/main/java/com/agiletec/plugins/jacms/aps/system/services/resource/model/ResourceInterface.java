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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import java.util.Map;

/**
 * Interfaccia per gli oggetti risorsa.
 *
 * @author W.Ambu - E.Santoboni
 */
public interface ResourceInterface {

    /**
     * Specifica se la risorsa è composta da più istanze.
     *
     * @return true se la risorsa è composta da più istanze, false se è composta
     * da una sola istanza.
     */
    public boolean isMultiInstance();

    /**
     * Restituisce l'identificativo della risorsa.
     *
     * @return L'identificativo della risorsa.
     */
    public String getId();

    /**
     * Setta l'identificativo della risorsa.
     *
     * @param id L'identificativo della risorsa.
     */
    public void setId(String id);

    /**
     * Restituisce il codice del tipo della risorsa.
     *
     * @return Il codice del tipo della risorsa.
     */
    public String getType();

    /**
     * Setta il codice del tipo della risorsa.
     *
     * @param typeCode Il codice del tipo della risorsa.
     */
    public void setType(String typeCode);

    /**
     * Restituisce la descrizione della risorsa.
     *
     * @return La descrizione della risorsa.
     * @deprecated use getDescription method
     */
    public String getDescr();

    /**
     * Setta la descrizione della risorsa.
     *
     * @param descr La descrizione della risorsa.
     * @deprecated use setDescription method
     */
    public void setDescr(String descr);

    /**
     * Return the description of the resource.
     *
     * @return The description of the resource
     */
    public String getDescription();

    /**
     * Set the description of the resource.
     *
     * @param description The description of the resource
     */
    public void setDescription(String description);

    /**
     * Restituisce la stringa identificante il gruppo principale di cui la
     * risorsa è membro.
     *
     * @return Il gruppo principale di cui la risorsa è membro.
     */
    public String getMainGroup();

    /**
     * Setta la stringa identificante il gruppo principale di cui il contenuto è
     * membro.
     *
     * @param mainGroup Il gruppo principale di cui il contenuto è membro.
     */
    public void setMainGroup(String mainGroup);

    /**
     * Return the name of the file master.
     *
     * @return The name of the master file.
     */
    public String getMasterFileName();

    /**
     * Set the name of the file master.
     *
     * @param mainFileName The name of the master file.
     */
    public void setMasterFileName(String mainFileName);

    public Date getCreationDate();

    public void setCreationDate(Date creationDate);

    public Date getLastModified();

    public void setLastModified(Date lastModified);

    public InputStream getResourceStream();

    public InputStream getResourceStream(ResourceInstance instance);

    public InputStream getResourceStream(int size, String langCode);

    public String getDefaultUrlPath();

    /**
     * Restituisce la cartella (a partire dalla cartella delle risorse) dove è
     * posizionata la risorsa.
     *
     * @return La cartella dove è posizionata la risorsa.
     */
    public String getFolder();

    /**
     * Setta la cartella (a partire dalla cartella delle risorse) dove è
     * posizionata la risorsa.
     *
     * @param folder La cartella dove è posizionata la risorsa.
     */
    public void setFolder(String folder);

    /**
     * Aggiunge una categoria alla lista delle categorie della risorsa.
     *
     * @param category La categoria da aggiungere.
     */
    public void addCategory(Category category);

    /**
     * Restituisce la lista di categorie associate alla risorsa.
     *
     * @return La lista di categorie associate alla risorsa.
     */
    public List<Category> getCategories();

    /**
     * Setta la lista di categorie associate alla risorsa.
     *
     * @param categories La lista di categorie associate alla risorsa.
     */
    public void setCategories(List<Category> categories);

    /**
     * Crea un oggetto risorsa prototipo da un clone del corrente.
     *
     * @return L'oggetto risorsa prototipo.
     */
    public ResourceInterface getResourcePrototype();

    /**
     * Aggiunge in'istanza alla risorsa.
     *
     * @param instance L'istanza da aggiungere alla risorsa.
     */
    public void addInstance(ResourceInstance instance);

    /**
     * Restituisce l'array di estensioni di file consentiti per il particolare
     * ripo di risorsa. Se l'array è null o vuoto sono consentiti tutti i tipi
     * di file.
     *
     * @return L'array di estensioni di file consentiti.
     */
    public String[] getAllowedFileTypes();

    /**
     * Restituisce l'xml completo della risorsa.
     *
     * @return L'xml completo della risorsa.
     */
    public String getXML();

    /**
     * Ricava ed salva tutte le istanze associate ad una risorsa, valorizzando
     * quest'ultima con i dati delle istanze ricavate.
     *
     * @param bean L'oggetto detentore dei dati della risorsa da inserire.
     * @throws ApsSystemException In caso di eccezioni.
     */
    public void saveResourceInstances(ResourceDataBean bean) throws ApsSystemException;

    /**
     * Cancella tutte le istanze associate alla risorsa.
     *
     * @throws ApsSystemException In caso di eccezioni.
     */
    public void deleteResourceInstances() throws ApsSystemException;

    public void reloadResourceInstances() throws ApsSystemException;

    public ResourceInstance getDefaultInstance();

    /**
     * Setta la lista dei metadata da associare alla risorsa.
     *
     * @param metadata La lista dei metadata da associare alla risorsa.
     */
    public void setMetadata(Map<String, String> metadata);

    /**
     * Restituisce un oggetto Map con l'elenco dei metadati della risorsa.
     *
     * @return Un oggetto Map con l'elenco dei metadati della risorsa.
     */
    public Map<String, String> getMetadata();

    /**
     * Restituisce una stringa con l'elenco dei metadati da non salvare nella
     * risorsa xml nel database.
     *
     * @return Una stringa con l'elenco dei metadati da non salvare nella
     * risorsa xml nel database.
     */
    public String getMetadataIgnoreKeys();

    /**
     * Setta la lista dei metadata da da non salvare nella risorsa xml nel
     * database.
     *
     * @param metadataIgnoreKeys La lista dei metadata da non salvare nella
     * risorsa xml nel database.
     */
    public void setMetadataIgnoreKeys(String metadataIgnoreKeys);

}
