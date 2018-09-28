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

import com.agiletec.apsadmin.admin.BaseAdminAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.LastReloadInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Classi Action delegata alla esecuzione delle operazioni di amministrazione
 * dei contenuti.
 *
 * @author E.Santoboni
 */
public class ContentAdminAction extends BaseAdminAction {

    private static final Logger logger = LoggerFactory.getLogger(ContentAdminAction.class);

    private Map<String, List<String>> mapping;
    private String metadataKey;
    private String metadataMapping;
    private List<String> metadataKeys;

    private IContentManager contentManager;
    private IResourceManager resourceManager;
    private ICmsSearchEngineManager searchEngineManager;

    @Override
    public void validate() {
        super.validate();
        Map<String, List<String>> mapping = this.buildMapping();
        if (!StringUtils.isBlank(this.getMetadataKey())) {
            if (mapping.containsKey(this.getMetadataKey().trim())) {
                this.addFieldError("metadataKey", this.getText("error.contentSettings.metadataAlreadyPresent", new String[]{this.getMetadataKey().trim()}));
            }
        }
    }

    @Override
    public String configSystemParams() {
        String result = super.configSystemParams();
        if (!result.equals(SUCCESS)) {
            return result;
        }
        try {
            this.setMapping(this.getResourceManager().getMetadataMapping());
        } catch (Throwable t) {
            logger.error("error in configSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String buildCsv(String key) {
        List<String> list = (null != this.getMapping()) ? this.getMapping().get(key) : null;
        if (null == list) {
            return null;
        }
        return String.join(",", list);
    }

    @Override
    public String updateSystemParams() {
        String result = this.updateSystemParams(true);
        if (!result.equals(SUCCESS)) {
            return result;
        }
        try {
            Map<String, List<String>> newMapping = this.buildMapping();
            this.getResourceManager().updateMetadataMapping(newMapping);
        } catch (Throwable t) {
            logger.error("error in configSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String addMetadata() {
        Map<String, List<String>> newMappings = this.buildMapping();
        if (!StringUtils.isBlank(this.getMetadataKey()) && !newMappings.containsKey(this.getMetadataKey().trim())) {
            List<String> newMapping = new ArrayList<>();
            if (!StringUtils.isBlank(this.getMetadataMapping())) {
                newMapping.addAll(Arrays.asList(this.getMetadataMapping().trim().split(",")));
            }
            newMappings.put(this.getMetadataKey().trim(), newMapping);
        }
        return SUCCESS;
    }

    public String removeMetadata() {
        Map<String, List<String>> newMapping = this.buildMapping();
        if (!StringUtils.isBlank(this.getMetadataKey())) {
            newMapping.remove(this.getMetadataKey().trim());
        }
        return SUCCESS;
    }

    protected Map<String, List<String>> buildMapping() {
        Map<String, List<String>> newMapping = new HashMap<>();
        if (null == this.getMetadataKeys()) {
            return newMapping;
        }
        for (String key : this.getMetadataKeys()) {
            String csv = this.getRequest().getParameter("resourceMetadata_mapping_" + key);
            this.updateResourceMapping(newMapping, csv, key);
        }
        this.setMapping(newMapping);
        return newMapping;
    }

    private void updateResourceMapping(Map<String, List<String>> mapping, String csv, String key) {
        List<String> mappingList = (!StringUtils.isBlank(csv)) ? Arrays.asList(csv.split(",")) : new ArrayList<>();
        mapping.put(key, mappingList);
    }

    /**
     * Effettua la richiesta di ricaricamento degli indici a servizio del motore
     * di ricerca interno.
     *
     * @return Il codice del risultato dell'azione.
     */
    public String reloadContentsIndex() {
        try {
            this.buildMapping();
            String result = this.configSystemParams();
            if (!result.equals(SUCCESS)) {
                return result;
            }
            this.getSearchEngineManager().startReloadContentsReferences();
            logger.info("Reload contents index started");
        } catch (Throwable t) {
            logger.error("error in reloadContentsIndex", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    /**
     * Effettua la richeista di ricaricamento delle referenze dei contenuti. Le
     * referenze sono rappresentate sia dalle repliche dei valori degli
     * Attributi di Contenuto dichiarati ricercabili in apposita tabella
     * (elementi a servizio degli erogatori di contenuti in lista) e che delle
     * referenze tra Contenuti e Pagine, Risorse, Gruppi e Contenuti stessi in
     * apposita tabella (elementi a servizio di controlli di autorizzazione e
     * validazione).
     *
     * @return Il codice del risultato dell'azione.
     */
    public String reloadContentsReference() {
        try {
            this.buildMapping();
            String result = this.configSystemParams();
            if (!result.equals(SUCCESS)) {
                return result;
            }
            String typeCode = null;
            this.getContentManager().reloadEntitiesReferences(typeCode);
            logger.info("Reload contents reference started");
        } catch (Throwable t) {
            logger.error("error in reloadContentsReference", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public List<String> getResourceMetadataKeys() {
        List<String> keys = new ArrayList<>();
        if (null != this.getMapping()) {
            keys.addAll(this.getMapping().keySet());
            Collections.sort(keys);
        }
        return keys;
    }

    public int getContentManagerStatus() {
        return this.getContentManager().getStatus();
    }

    public int getSearcherManagerStatus() {
        return this.getSearchEngineManager().getStatus();
    }

    /**
     * @deprecated From jAPS 2.0 version 2.0.9. Use getContentManagerStatus()
     * method
     */
    public int getContentManagerState() {
        return this.getContentManagerStatus();
    }

    /**
     * @deprecated From jAPS 2.0 version 2.0.9. Use getSearcherManagerStatus()
     * method
     */
    public int getSearcherManagerState() {
        return this.getSearcherManagerStatus();
    }

    public LastReloadInfo getLastReloadInfo() {
        return this.getSearchEngineManager().getLastReloadInfo();
    }

    public Map<String, List<String>> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, List<String>> mapping) {
        this.mapping = mapping;
    }

    public String getMetadataKey() {
        return metadataKey;
    }

    public void setMetadataKey(String metadataKey) {
        this.metadataKey = metadataKey;
    }

    public List<String> getMetadataKeys() {
        return metadataKeys;
    }

    public String getMetadataMapping() {
        return metadataMapping;
    }

    public void setMetadataMapping(String metadataMapping) {
        this.metadataMapping = metadataMapping;
    }

    public void setMetadataKeys(List<String> metadataKeys) {
        this.metadataKeys = metadataKeys;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected ICmsSearchEngineManager getSearchEngineManager() {
        return searchEngineManager;
    }

    public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
        this.searchEngineManager = searchEngineManager;
    }

    protected IResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

}
