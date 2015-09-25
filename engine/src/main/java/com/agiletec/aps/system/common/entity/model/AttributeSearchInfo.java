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
package com.agiletec.aps.system.common.entity.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This is the representation of the search informations which apply to a single attribute.
 * @author E.Santoboni
 */
public class AttributeSearchInfo {
	
	/**
	 * This is the constructor of the search informations.
	 * @param string The string characterizing the value of the attribute. It may be null.
	 * @param date  The date characterizing the value of the attribute. It may be null.
	 * @param bigDecimal The number characterizing the value of the attribute. It may be null.
	 * @param langCode The code of the language.
	 */
	public AttributeSearchInfo(String string, Date date, BigDecimal bigDecimal, String langCode) {
		this._string = string;
		this._date = date;
		this._bigDecimal = bigDecimal;
		this._langCode = langCode;
	}
	
	/**
	 * Return the string characterizing the value of the attribute; it may be null.
	 * @return The string value.
	 */
	public String getString() {
		return _string;
	}
	
	/**
	 * Return the date  characterizing the value of the attribute; it may be null.
	 * @return The date value.
	 */
	public Date getDate() {
		return _date;
	}
	
	/**
	 * Return the number characterizing the value of the attribute; it may be null.
	 * @return The numeric value.
	 */
	public BigDecimal getBigDecimal() {
		return _bigDecimal;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	
	private String _string;
	private Date _date;
	private BigDecimal _bigDecimal;
	private String _langCode;
	
}
