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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.cache.IConfigManagerCacheWrapper;

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

	private Map<String, String> _systemParams;

	private IConfigManagerCacheWrapper _cacheWrapper;

	private IConfigItemDAO _configDao;

	@Override
	public void init() throws Exception {
		String version = this.getSystemParams().get(SystemConstants.INIT_PROP_CONFIG_VERSION);
		this.getCacheWrapper().initCache(this.getConfigDAO(), version);
		_logger.debug("{} ready. Initialized", this.getClass().getName());
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
			_logger.error("Error while updating item {}", itemName, t);
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

	protected Map<String, String> getSystemParams() {
		return this._systemParams;
	}

	public void setSystemParams(Map<String, String> systemParams) {
		this._systemParams = systemParams;
	}

	protected IConfigManagerCacheWrapper getCacheWrapper() {
		return _cacheWrapper;
	}

	public void setCacheWrapper(IConfigManagerCacheWrapper cacheWrapper) {
		this._cacheWrapper = cacheWrapper;
	}

	protected IConfigItemDAO getConfigDAO() {
		return _configDao;
	}

	public void setConfigDAO(IConfigItemDAO configDao) {
		this._configDao = configDao;
	}

}
