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
package org.entando.entando.apsadmin.system.services.activitystream;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.entando.entando.aps.system.services.actionlog.ActionLoggerTestHelper;

/**
 * @author E.Santoboni
 */
public class SocialStreamTestHelper extends ActionLoggerTestHelper {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SocialStreamTestHelper.class);
	
	public SocialStreamTestHelper(ApplicationContext applicationContext) {
		super(applicationContext);
	}
	
	@Override
	public void cleanRecords() {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteRecords(conn, DELETE_LOG_COMMENT_RECORDS);
			this.deleteRecords(conn, DELETE_LOG_LIKE_RECORDS);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on delete records",  t);
			throw new RuntimeException("Error on delete records", t);
		} finally {
			closeConnection(conn);
		}
		super.cleanRecords();
	}
	
	private static final String DELETE_LOG_LIKE_RECORDS = 
		"DELETE from actionloglikerecords";
	
	private static final String DELETE_LOG_COMMENT_RECORDS = 
		"DELETE from actionlogcommentrecords";
	
}