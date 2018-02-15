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

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.cache.IConfigManagerCacheWrapper;
import de.mkammerer.argon2.Argon2Factory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.StringUtils;

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
public class BaseConfigManager extends AbstractService implements ConfigInterface {

    private static final Logger logger = LoggerFactory.getLogger(BaseConfigManager.class);

    private Map<String, String> systemParams;
	
    private IConfigManagerCacheWrapper cacheWrapper;

    private String configFolder;

    private boolean argon2 = false;

    private IConfigItemDAO configDao;

    @Override
    public void init() throws Exception {
        String version = this.getSystemParams().get(SystemConstants.INIT_PROP_CONFIG_VERSION);
        this.getCacheWrapper().initCache(this.getConfigDAO(), version);
        argon2 = (this.getParam("argon2") != null
                && this.getParam("argon2").equalsIgnoreCase("true"));
        Properties props = this.loadParams();
        this.saveParamChanges(props);
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

    private Properties loadParams() throws IOException {
        Properties props = new Properties();
        InputStream is = null;
        // First try loading from the current directory
        try {
            File f = new File(this.getConfigFolder() + File.separator + "security.properties");
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }
        try {
            if (is == null) {
                // Try loading from classpath
                is = this.getClass().getResourceAsStream("security.properties");
            }
            // Try loading properties from the file (if found)
            props.load(is);
        } catch (Exception e) {
			if (null != is) {
				is.close();
			}
            logger.error("Error getting security.properties");
        }
        return props;
    }
	
	public void saveParamChanges(Properties mainProps) throws Exception {
		Properties props = new Properties();
		String keyString = mainProps.getProperty(ALGO_KEY_ENCRYPTION_PARAM_NAME);
		if (StringUtils.isBlank(keyString)) {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256);
			SecretKey secretKey = keyGen.generateKey();
			keyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		}
		System.getProperties().setProperty(ALGO_KEY_ENCRYPTION_PARAM_NAME, keyString);
		props.setProperty(ALGO_KEY_ENCRYPTION_PARAM_NAME, keyString);
		
		String algoType = null;
		try {
            algoType = Argon2Factory.Argon2Types.valueOf(mainProps.getProperty(ALGO_TYPE_PARAM_NAME)).name();
        } catch (Exception e) {
            algoType = Argon2Factory.Argon2Types.ARGON2i.name();
            logger.warn("Value for Argon2 hashType not correctly set; using default value...", e.getMessage());
        }
		System.getProperties().setProperty(ALGO_TYPE_PARAM_NAME, algoType);
		props.setProperty(ALGO_TYPE_PARAM_NAME, algoType);
		
		Integer hashLength = null;
        try {
            hashLength = Integer.valueOf(mainProps.getProperty(ALGO_HASH_LENGTH_PARAM_NAME));
            if (hashLength < 4) {
                throw new RuntimeException("Hash length must be greater than 4");
            }
        } catch (Exception e) {
            hashLength = 32;
            logger.warn("Value for Argon2 hashLength not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty(ALGO_HASH_LENGTH_PARAM_NAME, String.valueOf(hashLength));
		props.setProperty(ALGO_HASH_LENGTH_PARAM_NAME, String.valueOf(hashLength));
		
		Integer saltLength = null;
		 try {
            saltLength = Integer.valueOf(mainProps.getProperty(ALGO_SALT_LENGTH_PARAM_NAME));
            if (saltLength < 8) {
                throw new RuntimeException("Salt length must be greater than 8");
            }
        } catch (Exception e) {
            saltLength = 16;
            logger.warn("Value for Argon2 saltLength not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty(ALGO_SALT_LENGTH_PARAM_NAME, String.valueOf(saltLength));
		props.setProperty(ALGO_SALT_LENGTH_PARAM_NAME, String.valueOf(saltLength));
		
		Integer iterations = null;
		try {
            iterations = Integer.valueOf(mainProps.getProperty(ALGO_ITERATIONS_PARAM_NAME));
            if (iterations < 1) {
                throw new RuntimeException("Iterations number must be greater than 1");
            }
        } catch (Exception e) {
            iterations = 4;
            logger.warn("Value for Argon2 iterations not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty(ALGO_ITERATIONS_PARAM_NAME, String.valueOf(iterations));
		props.setProperty(ALGO_ITERATIONS_PARAM_NAME, String.valueOf(iterations));
		
		Integer parallelism = null;
		try {
            parallelism = Integer.valueOf(mainProps.getProperty(ALGO_PARALLELISM_PARAM_NAME));
            if (parallelism < 1) {
                throw new RuntimeException("Parallelism number must be greater than 1");
            }
        } catch (Exception e) {
            parallelism = 4;
            logger.warn("Value for Argon2 parallelism not correctly set; using default value...", e.getMessage());
        }
		props.setProperty(ALGO_PARALLELISM_PARAM_NAME, String.valueOf(parallelism));
		System.getProperties().setProperty(ALGO_PARALLELISM_PARAM_NAME, String.valueOf(parallelism));
		
		Integer memory = null;
		try {
            memory = Integer.valueOf(mainProps.getProperty(ALGO_MEMORY_PARAM_NAME));
            if (memory < (8 * parallelism)) {
                throw new RuntimeException("Memory size must be greater than 8xparallelism");
            }
        } catch (Exception e) {
            memory = (1 << 16);
            logger.warn("Value for Argon2 memory not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty(ALGO_MEMORY_PARAM_NAME, String.valueOf(memory));
		props.setProperty(ALGO_MEMORY_PARAM_NAME, String.valueOf(memory));
		
		try {
			File f = new File(this.getConfigFolder() + File.separator + "security.properties");
			OutputStream out = new FileOutputStream(f);
			props.store(out, "DO NOT EDIT THIS FILE");
		} catch (Exception e) {
			throw new RuntimeException("Error saving configuration", e);
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
    public boolean isArgon2() {
        return argon2;
    }

    public void setArgon2(boolean argon2) {
        this.argon2 = argon2;
    }

    public String getConfigFolder() {
        return configFolder;
    }

    public void setConfigFolder(String configFolder) {
        this.configFolder = configFolder;
    }

}
