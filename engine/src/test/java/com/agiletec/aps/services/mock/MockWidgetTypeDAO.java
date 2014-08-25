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
package com.agiletec.aps.services.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * @author E.Santoboni
 */
public class MockWidgetTypeDAO extends AbstractDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(MockWidgetTypeDAO.class);
	
	public void deleteWidgetType(String widgetTypeCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_WIDGET_TYPE);
			stat.setString(1, widgetTypeCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting widget type {}", widgetTypeCode,  t);
			throw new RuntimeException("Error deleting widget type", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private final String DELETE_WIDGET_TYPE =
		"DELETE FROM widgetcatalog WHERE code = ?";
	
}
