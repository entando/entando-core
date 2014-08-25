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
package com.agiletec.aps.system.services.baseconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object delegate 
 * alla gestione delle le voci di configurazione. 
 * Opera sulla tabella "system".
 * @author 
 */
public class ConfigItemDAO extends AbstractDAO implements IConfigItemDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ConfigItemDAO.class);
	
	/**
	 * Carica e restituisce un Map con tutte le voci di
	 * configurazione di una versione di configurazione.
	 * @param version La versione di configurazione.
	 * @return Il Map con le voci di configurazione
	 */
	public Map<String, String> loadVersionItems(String version) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, String> itemMap = new HashMap<String, String>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(VERSION_ITEMS);
			stat.setString(1, version);
			res = stat.executeQuery();
			while(res.next()){
				String name = res.getString(1);
				String value = res.getString(2);
				itemMap.put(name, value);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading configuration item - version '{}'", version,  t);
			throw new RuntimeException("Error while loading configuration item - version " 	+ version, t);
			//processDaoException(e, "Error while loading configuration item - version " 	+ version, "loadVersionItems");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return itemMap;
	}
	
	/**
	 * Carica e restituisce una voce di configurazione
	 * di una versione di configurazione. Questo metodo NON deve essere utilizzato
	 * normalmente, ma solo nelle fasi di inizializzazione del sistema, quando
	 * il SysContext non è ancora disponibile.
	 * @param version La versione di configurazione
	 * @param itemName Il nome della voce di configurazione.
	 * @return La voce di configurazione richiesta
	 */
	public String loadVersionItem(String version, String itemName) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String value = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(VERSION_ITEM);
			stat.setString(1, version);
			stat.setString(2, itemName);
			res = stat.executeQuery();
			res.next();
			value = res.getString(1);
		} catch (Throwable t) {
			_logger.error("Error while loading the configuration item - version: '{}' item: '{}'", version, itemName, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return value;
	}
	
	/**
	 * Aggiorna un'item di configurazione nel db.
	 * @param itemName Il nome dell'item da aggiornare.
	 * @param config La nuova configurazione.
	 * @param version La versione da aggiornare.
	 */
	public void updateConfigItem(String itemName, String config, String version) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_ITEM);
			stat.setString(1, config);
			stat.setString(2, itemName);
			stat.setString(3, version);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating configuration item '{}'", itemName, t);
			throw new RuntimeException("Error while updating oconfiguration item", t);
			//processDaoException(t, "Error while updating oconfiguration item",	"updateConfigItem");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private final String VERSION_ITEMS =
		"SELECT item, config FROM sysconfig WHERE version = ?";
	
	private final String VERSION_ITEM =
		"SELECT config FROM sysconfig WHERE version = ? AND item = ?";
	
	private static final String UPDATE_ITEM = 
		"UPDATE sysconfig SET config = ? WHERE item = ? AND version = ?";
	
}
