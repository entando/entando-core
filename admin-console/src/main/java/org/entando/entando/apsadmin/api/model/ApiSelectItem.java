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
