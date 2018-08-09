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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceDOM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe astratta di base per gli oggetti Resource.
 *
 * @author W.Ambu - E.Santoboni
 */
public abstract class AbstractResource implements ResourceInterface, Serializable {

    private static final Logger _logger = LoggerFactory.getLogger(AbstractResource.class);
    
    private String id;
    private String typeCode;
    private String description;
    private String mainGroup;
    private String masterFileName;
    private List<Category> categories;
    private String folder;
    private String protectedBaseURL;

    private String allowedExtensions;

    private Date creationDate;
    private Date lastModified;

    private IStorageManager storageManager;
    private Map<String, String> metadata;

    private String metadataIgnoreKeys;


    /**
     * Inizializza gli elementi base costituenti la Risorsa.
     */
    public AbstractResource() {
        this.setId("");
        this.setType("");
        this.setDescription("");
        this.setMainGroup("");
        this.setMasterFileName("");
        this.categories = new ArrayList<Category>();
        this.setFolder("");
        this.setCreationDate(null);
        this.setMetadata(new HashMap<String,String>());
        this.setLastModified(null);
    }

    /**
     * Restituisce l'identificativo della risorsa.
     *
     * @return L'identificativo della risorsa.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Setta l'identificativo della risorsa.
     *
     * @param id L'identificativo della risorsa.
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Restituisce il codice del tipo di risorsa.
     *
     * @return Il codice del tipo di risorsa.
     */
    @Override
    public String getType() {
        return typeCode;
    }

