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
package com.agiletec.aps.system.services.baseconfig;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia per il servizio di configurazione.
 * @author M.Diana - E.Santoboni
 */
public interface ConfigInterface {
	
	/**
	 * Restituisce una voce di configurazione. La voce è un elemento di testo
	 * che può essere complesso (es. XML). I valori restituiti sono relativi
	 * alla versione di configurazione con cui è stato avviato il sistema.
	 * @param name Il codice della voce da restituire.
	 * @return Il testo della voce di configurazione.
	 */
	public String getConfigItem(String name);
	
	/**
	 * Aggiorna una voce di configurazione. La voce è un elemento di testo
	 * che può essere complesso (es. XML). 
	 * @param name Il codice della voceda aggiornare.
	 * @param config Il testo della voce di configurazione da aggiornare.
	 * @throws ApsSystemException In caso di errore nell'aggiornamento
	 */
	public void updateConfigItem(String name, String config) throws ApsSystemException;
	
	/**
	 * Restituisce un parametro di configurazione. 
	 * Un parametro è una stringa semplice.
	 * @param name Il nome del parametro
	 * @return Il valore del parametro
	 */
	public String getParam(String name);
	
}