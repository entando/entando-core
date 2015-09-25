/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public abstract class AbstractReport {
	
	public SystemInstallationReport.Status getStatus() {
		if (null == this.getDatabaseStatus() || this.getDatabaseStatus().isEmpty()) {
			return SystemInstallationReport.Status.INIT;
		}
		if (this.getDatabaseStatus().containsValue(SystemInstallationReport.Status.INCOMPLETE)) {
			return SystemInstallationReport.Status.INCOMPLETE;
		}
		if (this.getDatabaseStatus().containsValue(SystemInstallationReport.Status.UNINSTALLED)) {
			return SystemInstallationReport.Status.UNINSTALLED;
		}
		return SystemInstallationReport.Status.OK;
	}
	
	public void upgradeDatabaseStatus(SystemInstallationReport.Status status) {
		Iterator<String> iter = this.getDatabaseStatus().keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			this.getDatabaseStatus().put(key, status);
		}
	}
	
	public Map<String, SystemInstallationReport.Status> getDatabaseStatus() {
		return _databaseStatus;
	}
	
	private Map<String, SystemInstallationReport.Status> _databaseStatus = new HashMap<String, SystemInstallationReport.Status>();
	
}
