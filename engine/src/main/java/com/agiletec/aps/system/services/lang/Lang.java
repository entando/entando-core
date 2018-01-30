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
package com.agiletec.aps.system.services.lang;

import java.io.Serializable;

/**
 * Rappresentazione di una lingua
 *
 * @author M.Diana
 */
public class Lang implements Comparable, Serializable {

	/**
	 * Restituisce il codice della lingua
	 *
	 * @return Il codice della lingua
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Imposta il codice della lingua
	 *
	 * @param code Il codice da impostare
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Restituisce la descrizione della lingua
	 *
	 * @return la descrizione della lingua
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * Imposta la descrizione della lingua
	 *
	 * @param descr la descrizione da impostare
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * Definisce se la lingua è di default
	 *
	 * @param isDefault Vero se la lingua è quella di default
	 */
	public void setDefault(boolean isDefault) {
		this.defaultLang = isDefault;
	}

	/**
	 * Restituisce un booleano che indica se la lingua è quella di default.
	 *
	 * @return Vero se la lingua è quella di default.
	 */
	public boolean isDefault() {
		return this.defaultLang;
	}

	@Override
	public int compareTo(Object lang) {
		return this.descr.compareTo(((Lang) lang).getDescr());
	}

	/**
	 * Il codice della lingua
	 */
	private String code;

	/**
	 * La descrizione della lingua
	 */
	private String descr;

	/**
	 * La proprietà default
	 */
	private boolean defaultLang = false;

}
