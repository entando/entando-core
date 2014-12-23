/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.portal.util;

import com.agiletec.apsadmin.util.SelectItem;

/**
 * @deprecated from jAPS 2.0 version 2.1 - use {@link SelectItem}
 */
public class ShowletTypeSelectItem extends SelectItem {
	
	public ShowletTypeSelectItem(String key, String value, String optgroup) {
		super(key, value, optgroup);
	}
	
	/**
	 * Return the code of the group owning the showlet type.
	 * The field is null if the showlet type belongs to jAPS Core.
	 * @return The group code.
	 */
	public String getGroupCode() {
		return super.getOptgroup();
	}
	
}