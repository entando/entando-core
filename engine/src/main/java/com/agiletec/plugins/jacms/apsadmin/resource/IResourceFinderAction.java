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
package com.agiletec.plugins.jacms.apsadmin.resource;

import java.util.List;

/**
 * Interfaccia base per le Action gestrici delle interfaccie di lista risorse.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IResourceFinderAction {
	
	/**
	 * Restituisce la lista di identificativi delle risorse che 
	 * soddisfano i parametri di ricerca immessi.
	 * @return La lista di identificativi di risorse.
	 * @throws Throwable In caso di errore.
	 */
	public List<String> getResources() throws Throwable;
	
}
