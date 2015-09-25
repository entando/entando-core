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