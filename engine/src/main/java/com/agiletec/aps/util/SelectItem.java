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
package com.agiletec.aps.util;

/**
 * A single item for a select form element.
 * @author E.Santoboni
 */
public class SelectItem {
	
	public SelectItem(String key, String value) {
		this._key = key;
		this._value = value;
	}
	
	public SelectItem(String key, String value, String optgroup) {
		this(key, value);
		this._optgroup = optgroup;
	}
	
	public String getKey() {
		return _key;
	}
	public String getValue() {
		return _value;
	}
	public String getOptgroup() {
		return _optgroup;
	}
	
	private String _key;
	private String _value;
	private String _optgroup;
	
}