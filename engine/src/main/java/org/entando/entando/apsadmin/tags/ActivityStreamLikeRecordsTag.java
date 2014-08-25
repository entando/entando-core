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
package org.entando.entando.apsadmin.tags;

import org.entando.entando.aps.system.services.actionlog.IActionLogManager;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.tags.AbstractObjectInfoTag;

/**
 * Returns the list of like records of an activity through the code.
 * @author E.Santoboni
 */
public class ActivityStreamLikeRecordsTag extends AbstractObjectInfoTag {

	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		Integer recordId = Integer.parseInt(keyValue);
		IActionLogManager loggerManager = (IActionLogManager) ApsWebApplicationUtils.getBean(SystemConstants.ACTION_LOGGER_MANAGER, this.pageContext);
		return loggerManager.getActionLikeRecords(recordId);
	}

	public String getRecordId() {
		return super.getKey();
	}
	public void setRecordId(String recordId) {
		super.setKey(recordId);
	}

}