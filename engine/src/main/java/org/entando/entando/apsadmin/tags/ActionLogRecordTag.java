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
 * Returns a single record of Action Logger Manager (or one of its property) through the code.
 * You can choose whether to return the entire object (leaving the attribute "property" empty) or a single property.
 * The names of the available property of "ActivityStream": "id", "actionDate", "username", "namespace", "actionName", "parameters", "activityStreamInfo".
 * @author E.Santoboni
 */
public class ActionLogRecordTag extends AbstractObjectInfoTag {

	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		IActionLogManager loggerManager = (IActionLogManager) ApsWebApplicationUtils.getBean(SystemConstants.ACTION_LOGGER_MANAGER, this.pageContext);
		return loggerManager.getActionRecord(Integer.parseInt(keyValue));
	}

}