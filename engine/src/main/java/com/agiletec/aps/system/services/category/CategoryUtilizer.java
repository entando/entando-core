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
package com.agiletec.aps.system.services.category;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi, i cui elementi gestiti, 
 * fanno uso delle categorie.
 * @author E.Santoboni
 */
public interface CategoryUtilizer {
	
	/**
	 * Restituisce l'identificativo del servizio utilizzatore.
	 * @return L'identificativo del servizio utilizzatore.
	 */
	public String getName();
	
	/**
	 * Restituisce la lista degli oggetti referenzianti 
	 * la categoria identificata dal codice specificato.
	 * @param categoryCode Il codice della categoria.
	 * @return La lista degli oggetti referenzianti 
	 * la categoria identificata dal codice specificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List getCategoryUtilizers(String categoryCode) throws ApsSystemException;
	
}
