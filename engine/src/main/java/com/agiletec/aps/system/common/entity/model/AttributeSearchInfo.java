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
