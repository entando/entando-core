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
package com.agiletec.aps.system.services.keygenerator;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.cache.IKeyGeneratorManagerCacheWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio gestore di sequenze univoche.
 *
 * @author S.Didaci - E.Santoboni
 */
public class KeyGeneratorManager extends AbstractService implements IKeyGeneratorManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String UNIQUE_KEY_LOCK = "UNIQUE_KEY_LOCK";

	private IKeyGeneratorDAO keyGeneratorDao;

	private IKeyGeneratorManagerCacheWrapper cacheWrapper;

	@Override
	public void init() throws Exception {
		this.getCacheWrapper().initCache(this.getKeyGeneratorDAO());
		logger.debug("{} ready. : last loaded key {}", this.getClass().getName(), this.getCacheWrapper().getUniqueKeyCurrentValue());
	}

	/**
	 * Restituisce la chiave univoca corrente.
	 *
	 * @return La chiave univoca corrente.
	 * @throws ApsSystemException In caso di errore nell'aggiornamento della
	 * chiave corrente.
	 */
	@Override
	public int getUniqueKeyCurrentValue() throws ApsSystemException {
		int key;
		synchronized (UNIQUE_KEY_LOCK) {
			key = this.getCacheWrapper().getUniqueKeyCurrentValue() + 1;
		}
		this.updateKey(key);
		return key;
	}

	private void updateKey(int val) throws ApsSystemException {
		try {
			this.getKeyGeneratorDAO().updateKey(val);
			this.cacheWrapper.updateCurrentKey(val);
		} catch (Throwable t) {
			logger.error("Error updating the unique key", t);
			throw new ApsSystemException("Error updating the unique key", t);
		}
	}

	protected IKeyGeneratorDAO getKeyGeneratorDAO() {
		return keyGeneratorDao;
	}

	public void setKeyGeneratorDAO(IKeyGeneratorDAO generatorDAO) {
		this.keyGeneratorDao = generatorDAO;
	}

	protected IKeyGeneratorManagerCacheWrapper getCacheWrapper() {
		return cacheWrapper;
	}

	public void setCacheWrapper(IKeyGeneratorManagerCacheWrapper cacheWrapper) {
		this.cacheWrapper = cacheWrapper;
	}

}
