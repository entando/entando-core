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