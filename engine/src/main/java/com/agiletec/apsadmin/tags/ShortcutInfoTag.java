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
package com.agiletec.apsadmin.tags;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.services.shortcut.IShortcutManager;

/**
 * Returns a shortcut (or one of its property) through the code.
 * You can choose whether to return the entire object (leaving the attribute "property" empty) or a single property.
 * The names of the available property of "Shortcut": "id", "descriptionKey", "description", "longDescription", "longDescriptionKey", 
 * "requiredPermission", "menuSectionCode", "menuSection", "source", "namespace", "actionName", "parameters".
 * @author E.Santoboni
 */
public class ShortcutInfoTag extends AbstractObjectInfoTag {
	
	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		IShortcutManager shortcutManager = (IShortcutManager) ApsWebApplicationUtils.getBean(ApsAdminSystemConstants.SHORTCUT_MANAGER, this.pageContext);
		return shortcutManager.getShortcut(keyValue);
	}
	
}