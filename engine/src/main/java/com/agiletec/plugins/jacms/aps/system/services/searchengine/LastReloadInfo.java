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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.util.Date;

/**
 * Informazioni sull'ultimo ricaricamento indici effettuato.
 * @author E.Santoboni
 */
public class LastReloadInfo {
	
	/**
	 * Restituisce la data dell'ultimo ricaricamento indici effettuata.
	 * @return La data dell'ultimo ricaricamento.
	 */
	public Date getDate() {
		return _date;
	}
	
	/**
	 * Setta la data dell'ultimo ricaricamento indici effettuata.
	 * @param date La data dell'ultimo ricaricamento.
	 */
	protected void setDate(Date date) {
		this._date = date;
	}
	
	/**
	 * Restituisce il risultato dell'ultimo ricaricamento indici effettuata. 
	 * True se è andato a buon fine,false in caso contrario.
	 * @return Il risultato dell'ultimo ricaricamento.
	 */
	public int getResult() {
		return _result;
	}
	
	/**
	 * Setta il risultato dell'ultimo ricaricamento indici effettuata. 
	 * True se è andato a buon fine,false in caso contrario.
	 * @param result Il risultato dell'ultimo ricaricamento.
	 */
	protected void setResult(int result) {
		this._result = result;
	}
	
	private Date _date;
	private int _result;
	
	public static final int ID_FAILURE_RESULT = 0;
	public static final int ID_SUCCESS_RESULT = 1;
	
}
