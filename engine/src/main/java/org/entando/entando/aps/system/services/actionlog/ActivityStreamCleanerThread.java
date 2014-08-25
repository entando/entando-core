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
package org.entando.entando.aps.system.services.actionlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ActivityStreamCleanerThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(ActivityStreamCleanerThread.class);
	
	public ActivityStreamCleanerThread(Integer maxActivitySizeByGroup, 
			IActionLogDAO actionLogDAO) {
		this._maxActivitySizeByGroup = maxActivitySizeByGroup;
		this._actionLogDAO = actionLogDAO;
	}
	
	@Override
	public void run() {
		try {
			this._actionLogDAO.cleanOldActivityStreamLogs(this._maxActivitySizeByGroup);
		} catch (Throwable t) {
			_logger.error("error in run ", t);
			//ApsSystemUtils.logThrowable(t, this, "run");
		}
	}
	
	private Integer _maxActivitySizeByGroup;
	private IActionLogDAO _actionLogDAO;
	
}