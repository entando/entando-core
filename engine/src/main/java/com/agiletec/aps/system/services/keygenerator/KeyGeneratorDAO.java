/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object per la generazione di chiavi univoche.
 * @author S.Didaci - E.Santoboni
 */
public class KeyGeneratorDAO extends AbstractDAO implements IKeyGeneratorDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(KeyGeneratorDAO.class);
	
	/**
	 * Estrae la chiave presente nel db.
	 * Il metodo viene chiamato solo in fase di inizializzazione.
	 * @return La chiave estratta.
	 */
	public int getUniqueKey() {
		Connection conn = null;
		int currentKey = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(EXTRACT_KEY);
			if (res.next()) {
				currentKey = res.getInt(1);
			}
		} catch (Throwable t) {
			_logger.error("Error while getting the unique key",  t);
			throw new RuntimeException("Error while getting the unique key", t);
			//processDaoException(e, "Error while getting the unique key", "getUniqueKey");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return currentKey;
	}

	/**
	 * Aggiorna la chiave univoca nel db.
	 * @param currentKey Il valore della chiave corrente.
	 */
	public synchronized void updateKey(int currentKey) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_KEY);
			stat.setInt(1, currentKey);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a key",  t);
			throw new RuntimeException("Error while updating a key", t);
			//processDaoException(e, "Error while updating a key", "getUpdateKey");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	private final String EXTRACT_KEY = "SELECT keyvalue FROM uniquekeys";

	private final String UPDATE_KEY = "UPDATE uniquekeys SET keyvalue = ? ";
}
