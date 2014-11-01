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