    /**
     * Setta il codice del tipo di risorsa.
     *
     * @param typeCode Il codice del tipo di risorsa.
     */
    @Override
    public void setType(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * Restituisce la descrizione della risorsa.
     *
     * @return La descrizione della risorsa.
     * @deprecated use getDescription method
     */
    @Override
    public String getDescr() {
        return this.getDescription();
    }

    /**
     * Setta la descrizione della risorsa.
     *
     * @param descr La descrizione della risorsa.
     * @deprecated use setDescription method
     */
    @Override
    public void setDescr(String descr) {
        this.setDescription(descr);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Restituisce la stringa identificante il gruppo principale di cui la
     * risorsa è membro.
     *
     * @return Il gruppo principale di cui la risorsa è membro.
     */
    @Override
    public String getMainGroup() {
        return mainGroup;
    }

    /**
     * Setta la stringa identificante il gruppo principale di cui il contenuto è
     * membro.
     *
     * @param mainGroup Il gruppo principale di cui il contenuto è membro.
     */
    @Override
    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

    @Override
    public String getMasterFileName() {
        return masterFileName;
    }

    @Override
    public void setMasterFileName(String masterFileName) {
        this.masterFileName = masterFileName;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Aggiunge una categoria alla lista delle categorie della risorsa.
     *
     * @param category La categoria da aggiungere.
     */
    @Override
    public void addCategory(Category category) {
        this.categories.add(category);
    }

    /**
     * Restituisce la lista di categorie associate alla risorsa.
     *
     * @return La lista di categorie associate alla risorsa.
     */
    @Override
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Setta la lista di categorie associate alla risorsa.
     *
     * @param categories La lista di categorie associate alla risorsa.
     */
    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Rimuove una categoria alla lista delle categorie della risorsa.
     *
     * @param category La categoria da rimuovere.
     */
    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    /**
     * Restituisce il nome della cartella contenitore delle risorse.
     *
     * @return Il nome della cartella contenitore delle risorse.
     */
    @Override
    public String getFolder() {
        return folder;
    }

    /**
     * Setta il nome della cartella contenitore delle risorse.
     *
     * @param folder Il nome della cartella contenitore delle risorse.
     */
    @Override
    public void setFolder(String folder) {
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        this.folder = folder;
    }

    /**
     * Restituisce l'url base della cartella delle risorse pretette.
     *
     * @return L'url base della cartella delle risorse protette.
     */
    protected String getProtectedBaseURL() {
        return protectedBaseURL;
    }

    public void setProtectedBaseURL(String protBaseURL) {
        this.protectedBaseURL = protBaseURL;
    }

    @Override
    public String[] getAllowedFileTypes() {
        return this.getAllowedExtensions().split(",");
    }

    /**
     * Setta la stringa rappresentante l'insieme delle estensioni consentite
     * separate da virgola.
     *
     * @return L'insieme delle estensioni consentite.
     */
    protected String getAllowedExtensions() {
        return allowedExtensions;
    }

    /**
     * Setta la stringa rappresentante l'insieme delle estensioni consentite
     * separate da virgola.
     *
     * @param allowedExtensions L'insieme delle estensioni consentite.
     */
    public void setAllowedExtensions(String allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    @Override
    public void setMetadata(Map<String, String> metadata) {
        this.metadata=metadata;
    }

    @Override
    public ResourceInterface getResourcePrototype() {
        AbstractResource prototype = null;
        try {
            Class resourceClass = Class.forName(this.getClass().getName());
            prototype = (AbstractResource) resourceClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException t) {
            throw new RuntimeException("Errore in creazione prototipo "
                    + "Risorsa tipo '" + this.getType() + "'", t);
        }
        prototype.setId("");
        prototype.setType(this.getType());
        prototype.setDescription("");
        prototype.setMainGroup("");
        prototype.setMasterFileName("");
        prototype.setCategories(new ArrayList<Category>());
        prototype.setFolder(this.getFolder());
        prototype.setProtectedBaseURL(this.getProtectedBaseURL());
        prototype.setAllowedExtensions(this.getAllowedExtensions());
        prototype.setStorageManager(this.getStorageManager());
        prototype.setCreationDate(null);
        prototype.setLastModified(null);
        prototype.setMetadata(new HashMap<String,String>());
        return prototype;
    }

    /**
     * Restituisce la classe dom (necessaria per la generazione dell'xml della
     * risorsa) preparata con gli attributi base della risorsa.
     *
     * @return La classe dom preparata con gli attributi base della risorsa.
     */
    protected ResourceDOM getResourceDOM() {
        ResourceDOM resourceDom = this.getNewResourceDOM();
        resourceDom.setTypeCode(this.getType());
        resourceDom.setId(this.getId());
        resourceDom.setDescription(this.getDescription());
        resourceDom.setMainGroup(this.getMainGroup());
        resourceDom.setMasterFileName(this.getMasterFileName());
        if (null != this.getCategories()) {
            for (int i = 0; i < this.getCategories().size(); i++) {
                Category cat = (Category) this.getCategories().get(i);
                resourceDom.addCategory(cat.getCode());
            }
        }
        return resourceDom;
    }

    protected ResourceDOM getNewResourceDOM() {
        return new ResourceDOM();
    }

    protected String getDiskSubFolder() {
        StringBuilder diskFolder = new StringBuilder(this.getFolder());
        if (this.isProtectedResource()) {
            //PROTECTED Resource
            diskFolder.append(this.getMainGroup()).append("/");
        }
        return diskFolder.toString();
    }

    @Override
    public InputStream getResourceStream(ResourceInstance instance) {
        return this.getResourceStream(instance.getSize(), instance.getLangCode());
    }

    /**
     * Restitituisce il nome file corretto da utilizzare per i salvataggi di
     * istanze risorse all'interno del fileSystem.
     *
     * @param masterFileName Il nome del file principale.
     * @return Il nome file corretto.
     * @deprecated from jAPS 2.1
     */
    protected String getRevisedInstanceFileName(String masterFileName) {
        String instanceFileName = masterFileName.replaceAll("[^ _.a-zA-Z0-9]", "");
        instanceFileName = instanceFileName.trim().replace(' ', '_');
        return instanceFileName;
    }

    @Override
    public String getDefaultUrlPath() {
        ResourceInstance defaultInstance = this.getDefaultInstance();
        return this.getUrlPath(defaultInstance);
    }

    /**
     * Repurn the url path of the given istance.
     *
     * @param instance the resource instance
     * @return Il path del file relativo all'istanza.
     */
    protected String getUrlPath(ResourceInstance instance) {
        if (null == instance) {
            return null;
        }
        StringBuilder urlPath = new StringBuilder();
        if (this.isProtectedResource()) {
            //PATH di richiamo della servlet di autorizzazione
            //Sintassi /<RES_ID>/<SIZE>/<LANG_CODE>/
            String DEF = "def";
            urlPath.append(this.getProtectedBaseURL());
            if (!urlPath.toString().endsWith("/")) {
                urlPath.append("/");
            }
            urlPath.append(this.getId()).append("/");
            if (instance.getSize() < 0) {
                urlPath.append(DEF);
            } else {
                urlPath.append(instance.getSize());
            }
            urlPath.append("/");
            if (instance.getLangCode() == null) {
                urlPath.append(DEF);
            } else {
                urlPath.append(instance.getLangCode());
            }
            urlPath.append("/");
        } else {
            StringBuilder subFolder = new StringBuilder(this.getFolder());
            if (!subFolder.toString().endsWith("/")) {
                subFolder.append("/");
            }
            subFolder.append(instance.getFileName());
            String path = this.getStorageManager().getResourceUrl(subFolder.toString(), false);
            urlPath.append(path);
        }
        return urlPath.toString();
    }

    protected boolean isProtectedResource() {
        return (!Group.FREE_GROUP_NAME.equals(this.getMainGroup()));
    }

    protected File saveTempFile(String filename, InputStream is) throws ApsSystemException, IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + filename;
        FileOutputStream outStream = null;
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            outStream = new FileOutputStream(filePath);
            while ((length = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (Throwable t) {
            _logger.error("Error on saving temporary file '{}'", filename, t);
            throw new ApsSystemException("Error on saving temporary file", t);
        } finally {
            if (null != outStream) {
                outStream.close();
            }
            if (null != is) {
                is.close();
            }
        }
        return new File(filePath);
    }

    protected String getNewUniqueFileName(String masterFileName) {
        int index = (null != masterFileName) ? masterFileName.lastIndexOf('.') : -1;
        String baseName = (index > 0) ? masterFileName.substring(0, index) : masterFileName;
        baseName += System.currentTimeMillis();
        return DigestUtils.md5Hex(baseName);
    }

    protected String getFileExtension(String fileName) {
        int index = (null != fileName) ? fileName.lastIndexOf('.') : -1;
        if (index < 0) {
            return "";
        }
        return fileName.substring(index + 1).trim();
    }

    protected boolean exists(String instanceFileName) {
        try {
            String subPath = this.getDiskSubFolder() + instanceFileName;
            return this.getStorageManager().exists(subPath, this.isProtectedResource());
        } catch (Throwable t) {
            _logger.error("Error testing existing file '{}'", instanceFileName, t);
            throw new RuntimeException("Error testing existing file", t);
        }
    }

    protected IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    public String getMetadataIgnoreKeys() {
        return metadataIgnoreKeys;
    }

    public void setMetadataIgnoreKeys(String metadataIgnoreKeys) {
        this.metadataIgnoreKeys = metadataIgnoreKeys;
    }
}
