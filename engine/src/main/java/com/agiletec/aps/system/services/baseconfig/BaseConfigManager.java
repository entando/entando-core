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

import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.BlowfishApsEncrypter;
import com.agiletec.aps.util.MapSupportRule;
import de.mkammerer.argon2.Argon2Factory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.util.argon2.Argon2Encrypter;

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

    private static final Logger _logger = LoggerFactory.getLogger(BaseConfigManager.class);

    @Override
    public void init() throws Exception {
        this.loadConfigItems();
        this.parseParams();
        _argon2 = (this.getParam("argon2") != null
                && this.getParam("argon2").equalsIgnoreCase("true"));
        _logger.debug("{} ready. Initialized {} configuration items and {} parameters", this.getClass().getName(), this._configItems.size(), this._params.size());
        Properties props = this.loadParams();
        this.validateProps(props);
        this.saveParamChanges();
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
        return (String) this._configItems.get(name);
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
        String oldParamValue = this.getConfigItem(itemName);
        this._configItems.put(itemName, config);
        String version = (String) this._params.get(SystemConstants.INIT_PROP_CONFIG_VERSION);
        try {
            this.getConfigDAO().updateConfigItem(itemName, config, version);
            this.refresh();
        } catch (Throwable t) {
            this._configItems.put(itemName, oldParamValue);
            _logger.error("Error while updating item {}", itemName, t);
            //ApsSystemUtils.logThrowable(t, this, "updateConfigItem");
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
        return (String) this._params.get(name);
    }

    /**
     * Carica le voci di configurazione da db e le memorizza su un Map.
     *
     * @throws ApsSystemException in caso di errori di lettura da db
     */
    private void loadConfigItems() throws ApsSystemException {
        String version = (String) this._params.get(SystemConstants.INIT_PROP_CONFIG_VERSION);
        try {
            _configItems = this.getConfigDAO().loadVersionItems(version);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading items", t);
        }
    }

    /**
     * Esegue il parsing della voce di configurazione "params" per estrarre i
     * parametri. I parametri sono caricati sul Map passato come argomento. I
     * parametri corrispondono a tag del tipo:<br>
     * &lt;Param name="nome_parametro"&gt;valore_parametro&lt;/Param&gt;<br>
     * qualunque sia la loro posizione relativa nel testo XML.<br>
     * ATTENZIONE: non viene controllata l'univocità del nome, in caso di
     * doppioni il precedente valore perso.
     *
     * @throws ApsSystemException In caso di errori IO e Sax
     */
    private void parseParams() throws ApsSystemException {
        String xml = this.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        Digester dig = new Digester();
        Rule rule = new MapSupportRule("name");
        dig.addRule("*/Param", rule);
        dig.push(this._params);
        try {
            dig.parse(new StringReader(xml));
        } catch (Exception e) {
            _logger.error("Error detected while parsing the \"params\" item in the \"sysconfig\" table: verify the \"sysconfig\" table", e);
            //ApsSystemUtils.logThrowable(e, this, "parseParams");
            throw new ApsSystemException(
                    "Error detected while parsing the \"params\" item in the \"sysconfig\" table:"
                    + " verify the \"sysconfig\" table", e);
        }
    }

    public void keyGenerate() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        this.setKeyString(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        System.getProperties().setProperty("key.string.encryption", this.getKeyString());
        try {
            Properties props = new Properties();
            props.setProperty("key.string.encryption", this.getKeyString());
            File f = new File(this.getConfigFolder() + File.separator + "security.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "DO NOT EDIT THIS FILE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Properties loadParams() {
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
                is = getClass().getResourceAsStream("security.properties");
            }

            // Try loading properties from the file (if found)
            props.load(is);
        } catch (Exception e) {
            _logger.error("Error getting security.properties");
        }
        return props;
    }

    public void saveParamChanges() throws Exception {
        if (StringUtils.isBlank(_keyString)) {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            this.setKeyString(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        }
        System.getProperties().setProperty("key.string.encryption", this.getKeyString());
        try {
            Properties props = new Properties();
            props.setProperty("key.string.encryption", this.getKeyString());
            props.setProperty("algo.argon2.type", this.getAlgoType());
            props.setProperty("algo.argon2.hash.length", String.valueOf(this.getHashLen()));
            props.setProperty("algo.argon2.salt.length", String.valueOf(this.getSaltLen()));
            props.setProperty("algo.argon2.iterations", String.valueOf(this.getIterations()));
            props.setProperty("algo.argon2.memory", String.valueOf(this.getMemory()));
            props.setProperty("algo.argon2.parallelism", String.valueOf(this.getParallelism()));
            File f = new File(this.getConfigFolder() + File.separator + "security.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "DO NOT EDIT THIS FILE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateProps(Properties props) {
        try {
            this.setAlgoType(Argon2Factory.Argon2Types.valueOf(props.getProperty("algo.argon2.type")).name());
        } catch (Exception e) {
            this.setAlgoType(Argon2Factory.Argon2Types.ARGON2i.name());
            _logger.warn("Value for Argon2 hashType not correctly set; using default value...");
        }
        System.getProperties().setProperty("algo.argon2.type", this.getAlgoType());

        try {
            this.setHashLen(Integer.valueOf(props.getProperty("algo.argon2.hash.length")));
            if (this.getHashLen() < 4) {
                throw new RuntimeException("Hash length must be greater than 4");
            }
        } catch (Exception e) {
            this.setHashLen(32);
            _logger.warn("Value for Argon2 hashLength not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty("algo.argon2.hash.length", String.valueOf(this.getHashLen()));

        try {
            this.setSaltLen(Integer.valueOf(props.getProperty("algo.argon2.salt.length")));
            if (this.getSaltLen() < 8) {
                throw new RuntimeException("Salt length must be greater than 8");
            }
        } catch (Exception e) {
            this.setSaltLen(16);
            _logger.warn("Value for Argon2 saltLength not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty("algo.argon2.salt.length", String.valueOf(this.getSaltLen()));

        try {
            this.setIterations(Integer.valueOf(props.getProperty("algo.argon2.iterations")));
            if (this.getIterations() < 1) {
                throw new RuntimeException("Iterations number must be greater than 1");
            }
        } catch (Exception e) {
            this.setIterations(4);
            _logger.warn("Value for Argon2 iterations not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty("algo.argon2.iterations", String.valueOf(this.getIterations()));

        try {
            this.setParallelism(Integer.valueOf(props.getProperty("algo.argon2.parallelism")));
            if (this.getParallelism() < 1) {
                throw new RuntimeException("Parallelism number must be greater than 1");
            }
        } catch (Exception e) {
            this.setParallelism(4);
            _logger.warn("Value for Argon2 parallelism not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty("algo.argon2.parallelism", String.valueOf(this.getParallelism()));

        try {
            this.setMemory(Integer.valueOf(props.getProperty("algo.argon2.memory")));
            if (this.getMemory() < 8 * this.getParallelism()) {
                throw new RuntimeException("Memory size must be greater than 8xparallelism");
            }
        } catch (Exception e) {
            this.setMemory(1 << 12);
            _logger.warn("Value for Argon2 memory not correctly set; using default value...", e.getMessage());
        }
        System.getProperties().setProperty("algo.argon2.memory", String.valueOf(this.getMemory()));
    }

    /**
     * Restituisce il dao in uso al manager.
     *
     * @return Il dao in uso al manager.
     */
    protected IConfigItemDAO getConfigDAO() {
        return _configDao;
    }

    /**
     * Setta il dao in uso al manager.
     *
     * @param configDao Il dao in uso al manager.
     */
    public void setConfigDAO(IConfigItemDAO configDao) {
        this._configDao = configDao;
    }

    /**
     * Setta la mappa dei parametri di inizializzazione del sistema.
     *
     * @param systemParams La mappa dei parametri di inizializzazione.
     */
    public void setSystemParams(Map<String, String> systemParams) {
        this._params = systemParams;
    }

    public String getKeyString() {
        return _keyString;
    }

    public void setKeyString(String _keyString) {
        this._keyString = _keyString;
    }

    public String getAlgoType() {
        return _algoType;
    }

    public void setAlgoType(String _algoType) {
        this._algoType = _algoType;
    }

    public int getHashLen() {
        return _hashLen;
    }

    public void setHashLen(int _hashLen) {
        this._hashLen = _hashLen;
    }

    public int getSaltLen() {
        return _saltLen;
    }

    public void setSaltLen(int _saltLen) {
        this._saltLen = _saltLen;
    }

    public int getIterations() {
        return _iterations;
    }

    public void setIterations(int _iterations) {
        this._iterations = _iterations;
    }

    public int getMemory() {
        return _memory;
    }

    public void setMemory(int _memory) {
        this._memory = _memory;
    }

    public int getParallelism() {
        return _parallelism;
    }

    public void setParallelism(int _parallelism) {
        this._parallelism = _parallelism;
    }

    @Override
    public boolean isArgon2() {
        return _argon2;
    }

    public void setArgon2(boolean _argon2) {
        this._argon2 = _argon2;
    }

    public String getConfigFolder() {
        return _configFolder;
    }

    public void setConfigFolder(String configFolder) {
        this._configFolder = configFolder;
    }

    /**
     * Map contenente tutte le voci di configurazione di una versione.
     */
    private Map<String, String> _configItems;

    /**
     * Map contenente tutti i parametri di configurazione di una versione.
     */
    private Map<String, String> _params;

    private String _keyString;

    private String _algoType;

    private int _hashLen;

    private int _saltLen;

    private int _iterations;

    private int _memory;

    private int _parallelism;

    private String _configFolder;

    private boolean _argon2 = false;

    private IConfigItemDAO _configDao;

}
