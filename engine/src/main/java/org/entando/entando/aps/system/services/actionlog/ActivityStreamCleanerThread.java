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

import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ActivityStreamCleanerThread extends Thread {
	
	private static final Logger _logger = LoggerFactory.getLogger(ActivityStreamCleanerThread.class);
	
	public ActivityStreamCleanerThread(Integer maxActivitySizeByGroup, IActionLogManager actionLogManager) {
		this._maxActivitySizeByGroup = maxActivitySizeByGroup;
		this._actionLogManager = actionLogManager;
	}
	
	@Override
	public void run() {
		try {
			Set<Integer> ids = this._actionLogManager.extractOldRecords(this._maxActivitySizeByGroup);
			if (null != ids) {
				Iterator<Integer> iter = ids.iterator();
				while (iter.hasNext()) {
					Integer id = iter.next();
					this._actionLogManager.deleteActionRecord(id);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error in run ", t);
			//ApsSystemUtils.logThrowable(t, this, "run");
		}
	}
	
	private Integer _maxActivitySizeByGroup;
	private IActionLogManager _actionLogManager;
	
}