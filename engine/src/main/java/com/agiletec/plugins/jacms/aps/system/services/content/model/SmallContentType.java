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
