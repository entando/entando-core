/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system.services.keygenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Servizio gestore di sequenze univoche.
 * @author S.Didaci - E.Santoboni
 */
public class KeyGeneratorManager extends AbstractService implements IKeyGeneratorManager {

	private static final Logger _logger = LoggerFactory.getLogger(KeyGeneratorManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadUniqueKey();
		_logger.debug("{} ready. : last loaded key {}", this.getClass().getName(), _uniqueKeyCurrentValue );
	}

	/**
	 * Estrae la chiave presente nel db.
	 * Il metodo viene chiamato solo in fase di inizializzazione.
	 * @throws ApsSystemException
	 */
	private void loadUniqueKey() throws ApsSystemException {
		try {
			_uniqueKeyCurrentValue = this.getKeyGeneratorDAO().getUniqueKey();
		} catch (Throwable t) {
			_logger.error("Error retrieving the unique key", t);
			//ApsSystemUtils.logThrowable(t, this, "loadUniqueKey","Error retrieving the unique key");
			throw new ApsSystemException("Error retrieving the unique key", t);
		}
	}

	/**
	 * Restituisce la chiave univoca corrente.
	 * @return La chiave univoca corrente.
	 * @throws ApsSystemException In caso di errore 
	 * nell'aggiornamento della chiave corrente.
	 */
	public int getUniqueKeyCurrentValue() throws ApsSystemException {
		++_uniqueKeyCurrentValue;
		int key = _uniqueKeyCurrentValue;
		this.updateKey();
		return key;
	}

	private void updateKey() throws ApsSystemException {
		try {
			this.getKeyGeneratorDAO().updateKey(_uniqueKeyCurrentValue);
		} catch (Throwable t) {
			_logger.error("Error updating the unique key", t);
			//ApsSystemUtils.logThrowable(t, this, "loadUniqueKey");
			throw new ApsSystemException("Error updating the unique key", t);
		}
	}

	protected IKeyGeneratorDAO getKeyGeneratorDAO() {
		return _keyGeneratorDao;
	}

	public void setKeyGeneratorDAO(IKeyGeneratorDAO generatorDAO) {
		this._keyGeneratorDao = generatorDAO;
	}

	private int _uniqueKeyCurrentValue;

	private IKeyGeneratorDAO _keyGeneratorDao;
	
}
