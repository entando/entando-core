/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * @author E.Santoboni
 */
public class ActionLogAppenderThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(ActionLogAppenderThread.class);
	
	public ActionLogAppenderThread(ActionLogRecord actionRecordToAdd, 
			ActionLogManager actionLogManager) {
		this._actionLogManager = actionLogManager;
		this._actionRecordToAdd = actionRecordToAdd;
	}
	
	@Override
	public void run() {
		try {
			this._actionLogManager.addActionRecordByThread(this._actionRecordToAdd);
		} catch (Throwable t) {
			_logger.error("error in run", t);
			//ApsSystemUtils.logThrowable(t, this, "run");
		}
	}
	
	private ActionLogRecord _actionRecordToAdd;
	private ActionLogManager _actionLogManager;
	
}