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
package com.agiletec.aps.system.services.lang;

/**
 * Rappresentazione di una lingua
 * @author M.Diana
 */
public class Lang implements Comparable {
	
	/**
	 * Restituisce il codice della lingua
	 * @return Il codice della lingua
	 */
	public String getCode() {
		return _code;
	}

	/**
	 * Imposta il codice della lingua
	 * @param code Il codice da impostare
	 */
	public void setCode(String code) {
		this._code = code;
	}

	/**
	 * Restituisce la descrizione della lingua
	 * @return la descrizione della lingua
	 */
	public String getDescr() {
		return _descr;
	}

	/**
	 * Imposta la descrizione della lingua
	 * @param descr la descrizione da impostare
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	/**
	 * Definisce se la lingua è di default
	 * @param isDefault Vero se la lingua è quella di default
	 */
	public void setDefault(boolean isDefault) {
		this._isDefaultLang = isDefault;
	}
	
	/**
	 * Restituisce un booleano che indica se la lingua è quella di default.
	 * @return Vero se la lingua è quella di default.
	 */
	public boolean isDefault(){
		return this._isDefaultLang;
	}
	
	public int compareTo(Object lang) {
		return _descr.compareTo(((Lang) lang).getDescr());
	}
	
	/**
	 * Il codice della lingua
	 */
	private String _code;
	
	/**
	 * La descrizione della lingua
	 */
	private String _descr;
	
	/**
	 * La proprietà default
	 */
	private boolean _isDefaultLang = false;
	
}
