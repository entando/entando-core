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
import static com.agiletec.aps.system.services.baseconfig.ConfigInterface.ALGO_HASH_LENGTH_PARAM_NAME;
import static com.agiletec.aps.system.services.baseconfig.ConfigInterface.ALGO_ITERATIONS_PARAM_NAME;
import static com.agiletec.aps.system.services.baseconfig.ConfigInterface.ALGO_MEMORY_PARAM_NAME;
import static com.agiletec.aps.system.services.baseconfig.ConfigInterface.ALGO_PARALLELISM_PARAM_NAME;
import static com.agiletec.aps.system.services.baseconfig.ConfigInterface.ALGO_SALT_LENGTH_PARAM_NAME;
import static com.agiletec.aps.system.services.baseconfig.ConfigInterface.ALGO_TYPE_PARAM_NAME;
import com.agiletec.aps.util.MapSupportRule;
import de.mkammerer.argon2.Argon2Factory;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
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

	private static final Logger _logger = LoggerFactory.getLogger(BaseConfigManager.class);

	@Override
	public void init() throws Exception {
		this.loadConfigItems();
		this.parseParams();
		_argon2 = (this.getParam("argon2") != null
				&& this.getParam("argon2").equalsIgnoreCase("true"));
		Properties props = this.extractSecurityConfiguration();
		this.checkSecurityConfiguration(props);
		_logger.debug("{} ready. Initialized {} configuration items and {} parameters", this.getClass().getName(), this._configItems.size(), this._params.size());
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

	protected Properties extractSecurityConfiguration() throws IOException {
		Properties props = new Properties();
		InputStream is = this.getServletContext().getResourceAsStream(this.getSecurityConfigPath());
		if (null == is) {
			throw new RuntimeException("Null security configuration inside " + this.getSecurityConfigPath());
		}
		props.load(is);
		is.close();
		return props;
	}

	protected void checkSecurityConfiguration(Properties mainProps) {
		String algoType = null;
		try {
			algoType = Argon2Factory.Argon2Types.valueOf(mainProps.getProperty(ALGO_TYPE_PARAM_NAME)).name();
		} catch (Exception e) {
			String defaultAlgoType = Argon2Factory.Argon2Types.ARGON2i.name();
			_logger.error("Invalid value for Argon2 hashType '{}'; the default value is '{}'", algoType, defaultAlgoType, e);
			throw new RuntimeException("Invalid value for Argon2 hashType '" + algoType + "'; the default value is '" + defaultAlgoType + "'", e);
		}
		System.getProperties().setProperty(ALGO_TYPE_PARAM_NAME, algoType);

		Integer hashLength = Integer.valueOf(mainProps.getProperty(ALGO_HASH_LENGTH_PARAM_NAME));
		if (hashLength < 4) {
			throw new RuntimeException("Hash length must be greater than 4 - value '" + hashLength + "'");
		}
		System.getProperties().setProperty(ALGO_HASH_LENGTH_PARAM_NAME, String.valueOf(hashLength));

		Integer saltLength = Integer.valueOf(mainProps.getProperty(ALGO_SALT_LENGTH_PARAM_NAME));
		if (saltLength < 8) {
			throw new RuntimeException("Salt length must be greater than 8 - value '" + saltLength + "'");
		}
		System.getProperties().setProperty(ALGO_SALT_LENGTH_PARAM_NAME, String.valueOf(saltLength));

		Integer iterations = Integer.valueOf(mainProps.getProperty(ALGO_ITERATIONS_PARAM_NAME));
		if (iterations < 1) {
			throw new RuntimeException("Iterations number must be greater than 1 - value '" + iterations + "'");
		}
		System.getProperties().setProperty(ALGO_ITERATIONS_PARAM_NAME, String.valueOf(iterations));

		Integer parallelism = Integer.valueOf(mainProps.getProperty(ALGO_PARALLELISM_PARAM_NAME));
		if (parallelism < 1) {
			throw new RuntimeException("Parallelism number must be greater than 1 - value '" + parallelism + "'");
		}
		System.getProperties().setProperty(ALGO_PARALLELISM_PARAM_NAME, String.valueOf(parallelism));

		Integer memory = Integer.valueOf(mainProps.getProperty(ALGO_MEMORY_PARAM_NAME));
		if (memory < (8 * parallelism)) {
			throw new RuntimeException("Memory size must be greater than 8xparallelism - value '" + memory + "'");
		}
		System.getProperties().setProperty(ALGO_MEMORY_PARAM_NAME, String.valueOf(memory));
	}

	protected String getSecurityConfigPath() {
		return securityConfigPath;
	}

	public void setSecurityConfigPath(String securityConfigPath) {
		this.securityConfigPath = securityConfigPath;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
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

	@Override
	public boolean isArgon2() {
		return _argon2;
	}

	public void setArgon2(boolean _argon2) {
		this._argon2 = _argon2;
	}

	/**
	 * Map contenente tutte le voci di configurazione di una versione.
	 */
	private Map<String, String> _configItems;

	/**
	 * Map contenente tutti i parametri di configurazione di una versione.
	 */
	private Map<String, String> _params;

	private boolean _argon2 = false;

	private String securityConfigPath = null;

	private ServletContext servletContext;

	private IConfigItemDAO _configDao;

}
