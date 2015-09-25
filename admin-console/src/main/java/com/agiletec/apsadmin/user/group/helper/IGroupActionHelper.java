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
