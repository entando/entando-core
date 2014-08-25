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
package com.agiletec.aps.system.services.keygenerator;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi gestori di sequenze univoche.
 * @author S.Didaci - E.Santoboni
 */
public interface IKeyGeneratorManager {

	/**
	 * Restituisce la chiave univoca corrente.
	 * @return La chiave univoca corrente.
	 * @throws ApsSystemException In caso di errore 
	 * nell'aggiornamento della chiave corrente.
	 */
	public int getUniqueKeyCurrentValue() throws ApsSystemException;

}
