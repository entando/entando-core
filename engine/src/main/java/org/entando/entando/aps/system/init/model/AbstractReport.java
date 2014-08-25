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
package org.entando.entando.aps.system.init.model;

import java.util.HashMap;
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
		return SystemInstallationReport.Status.OK;
	}
	
	public Map<String, SystemInstallationReport.Status> getDatabaseStatus() {
		return _databaseStatus;
	}
	
	private Map<String, SystemInstallationReport.Status> _databaseStatus = new HashMap<String, SystemInstallationReport.Status>();
	
}
