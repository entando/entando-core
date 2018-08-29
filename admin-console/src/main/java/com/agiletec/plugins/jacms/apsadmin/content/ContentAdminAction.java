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
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.LastReloadInfo;
import java.util.Arrays;
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

    private String resourceTitleMapping;
    private String resourceLegendMapping;
    private String resourceDescriptionMapping;
    private String resourceAltMapping;

    private IContentManager contentManager;
    private IResourceManager resourceManager;
    private ICmsSearchEngineManager searchEngineManager;

    @Override
    public String configSystemParams() {
        String result = super.configSystemParams();
        if (!result.equals(SUCCESS)) {
            return result;
        }
        try {
            Map<String, List<String>> mapping = this.getResourceManager().getMetadataMapping();
            this.setResourceAltMapping(this.buildCsv(mapping, IResourceManager.ALT_METADATA_MAPPING_KEY));
            this.setResourceDescriptionMapping(this.buildCsv(mapping, IResourceManager.DESCRIPTION_METADATA_MAPPING_KEY));
            this.setResourceLegendMapping(this.buildCsv(mapping, IResourceManager.LEGEND_METADATA_MAPPING_KEY));
            this.setResourceTitleMapping(this.buildCsv(mapping, IResourceManager.TITLE_METADATA_MAPPING_KEY));
        } catch (Throwable t) {
            logger.error("error in configSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    private String buildCsv(Map<String, List<String>> mapping, String key) {
        List<String> list = mapping.get(key);
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
            Map<String, List<String>> mapping = new HashMap<>();
            this.updateResourceMapping(mapping, this.getResourceAltMapping(), IResourceManager.ALT_METADATA_MAPPING_KEY);
            this.updateResourceMapping(mapping, this.getResourceDescriptionMapping(), IResourceManager.DESCRIPTION_METADATA_MAPPING_KEY);
            this.updateResourceMapping(mapping, this.getResourceLegendMapping(), IResourceManager.LEGEND_METADATA_MAPPING_KEY);
            this.updateResourceMapping(mapping, this.getResourceTitleMapping(), IResourceManager.TITLE_METADATA_MAPPING_KEY);
            this.getResourceManager().updateMetadataMapping(mapping);
        } catch (Throwable t) {
            logger.error("error in configSystemParams", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    private void updateResourceMapping(Map<String, List<String>> mapping, String csv, String key) {
        List<String> mappingList = (!StringUtils.isBlank(csv)) ? Arrays.asList(csv.split(",")) : null;
        if (null == mappingList) {
            mapping.remove(key);
        } else {
            mapping.put(key, mappingList);
        }
    }

    /**
     * Effettua la richiesta di ricaricamento degli indici a servizio del motore
     * di ricerca interno.
     *
     * @return Il codice del risultato dell'azione.
     */
    public String reloadContentsIndex() {
        try {
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

    public String getResourceTitleMapping() {
        return resourceTitleMapping;
    }

    public void setResourceTitleMapping(String resourceTitleMapping) {
        this.resourceTitleMapping = resourceTitleMapping;
    }

    public String getResourceLegendMapping() {
        return resourceLegendMapping;
    }

    public void setResourceLegendMapping(String resourceLegendMapping) {
        this.resourceLegendMapping = resourceLegendMapping;
    }

    public String getResourceDescriptionMapping() {
        return resourceDescriptionMapping;
    }

    public void setResourceDescriptionMapping(String resourceDescriptionMapping) {
        this.resourceDescriptionMapping = resourceDescriptionMapping;
    }

    public String getResourceAltMapping() {
        return resourceAltMapping;
    }

    public void setResourceAltMapping(String resourceAltMapping) {
        this.resourceAltMapping = resourceAltMapping;
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
