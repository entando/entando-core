/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
