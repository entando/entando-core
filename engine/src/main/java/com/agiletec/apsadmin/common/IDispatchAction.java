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
package com.agiletec.apsadmin.common;

/**
 * Interfaccia base per le classi action delegate 
 * alla gestione delle operazioni di login.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IDispatchAction {
	
	/**
	 * Esegue l'operazione di richiesta login utente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String doLogin();
    
	/**
	 * Esegue l'operazione di richiesta logout utente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String doLogout();
	
}
