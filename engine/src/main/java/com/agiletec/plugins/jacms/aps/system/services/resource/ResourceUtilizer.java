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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi, i cui elementi gestiti, 
 * possono presentare delle referenziazione con delle risorse.
 * @author E.Santoboni
 */
public interface ResourceUtilizer {
	
	/**
	 * Restituisce l'identificativo del servizio utilizzatore.
	 * @return L'identificativo del servizio utilizzatore.
	 */
	public String getName();
	
	/**
	 * Restituisce la lista degli oggetti referenzianti la risorsa 
	 * identificata dal codice immesso.
	 * @param resourceId L'identificativo della risorsa.
	 * @return La lista degli oggetti referenzianti la risorsa.
	 * @throws ApsSystemException in caso di errore.
	 */
	public List getResourceUtilizers(String resourceId) throws ApsSystemException;
	
}
