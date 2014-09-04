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
package com.agiletec.apsadmin.user.group.helper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;

/**
 * Interfaccia base per le classi action helper della gestione Gruppi.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IGroupActionHelper {
	
	/**
	 * Restituisce l'insieme delle referenze sul gruppo specificato.
	 * Il metodo restituisce la mappa della lista degli oggetti referenzianti, indicizzata in base al nome 
	 * del manager che gestisce gli oggetti specifici.
	 * @param group Il gruppo cui ricavare le referenze.
	 * @param request La request.
	 * @return La mappa della lista di oggetti referenzianti.
	 * @throws ApsSystemException In caso di errore.
	 */
	public Map<String, List<Object>> getReferencingObjects(Group group, HttpServletRequest request) throws ApsSystemException;
	
}
