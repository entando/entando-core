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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

/**
 * Rappresenta un tipo di contenuto il forma Small. 
 * Contiene gli elementi minimi per poter visualizzare il codice 
 * e la descrizione del tipo di contenuto.
 * @author E.Santoboni
 */
public class SmallContentType implements Comparable {
	
	/**
	 * Restituisce il codice del tipo di contenuto.
	 * @return Il codice del tipo di contenuto.
	 */
	public String getCode() {
		return _code;
	}

	/**
	 * Setta il codice del tipo di contenuto.
	 * @param code Il codice del tipo di contenuto.
	 */
	public void setCode(String code) {
		this._code = code;
	}

	/**
	 * Restituisce la descrizione del tipo di contenuto.
	 * @return La descrizione del tipo di contenuto.
	 */
	public String getDescr() {
		return _descr;
	}

	/**
	 * Setta la descrizione del tipo di contenuto.
	 * @param descr La descrizione del tipo di contenuto.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	public int compareTo(Object smallContentType) {
		return _descr.compareTo(((SmallContentType) smallContentType).getDescr());
	}
	
	private String _code;
	private String _descr;
	
}
