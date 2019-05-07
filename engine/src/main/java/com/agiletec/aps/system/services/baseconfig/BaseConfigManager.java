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
package com.agiletec.aps.system.services.baseconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.cache.IConfigManagerCacheWrapper;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

/**
 * Servizio di configurazione. Carica da db e rende disponibile la
 * configurazione. La configurazione è costituita da voci (items), individuate
 * da un nome, e da parametri, anch'essi individuati da un nome. I parametri
 * sono stringhe semplici, le voci possono essere testi XML complessi. In
 * particolare, una delle voci contiene la configurazione dei parametri in forma
 * di testo XML. L'insieme dei parametri comprende anche le proprietà di
 * inizializzazione, passate alla factory del contesto di sistema; i valori di
 * queste possono essere sovrascritti dai valori di eventuali parametri omonimi.
 *
 * @author M.Diana - E.Santoboni
 */
public class BaseConfigManager extends AbstractService implements ConfigInterface, ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(BaseConfigManager.class);

    private Map<String, String> systemParams;

    private IConfigManagerCacheWrapper cacheWrapper;

    private boolean legacyPasswordsUpdated = false;

    private IConfigItemDAO configDao;

    private ServletContext servletContext;

    @Override
    public void init() throws Exception {
        String version = this.getSystemParams().get(SystemConstants.INIT_PROP_CONFIG_VERSION);
        this.getCacheWrapper().initCache(this.getConfigDAO(), version);
        legacyPasswordsUpdated = (this.getParam(LEGACY_PASSWORDS_UPDATED) != null
                && this.getParam(LEGACY_PASSWORDS_UPDATED).equalsIgnoreCase("true"));
        logger.debug("{} ready. Initialized", this.getClass().getName());
    }

    /**
     * Restituisce il valore di una voce della tabella di sistema. Il valore può
     * essere un XML complesso.
     *
     * @param name Il nome della voce di configurazione.
     * @return Il valore della voce di configurazione.
     */
    @Override
    public String getConfigItem(String name) {
        return this.getCacheWrapper().getConfigItem(name);
    }

    /**
     * Aggiorna un'item di configurazione nella mappa della configurazione degli
     * item e nel db.
     *
     * @param itemName Il nome dell'item da aggiornare.
     * @param config La nuova configurazione.
     * @throws ApsSystemException
     */
    @Override
    public void updateConfigItem(String itemName, String config) throws ApsSystemException {
        String version = this.getSystemParams().get(SystemConstants.INIT_PROP_CONFIG_VERSION);
        try {
            this.getConfigDAO().updateConfigItem(itemName, config, version);
            this.refresh();
        } catch (Throwable t) {
            logger.error("Error while updating item {}", itemName, t);
            throw new ApsSystemException("Error while updating item", t);
        }
    }

    /**
     * Restituisce il valore di un parametro di configurazione. I parametri sono
     * desunti dalla voce "params" della tabella di sistema.
     *
     * @param name Il nome del parametro di configurazione.
     * @return Il valore del parametro di configurazione.
     */
    @Override
    public String getParam(String name) {
        String param = this.getSystemParams().get(name);
        if (null != param) {
            return param;
        } else {
            return this.getCacheWrapper().getParam(name);
        }
    }

    @Override
    public void updateParam(String name, String value) throws ApsSystemException {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(name, value);
        this.updateParams(params);
    }

    @Override
    public void updateParams(Map<String, String> params) throws ApsSystemException {
        if (null == params) {
            return;
        }
        try {
            String xmlParams = this.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
            String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, params, false);
            this.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
        } catch (Exception e) {
            logger.error("Error while updating parameters {}", params, e);
            throw new ApsSystemException("Error while updating parameters", e);
        }
    }

    /**
     * Restituisce il dao in uso al manager.
     *
     * @return Il dao in uso al manager.
     */
    protected IConfigItemDAO getConfigDAO() {
        return configDao;
    }

    /**
     * Setta il dao in uso al manager.
     *
     * @param configDao Il dao in uso al manager.
     */
    public void setConfigDAO(IConfigItemDAO configDao) {
        this.configDao = configDao;
    }

    protected Map<String, String> getSystemParams() {
        return this.systemParams;
    }

    public void setSystemParams(Map<String, String> systemParams) {
        this.systemParams = systemParams;
    }

    protected IConfigManagerCacheWrapper getCacheWrapper() {
        return cacheWrapper;
    }

    public void setCacheWrapper(IConfigManagerCacheWrapper cacheWrapper) {
        this.cacheWrapper = cacheWrapper;
    }

    @Override
    public boolean areLegacyPasswordsUpdated() {
        return legacyPasswordsUpdated;
    }

    public void setLegacyPasswordsUpdated(boolean updated) {
        this.legacyPasswordsUpdated = updated;
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
