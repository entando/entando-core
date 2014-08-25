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
package org.entando.entando.aps.system.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * @author E.Santoboni
 */
public class InstallationReportDAO extends AbstractDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(InstallationReportDAO.class);
	
	public SystemInstallationReport loadReport(String version) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		SystemInstallationReport report = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(VERSION_ITEM);
			stat.setString(1, version);
			stat.setString(2, InitializerManager.REPORT_CONFIG_ITEM);
			res = stat.executeQuery();
			if (res.next()) {
				String xml = res.getString(1);
				report = new SystemInstallationReport(xml);
			} else {
				//PORTING
				report = SystemInstallationReport.getPortingInstance();
			}
		} catch (SQLException sqle) {
			//NOT_AVAILABLE
			_logger.info("Report not available" + sqle);
			return null;
		} catch (Throwable t) {
			_logger.error("Error while loading component installation report - version: {}", version,  t);
			throw new RuntimeException("Error while loading component installation report - version: " + version, t);
			//this.processDaoException(t, "Error while loading component installation report - version: " + version, "loadReport");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return report;
	}
	
	public void saveConfigItem(String config, String version) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteItem(version, conn);
			stat = conn.prepareStatement(ADD_ITEM);
			stat.setString(1, version);
			stat.setString(2, InitializerManager.REPORT_CONFIG_ITEM);
			stat.setString(3, "The component installation report");
			stat.setString(4, config);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error saving item" ,  t);
			throw new RuntimeException("Error saving item", t);
			//processDaoException(t, "Error while updating saving item",	"saveConfigItem");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private void deleteItem(String version, Connection conn) {
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_ITEM);
			stat.setString(1, InitializerManager.REPORT_CONFIG_ITEM);
			stat.setString(2, version);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting item" ,  t);
			throw new RuntimeException("Error deleting item", t);
			//processDaoException(t, "Error while deleting item", "deleteItem");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	private final String VERSION_ITEM =
		"SELECT config FROM sysconfig WHERE version = ? AND item = ?";
	
	private static final String DELETE_ITEM = 
		"DELETE FROM sysconfig WHERE item = ? AND version = ?";
	
	private static final String ADD_ITEM = 
		"INSERT INTO sysconfig(version, item, descr, config) VALUES (?, ?, ?, ?)";
	
}
