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
