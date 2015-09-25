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
package org.entando.entando.apsadmin.api.model;

import com.agiletec.aps.util.SelectItem;

/**
 * @author E.Santoboni
 */
public class ApiSelectItem extends SelectItem {
	
	public ApiSelectItem(String key, String value, String optgroup) {
		super(key, value, optgroup);
	}
	
	public String getValue() {
		String value = super.getValue();
		if (null == value) {
			value = this.getKey();
		}
		return value;
	}
	
	public boolean isActiveItem() {
		return _activeItem;
	}
	public void setActiveItem(boolean activeItem) {
		this._activeItem = activeItem;
	}
	public boolean isPublicItem() {
		return _publicItem;
	}
	public void setPublicItem(boolean publicItem) {
		this._publicItem = publicItem;
	}
	
	private boolean _activeItem;
	private boolean _publicItem;
	
}
