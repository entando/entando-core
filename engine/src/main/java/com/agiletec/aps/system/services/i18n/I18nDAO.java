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
package com.agiletec.aps.system.services.i18n;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

/**
 * Data Access Object per gli oggetti label (I18n).
 * @author S.Didaci- E.Santoboni
 */
public class I18nDAO extends AbstractDAO implements II18nDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(I18nDAO.class);
	
	/**
	 * Carica la mappa che contiene tutte le label in tutte le lingue.
	 * @return La mappa contenente tutte le label.
	 */
	public Map<String, ApsProperties> loadLabelGroups() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<String, ApsProperties> labels = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_ALL_LABELS);
			labels = this.createLabels(res);
		} catch (Throwable t) {
			_logger.error("Error loading labels",  t);
			throw new RuntimeException("Error loading labels", t);
			//processDaoException(t, "Error loading label", "loadLabelMap");
		} finally{
			closeDaoResources(res, stat, conn);
		}
		return labels;
	}

	public void updateLabelGroup(String key, ApsProperties labels) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteLabelGroup(key, conn);
			this.addLabelGroup(key, labels, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a label '{}'", key, t);
			throw new RuntimeException("Error while updating a label", t);
			//processDaoException(t, "Error while updating a label", "updateLabel");
		} finally {
			this.closeConnection(conn);
		}
	}

	public void addLabelGroup(String key, ApsProperties labels) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.addLabelGroup(key, labels, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a label '{}'", key, t);
			throw new RuntimeException("Error while adding a label", t);
			//processDaoException(t, "Error while adding a label", "addLabelGroup");
		} finally {
			this.closeConnection(conn);
		}
	}

	private void addLabelGroup(String key, ApsProperties labels, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_LABEL);
			Iterator<Object> labelKeysIter = labels.keySet().iterator();
			while (labelKeysIter.hasNext()) {
				String labelLangCode = (String) labelKeysIter.next();
				String label = labels.getProperty(labelLangCode);
				stat.setString(1, key);
				stat.setString(2, labelLangCode);
				stat.setString(3, label);
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (BatchUpdateException e) {
			_logger.error("Error adding a new label record",  e.getNextException());
			throw new RuntimeException("Error adding a new label record", e.getNextException());
			//processDaoException(e.getNextException(), "Error adding a new label record", "addLabel");
		} catch (Throwable t) {
			_logger.error("Error while adding a new label",  t);
			throw new RuntimeException("Error while adding a new label", t);
			//processDaoException(t, "Error while adding a new label", "addLabel");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	public void deleteLabelGroup(String key) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteLabelGroup(key, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting labels by key '{}'", key, t);
			throw new RuntimeException("Error deleting labels by key " + key, t);
			//processDaoException(t, "Erro while deleting multiple labels", "deleteLabelGroup");
		} finally {
			this.closeConnection(conn);
		}
	}

	private void deleteLabelGroup(String key, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_LABEL);
			stat.setString(1, key);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting labels by key '{}'", key, t);
			throw new RuntimeException("Error deleting labels by key " + key, t);
			//processDaoException(t, "Error while deleting a label group", "deleteLabel");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	/**
	 * Metodo di servizio: legge tutte le label. 
	 * @param res Il resultset ottenuto dall'estrazione delle labels.
	 * @throws SQLException
	 * @throws ApsSystemException
	 */
	private Map<String, ApsProperties> createLabels(ResultSet res) 
	throws SQLException, ApsSystemException {
		//1  l.key,  2 l.langcode, 3  l.value"
		Map<String, ApsProperties> labels = new HashMap<String, ApsProperties>();
		while (res.next()) {
			String key = res.getString(1);
			ApsProperties labelsProp = labels.get(key);
			if (labelsProp == null) {
				labelsProp = new ApsProperties();
				labels.put(key, labelsProp);
			}
			String langCode = res.getString(2);
			String value = res.getString(3);
			labelsProp.put(langCode, value);
		}
		return labels;
	}

	private final String LOAD_ALL_LABELS = 
		"SELECT keycode, langcode, stringvalue FROM localstrings";

	private final String ADD_LABEL =
		"INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ( ? , ? , ? )";

	private final String DELETE_LABEL =
		"DELETE FROM localstrings WHERE keycode = ? ";

}